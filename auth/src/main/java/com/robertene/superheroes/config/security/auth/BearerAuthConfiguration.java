package com.robertene.superheroes.config.security.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.robertene.superheroes.common.CurrentThreadService;
import com.robertene.superheroes.config.security.filter.JWTAuthFilter;
import com.robertene.superheroes.config.security.util.TokenHelper;

@EnableWebSecurity
@Configuration
@Order(4)
public class BearerAuthConfiguration extends WebSecurityConfigurerAdapter {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(BearerAuthConfiguration.class);

	@Autowired
	private TokenHelper tokenHelper;

	@Autowired
	private CurrentThreadService currentThreadService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
				.antMatchers("/h2-console/**", "/swagger-ui**", "/api/auth/session/**").permitAll().and().headers()
				.frameOptions().disable();

		http.csrf().disable().authorizeRequests().filterSecurityInterceptorOncePerRequest(true)
			.antMatchers("/api/**")
			.authenticated().anyRequest().permitAll()
			.and()
			.addFilterAfter(new JWTAuthFilter(tokenHelper, currentThreadService), BasicAuthenticationFilter.class)
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.logout().permitAll();
	}
}
