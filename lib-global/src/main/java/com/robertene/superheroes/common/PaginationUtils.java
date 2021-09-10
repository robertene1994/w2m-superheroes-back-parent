package com.robertene.superheroes.common;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * Clase que representa el servicio que contiene diferentes métodos de utilidad
 * para las funciones de paginación de Spring Data.
 * 
 * @author Robert Ene
 *
 */
@Service
public class PaginationUtils {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(PaginationUtils.class);

	public Sort generateSort(List<String> sortFields, Sort.Direction sortDirection) {
		if (ObjectUtils.isEmpty(sortFields)) {
			return Sort.unsorted();
		}
		if (sortDirection == null) {
			sortDirection = Sort.DEFAULT_DIRECTION;
		}
		return Sort.by(sortDirection, generateSortFields(sortFields));
	}

	private String[] generateSortFields(List<String> sortFields) {
		return sortFields.stream().<String>map(field -> {
			if (field.contains("_")) {
				return Arrays.asList(field.split("_")).stream()
						.reduce((str, part) -> str + StringUtils.capitalize(part)).get();
			}
			return field;
		}).toArray(String[]::new);
	}

	public Pageable generatePageableRequest(List<String> sortFields, Sort.Direction order, Integer pageNumber,
			Integer pageSize) {
		return generatePageableRequest(generateSort(sortFields, order), pageNumber, pageSize);
	}

	public Pageable generatePageableRequest(Sort sort, Integer pageNumber, Integer pageSize) {
		return PageRequest.of(pageNumber - 1, pageSize, sort);
	}

	public Pageable generateNativePageableRequest(List<String> sortFields, Sort.Direction order, Integer pageNumber,
			Integer pageSize) {
		return generatePageableRequest(generateSortNative(sortFields, order), pageNumber, pageSize);
	}

	public Sort generateSortNative(List<String> sortFields, Sort.Direction sortDirection) {
		if (ObjectUtils.isEmpty(sortFields)) {
			return Sort.unsorted();
		}
		if (sortDirection == null) {
			sortDirection = Sort.DEFAULT_DIRECTION;
		}
		return Sort.by(sortDirection, generateNativeSortFields(sortFields));
	}

	private String[] generateNativeSortFields(List<String> sortFields) {
		return sortFields.stream().toArray(String[]::new);
	}
}
