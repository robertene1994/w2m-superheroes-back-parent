package com.robertene.superheroes.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robertene.superheroes.dto.ErrorInfo;
import com.robertene.superheroes.exception.AccessDeniedException;
import com.robertene.superheroes.exception.InvalidTokenException;
import com.robertene.superheroes.exception.W2MException;

import reactor.core.publisher.Mono;

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFilter.class);

	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();

		if (RouterValidator.isSecured.test(request)) {
			try {
				List<String> authHeaders = request.getHeaders().getOrEmpty("Authorization");
				if (authHeaders.isEmpty()) {
					throw new AccessDeniedException();
				}
				
				String authHeader = authHeaders.get(0);
				if (authHeader == null) {
					throw new AccessDeniedException();
				}
					
				if (!authHeader.startsWith("Bearer ")) {
					throw new InvalidTokenException();
				}
				
				if (!validateToken(authHeader)) {
					throw new InvalidTokenException();
				}
			} catch (W2MException ex) {
				return onError(exchange, ex);
			}
		}
		return chain.filter(exchange);
	}

	private boolean validateToken(String authHeader) {
		String token = authHeader.substring("Bearer ".length()).trim();
		String url = "http://localhost:8081/api/auth/session/validate";
		HttpHeaders headers = new HttpHeaders();
		headers.add("x-forwarded-host", "localhost:8080");
		return new RestTemplate().postForObject(url,new HttpEntity<>(token,headers), Boolean.class);
	}

	private Mono<Void> onError(ServerWebExchange exchange, W2MException ex) {
		ErrorInfo error = ErrorInfo.of(ex);
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
		
		try {
			DataBuffer dataBuffer = response.bufferFactory().wrap(objectMapper.writeValueAsString(error).getBytes());
			return response.writeWith(Mono.just(dataBuffer));
		} catch (JsonProcessingException e) {
			return response.setComplete();
		}
	}
}
