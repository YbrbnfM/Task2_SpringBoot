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
import commonnode.entities.PaidType;
import commonnode.entities.Result;
import commonnode.securiry.params.JWTParams;
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
		return new ResponseEntity<>(os.getAll(), HttpStatus.OK);
	}

	@Override
	@GetMapping("/offers/{id}")
	public ResponseEntity<Offer> get(@PathVariable("id") int id) {
		return new ResponseEntity<>(os.get(id), HttpStatus.OK);
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
	@PutMapping("/offers/{id}")
	public ResponseEntity<Offer> put(@PathVariable("id") int id, @Valid @RequestBody Offer o) {
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
	}

	@Override
	@DeleteMapping("/offers/{id}")
	public ResponseEntity<Result<Boolean>> delete(@PathVariable("id") int id) {
		return new ResponseEntity<>(new Result<>(os.delete(id)), HttpStatus.OK);
	}

	@GetMapping("/offers/customoffers")
	public ResponseEntity<List<Offer>> getOffersForCustom() {
		// TODO: 1вывести повторы в функцию
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(JWTParams.header.getValue(), JWTParams.JWTValue.getValue());
		List<PaidType> paidTypes = new RestTemplate().exchange(
				Routes.CUSTOMER_NODE.getValue() + "/paidtypes/customer/"
						+ (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
				HttpMethod.GET, new HttpEntity<>("parameters", headers),
				new ParameterizedTypeReference<List<PaidType>>() {
				}).getBody();
		return new ResponseEntity<>(
				os.get(x -> paidTypes.stream().map(y -> y.getId()).anyMatch(y -> y == x.getPaidTypeId())),
				HttpStatus.OK);
	}

	// @PreAuthorize("hasRole(ROLE_USER)")
	@PostMapping("/offers/{idOffer}/delivered/{deliveryTime}")
	public ResponseEntity<Result<String>> buy(@RequestHeader("Authorization") String jwt,
			@PathVariable("idOffer") int idOffer, @PathVariable("deliveryTime") String d) {
		Offer o = os.get(idOffer);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(JWTParams.header.getValue(), jwt);
		ResponseEntity<Result<String>> re = new RestTemplate().exchange(Routes.ORDER_NODE.getValue() + "/orders/buy/delivered/" + d,
				HttpMethod.POST, new HttpEntity<Offer>(o, headers), new ParameterizedTypeReference<Result<String>>() {
				});
		if (re.getStatusCode() == HttpStatus.CREATED)
			return new ResponseEntity<>(new Result<>("Success"), HttpStatus.OK);
		return new ResponseEntity<>(re.getBody(), re.getStatusCode());
	}
}
