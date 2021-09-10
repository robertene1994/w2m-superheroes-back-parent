package com.robertene.superheroes.exception;

import org.springframework.http.HttpStatus;

import com.google.common.collect.ImmutableMap;
import com.robertene.superheroes.domain.dto.error.ErrorInfo;

public class ElementNotFoundException extends W2MException {

	private static final long serialVersionUID = 5207795630755628143L;

	public ElementNotFoundException(String entity, Object id) {
		super(HttpStatus.NOT_FOUND.value(), ErrorInfo.ErrorCodes.ELEMENT_NOT_FOUND,
				String.format("El elemento '%s' con el ID '%s' no existe en el sistema", entity, id),
				ImmutableMap.of("entity", entity, "id", id), null);
	}

	public ElementNotFoundException(String msg) {
		super(HttpStatus.NOT_FOUND.value(), ErrorInfo.ErrorCodes.ELEMENT_NOT_FOUND, msg);
	}
}
