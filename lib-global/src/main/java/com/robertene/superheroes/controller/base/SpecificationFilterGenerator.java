package com.robertene.superheroes.controller.base;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robertene.superheroes.common.ParamConverterUtils;
import com.robertene.superheroes.exception.W2MException;
import com.robertene.superheroes.specification.Condition;
import com.robertene.superheroes.specification.FilterOperator;
import com.robertene.superheroes.specification.GroupConditions;
import com.robertene.superheroes.specification.QuerySpecificationsBuilder;

/**
 * Interfaz que define las operaciones necesarias para generar un objeto
 * Specification en base a una estructura Map.
 * 
 * @author Robert Ene
 *
 * @param <T> el tipo de la entidad.
 */
public interface SpecificationFilterGenerator<T> {
	public static final String PREFIX_FILTER = "cf_";

	public default Specification<T> getSpecificationFilter(MultiValueMap<String, String> params) throws W2MException {
		GroupConditions groupConditions = new GroupConditions();

		if (!CollectionUtils.isEmpty(params)) {
			params = checkSwaggerFilterParams(params);

			for (Map.Entry<String, List<String>> entry : params.entrySet()) {
				Condition condition = new Condition();
				String key = entry.getKey();
				String operador;
				if (key.startsWith(PREFIX_FILTER)) {
					String ketTrans = key.substring(PREFIX_FILTER.length());
					int index = ketTrans.lastIndexOf("_");
					key = ketTrans.substring(0, index);
					operador = ketTrans.substring(index + 1);
					FilterOperator operator = FilterOperator.of(operador);
					condition.setField(key);
					condition.setOperator(operator);

					if (StringUtils.hasLength(operador)
							&& (operator == FilterOperator.IN || operator == FilterOperator.NOT_IN)) {
						List<String> paramValuesOfKey = params.get(entry.getKey());
						String paramValueOfKey = params.getFirst(entry.getKey());
						if (paramValuesOfKey != null && paramValuesOfKey.size() > 1) {
							condition.setValue(ParamConverterUtils.verifyConvertTypeValueList(paramValuesOfKey));
						} else {
							condition.setValue(convertValueType(paramValueOfKey));
						}
					} else {
						condition.setValue(convertValueType(params.getFirst(entry.getKey())));
					}
					groupConditions.add(condition);
				}
			}
		}
		return getSpecificationGroupCondition(groupConditions);
	}

	public default Specification<T> getSpecificationGroupCondition(GroupConditions group) {
		QuerySpecificationsBuilder<T> builder = new QuerySpecificationsBuilder<>();
		if (!group.getConditions().isEmpty()) {
			for (Condition conditions : group.getConditions()) {
				builder.with(conditions.getField(), conditions.getOperator(), conditions.getValue());
			}
		}
		return builder.build();
	}

	public default Object convertValueType(String value) {
		if (ParamConverterUtils.isBlankOrNull(value)) {
			return ParamConverterUtils.isValueBlank(value);
		} else if (ParamConverterUtils.isInteger(value)) {
			return NumberUtils.createInteger(value);
		} else if (ParamConverterUtils.isDecimal(value)) {
			return NumberUtils.createDouble(value);
		} else if (ParamConverterUtils.isLocalDate(value)) {
			return LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE);
		} else if (ParamConverterUtils.isLocalDateTimeIsoJava(value)) {
			return LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
		} else if (ParamConverterUtils.isDateTypeFormatterIsoJavascript(value)) {
			return Instant.parse(value).atZone(ZoneId.systemDefault()).toLocalDateTime();
		} else if (ParamConverterUtils.isBoolean(value)) {
			return Boolean.valueOf(value);
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	private MultiValueMap<String, String> checkSwaggerFilterParams(MultiValueMap<String, String> params) {
		List<String> filters = params.get(PaginationController.FILTER_PARAMS_PARAM);
		if (filters != null && !filters.isEmpty()) {
			try {
				Map<String, String> filterParams = new ObjectMapper().readValue(filters.get(0), Map.class);
				filterParams.entrySet().forEach(entry -> params.add(entry.getKey(), entry.getValue()));
			} catch (JsonProcessingException e) {
				// se ignora
			}
		}
		return params;
	}
}
