package com.robertene.superheroes.config.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import com.robertene.superheroes.domain.entity.User;
import com.robertene.superheroes.repository.UserRepository;

/**
 * 
 * 
 * @author Robert
 *
 */
@Component
public class BasicAuthAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserRepository usuarioRepository;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
		String username = auth.getName();
		String password = auth.getCredentials().toString();

		UsernamePasswordAuthenticationToken invalidUser = new UsernamePasswordAuthenticationToken(null, null, null);

		if (username == null || username.isEmpty()) {
			return invalidUser;
		}
		User user = usuarioRepository.findByUsername(username).orElse(null);

		if (user == null) {
			return invalidUser;
		}

		String passwordHash = user.getPassword();

		if (passwordHash == null || passwordHash.isEmpty() || !BCrypt.checkpw(password, passwordHash)) {
			return invalidUser;
		}

		auth = new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
		auth.setDetails(authentication.getDetails());
		return auth;

	}

	@Override
	public boolean supports(Class<? extends Object> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
