package com.robertene.superheroes.exception;

public class W2MRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -6536179916629206696L;

	public W2MRuntimeException(String msg) {
		super(msg);
	}

	public W2MRuntimeException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
