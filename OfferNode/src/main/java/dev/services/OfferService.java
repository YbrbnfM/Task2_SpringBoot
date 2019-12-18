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
import dev.entities.Characteristic;
import dev.entities.Offer;
import lombok.NonNull;

@Repository
@Transactional
public class OfferService implements Service<Offer> {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Offer> getAll() {
		return em.createQuery("from offers", Offer.class).getResultList();
	}

	@Override
	public List<Offer> get(@NonNull Predicate<Offer> p) throws PersistenceException {
		return getAll().stream().filter(p).collect(Collectors.toList());
	}

	@Override
	public Offer get(int id) throws PersistenceException, NoSuchElementException {
		List<Offer> lst = em.createQuery("from offers pt where pt.id = " + id, Offer.class).getResultList();
		if (lst.isEmpty())
			throw new NoSuchElementException("Остутствует элемент по заданному id");
		return lst.get(0);
	}

	@Override
	public Offer create_edit(@NonNull Offer o) {
		// TODO: Проверить работоспособность без искусственного добавления характеристик
//		List<Characteristic> lst = cs.getAll();
//		List<Characteristic> cache = o.getCharacteristics();
//		for (int i = 0; i < cache.size(); i++)
//			if (!lst.contains(cache.get(i)))
//				cache.set(i, cs.create_edit(cache.get(i)));
		// Перенести логику в контроллер
		if (o.getId() == 0) {
			em.persist(o);
			return o;
		}
		Offer orig = em.find(Offer.class, o.getId());
		if (orig == null)
			throw new NoSuchElementException();
		orig.setName(o.getName());
		orig.setCategory(o.getCategory());
		orig.setCharacteristics(o.getCharacteristics());
		if (o.getCharacteristics() != null)
			for (Characteristic el : o.getCharacteristics())
				orig.getCharacteristics().add(el);
		orig.setPaidTypeId(o.getPaidTypeId());
		orig.setPrice(o.getPrice());
		em.merge(orig);
		return orig;
	}

	@Override
	public boolean delete(int id) {
		// TODO: каскадное удаление категорий
		Offer orig = em.find(Offer.class, id);
		if (orig != null) {
			em.remove(orig);
			return true;
		}
		return false;
	}

}
