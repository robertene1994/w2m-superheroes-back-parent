package com.robertene.superheroes.exception;

import org.springframework.http.HttpStatus;

import com.robertene.superheroes.dto.ErrorInfo.ErrorCodes;

public class AccessDeniedException extends W2MException {

	private static final long serialVersionUID = 5207795630755628143L;

	public AccessDeniedException() {
		super(HttpStatus.FORBIDDEN.value(), ErrorCodes.ACCESS_DENIED, "Acceso denegado");
	}
}
