package com.robertene.superheroes.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.robertene.superheroes.domain.entity.Superhero;
import com.robertene.superheroes.repository.base.CRUDRepositoryEnh;

@Repository
public interface SuperheroRepository extends CRUDRepositoryEnh<Superhero, Long> {

	Optional<Superhero> findByAlias(String alias);
}
