package com.robertene.superheroes.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.robertene.superheroes.config.gateway.GatewayFilter;

@Configuration
@Order(1)
public class GatewayConfiguration extends WebSecurityConfigurerAdapter {

	@Value("${gateway.filter.disabled}")
	private Boolean isDisabled;

	@Value("${gateway.filter.allowed-forwards}")
	private List<String> allowedForwards;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
				.antMatchers("/api/auth/session/**", "/h2_console/**", "/swagger-resources/**", "/swagger-ui.html",
						"/v2/api-docs", "/webjars/**")
				.permitAll().and()
				.addFilterBefore(new GatewayFilter(isDisabled, allowedForwards), BasicAuthenticationFilter.class)
				.headers().frameOptions().sameOrigin();
	}
}
