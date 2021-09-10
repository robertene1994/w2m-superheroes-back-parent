package com.robertene.superheroes.specification;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Condition {

	private String field;
	private FilterOperator operator;
	private Object value;
}
