package com.robertene.superheroes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.robertene.superheroes.exception.W2MException;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
@JsonInclude(Include.NON_NULL)
public class ErrorInfo {

	private final Integer code;
	private final String message;
	private final ErrorParams data;

	public static ErrorInfo of(W2MException ex) {
		return ErrorInfo.of(ex.getCode(), ex.getMessage(), ex.getParams());
	}

	public static ErrorInfo of(Integer code, String message) {
		return ErrorInfo.of(code, message, null);
	}

	public static class ErrorCodes {

		private ErrorCodes() {
		}

		public static final int UNKNOWN = 9999;

		public static final int ACCESS_DENIED = 1001;
		public static final int INVALID_CREDENTIALS = 1002;
		public static final int INVALID_TOKEN = 1003;

		public static final int DESCRIPTOR_UPDATE_ERROR = 2001;
		public static final int ELEMENT_NOT_FOUND = 2004;

		public static final int MISSING_REQUIRED_DATA = 3001;
		public static final int INVALID_DATA = 3002;
	}
}
