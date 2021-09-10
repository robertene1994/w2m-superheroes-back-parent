package com.robertene.superheroes.domain.entity.base;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public abstract class EntityBase<I> {

	@ApiModelProperty(value = "Identificador único de la entidad en la base de datos.", 
			name = "id", dataType = "Long", example = "1")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	I id;
}
