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
import dev.entities.Characteristic;
import dev.services.Service;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class CharacteristicController implements Controller<Characteristic> {

	@Autowired
	private Service<Characteristic> cs;

	@Override
	@GetMapping("/characteristics")
	public ResponseEntity<List<Characteristic>> getAll() {
		try {
			return new ResponseEntity<>(cs.getAll(), HttpStatus.OK);
		} catch (PersistenceException e) {
			return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@Override
	@GetMapping("/characteristics/id={id}")
	public ResponseEntity<Characteristic> get(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<>(cs.get(id), HttpStatus.OK);
		} catch (PersistenceException e) {
			return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@PostMapping("/characteristics")
	public ResponseEntity<Characteristic> post(@Valid @RequestBody Characteristic o) {
		o.setId(0);
		return new ResponseEntity<>(cs.create_edit(o), HttpStatus.CREATED);
	}

	@Override
	@PutMapping("/characteristics/id={id}")
	public ResponseEntity<Characteristic> put(@PathVariable("id") int id, @Valid @RequestBody Characteristic o) {
		try {
			o.setId(id);
			return new ResponseEntity<>(cs.create_edit(o), HttpStatus.CREATED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@DeleteMapping("/characteristics/id={id}")
	public ResponseEntity<Boolean> delete(@PathVariable("id") int id) {
		return new ResponseEntity<>(cs.delete(id), HttpStatus.OK);
	}
}
