package com.robertene.superheroes.exception;

import java.util.Map;

import com.robertene.superheroes.domain.dto.error.ErrorParams;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(of = { "code" })
@EqualsAndHashCode(callSuper = false)
public class W2MException extends Exception {

	private static final long serialVersionUID = -568703452341099040L;

	public static final int DEFAULT_APP_ERROR_STATUS_CODE = 418;

	private Integer httpStatus;
	private Integer code;
	private ErrorParams params;

	public W2MException(String msg) {
		this(DEFAULT_APP_ERROR_STATUS_CODE, DEFAULT_APP_ERROR_STATUS_CODE, msg, null, null);
	}

	public W2MException(int code, String msg) {
		this(DEFAULT_APP_ERROR_STATUS_CODE, code, msg, null, null);
	}

	public W2MException(int httpStatus, int code, String msg) {
		this(httpStatus, code, msg, null, null);
	}

	public W2MException(int code, String msg, Throwable cause) {
		this(DEFAULT_APP_ERROR_STATUS_CODE, code, msg, null, cause);
	}

	public W2MException(int httpStatus, int code, String msg, Map<String, Object> params, Throwable cause) {
		super(msg, cause);
		this.setHttpStatus(httpStatus);
		this.setCode(code);
		this.setParams(params == null ? null : ErrorParams.of(params));
	}

	public Integer getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(Integer httpStatus) {
		this.httpStatus = httpStatus;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public ErrorParams getParams() {
		return params;
	}

	public void setParams(ErrorParams params) {
		this.params = params;
	}
}
