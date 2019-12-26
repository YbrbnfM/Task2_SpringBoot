package dev.controllers;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.config.security.JWTParams;
import dev.entities.Customer;
import dev.services.Service;
import dev.util.Cryptography;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//TODO: протестировать работу
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

	@Autowired
	private Service<Customer> cs;

	@PostMapping("/login")
	public ResponseEntity<String> login(
//			@RequestParam("email") String email,
//			@RequestParam("password") String password
			@RequestBody Customer c) {
		// временно! переделать
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
		List<Customer> lst = cs.get(x -> x.getEmail() == c.getEmail());
		if (lst.isEmpty())
			return new ResponseEntity<>("Отсутствует пользователь с заданным email", HttpStatus.NOT_FOUND);
		if (lst.get(0).getPassword().equalsIgnoreCase(Cryptography.encryptWhithSha512(c.getPassword())))
			return new ResponseEntity<>("Не верная пара email-password", HttpStatus.UNPROCESSABLE_ENTITY);
		return new ResponseEntity<String>(Jwts.builder().setId("ShopDevJWT").setSubject(lst.get(0).getId() + "")
				.claim("authorities",
						grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 3600000))
				.signWith(SignatureAlgorithm.HS512, JWTParams.secretKey.getValue().getBytes()).compact(), HttpStatus.OK);
	}

	@PostMapping("/customerId")
	public ResponseEntity<Integer> getId(@RequestHeader("Authorization") String jwt) {//TODO: избавиться от хардкода
		Claims claims = // (Claims) Jwts.parser().parse(jwt).getBody();
				Jwts.parser().setSigningKey(JWTParams.secretKey.getValue().getBytes()).parseClaimsJws(jwt).getBody();
		List<Customer> lst = cs.get(x -> (x.getId() + "").equalsIgnoreCase(claims.getSubject()));
		if (lst.isEmpty())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(lst.get(0).getId(), HttpStatus.OK);
	}
}
