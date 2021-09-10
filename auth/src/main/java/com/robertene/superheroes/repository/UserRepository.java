package com.robertene.superheroes.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.robertene.superheroes.domain.entity.User;
import com.robertene.superheroes.repository.base.CRUDRepositoryEnh;

@Repository
public interface UserRepository extends CRUDRepositoryEnh<User, Long> {

	Optional<User> findByUsername(String username);
}
