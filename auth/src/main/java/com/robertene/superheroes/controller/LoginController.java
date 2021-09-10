package com.robertene.superheroes.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robertene.superheroes.config.SwaggerConfiguration;
import com.robertene.superheroes.domain.dto.LoginResponse;
import com.robertene.superheroes.exception.W2MException;
import com.robertene.superheroes.service.LoginService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = SwaggerConfiguration.LOGIN_TAG)
@RestController
@RequestMapping({ "${w2m.rest.url.prefix}/session" })
public class LoginController {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private LoginService loginService;

	@ApiOperation(value = "Login", nickname = "login")
	@GetMapping(value = "/login")
	public LoginResponse login() throws W2MException {
		String token = loginService.buildTokenFromSpringSession();
		return LoginResponse.builder().authorization(token).username(loginService.getAuth().getName()).build();
	}

	@ApiOperation(value = "Validación", nickname = "validate post")
	@PostMapping(value = "/validate")
	public boolean validatePost(@ApiParam(value = "token", required = true) @RequestBody String token) {
		return loginService.validateToken(token);
	}

	@ApiOperation(value = "Logout", nickname = "logout")
	@GetMapping("/logout")
	public void logout() {
		loginService.logout();
	}
}
