package commonnode;

import lombok.Getter;

public enum Routes {
	CUSTOMER_NODE("http://localhost:8083/api"),
	OFFER_NODE("http://localhost:8084/api"),
	ORDER_NODE("http://localhost:8085/api");

	@Getter
	private String value;
	
	private Routes(String value) {
		this.value = value;
	}
}
