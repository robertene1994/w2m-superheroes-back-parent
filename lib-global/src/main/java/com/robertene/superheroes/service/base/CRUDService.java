package com.robertene.superheroes.service.base;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.robertene.superheroes.domain.entity.base.EntityBase;
import com.robertene.superheroes.exception.ElementNotFoundException;
import com.robertene.superheroes.exception.W2MException;
import com.robertene.superheroes.repository.base.CRUDRepositoryEnh;

/**
 * Interfaz que define las principales operaciones CRUD que deben implementar
 * todos los servicios que gestionan entidades.
 * 
 * @author Robert Ene
 *
 * @param <T> el tipo de la entidad.
 * @param <I> el tipo del ID de la entidad.
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public interface CRUDService<T extends EntityBase<I>, I> {

	public static final Logger LOG = LoggerFactory.getLogger(CRUDService.class);

	public String getEntityName();

	public CRUDRepositoryEnh<T, I> getRepository();

	public Long getCurrentUser();

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public default Optional<T> findById(I id) throws W2MException {
		return getRepository().findById(id);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public default T getById(I id) throws W2MException {
		T entity = getRepository().findById(id).orElse(null);
		if (entity == null) {
			throw new ElementNotFoundException(getEntityName(), id);
		}
		return entity;
	}

	public default T create(T element) throws W2MException {
		return getRepository().save(element);
	}

	public default T update(T element) throws W2MException {
		getRepository().findById(element.getId())
				.orElseThrow(() -> new ElementNotFoundException(getEntityName(), element.getId()));
		return getRepository().save(element);
	}

	public default void delete(I id) throws W2MException {
		getRepository().deleteById(id);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public default Iterable<T> findAll() throws W2MException {
		return getRepository().findAll();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public default Long count(Specification<T> specs) throws W2MException {
		return getRepository().count(specs);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public default Iterable<T> findAll(Specification<T> specs) throws W2MException {
		return this.findAll(specs, Sort.unsorted());
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public default Iterable<T> findAll(Specification<T> specs, Sort sort) throws W2MException {
		Iterable<T> entities;
		if (sort == null) {
			entities = getRepository().findAll(specs);
		} else {
			entities = getRepository().findAll(specs, sort);
		}
		return entities;
	}
}
