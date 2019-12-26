package dev.config;

import lombok.Getter;

public enum Routes {
	CustomerNode("localhost:8083/api");

	@Getter
	private String value;
	
	private Routes(String value) {
		this.value = value;
	}
}
