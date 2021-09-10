package com.robertene.superheroes.exception;

import org.springframework.http.HttpStatus;

import com.robertene.superheroes.domain.dto.error.ErrorInfo;

public class InvalidInputDataException extends W2MException {

	private static final long serialVersionUID = 5207795630755628143L;

	public InvalidInputDataException() {
		super(HttpStatus.I_AM_A_TEAPOT.value(), ErrorInfo.ErrorCodes.INVALID_DATA,
				"Los datos de entrada no son válidos");
	}

	public InvalidInputDataException(String msg) {
		super(HttpStatus.I_AM_A_TEAPOT.value(), ErrorInfo.ErrorCodes.INVALID_DATA, msg);
	}
}
