package com.robertene.superheroes.config.swagger;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Clase abstracta que contiene la configuración común de Swagger. Esta clase
 * debe ser extendida con el objetivo de definir las operaciones (tags)
 * proporcionadas.
 * 
 * @author Robert Ene
 * 
 */
public abstract class SwaggerCommonConfig {

	public static final String AUTHORIZATION_HEADER = "Authorization";

	@Bean
	public Docket api() throws XmlPullParserException, IOException {
		Model model = new MavenXpp3Reader().read(new FileReader("../pom.xml"));

		Contact contact = new Contact("Robert Ene", "http://www.robertene.com/", "robertene1994@gmail.com");

		ApiInfo apinInfo = new ApiInfo(model.getName(), model.getDescription(), model.getVersion(), "termsOfServiceURL",
				contact, "Licencia", "Licencia URL", new ArrayList<>());

		Docket docket = new Docket(DocumentationType.SWAGGER_2).apiInfo(apinInfo)
				.securityContexts(List.of(securityContext())).securitySchemes(List.of(apiKey()))
				.useDefaultResponseMessages(false).select()
				.apis(RequestHandlerSelectors.basePackage("com.robertene.superheroes.controller")).build()
				.forCodeGeneration(true).globalOperationParameters(operationParameters());

		List<Tag> swaggerTags = getSwaggerTags();
		if (!swaggerTags.isEmpty()) {
			swaggerTags.forEach(docket::tags);
		}
		return docket;
	}

	private List<Parameter> operationParameters() {
		List<Parameter> headers = new ArrayList<>();
		headers.add(new ParameterBuilder().name("x-forwarded-host").modelRef(new ModelRef("string"))
				.parameterType("header").required(false).defaultValue("localhost:8080").build());
		return headers;
	}

	protected abstract List<Tag> getSwaggerTags();

	private ApiKey apiKey() {
		return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return List.of(new SecurityReference("JWT", authorizationScopes));
	}
}
