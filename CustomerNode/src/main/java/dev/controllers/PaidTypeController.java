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
import dev.entities.PaidType;
import dev.services.Service;

//TODO: (Условно выполнено)Валидация всех невалидных данных и обработка исключений, пример обработка null объектов
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaidTypeController implements Controller<PaidType> {

	@Autowired
	private Service<PaidType> pts;

	@Override
	@GetMapping("/paidtypes")
	public ResponseEntity<List<PaidType>> getAll() {
		try {
			return new ResponseEntity<>(pts.getAll(), HttpStatus.OK);
		} catch (PersistenceException e) {
			return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@Override
	@GetMapping("/paidtypes/id={paidTypeId}")
	public ResponseEntity<PaidType> get(@PathVariable("paidTypeId") int id) {
		try {
			return new ResponseEntity<>(pts.get(id), HttpStatus.OK);
		} catch (PersistenceException e) {
			return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@PostMapping("/paidtypes")
	public ResponseEntity<PaidType> post(@Valid @RequestBody PaidType o) {
		o.setId(0);
		return new ResponseEntity<>(pts.create_edit(o), HttpStatus.CREATED);
	}

	@Override
	@PutMapping("/paidtypes/id={paidTypeId}")
	public ResponseEntity<PaidType> put(@PathVariable("paidTypeId") int id, @Valid @RequestBody PaidType o) {
		try {
			o.setId(id);
			return new ResponseEntity<>(pts.create_edit(o), HttpStatus.CREATED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@Override
	@DeleteMapping("/paidtypes/id={paidTypeId}")
	public ResponseEntity<Boolean> delete(@PathVariable("paidTypeId") int id) {
		return new ResponseEntity<>(pts.delete(id), HttpStatus.OK);
	}
}
