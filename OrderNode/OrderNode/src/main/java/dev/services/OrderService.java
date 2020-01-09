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
import dev.entities.Status;
import lombok.NonNull;

@Repository
@Transactional
public class OrderService implements Service<Order> {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Order> getAll() throws PersistenceException {
		return em.createQuery("from Order", Order.class).getResultList();
	}

	@Override
	public List<Order> get(@NonNull Predicate<Order> p) throws PersistenceException {
		return getAll().stream().filter(p).collect(Collectors.toList());
	}

	@Override
	public Order get(int id) throws PersistenceException, NoSuchElementException {
		List<Order> lst = em.createQuery("from Order pt where pt.id = " + id, Order.class).getResultList();
		if (lst.isEmpty())
			throw new NoSuchElementException("Остутствует элемент по заданному id");
		return lst.get(0);
	}

	@Override
	public Order create_edit(@NonNull Order o) {
		try {
			Status s = em.createQuery("from Status", Status.class).getResultList().stream()
					.filter(x -> x.getName().equalsIgnoreCase(o.getStatus().getName())).findAny().get();
			o.setStatus(s);
		} catch (NoSuchElementException e) {
		}
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
		Status s = orig.getStatus();
		orig.setStatus(o.getStatus());
		em.merge(orig);
		if(!getAll().stream().anyMatch(x->x.getStatus().getId()==s.getId()))
			em.remove(s);
		return orig;
	}

	@Override
	public boolean delete(int id) {
		Order orig = em.find(Order.class, id);
		if (orig != null) {
			Status s = orig.getStatus();
			em.remove(orig);
			if(!getAll().stream().anyMatch(x->x.getStatus().getId()==s.getId()))
				em.remove(s);
			return true;
		}
		return false;
	}

}
