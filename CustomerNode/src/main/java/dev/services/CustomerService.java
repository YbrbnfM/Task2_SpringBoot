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
import dev.entities.Customer;
import dev.entities.PaidType;
import dev.util.Cryptography;
import lombok.NonNull;

@Repository
@Transactional
public class CustomerService implements Service<Customer> {
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Customer> getAll() throws PersistenceException {
		return em.createQuery("from Customer", Customer.class).getResultList();
	}

	@Override
	public List<Customer> get(@NonNull Predicate<Customer> p) throws PersistenceException {
		return getAll().stream().filter(p).collect(Collectors.toList());
	}

	@Override
	public Customer get(int id) throws PersistenceException, NoSuchElementException {
		List<Customer> lst = em.createQuery("from Customer c where c.id = " + id, Customer.class).getResultList();
		if (lst.isEmpty())
			throw new NoSuchElementException("Остутствует элемент по заданному id");
		return lst.get(0);
	}

	@Override
	public Customer create_edit(@NonNull Customer o) throws NoSuchElementException {
		o.setPassword(Cryptography.encryptWhithSha512(o.getPassword()));
		if (o.getId() == 0) {
			em.persist(o);
			return o;
		}
		Customer orig = em.find(Customer.class, o.getId());
		if (orig == null)
			throw new NoSuchElementException();
		orig.setAddress(o.getAddress());
		orig.setEmail(o.getEmail());
		orig.setFirstName(o.getFirstName());
		orig.setLastName(o.getLastName());
		orig.getPaidTypes().clear();
		if (o.getPaidTypes() != null)
			for (PaidType pt : o.getPaidTypes())
				orig.getPaidTypes().add(pt);
		orig.setPassword(o.getPassword());
		orig.setPhoneNumber(o.getPhoneNumber());
		em.merge(orig);
		return orig;
	}

	@Override
	public boolean delete(int id) {
		Customer orig = em.find(Customer.class, id);
		if (orig != null) {
			em.remove(orig);
			return true;
		}
		return false;
	}

}
