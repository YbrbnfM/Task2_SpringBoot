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

@Repository
@Transactional
public class CharacteristicService implements Service<Characteristic> {
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Characteristic> getAll() throws PersistenceException {
		return em.createQuery("from Characteristic", Characteristic.class).getResultList();
	}

	@Override
	public List<Characteristic> get(Predicate<Characteristic> p) throws PersistenceException {
		return getAll().stream().filter(p).collect(Collectors.toList());
	}

	@Override
	public Characteristic get(int id) throws PersistenceException, NoSuchElementException {
		List<Characteristic> lst = em.createQuery("from Characteristic pt where pt.id = " + id, Characteristic.class)
				.getResultList();
		if (lst.isEmpty())
			throw new NoSuchElementException("Остутствует элемент по заданному id");
		return lst.get(0);
	}

	@Override
	public Characteristic create_edit(Characteristic o) {
		if (o.getId() == 0) {
			em.persist(o);
			return o;
		}
		Characteristic orig = em.find(Characteristic.class, o.getId());
		if (orig == null)
			throw new NoSuchElementException();
		orig.setName(o.getName());
		orig.setDescription(o.getDescription());
		em.merge(orig);
		return orig;
	}

	@Override
	public boolean delete(int id) {
		Characteristic orig = em.find(Characteristic.class, id);
		if (orig != null) {
			em.remove(orig);
			return true;
		}
		return false;
	}

}
