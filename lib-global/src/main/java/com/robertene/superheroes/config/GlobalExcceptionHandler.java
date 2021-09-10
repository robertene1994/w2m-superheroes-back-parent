package com.robertene.superheroes.config;

import java.lang.reflect.Field;
import java.nio.file.AccessDeniedException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpServerErrorException;

import com.robertene.superheroes.domain.dto.error.ErrorInfo;
import com.robertene.superheroes.exception.W2MException;
import com.robertene.superheroes.exception.W2MRuntimeException;

/**
 * Clase que se encarga de la gestión centralizada de las excepciones producidas
 * en el sistema.
 * 
 * @author Robert Ene
 *
 */
@ControllerAdvice
public class GlobalExcceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(GlobalExcceptionHandler.class);

	@ResponseBody
	@ExceptionHandler(W2MException.class)
	public ResponseEntity<ErrorInfo> handleCustomException(W2MException ex) {
		ErrorInfo error = ErrorInfo.of(ex);
		return ResponseEntity.status(ex.getHttpStatus()).body(error);
	}

	@ResponseBody
	@ExceptionHandler(W2MRuntimeException.class)
	public ResponseEntity<ErrorInfo> handleCustomException(W2MRuntimeException ex) {
		ErrorInfo error = ErrorInfo.of(W2MException.DEFAULT_APP_ERROR_STATUS_CODE, ex.getMessage());
		return ResponseEntity.status(W2MException.DEFAULT_APP_ERROR_STATUS_CODE).body(error);
	}

	@ResponseBody
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorInfo> handleCustomException(AccessDeniedException ex) {
		ErrorInfo error = ErrorInfo.of(HttpStatus.FORBIDDEN.value(), ex.getMessage());
		return ResponseEntity.status(403).body(error);
	}

	@ResponseBody
	@ExceptionHandler(BindException.class)
	public ResponseEntity<ErrorInfo> handleBindException(BindException ex) {
		StringBuilder strBuilder = new StringBuilder();

		List<ObjectError> errorList = ex.getAllErrors();
		for (ObjectError itemerror : errorList) {
			String fieldName = "";
			try {
				Field field = itemerror.getClass().getSuperclass().getDeclaredField("field");
				field.setBoolean(this, true);
				fieldName = (String) field.get(itemerror);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			strBuilder
					.append(itemerror.getObjectName() + "(" + fieldName + ")=" + itemerror.getDefaultMessage() + ";\n");
		}

		ErrorInfo error = ErrorInfo.of(W2MException.DEFAULT_APP_ERROR_STATUS_CODE, strBuilder.toString());
		return ResponseEntity.status(W2MException.DEFAULT_APP_ERROR_STATUS_CODE).body(error);
	}

	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorInfo> handleAllException(Exception ex) {
		LOG.error("Excepción inesperada", ex);
		int httpStatus = (ex instanceof HttpServerErrorException)
				? HttpServerErrorException.class.cast(ex).getRawStatusCode()
				: HttpStatus.INTERNAL_SERVER_ERROR.value();
		ErrorInfo error = ErrorInfo.of(httpStatus, ex.getMessage());

		return ResponseEntity.status(httpStatus).body(error);
	}
}
