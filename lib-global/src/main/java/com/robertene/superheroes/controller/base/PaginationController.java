package com.robertene.superheroes.controller.base;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.robertene.superheroes.common.PaginationUtils;
import com.robertene.superheroes.domain.dto.pagination.PageData;
import com.robertene.superheroes.domain.dto.pagination.PageParams;
import com.robertene.superheroes.exception.W2MException;
import com.robertene.superheroes.service.base.PaginationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;

/**
 * Interfaz que define las operaciones necesarias para implementar la paginación
 * sobre las entidades en los diferentes controladores.
 * 
 * @author Robert Ene
 *
 * @param <T> el tipo de la entidad.
 */
public interface PaginationController<T> extends SpecificationFilterGenerator<T> {

	public static final String SORT_FIELD_PARAM = "sortFields";
	public static final String SORT_DIR_PARAM = "sortDir";
	public static final String PAGE_PARAM = "page";
	public static final String PAGE_SIZE_PARAM = "pageSize";
	public static final String FILTER_PARAMS_PARAM = "filterParams";

	public PaginationService<T> getService();

	public PaginationUtils getPaginationUtils();

	@ApiOperation(value = "Obtención de una página", nickname = "getPage")
	@GetMapping(value = "/page")
	public default PageData<T> getPage(
			@ApiParam(name = PAGE_PARAM, type = "Integer", value = "Número de la página", example = "1") @RequestParam(PAGE_PARAM) Integer pageNumber,
			@ApiParam(name = PAGE_SIZE_PARAM, type = "Integer", value = "Tamaño de la página", example = "5") @RequestParam(PAGE_SIZE_PARAM) Integer pageSize,
			@ApiParam(name = SORT_FIELD_PARAM, type = "List", value = "Campos de ordenación (Ejemplo: username)", example = "[\"username\"]") @RequestParam(SORT_FIELD_PARAM) List<String> sortFields,
			@ApiParam(name = SORT_DIR_PARAM, type = "String", value = "Dirección de la ordenación", example = "ASC") @RequestParam(SORT_DIR_PARAM) Sort.Direction sortDirection,
			 @ApiParam(name = FILTER_PARAMS_PARAM, type="Map", value = "Map con los datos de los filtros (Ejemplo: {\"cf_username_like\":\"jo\"})", examples = @Example(value = {
					@ExampleProperty(mediaType = "application/json", value = "{\"cf_username_like\":\"jo\"}") })) @RequestParam MultiValueMap<String, String> filterParams)
			throws W2MException {
		Specification<T> specs = this.getSpecificationFilter(filterParams);
		Pageable pageConfig = getPaginationUtils().generatePageableRequest(sortFields, sortDirection, pageNumber,
				pageSize);
		PageParams params = PageParams.of(pageNumber, pageSize, sortFields, sortDirection);
		return getService().getPage(pageConfig, specs, params);
	}
}
