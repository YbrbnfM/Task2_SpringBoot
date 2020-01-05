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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import commonnode.entities.Credential;
import commonnode.entities.PaidType;
import commonnode.securiry.params.JWTParams;
import commonnode.securiry.params.SystemAccounts;
import dev.config.Routes;
import dev.entities.Characteristic;
import dev.entities.Offer;
import dev.services.Service;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class OfferController implements Controller<Offer> {

	@Autowired
	private Service<Offer> os;
	@Autowired
	private Service<Characteristic> cs;

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
		List<Characteristic> lst = cs.getAll();
		List<Characteristic> cache = o.getCharacteristics();
		for (int i = 0; i < cache.size(); i++) {
			int ii = i;// ???
			if (!lst.stream().anyMatch(x -> x.getName().equalsIgnoreCase(cache.get(ii).getName())
					&& x.getDescription().equalsIgnoreCase(cache.get(ii).getDescription())))
				cache.set(i, cs.create_edit(cache.get(i)));
			else {
				cache.get(i)
						.setId(lst.stream()
								.filter(x -> x.getName().equalsIgnoreCase(cache.get(ii).getName())
										&& x.getDescription().equalsIgnoreCase(cache.get(ii).getDescription()))
								.findAny().get().getId());
			}
		}
		return new ResponseEntity<>(os.create_edit(o), HttpStatus.CREATED);
	}

	@Override
	@PutMapping("/offers/id={id}")
	public ResponseEntity<Offer> put(@PathVariable("id") int id, @Valid @RequestBody Offer o) {
		try {
			o.setId(id);
			List<Characteristic> lst = cs.getAll();
			List<Characteristic> cache = o.getCharacteristics();
			for (int i = 0; i < cache.size(); i++) {
				int ii = i;// ???
				if (!lst.stream().anyMatch(x -> x.getName().equalsIgnoreCase(cache.get(ii).getName())
						&& x.getDescription().equalsIgnoreCase(cache.get(ii).getDescription())))
					cache.set(i, cs.create_edit(cache.get(i)));
				else {
					cache.get(i)
							.setId(lst.stream()
									.filter(x -> x.getName().equalsIgnoreCase(cache.get(ii).getName())
											&& x.getDescription().equalsIgnoreCase(cache.get(ii).getDescription()))
									.findAny().get().getId());
				}
			}
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
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			// TODO: переделать под хранение токенов в оперативке
			headers.add(JWTParams.header.getValue(),
					new RestTemplate().postForObject(Routes.CustomerNode.getValue() + "/login",
							new HttpEntity<>(new Credential(0, SystemAccounts.OfferNode.getLogin(), SystemAccounts.OfferNode.getDecryptPassword()), headers),
							String.class));
			List<PaidType> paidTypes = new RestTemplate().exchange(
					Routes.CustomerNode.getValue() + "/paidtypes/idcustomer="
							+ (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
					HttpMethod.GET, new HttpEntity<>("parameters", headers), new ParameterizedTypeReference<List<PaidType>>() {
					}).getBody();
			return new ResponseEntity<>(
					os.get(x -> paidTypes.stream().map(y -> y.getId()).anyMatch(y -> y == x.getPaidTypeId())),
					HttpStatus.OK);
		} catch (PersistenceException e) {
			return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
		}
	}

}
