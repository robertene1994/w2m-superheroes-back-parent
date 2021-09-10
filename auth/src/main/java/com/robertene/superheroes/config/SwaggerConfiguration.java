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

	public static final String LOGIN_TAG = "Login Controller";
	public static final String USER_TAG = "User Controller";

	@Override
	protected List<Tag> getSwaggerTags() {
		List<Tag> tags = new ArrayList<>();
		tags.add(new Tag(LOGIN_TAG, "Controlador para la gestión de la sesión (autenticación) en el sistema."));
		tags.add(new Tag(USER_TAG, "Controlador para la gestión de los usuarios."));
		return tags;
	}
}
