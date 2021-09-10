package com.robertene.superheroes.domain.dto;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.robertene.superheroes.common.JsonUtils;
import com.robertene.superheroes.exception.W2MRuntimeException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DtoWrapper<T> {
	private static final Logger LOG = LoggerFactory.getLogger(DtoWrapper.class);

	@Getter
	private final T bean;
	
	@JsonValue
	@JsonRawValue
	private final String jsonValue;

	public static <E> DtoWrapper<E> of(final E bean) {
		String json;
		try {
			json = JsonUtils.toJSON(bean);
			return new DtoWrapper<>(bean, json);
		} catch (IOException e) {
			LOG.error("Error serializando el bean", e);
			throw new W2MRuntimeException("Error serializando el bean: " + bean);
		}
	}

	public static <E> Iterable<DtoWrapper<E>> of(final Iterable<E> beans) {
		return StreamSupport.stream(beans.spliterator(), false).<DtoWrapper<E>>map(DtoWrapper::of)
				.collect(Collectors.toList());
	}

	public static <E> DtoWrapper<E> of(final String jsonValue, Class<E> klass) {
		E bean;
		try {
			bean = JsonUtils.parseJSON(jsonValue, klass);
			return new DtoWrapper<>(bean, jsonValue);
		} catch (IOException e) {
			LOG.error("Error deserializando los datos JSON", e);
			throw new W2MRuntimeException("Error deserializando los datos JSON: " + jsonValue);
		}
	}
}
