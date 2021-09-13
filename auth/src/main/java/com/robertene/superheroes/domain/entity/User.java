package com.robertene.superheroes.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.robertene.superheroes.config.DBConfiguration;
import com.robertene.superheroes.domain.entity.base.EntityBase;
import com.robertene.superheroes.domain.enums.Role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ApiModel(description = "Clase que representa las entidades de tipo usuario")
@Data
@ToString(callSuper = true, of = { "username", "role", "firstName", "lastName", "email" })
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "users", schema = DBConfiguration.DB_SCHEMA)
public class User extends EntityBase<Long> {

	@ApiModelProperty(value = "El nombre de usuario único en la base de datos", name = "username", dataType = "String", example = "juan")
	@Column(name = "username")
	private String username;

	@ApiModelProperty(value = "El rol del usuario", name = "role", dataType = "Role", example = "USER")
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private Role role;

	@ApiModelProperty(value = "El nombre del usuario", name = "firstName", dataType = "String", example = "Juan")
	@Column(name = "first_name")
	private String firstName;

	@ApiModelProperty(value = "Los apellidos del usuario", name = "lastName", dataType = "String", example = "Rodríguez García")
	@Column(name = "last_name")
	private String lastName;

	@ApiModelProperty(value = "El correo electrónico del usuario", name = "email", dataType = "String", example = "juan@w2m.com")
	@Column(name = "email")
	private String email;

	@ApiModelProperty(value = "La contraseña del usuario", name = "password", dataType = "String", example = "passwordjuan")
	@JsonIgnore
	@Column(name = "password")
	private String password;
}
