package dev.controllers;

import java.util.NoSuchElementException;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import commonnode.entities.Result;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Object> handlerDataIntegrityViolationException(DataIntegrityViolationException e) {
		return new ResponseEntity<Object>(new Result<String>(e.getMostSpecificCause().getMessage()),
				HttpStatus.FAILED_DEPENDENCY);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handlerConstraintViolationException(ConstraintViolationException e) {
		return new ResponseEntity<Object>(new Result<String>(e.getConstraintViolations().stream()
				.map(x -> x.getMessage() + " " + x.getPropertyPath()).findAny().get()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(PersistenceException.class)
	public ResponseEntity<Object> handlerPersistenceException(PersistenceException e) {
		return new ResponseEntity<Object>(new Result<String>(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<Object> handlerPersistenceException(NoSuchElementException e) {
		return new ResponseEntity<Object>(new Result<String>(e.getMessage()), HttpStatus.NOT_FOUND);
	}
}
