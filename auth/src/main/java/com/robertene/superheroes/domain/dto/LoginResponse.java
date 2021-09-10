package com.robertene.superheroes.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "Clase que representa los datos de autenticación de un determinado usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

	@ApiModelProperty(value = "El nombre de usuario autenticado en el sistema", name = "username", dataType = "String", example = "juan")
	private String username;

	@ApiModelProperty(value = "El token de autenticación (JWT) generado", name = "authorization", dataType = "String", example = "eyJhbGciOiJIUzI1NiJ9.eyJzZXNzaW9uX2RhdGEiOnsidXNlcklkIjoyLCJ1c...")
	private String authorization;
}
