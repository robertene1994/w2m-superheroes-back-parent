package com.robertene.superheroes.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.robertene.superheroes.config.security.util.TokenHelper;
import com.robertene.superheroes.domain.dto.session.SessionData;
import com.robertene.superheroes.domain.entity.User;
import com.robertene.superheroes.domain.exception.UnathorizedException;
import com.robertene.superheroes.exception.W2MException;

/**
 * Clase que define los servicios correspondientes a la autenticación y gestión
 * de la sesión de los usuarios.
 * 
 * @author Robert Ene
 *
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class LoginService {
	public static final Logger LOG = LoggerFactory.getLogger(LoginService.class);

	@Autowired
	private UserService userService;

	@Autowired
	private TokenHelper tokenHelper;

	public String buildTokenFromSpringSession() throws W2MException {

		Authentication auth = getAuth();

		if (auth == null) {
			throw new UnathorizedException();
		}
		String username = auth.getName();
		String dbUsername = username.contains("@") ? username.substring(0, username.indexOf("@")) : username;

		User user = userService.findByUsername(dbUsername);

		if (user == null) {
			throw new UnathorizedException();
		}

		SessionData sessionData = SessionData.builder().userId(user.getId()).username(user.getUsername())
				.role(user.getRole()).email(user.getEmail()).build();

		return tokenHelper.generate(sessionData);
	}

	public boolean validateToken(String token) {
		return tokenHelper.validate(token);
	}

	public Authentication getAuth() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}
}
