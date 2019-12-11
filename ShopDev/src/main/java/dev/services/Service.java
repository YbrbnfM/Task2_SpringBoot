package dev.services;

import java.util.List;
import java.util.function.Predicate;

public interface Service<T> {
	List<T> getAll();
	List<T> get(Predicate<T> p);
	T get(int id);
	/*
	 * Определение операции по id Если id существует - обновить Если id имеет
	 * дефолтное значение - создать
	 */
	T create_edit(T o);
	boolean delete(int id);
}
