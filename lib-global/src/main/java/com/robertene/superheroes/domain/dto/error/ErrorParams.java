package com.robertene.superheroes.domain.dto.error;

import java.util.HashMap;
import java.util.Map;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ErrorParams extends HashMap<String, Object> {

	private static final long serialVersionUID = -765004825907906705L;

	private ErrorParams(Map<String, Object> params) {
		super(params);
	}

	public static ErrorParams of(Map<String, Object> params) {
		return new ErrorParams(params);
	}
}