package com.robertene.superheroes.domain.dto.pagination;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.robertene.superheroes.domain.dto.DtoWrapper;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@ApiModel(description = "Clase que representa los datos de las páginas (paginación)")
@Data
@AllArgsConstructor(staticName = "of")
@JsonInclude(content = Include.NON_NULL)
public class PageData<T> {

	@ApiModelProperty(value = "La lista que contiene los datos paginados", name = "content", dataType = "Iterable")
	private final Iterable<DtoWrapper<T>> content;

	@ApiModelProperty(value = "El número de elementos que contiene la página", name = "total", dataType = "Long", example = "5")
	private final Long total;

	@ApiModelProperty(value = "Los datos en base a los que se ha generado la página", name = "content", dataType = "PageParams")
	private final PageParams params;

	public static <T> PageData<T> of(Page<T> page, PageParams params) {
		return PageData.of(DtoWrapper.of(page.getContent()), page.getTotalElements(), params);
	}

	public static <T> PageData<T> of(Page<T> page) {
		return PageData.of(DtoWrapper.of(page.getContent()), page.getTotalElements(), null);
	}
}
