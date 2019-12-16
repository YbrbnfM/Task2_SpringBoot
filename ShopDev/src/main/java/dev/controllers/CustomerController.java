package dev.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import javax.persistence.PersistenceException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.entities.Customer;
import dev.services.CustomerService;

//TODO: (Условно выполнено)Валидация всех невалидных данных и обработка исключений, пример обработка null объектов
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController implements Controller<Customer> {

	@Autowired
	private CustomerService cs;

	@Override
	@GetMapping("/customers")
	public ResponseEntity<List<Customer>> getAll() {
		try {
			return new ResponseEntity<>(cs.getAll(), HttpStatus.OK);
		} catch (PersistenceException e) {
			return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@Override
	@GetMapping("/customers/id={customerId}")
	public ResponseEntity<Customer> get(@PathVariable("customerId") int id) {
		try {
			return new ResponseEntity<>(cs.get(id), HttpStatus.OK);
		} catch (PersistenceException e) {
			return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@PostMapping("/customers")
	public ResponseEntity<Customer> post(@Valid @RequestBody Customer o) {
		o.setId(0);
		return new ResponseEntity<>(cs.create_edit(o), HttpStatus.CREATED);
	}

	@Override
	@PutMapping("/customers/id={customerId}")
	public ResponseEntity<Customer> put(@PathVariable("customerId") int id, @Valid @RequestBody Customer o) {
		try {
			o.setId(id);
			return new ResponseEntity<>(cs.create_edit(o), HttpStatus.CREATED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@DeleteMapping("/customers/id={customerId}")
	public ResponseEntity<Boolean> delete(@PathVariable("customerId") int id) {
		return new ResponseEntity<>(cs.delete(id), HttpStatus.OK);
	}
}
