package com.robertene.superheroes.exception;

import org.springframework.http.HttpStatus;

import com.robertene.superheroes.dto.ErrorInfo;

public class InvalidTokenException extends W2MException {

	private static final long serialVersionUID = 5207795630755628143L;

	public InvalidTokenException() {
		super(HttpStatus.UNAUTHORIZED.value(), ErrorInfo.ErrorCodes.INVALID_TOKEN, "Token de sesión no válido");
	}
}
