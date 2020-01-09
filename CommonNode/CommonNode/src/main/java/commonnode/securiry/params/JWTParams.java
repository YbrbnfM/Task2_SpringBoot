package commonnode.securiry.params;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import commonnode.Routes;
import commonnode.entities.Credential;
import commonnode.entities.Result;
import lombok.Getter;

public enum JWTParams {
	id("ShopDevJWT"), header("Authorization"), secretKey("1123"), authorities("authorities"), JWTValue("") {
		@Override
		public String getValue() {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			value = new RestTemplate().exchange(Routes.CUSTOMER_NODE.getValue() + "/login", HttpMethod.POST,
					new HttpEntity<>(new Credential(0, SystemAccounts.SERVICE.getLogin(),
							SystemAccounts.SERVICE.getDecryptPassword()), headers),
					new ParameterizedTypeReference<Result<String>>() {
					}).getBody().getValue();
			return value;
		}
	};

	@Getter
	String value;

	private JWTParams(String value) {
		this.value = value;
	}
}
