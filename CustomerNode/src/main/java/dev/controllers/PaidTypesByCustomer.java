package dev.controllers;

import java.util.List;
import javax.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.entities.Customer;
import dev.entities.PaidType;
import dev.services.Service;

//TODO: Пересмотреть вариант с переносом в имеющийся контроллер
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaidTypesByCustomer {
	@Autowired
	private Service<Customer> cs;

	@GetMapping("/paidtypes/idcustomer={idCustomer}")
	public ResponseEntity<List<PaidType>> getAll(@PathVariable("idCustomer") int id) {
		try {
			return new ResponseEntity<>(cs.get(id).getPaidTypes(), HttpStatus.OK);
		} catch (PersistenceException e) {
			return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
		}
	}
}
