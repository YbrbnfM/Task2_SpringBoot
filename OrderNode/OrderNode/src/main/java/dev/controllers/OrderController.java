package dev.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import javax.persistence.PersistenceException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import commonnode.Routes;
import commonnode.entities.OfferShort;
import commonnode.securiry.params.AuthRoles;
import commonnode.securiry.params.JWTParams;
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
	@PostMapping("/orders")
	public ResponseEntity<Order> post(@Valid @RequestBody Order o) {
		o.setId(0);
		return new ResponseEntity<>(os.create_edit(o), HttpStatus.CREATED);
	}

	@Override
	@PutMapping("/orders/id={id}")
	public ResponseEntity<Order> put(@PathVariable("id") int id, @Valid @RequestBody Order o) {
		try {
			o.setId(id);
			return new ResponseEntity<>(os.create_edit(o), HttpStatus.CREATED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@DeleteMapping("/orders/id={id}")
	public ResponseEntity<Boolean> delete(@PathVariable("id") int id) {
		// TODO: пересмотреть вариант проверки
		if (!((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equalsIgnoreCase(id + "")
				&& !SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
						.anyMatch(x -> x.getAuthority().equalsIgnoreCase("ROLE_" + AuthRoles.ADMIN.getValue())))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return new ResponseEntity<>(os.delete(id), HttpStatus.OK);
	}

	@PutMapping("/orders/changestatus/id={id}")
	public ResponseEntity<Order> put(@PathVariable("id") int id, @RequestBody Status s) {
		try {
			Order o = os.get(id);
			o.setStatus(s);
			return new ResponseEntity<>(os.create_edit(o), HttpStatus.CREATED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/orders/categoryprice/idoffer={id}")
	public ResponseEntity<OfferShort> getCategoryPrice(@PathVariable("id") int idOffer) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(JWTParams.header.getValue(), JWTParams.JWTValue.getValue());
		return new RestTemplate().exchange(Routes.OFFER_NODE.getValue() + "/offers/id=" + idOffer, HttpMethod.GET,
				new HttpEntity<>("parameters", headers), new ParameterizedTypeReference<OfferShort>() {
				});
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/orders/buy")
	public ResponseEntity buy(@RequestHeader("Authorization") String jwt, @RequestBody OfferShort offer) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(JWTParams.header.getValue(), jwt);
		int idCustomer = new RestTemplate().exchange(Routes.CUSTOMER_NODE.getValue() + "/customerId", HttpMethod.GET,
				new HttpEntity<>("parameters", headers), new ParameterizedTypeReference<Integer>() {
				}).getBody();
		return new ResponseEntity<>(
				this.post(new Order(0, offer.getId(), Order.generateName(offer.getName(), idCustomer),
						Order.generateDeliveryTime(), new Status(), idCustomer, false)).getStatusCode());
	}
}
