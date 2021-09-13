package com.robertene.superheroes.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "Clase que representa los datos de autenticación en el sistema de un determinado usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

	@ApiModelProperty(value = "El nombre de usuario dentro del sistema", name = "username", dataType = "String", example = "juan")
	private String username;

	@ApiModelProperty(value = "La contraseña del usuario dentro del sistema", name = "password", dataType = "String", example = "password")
	private String password;
}
