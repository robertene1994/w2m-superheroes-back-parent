package com.robertene.superheroes.domain.dto.pagination;

import java.util.List;

import org.springframework.data.domain.Sort;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@ApiModel(description = "Clase que representa los parámetros del sistema de paginación")
@Data
@AllArgsConstructor(staticName = "of")
public class PageParams {

	@ApiModelProperty(value = "El número máximo de elementos", name = "size", dataType = "Integer", example = "5")
	private int size;

	@ApiModelProperty(value = "El número de la página", name = "page", dataType = "Integer", example = "2")
	private int page;

	@ApiModelProperty(value = "Los atributos de ordenación de los datos", name = "sortFields", dataType = "List", example = "username")
	private List<String> sortFields;

	@ApiModelProperty(value = "La dirección de ordenación de los datos", name = "sortDirection", dataType = "Sort.Direction", example = "DESC")
	private Sort.Direction sortDirection;
}
