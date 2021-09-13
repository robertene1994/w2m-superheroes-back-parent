package com.robertene.superheroes.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.robertene.superheroes.common.CurrentThreadService;
import com.robertene.superheroes.domain.dto.session.SessionData;
import com.robertene.superheroes.domain.entity.Superhero;
import com.robertene.superheroes.repository.SuperheroRepository;
import com.robertene.superheroes.service.base.CRUDService;
import com.robertene.superheroes.service.base.PaginationService;

/**
 * Clase que implementa las interfaces CRUDService y PaginationService y define
 * las operaciones correspondientes a la entidad "Superhero" (superhéroe).
 * 
 * @author Robert Ene
 * @see com.robertene.superheroes.service.base.CRUDService
 * @see com.robertene.superheroes.service.base.PaginationService
 * 
 */
@Service
public class SuperheroService implements CRUDService<Superhero, Long>, PaginationService<Superhero> {

	@Autowired
	private SuperheroRepository repository;

	@Autowired
	private CurrentThreadService cts;

	@Override
	public SuperheroRepository getRepository() {
		return repository;
	}

	@Override
	public String getEntityName() {
		return "Usuario";
	}

	@Override
	public Long getCurrentUser() {
		SessionData sd = cts.get(SessionData.KEY, SessionData.class);
		return sd == null ? 0 : sd.getUserId();
	}

	public Superhero findByAlias(String alias) {
		Optional<Superhero> optional = repository.findByAlias(alias);
		return optional.isPresent() ? optional.get() : null;
	}
}
