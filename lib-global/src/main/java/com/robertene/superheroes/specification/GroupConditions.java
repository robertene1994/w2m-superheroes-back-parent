package com.robertene.superheroes.specification;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupConditions {

	@Getter
	private final List<Condition> conditions = new ArrayList<>();

	@Getter
	@Setter
	private LogicalOperator logicalOp = LogicalOperator.AND;

	public void add(Condition val) {
		this.conditions.add(val);
	}

	public void addAll(List<Condition> val) {
		this.conditions.addAll(val);

	}

	public void clear() {
		conditions.clear();
	}
}
