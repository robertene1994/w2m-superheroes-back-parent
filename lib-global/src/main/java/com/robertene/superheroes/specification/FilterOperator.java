package com.robertene.superheroes.specification;

import com.robertene.superheroes.exception.W2MRuntimeException;

import lombok.Getter;

public enum FilterOperator {
	LIKE("like"), ILIKE("iLike"), STARTS_WITH("start"), IN("in"), NOT_IN("notIn"), NOT_NULL("notNull"),
	IS_NULL("isNull"), EQUAL("eq"), NOT_EQUAL("neq"), LOWER_THAN("lt"), LOWER_THAN_OR_EQUAL("lte"), GREATER_THAN("gt"),
	GREATER_THAN_OR_EQUAL("gte"), IS_MEMBER(null), IS_NOT_MEMBER(null), EXISTS_EQUALS(null), EXISTS_LIKE(null),
	EXISTS(null), NOT_EXISTS(null), FULL_TEXT_SEARCH("fts");

	@Getter
	private final String code;

	private FilterOperator(String code) {
		this.code = code;
	}

	public static FilterOperator of(String code) {
		for (FilterOperator fo : FilterOperator.values()) {
			if (code.equals(fo.code)) {
				return fo;
			}
		}
		throw new W2MRuntimeException("Operator no soportado: " + code);
	}
}
