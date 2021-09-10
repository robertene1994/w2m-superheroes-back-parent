package com.robertene.superheroes.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.robertene.superheroes.common.CurrentThreadService;
import com.robertene.superheroes.domain.dto.session.SessionData;
import com.robertene.superheroes.domain.entity.User;
import com.robertene.superheroes.repository.UserRepository;
import com.robertene.superheroes.service.base.CRUDService;
import com.robertene.superheroes.service.base.PaginationService;

/**
 * Clase que implementa las interfaces CRUDService y PaginationService y define
 * las operaciones correspondientes a la entidad "User" (usuario).
 * 
 * @author Robert Ene
 * @see com.robertene.superheroes.service.base.CRUDService
 * @see com.robertene.superheroes.service.base.PaginationService
 * 
 */
@Service
public class UserService implements CRUDService<User, Long>, PaginationService<User> {

	@Autowired
	private UserRepository repository;

	@Autowired
	private CurrentThreadService cts;

	@Override
	public UserRepository getRepository() {
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

	public User findByUsername(String username) {
		Optional<User> optional = repository.findByUsername(username);
		return optional.isPresent() ? optional.get() : null;
	}
}
