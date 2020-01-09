package dev.controllers;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import commonnode.securiry.params.AuthRoles;
import dev.entities.Customer;
import dev.services.Service;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController implements Controller<Customer> {

	@Autowired
	private Service<Customer> cs;

	@Override
	@GetMapping("/customers")
	public ResponseEntity<List<Customer>> getAll() {
		return new ResponseEntity<>(cs.getAll(), HttpStatus.OK);
	}

	@Override
	@GetMapping("/customers/id={id}")
	public ResponseEntity<Customer> get(@PathVariable("id") int id) {
		return new ResponseEntity<>(cs.get(id), HttpStatus.OK);
	}

	@Override
	@PostMapping("/customers")
	public ResponseEntity<Customer> post(@Valid @RequestBody Customer o) {
		o.setId(0);
		return new ResponseEntity<>(cs.create_edit(o), HttpStatus.CREATED);
	}

	@Override
	@PutMapping("/customers/id={id}")
	public ResponseEntity<Customer> put(@PathVariable("id") int id, @Valid @RequestBody Customer o) {
		// TODO: пересмотреть вариант проверки
		if (!((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equalsIgnoreCase(id + "")
				&& !SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
						.anyMatch(x -> x.getAuthority().equalsIgnoreCase("ROLE_" + AuthRoles.ADMIN.getValue())))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		o.setId(id);
		return new ResponseEntity<>(cs.create_edit(o), HttpStatus.CREATED);
	}

	@Override
	@DeleteMapping("/customers/id={id}")
	public ResponseEntity<Boolean> delete(@PathVariable("id") int id) {
		return new ResponseEntity<>(cs.delete(id), HttpStatus.OK);
	}
}
