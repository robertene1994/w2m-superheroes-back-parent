package com.robertene.superheroes.domain.exception;

import org.springframework.http.HttpStatus;

import com.robertene.superheroes.domain.dto.error.ErrorInfo;
import com.robertene.superheroes.exception.W2MException;

public class InvalidTokenException extends W2MException {

	private static final long serialVersionUID = 5207795630755628143L;

	public InvalidTokenException() {
		super(HttpStatus.UNAUTHORIZED.value(), ErrorInfo.ErrorCodes.INVALID_TOKEN, "Token de sesión no válido");
	}
}
