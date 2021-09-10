package com.robertene.superheroes.domain.exception;

import org.springframework.http.HttpStatus;

import com.robertene.superheroes.domain.dto.error.ErrorInfo;
import com.robertene.superheroes.exception.W2MException;

public class UnathorizedException extends W2MException {

	private static final long serialVersionUID = 520779563075562143L;

	public UnathorizedException() {
		this("Usuario no autorizado");
	}

	public UnathorizedException(String msg) {
		super(HttpStatus.UNAUTHORIZED.value(), ErrorInfo.ErrorCodes.INVALID_CREDENTIALS, msg);
	}
}
