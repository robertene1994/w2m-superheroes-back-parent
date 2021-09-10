package com.robertene.superheroes.config.security.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.robertene.superheroes.config.security.BasicAuthAuthenticationProvider;

@Configuration
@Order(3)
public class BasicAuthConfiguration extends WebSecurityConfigurerAdapter {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(BasicAuthConfiguration.class);

	@Autowired
	private BasicAuthAuthenticationProvider basicAuthAuthenticationProvider;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/auth/session/login")
        	.authorizeRequests().anyRequest()
        	.authenticated()
        	.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().httpBasic();
    }
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(basicAuthAuthenticationProvider);
	}
}
