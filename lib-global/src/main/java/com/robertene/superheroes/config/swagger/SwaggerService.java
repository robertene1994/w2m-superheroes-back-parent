package com.robertene.superheroes.config.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@EnableConfigurationProperties
@ConfigurationProperties(prefix = "documentation.swagger.services")
@Data
@ToString(callSuper = true, of = { "name", "url", "version" })
@Builder
public class SwaggerService {

	private String name;
	private String url;
	private String version;
}
