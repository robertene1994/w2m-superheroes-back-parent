package com.robertene.superheroes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Punto de entrada principal para el miscroservicio.
 * 
 * @author Robert Ene
 *
 */
@SpringBootApplication
@EnableEurekaClient
@ComponentScan("com.robertene.superheroes")
@EntityScan(basePackages = { "com.robertene.superheroes.domain" })
@EnableJpaRepositories(basePackages = { "com.robertene.superheroes.repository" })
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}
}
