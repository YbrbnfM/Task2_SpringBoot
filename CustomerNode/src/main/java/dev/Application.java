package dev;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "dev")
public class Application {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		Map<String, Object> properties = new HashMap<>();
		properties.put("server.port", "8083");
		//properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL94Dialect");
		app.setDefaultProperties(properties);
		app.run(args);
	}

}
