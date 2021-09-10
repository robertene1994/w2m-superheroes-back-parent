package com.robertene.superheroes.specification;

import java.util.Collection;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

public class QuerySpecification<T> implements Specification<T> {

	private static final long serialVersionUID = 6461819484732352113L;

	private transient SearchCriteria criteria;

	public QuerySpecification(SearchCriteria param) {
		this.criteria = param;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		if (criteria.getOperation() != FilterOperator.EXISTS && criteria.getOperation() != FilterOperator.NOT_EXISTS) {
			Path<?> fieldPath = fieldPath(root, criteria.getKey());
			return locateOperation(fieldPath, builder);
		} else {
			return existOperation(builder, root, query);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate locateOperation(Path<?> fieldPath, CriteriaBuilder builder) {
		switch (criteria.getOperation()) {
		case LIKE:
			return typeString(fieldPath, builder);
		case STARTS_WITH:
			return startWith(fieldPath, builder);
		case ILIKE:
			return equalsIgnoreCase(fieldPath, builder);
		case EQUAL:
			return builder.equal(fieldPath, criteria.getValue());
		case NOT_EQUAL:
			return builder.notEqual(fieldPath, criteria.getValue());
		case LOWER_THAN:
			return builder.lessThan((Path<Comparable>) fieldPath, (Comparable) criteria.getValue());
		case GREATER_THAN:
			return builder.greaterThan((Path<Comparable>) fieldPath, (Comparable) criteria.getValue());
		case LOWER_THAN_OR_EQUAL:
			return builder.lessThanOrEqualTo((Path<Comparable>) fieldPath, (Comparable) criteria.getValue());
		case GREATER_THAN_OR_EQUAL:
			return builder.greaterThanOrEqualTo((Path<Comparable>) fieldPath, (Comparable) criteria.getValue());
		case IS_NULL:
			return builder.isNull(fieldPath);
		case IN:
			return typeCollection(fieldPath, builder);
		case NOT_IN:
			return typeCollection(fieldPath, builder).not();
		case NOT_NULL:
			return builder.isNotNull(fieldPath);
		case IS_MEMBER:
			return builder.isMember(criteria.getValue(), fieldPath.as(Set.class));
		case IS_NOT_MEMBER:
			return builder.not(builder.isMember(criteria.getValue(), fieldPath.as(Set.class)));
		case FULL_TEXT_SEARCH:
			return fullTextSearch(fieldPath, builder);
		default:
			return null;
		}
	}

	public Predicate typeNotCollection(Path<?> fieldPath, CriteriaBuilder builder) {
		if (criteria.getValue() instanceof Collection) {
			return builder.not(fieldPath.as(String.class).in(criteria.getValue()));
		} else {
			return builder.notEqual(fieldPath, criteria.getValue());
		}
	}

	public Predicate typeCollection(Path<?> fieldPath, CriteriaBuilder builder) {
		if (criteria.getValue() instanceof Collection) {
			Object[] values = Collection.class.cast(criteria.getValue()).toArray();
			if (values.length > 0 && values[0] instanceof String) {
				return fieldPath.as(String.class).in(values);
			} else {
				return fieldPath.in(values);
			}
		} else {
			return builder.equal(fieldPath, criteria.getValue());
		}
	}

	public Predicate typeString(Path<?> fieldPath, CriteriaBuilder builder) {
		if (fieldPath.getJavaType() == String.class) {
			return builder.like(builder.lower(fieldPath.as(String.class)),
					"%" + criteria.getValue().toString().toLowerCase() + "%");
		} else {
			return builder.equal(fieldPath, criteria.getValue());
		}
	}

	public Predicate startWith(Path<?> fieldPath, CriteriaBuilder builder) {
		return typeILike(fieldPath, builder, criteria.getValue().toString() + "%");
	}

	public Predicate containt(Path<?> fieldPath, CriteriaBuilder builder) {
		return typeILike(fieldPath, builder, "%" + criteria.getValue().toString() + "%");
	}

	public Predicate equalsIgnoreCase(Path<?> fieldPath, CriteriaBuilder builder) {
		return typeILike(fieldPath, builder, criteria.getValue().toString());
	}

	public Predicate typeILike(Path<?> fieldPath, CriteriaBuilder builder, String pattern) {
		return builder
				.isTrue(builder.function("ilike", Boolean.class, fieldPath.as(String.class), builder.literal(pattern)));
	}

	public Predicate fullTextSearch(Path<?> fieldPath, CriteriaBuilder builder) {
		String[] values = criteria.getValue().toString().toLowerCase().split("\\s+");
		String pattern = String.join(":* & ", values);
		return builder.isTrue(builder.function("fts", Boolean.class, fieldPath.as(String.class),
				builder.literal(values.length > 0 ? pattern + ":*" : "")));
	}

	public Predicate existOperation(CriteriaBuilder builder, Root<T> root, CriteriaQuery<?> query) {
		if (criteria.getOperation() == FilterOperator.EXISTS) {
			Subquery<?> subquery = this.createExistsSubquery(root, query, builder,
					criteria.getSubQueryCriteria().getSubqueryClass());
			return builder.exists(subquery);
		} else if (criteria.getOperation() == FilterOperator.NOT_EXISTS) {
			Subquery<?> subquery = this.createExistsSubquery(root, query, builder,
					criteria.getSubQueryCriteria().getSubqueryClass());
			return builder.not(builder.exists(subquery));
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private <K> Subquery<Integer> createExistsSubquery(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder,
			Class<K> subqueryClass) {
		SearchCriteria.SubQueryCriteria subqueryCriteria = criteria.getSubQueryCriteria();

		Subquery<Integer> subQuery = query.subquery(Integer.class);
		Root<K> subRoot = subQuery.from(subqueryClass);
		subQuery.select(builder.literal(1));

		Predicate sqPredicate = null;
		if (subqueryCriteria.getCriteria() != null) {
			for (SearchCriteria sqCriteria : subqueryCriteria.getCriteria()) {
				Predicate predicate = new QuerySpecification<K>(sqCriteria).toPredicate(subRoot, query, builder);
				if (sqPredicate == null) {
					sqPredicate = predicate;
				} else {
					sqPredicate = builder.and(sqPredicate, predicate);
				}
			}
		} else {
			Specification<K> subquerySpec = (Specification<K>) subqueryCriteria.getSpecification();
			if (subquerySpec != null) {
				sqPredicate = subquerySpec.toPredicate(subRoot, query, builder);
			}
		}
		if (subqueryCriteria.getParentRefCriteria() != null) {
			Path<T> parentField = fieldPath(root, subqueryCriteria.getParentRefCriteria().getKey());
			Path<K> childField = fieldPath(subRoot, (String) subqueryCriteria.getParentRefCriteria().getValue());
			Predicate parentPredicate = builder.equal(parentField, childField);
			if (sqPredicate != null) {
				sqPredicate = builder.and(sqPredicate, parentPredicate);
			} else {
				sqPredicate = parentPredicate;
			}
		}

		subQuery.where(sqPredicate);
		return subQuery;
	}

	private <Z> Path<Z> fieldPath(Path<Z> root, String fieldname) {
		String[] fields = fieldname.split("\\.");
		Path<Z> result = root;
		for (String field : fields) {
			result = result.get(field);
		}
		return result;
	}
}
