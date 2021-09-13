package com.robertene.superheroes.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.robertene.superheroes.common.TokenHelper;
import com.robertene.superheroes.domain.dto.LoginRequest;
import com.robertene.superheroes.domain.dto.LoginResponse;
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
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(LoginService.class);

	@Autowired
	private UserService userService;

	@Autowired
	private TokenHelper tokenHelper;

	public LoginResponse login(LoginRequest loginRequest) throws W2MException {
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();

		if (username == null || username.isEmpty()) {
			throw new UnathorizedException();
		}

		User user = userService.findByUsername(username);
		if (user == null) {
			throw new UnathorizedException();
		}

		String passwordHash = user.getPassword();
		if (passwordHash == null || passwordHash.isEmpty() || !BCrypt.checkpw(password, passwordHash)) {
			throw new UnathorizedException();
		}

		SessionData sessionData = SessionData.builder()
				.userId(user.getId())
				.username(user.getUsername())
				.role(user.getRole())
				.email(user.getEmail())
				.build();
		String token = tokenHelper.generate(sessionData);
		return LoginResponse.builder().authorization(token).username(username).build();
	}

	public boolean validateToken(String token) {
		return tokenHelper.validate(token);
	}

	public void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}
}
