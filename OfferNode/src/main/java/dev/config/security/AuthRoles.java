package dev.config.security;

import lombok.Getter;

public enum AuthRoles {
	USER("USER"),
	ADMIN("ADMIN");

	@Getter
	private String value;

	private AuthRoles(String value) {
		this.value = value;
	}
}
