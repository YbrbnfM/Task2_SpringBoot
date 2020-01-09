package dev.controllers;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;

import commonnode.entities.Result;

public interface Controller<T> {
	ResponseEntity<List<T>> getAll();

	ResponseEntity<T> get(int id);

	ResponseEntity<T> post(@Valid T o);

	ResponseEntity<T> put(int id, @Valid T o);

	ResponseEntity<Result<Boolean>> delete(int id);
}
