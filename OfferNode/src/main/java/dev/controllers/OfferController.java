package dev.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import javax.persistence.PersistenceException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.client.RestTemplate;

import dev.config.Routes;
import dev.entities.Offer;
import dev.entities.util.PaidType;
import dev.services.Service;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class OfferController implements Controller<Offer> {

	@Autowired
	private Service<Offer> os;

	@Override
	@GetMapping("/offers")
	public ResponseEntity<List<Offer>> getAll() {
		try {
			return new ResponseEntity<>(os.getAll(), HttpStatus.OK);
		} catch (PersistenceException e) {
			return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@Override
	@GetMapping("/offers/id={id}")
	public ResponseEntity<Offer> get(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<>(os.get(id), HttpStatus.OK);
		} catch (PersistenceException e) {
			return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@PostMapping("/offers")
	public ResponseEntity<Offer> post(@Valid @RequestBody Offer o) {
		o.setId(0);
		return new ResponseEntity<>(os.create_edit(o), HttpStatus.CREATED);
	}

	@Override
	@PutMapping("/offers/id={id}")
	public ResponseEntity<Offer> put(@PathVariable("id") int id, @Valid @RequestBody Offer o) {
		try {
			o.setId(id);
			return new ResponseEntity<>(os.create_edit(o), HttpStatus.CREATED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@DeleteMapping("/offers/id={id}")
	public ResponseEntity<Boolean> delete(@PathVariable("id") int id) {
		return new ResponseEntity<>(os.delete(id), HttpStatus.OK);
	}

	@GetMapping("/customoffers")
	public ResponseEntity<List<Offer>> getOffersForCustom() {
		try {
			List<PaidType> paidTypes = new RestTemplate().exchange(
					Routes.CustomerNode.getValue() + "/paidtypes/idcustomer="
							+ (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
					HttpMethod.GET, null, new ParameterizedTypeReference<List<PaidType>>() {
					}).getBody();
			return new ResponseEntity<>(
					os.get(x -> paidTypes.stream().map(y -> y.getId()).anyMatch(y -> y == x.getPaidTypeId())),
					HttpStatus.OK);
		} catch (PersistenceException e) {
			return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
		}
	}

}
