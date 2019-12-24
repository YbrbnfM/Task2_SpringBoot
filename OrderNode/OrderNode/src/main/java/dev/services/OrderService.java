package dev.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import dev.entities.Order;
import lombok.NonNull;

@Repository
@Transactional
public class OrderService implements Service<Order> {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Order> getAll() throws PersistenceException {
		return em.createQuery("from orders", Order.class).getResultList();
	}

	@Override
	public List<Order> get(@NonNull Predicate<Order> p) throws PersistenceException {
		return getAll().stream().filter(p).collect(Collectors.toList());
	}

	@Override
	public Order get(int id) throws PersistenceException, NoSuchElementException {
		List<Order> lst = em.createQuery("from orders pt where pt.id = " + id, Order.class).getResultList();
		if (lst.isEmpty())
			throw new NoSuchElementException("Остутствует элемент по заданному id");
		return lst.get(0);
	}

	@Override
	public Order create_edit(@NonNull Order o) {
		// TODO: связь с кастомерами и офферами
		if (o.getId() == 0) {
			em.persist(o);
			return o;
		}
		Order orig = em.find(Order.class, o.getId());
		if (orig == null)
			throw new NoSuchElementException();
		orig.setCustomerId(o.getCustomerId());
		orig.setDeliveryTime(o.getDeliveryTime());
		orig.setName(o.getName());
		orig.setOfferId(o.getOfferId());
		orig.setPaid(o.isPaid());
		orig.setStatus(o.getStatus());
		em.merge(orig);
		return orig;
	}

	@Override
	public boolean delete(int id) {
		// TODO: каскадное удаление категорий???
		Order orig = em.find(Order.class, id);
		if (orig != null) {
			em.remove(orig);
			return true;
		}
		return false;
	}

}
