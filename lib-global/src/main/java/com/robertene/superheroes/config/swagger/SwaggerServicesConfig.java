package com.robertene.superheroes.config.swagger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

@Primary
@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "documentation.swagger")
public class SwaggerServicesConfig {

	private List<SwaggerService> swaggerServices;

	public List<SwaggerService> getSwaggerServices() {
		if (swaggerServices == null) {
			loadSwaggerServices();
		}
		return swaggerServices;
	}

	private void loadSwaggerServices() {
		this.swaggerServices = new ArrayList<>();
		this.swaggerServices.add(SwaggerService.builder()
				.name("Auth")
				.url("http://localhost:8081/v2/api-docs")
				.version("2.0")
				.build());
		this.swaggerServices.add(SwaggerService.builder()
				.name("Superheroes")
				.url("http://localhost:8082/v2/api-docs")
				.version("2.0")
				.build());
	}
}
