package com.robertene.superheroes.controller.base;

import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.robertene.superheroes.domain.dto.DtoWrapper;
import com.robertene.superheroes.domain.entity.base.EntityBase;
import com.robertene.superheroes.exception.ElementNotFoundException;
import com.robertene.superheroes.exception.InvalidInputDataException;
import com.robertene.superheroes.exception.W2MException;
import com.robertene.superheroes.service.base.CRUDService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Interfaz que define las principales operaciones CRUD que deben implementar
 * todos los controladores que gestionan entidades.
 * 
 * @author Robert Ene
 *
 * @param <T> el tipo de la entidad.
 * @param <I> el tipo del ID de la entidad.
 */
public interface CRUDController<T extends EntityBase<I>, I> extends SpecificationFilterGenerator<T> {

	public CRUDService<T, I> getService();

	@ApiOperation(value = "Devuelve listado", nickname = "list")
	@GetMapping
	default Iterable<DtoWrapper<T>> list(
			@ApiParam(value = "filterParams") @RequestParam MultiValueMap<String, String> filterParams)
			throws W2MException {
		Specification<T> specs = this.getSpecificationFilter(filterParams);
		Sort sort = getSort(filterParams);
		return DtoWrapper.of(getService().findAll(specs, sort));
	}

	@ApiOperation(value = "Devuelve el número de elementos", nickname = "count")
	@GetMapping(value = "/count")
	default Long count(@ApiParam(value = "filterParams") @RequestParam MultiValueMap<String, String> filterParams)
			throws W2MException {
		Specification<T> specs = this.getSpecificationFilter(filterParams);
		return getService().count(specs);
	}

	@ApiOperation(value = "Crea un elemento", nickname = "create")
	@PostMapping
	public default DtoWrapper<T> create(@ApiParam(value = "param") @RequestBody Optional<T> payload)
			throws W2MException {
		T element = payload.orElseThrow(InvalidInputDataException::new);
		return DtoWrapper.of(getService().create(element));
	}

	@ApiOperation(value = "Encuentra un elemento", nickname = "findOne")
	@GetMapping(value = "/{id}")
	public default DtoWrapper<T> findOne(
			@ApiParam(value = "idElement", required = true) @PathVariable("id") I idElement) throws W2MException {
		return DtoWrapper.of(getService().getById(idElement));
	}

	@ApiOperation(value = "Encuentra un elemento obligatoriamente", nickname = "findOneRequiered")
	@GetMapping(value = "requiered/{id}")
	public default T findOneRequiered(@ApiParam(value = "idElement", required = true) @PathVariable("id") I idElement)
			throws W2MException {
		return getService().findById(idElement)
				.orElseThrow(() -> new ElementNotFoundException(getService().getEntityName(), idElement));
	}

	@ApiOperation(value = "Actualiza un elemento", nickname = "update")
	@PutMapping(value = "/{id}")
	public default DtoWrapper<T> update(@ApiParam(value = "id", required = true) @PathVariable("id") I idElement,
			@ApiParam(value = "element") @RequestBody Optional<T> payload) throws W2MException {
		T element = payload.orElseThrow(InvalidInputDataException::new);
		element.setId(idElement);
		return DtoWrapper.of(getService().update(element));
	}

	@ApiOperation(value = "Elimina un elemento", nickname = "delete")
	@DeleteMapping(value = "/{id}")
	public default void delete(@ApiParam(value = "id", required = true) @PathVariable("id") I idElement)
			throws W2MException {
		getService().delete(idElement);
	}

	static Sort getSort(MultiValueMap<String, String> params) {
		if (!params.isEmpty()) {
			String sortField = params.getFirst(PaginationController.SORT_FIELD_PARAM);
			if (StringUtils.hasLength(sortField)) {
				String dir = params.getFirst(PaginationController.SORT_DIR_PARAM);
				if (dir == null) {
					return Sort.by(sortField);
				} else {
					Direction direction = Direction.fromString(dir);
					return Sort.by(direction, sortField);
				}
			}
		}
		return null;
	}
}
