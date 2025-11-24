package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad Restaurante
 */
@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    /**
     * Buscar restaurante por correo electrónico
     */
    Optional<Restaurante> findByCorreoElectronico(String correoElectronico);

    /**
     * Buscar restaurante por RUC
     */
    Optional<Restaurante> findByRuc(String ruc);

    /**
     * Verificar si existe un RUC
     */
    boolean existsByRuc(String ruc);

    /**
     * Buscar restaurantes por estado de aprobación
     */
    List<Restaurante> findByCodigoEstadoAprobacion(Long codigoEstadoAprobacion);

    /**
     * Buscar restaurantes por estado ordenados por fecha de creación
     */
    List<Restaurante> findByCodigoEstadoAprobacionOrderByFechaCreacionAsc(Long codigoEstadoAprobacion);
}

