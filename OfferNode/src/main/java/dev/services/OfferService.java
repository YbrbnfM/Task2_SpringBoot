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

import dev.entities.Category;
import dev.entities.Characteristic;
import dev.entities.Offer;
import lombok.NonNull;

@Repository
@Transactional
public class OfferService implements Service<Offer> {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Offer> getAll() throws PersistenceException {
		return em.createQuery("from Offer", Offer.class).getResultList();
	}

	@Override
	public List<Offer> get(@NonNull Predicate<Offer> p) throws PersistenceException {
		return getAll().stream().filter(p).collect(Collectors.toList());
	}

	@Override
	public Offer get(int id) throws PersistenceException, NoSuchElementException {
		List<Offer> lst = em.createQuery("from Offer pt where pt.id = " + id, Offer.class).getResultList();
		if (lst.isEmpty())
			throw new NoSuchElementException("Остутствует элемент по заданному id");
		return lst.get(0);
	}

	@Override
	public Offer create_edit(@NonNull Offer o) {
		try {
			Category c = em.createQuery("from Category", Category.class).getResultList().stream()
					.filter(x -> x.getName().equalsIgnoreCase(o.getCategory().getName())).findAny().get();
			o.setCategory(c);
		} catch (NoSuchElementException e) {
		}
		if (o.getId() == 0) {
			em.persist(o);
			return o;
		}
		Offer orig = em.find(Offer.class, o.getId());
		if (orig == null)
			throw new NoSuchElementException();
		orig.setName(o.getName());
		Category c = orig.getCategory();
		orig.setCategory(o.getCategory());
		orig.getCharacteristics().clear();
		if (o.getCharacteristics() != null)
			for (Characteristic el : o.getCharacteristics())
				orig.getCharacteristics().add(el);
		orig.setPaidTypeId(o.getPaidTypeId());
		orig.setPrice(o.getPrice());
		em.merge(orig);
		if(!getAll().stream().anyMatch(x->x.getCategory().getId()==c.getId()))
			em.remove(c);
		return orig;
	}

	@Override
	public boolean delete(int id) {
		Offer orig = em.find(Offer.class, id);
		if (orig != null) {
			Category c = orig.getCategory();
			em.remove(orig);
			if(!getAll().stream().anyMatch(x->x.getCategory().getId()==c.getId()))
				em.remove(c);
			return true;
		}
		return false;
	}

}
