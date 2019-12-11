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
import dev.entities.PaidType;
import lombok.NonNull;

@Repository
@Transactional
public class PaidTypeService implements Service<PaidType> {
	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public List<PaidType> getAll() throws PersistenceException {
		return em.createQuery("from PaidType").getResultList();
	}

	@Override
	public List<PaidType> get(@NonNull Predicate<PaidType> p) throws PersistenceException {
		return getAll().stream().filter(p).collect(Collectors.toList());
	}

	@Override
	public PaidType get(int id) throws PersistenceException, NoSuchElementException {
		@SuppressWarnings("unchecked")
		List<PaidType> paidTypes = em.createQuery("from PaidType pt where pt.id = " + id).getResultList();
		if (paidTypes.isEmpty())
			throw new NoSuchElementException("Остутствует элемент по заданному id");
		return paidTypes.get(0);
	}

	@Override
	public PaidType create_edit(@NonNull PaidType o) {
		if (o.getId() == 0) {
			em.persist(o);
			return o;
		}
		PaidType orig = em.find(PaidType.class, o.getId());
		orig.setName(o.getName());
		em.merge(orig);
		return orig;
		/*
		 * TODO: проверить допустим ли такой вариант 
		 * em.merge(o);
		 */
	}

	@Override
	public boolean delete(int id) {
		PaidType orig = em.find(PaidType.class, id);
		if (orig != null) {
			em.remove(orig);
			return true;
		}
		return false;
	}
}
