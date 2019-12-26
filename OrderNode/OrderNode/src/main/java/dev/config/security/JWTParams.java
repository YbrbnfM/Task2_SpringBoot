package dev.config.security;

import lombok.Getter;

public enum JWTParams {
	header("Authorization"),
	secretKey("1123");

	@Getter
	final String value;

	private JWTParams(String value) {
		this.value = value;
	}
}
