package com.robertene.superheroes.service.base;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.robertene.superheroes.domain.dto.pagination.PageData;
import com.robertene.superheroes.domain.dto.pagination.PageParams;
import com.robertene.superheroes.exception.W2MException;
import com.robertene.superheroes.repository.base.PaginationRepository;

/**
 * Interfaz que define las operaciones necesarias para implementar la paginación
 * sobre las entidades en los diferentes servicios.
 * 
 * @author Robert Ene
 *
 * @param <T> el tipo de la entidad.
 */
public interface PaginationService<T> {

	public PaginationRepository<T> getRepository();

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public default PageData<T> getPage(Pageable pageable, Specification<T> specs, PageParams params)
			throws W2MException {
		return PageData.of(getRepository().findAll(specs, pageable), params);
	}
}
