package commonnode.securiry.params;

import lombok.Getter;

public enum AuthRoles {
	USER("USER"),
	ADMIN("ADMIN"),
	SERVICE("SERVICE");

	@Getter
	private String value;

	private AuthRoles(String value) {
		this.value = value;
	}
}
