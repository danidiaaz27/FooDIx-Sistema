package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.PlatoMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para gestionar platos del menú
 */
@Repository
public interface PlatoMenuRepository extends JpaRepository<PlatoMenu, Long> {
    
    /**
     * Encontrar todos los platos activos de un restaurante
     */
    @Query("SELECT DISTINCT p FROM PlatoMenu p LEFT JOIN FETCH p.unidadesMedida u WHERE p.codigoRestaurante = :codigoRestaurante AND p.estado = true ORDER BY p.fechaCreacion DESC")
    List<PlatoMenu> findByCodigoRestauranteAndEstadoTrue(@Param("codigoRestaurante") Long codigoRestaurante);
    
    /**
     * Encontrar un plato con sus unidades de medida
     */
    @Query("SELECT DISTINCT p FROM PlatoMenu p LEFT JOIN FETCH p.unidadesMedida u WHERE p.codigo = :codigo")
    PlatoMenu findByIdWithUnidades(@Param("codigo") Long codigo);
}
