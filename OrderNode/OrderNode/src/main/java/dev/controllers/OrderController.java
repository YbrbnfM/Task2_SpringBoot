package dev.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import javax.persistence.PersistenceException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.entities.Order;
import dev.entities.Status;
import dev.services.Service;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController implements Controller<Order> {

	@Autowired
	private Service<Order> os;

	@Override
	@GetMapping("/orders")
	public ResponseEntity<List<Order>> getAll() {
		try {
			return new ResponseEntity<>(os.getAll(), HttpStatus.OK);
		} catch (PersistenceException e) {
			return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@Override
	@GetMapping("/orders/id={id}")
	public ResponseEntity<Order> get(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<>(os.get(id), HttpStatus.OK);
		} catch (PersistenceException e) {
			return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@GetMapping("/orders")
	public ResponseEntity<Order> post(@Valid @RequestBody Order o) {
		o.setId(0);
		return new ResponseEntity<>(os.create_edit(o), HttpStatus.CREATED);
	}

	@Override
	@GetMapping("/orders/id={id}")
	public ResponseEntity<Order> put(@PathVariable("id") int id, @Valid @RequestBody Order o) {
		try {
			o.setId(id);
			return new ResponseEntity<>(os.create_edit(o), HttpStatus.CREATED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@GetMapping("/orders/id={id}")
	public ResponseEntity<Boolean> delete(@PathVariable("id") int id) {
		return new ResponseEntity<>(os.delete(id), HttpStatus.OK);
	}
	
	//TODO: проверить возможность разделения id и статуса
	@GetMapping("/orders")
	public ResponseEntity<Order> put(@RequestBody int id, @RequestBody Status s) {
		try {
			Order o = os.get(id);
			o.setStatus(s);
			return new ResponseEntity<>(os.create_edit(o), HttpStatus.CREATED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
