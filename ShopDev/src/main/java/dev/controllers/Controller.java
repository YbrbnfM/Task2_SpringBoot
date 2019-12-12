package dev.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface Controller<T> {
	ResponseEntity<List<T>> getAll();
	ResponseEntity<T> get(int id);
	ResponseEntity<T> post(T o);
	ResponseEntity<T> put(T o);
	ResponseEntity<Boolean> delete(int id);
}
