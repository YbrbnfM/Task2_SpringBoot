package dev.controllers;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.client.RestTemplate;
import commonnode.Routes;
import commonnode.entities.OfferPaidType;
import commonnode.securiry.params.JWTParams;
import dev.entities.Customer;
import dev.entities.PaidType;
import dev.services.Service;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaidTypeController implements Controller<PaidType> {

	@Autowired
	private Service<PaidType> pts;
	@Autowired
	private Service<Customer> cs;

	@Override
	@GetMapping("/paidtypes")
	public ResponseEntity<List<PaidType>> getAll() {
		return new ResponseEntity<>(pts.getAll(), HttpStatus.OK);
	}

	@Override
	@GetMapping("/paidtypes/id={id}")
	public ResponseEntity<PaidType> get(@PathVariable("id") int id) {
		return new ResponseEntity<>(pts.get(id), HttpStatus.OK);
	}

	@Override
	@PostMapping("/paidtypes")
	public ResponseEntity<PaidType> post(@Valid @RequestBody PaidType o) {
		o.setId(0);
		return new ResponseEntity<>(pts.create_edit(o), HttpStatus.CREATED);
	}

	@Override
	@PutMapping("/paidtypes/id={id}")
	public ResponseEntity<PaidType> put(@PathVariable("id") int id, @Valid @RequestBody PaidType o) {
		o.setId(id);
		return new ResponseEntity<>(pts.create_edit(o), HttpStatus.CREATED);
	}

	@Override
	@DeleteMapping("/paidtypes/id={id}")
	public ResponseEntity<Boolean> delete(@PathVariable("id") int id) {
		// TODO: 1вывести повторы в функцию
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(JWTParams.header.getValue(), JWTParams.JWTValue.getValue());
		List<OfferPaidType> lst = new RestTemplate().exchange(Routes.OFFER_NODE.getValue() + "/offers", HttpMethod.GET,
				new HttpEntity<>("parameters", headers), new ParameterizedTypeReference<List<OfferPaidType>>() {
				}).getBody();
		for (OfferPaidType offerPaidType : lst)
			if (offerPaidType.getPaidTypeId() == id)
				return new ResponseEntity<>(false, HttpStatus.FAILED_DEPENDENCY);
		return new ResponseEntity<>(pts.delete(id), HttpStatus.OK);
	}

	@GetMapping("/paidtypes/idcustomer={id}")
	public ResponseEntity<List<PaidType>> getAll(@PathVariable("id") int id) {
		return new ResponseEntity<>(cs.get(id).getPaidTypes(), HttpStatus.OK);
	}
}
