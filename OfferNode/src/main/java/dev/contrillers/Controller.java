package dev.contrillers;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface Controller<T> {
	ResponseEntity<List<T>> getAll();

	ResponseEntity<T> get(int id);

	ResponseEntity<T> post(@Valid T o);

	ResponseEntity<T> put(int id, @Valid T o);

	ResponseEntity<Boolean> delete(int id);
}
