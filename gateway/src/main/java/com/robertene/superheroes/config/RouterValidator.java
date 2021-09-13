package com.robertene.superheroes.config;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;

public class RouterValidator {

	public static final List<String> openApiEndpoints = List.of("/h2-console/**", "/swagger-resources/**", "/swagger-ui.html*",
	        "/v2/api-docs", "/webjars/**", "/api/auth/session/**");

	public static final Predicate<ServerHttpRequest> isSecured = request -> openApiEndpoints.stream()
			.noneMatch(uri -> new AntPathMatcher().match(uri, request.getURI().getPath()));
}
