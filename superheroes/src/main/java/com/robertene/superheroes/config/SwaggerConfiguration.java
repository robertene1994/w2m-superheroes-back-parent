package com.robertene.superheroes.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;

import com.robertene.superheroes.config.swagger.SwaggerCommonConfig;

import springfox.documentation.service.Tag;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration extends SwaggerCommonConfig {

	public static final String SUPERHERO_TAG = "Superhero Controller";

	@Override
	protected List<Tag> getSwaggerTags() {
		List<Tag> tags = new ArrayList<>();
		tags.add(new Tag(SUPERHERO_TAG, "Controlador para la gestión de los superhéroes."));
		return tags;
	}
}
