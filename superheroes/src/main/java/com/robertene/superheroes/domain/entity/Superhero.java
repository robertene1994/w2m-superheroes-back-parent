package com.robertene.superheroes.domain.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.robertene.superheroes.config.DBConfiguration;
import com.robertene.superheroes.domain.entity.base.EntityBase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ApiModel(description = "Clase que representa las entidades de tipo superhéroe")
@Data
@ToString(callSuper = true, of = { "firstName", "lastName", "alias" })
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "superheroes", schema = DBConfiguration.DB_SCHEMA)
public class Superhero extends EntityBase<Long> {

	@ApiModelProperty(value = "El nombre del superhéroe", name = "firstName", dataType = "String", example = "Peter Benjamin")
	@Column(name = "first_name")
	private String firstName;

	@ApiModelProperty(value = "Los apellidos del superhéroe", name = "lastName", dataType = "String", example = "Parker")
	@Column(name = "last_name")
	private String lastName;

	@ApiModelProperty(value = "El alias del superhéroe", name = "alias", dataType = "String", example = "Spider-Man")
	@Column(name = "alias")
	private String alias;

	@ApiModelProperty(value = "La fecha de nacimiento del superhéroe", name = "birthdate", dataType = "Date", example = "2001-08-10")
	@Column(name = "birthdate")
	private LocalDate birthdate;

	@ApiModelProperty(value = "La ocupación del superhéroe", name = "occupation", dataType = "String", example = "Fotógrafo")
	@Column(name = "occupation")
	private String occupation;

	@ApiModelProperty(value = "La estatura del superhéroe", name = "height", dataType = "Double", example = "1.78")
	@Column(name = "height")
	private String height;

	@ApiModelProperty(value = "El peso del superhéroe", name = "weight", dataType = "Double", example = "76.0")
	@Column(name = "weight")
	private String weight;

	@ApiModelProperty(value = "La biografía del superhéroe", name = "El peso del superhéroe", dataType = "String", example = "Peter Parker era un adolescente...")
	@Column(name = "biography")
	private String biography;
}
