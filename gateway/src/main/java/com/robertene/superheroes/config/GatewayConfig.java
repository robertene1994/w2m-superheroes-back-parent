package com.robertene.superheroes.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableHystrix
@Configuration
public class GatewayConfig {
	
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, AuthenticationFilter filter) {
        return builder.routes()
        		.route("h2-console", r -> r.path("/h2-console/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://Auth"))
        		.route("swagger-ui-auth", r -> r.path("/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs", "/webjars/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://Auth"))
        		.route("swagger-ui-superheroes", r -> r.path("/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs", "/webjars/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://Superheroes"))
                .route("auth", r -> r.path("/api/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://Auth"))
                .route("superheroes", r -> r.path("/api/superheroes/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://Superheroes"))
                .build();
    }
}
