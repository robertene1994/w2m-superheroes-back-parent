package com.robertene.superheroes.repository.base;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface PaginationRepository<T> extends JpaSpecificationExecutor<T> {

}
