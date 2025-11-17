package com.example.SistemaDePromociones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.SistemaDePromociones.repository")
public class SistemaDePromocionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaDePromocionesApplication.class, args);
	}

}