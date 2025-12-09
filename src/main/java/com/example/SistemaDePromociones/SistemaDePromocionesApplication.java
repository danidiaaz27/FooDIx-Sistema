package com.example.SistemaDePromociones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.SistemaDePromociones.repository")
@EnableScheduling
public class SistemaDePromocionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaDePromocionesApplication.class, args);
	}

}