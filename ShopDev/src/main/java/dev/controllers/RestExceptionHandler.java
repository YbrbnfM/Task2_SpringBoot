package dev.controllers;

import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Object> handlerDataIntegrityViolationException(DataIntegrityViolationException e) {
		return new ResponseEntity<Object>(e.getMostSpecificCause().getMessage(), HttpStatus.FAILED_DEPENDENCY);

	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> universalHandler(ConstraintViolationException e) {
		return new ResponseEntity<Object>(e.getConstraintViolations().stream()
				.map(x -> x.getMessage() + " " + x.getPropertyPath()).findAny().get(), HttpStatus.BAD_REQUEST);
	}
}
