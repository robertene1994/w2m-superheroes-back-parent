package com.robertene.superheroes.repository.base;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CRUDRepositoryEnh<T, I> extends CrudRepository<T, I>, PaginationRepository<T> {

}
