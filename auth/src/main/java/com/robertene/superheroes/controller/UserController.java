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
import com.robertene.superheroes.domain.entity.User;
import com.robertene.superheroes.service.UserService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping({ "${w2m.rest.url.prefix}/users" })
@Api(tags = SwaggerConfiguration.USER_TAG)
public class UserController implements CRUDController<User, Long>, PaginationController<User> {

  @SuppressWarnings("unused")
  private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private PaginationUtils pagUtils;

  @Autowired
  private UserService service;
  
  @Override
  public UserService getService() {
    return service;
  }

  @Override
  public PaginationUtils getPaginationUtils() {
    return pagUtils;
  }
}
