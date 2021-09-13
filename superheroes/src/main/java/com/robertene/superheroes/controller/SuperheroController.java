package com.robertene.superheroes.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robertene.superheroes.common.PaginationUtils;
import com.robertene.superheroes.config.SwaggerConfiguration;
import com.robertene.superheroes.controller.base.CRUDController;
import com.robertene.superheroes.controller.base.PaginationController;
import com.robertene.superheroes.domain.entity.Superhero;
import com.robertene.superheroes.service.SuperheroService;

import io.swagger.annotations.Api;

/**
 * Clase que implementa las interfaces CRUDController y PaginationController y
 * define las operaciones correspondientes a la entidad "Superhero"
 * (superhéroe).
 * 
 * @author Robert Ene
 * @see com.robertene.superheroes.controller.base.CRUDController
 * @see com.robertene.superheroes.controller.base.PaginationController
 * 
 */
@RestController
@RequestMapping({ "${w2m.rest.url.prefix}/superheroes" })
@Api(tags = SwaggerConfiguration.SUPERHERO_TAG)
public class SuperheroController implements CRUDController<Superhero, Long>, PaginationController<Superhero> {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(SuperheroController.class);

	@Autowired
	private PaginationUtils pagUtils;

	@Autowired
	private SuperheroService service;

	@Override
	public SuperheroService getService() {
		return service;
	}

	@Override
	public PaginationUtils getPaginationUtils() {
		return pagUtils;
	}
}
