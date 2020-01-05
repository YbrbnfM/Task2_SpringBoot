package commonnode.securiry.params;

import lombok.Getter;

public enum JWTParams {
	id("ShopDevJWT"),
	header("Authorization"),
	secretKey("1123"),
	authorities("authorities");

	@Getter
	final String value;

	private JWTParams(String value) {
		this.value = value;
	}
}
