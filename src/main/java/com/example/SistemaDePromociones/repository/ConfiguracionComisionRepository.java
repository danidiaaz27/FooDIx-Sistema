package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.ConfiguracionComision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracionComisionRepository extends JpaRepository<ConfiguracionComision, Long> {
    
    /**
     * Obtener la configuración de comisión activa más reciente
     */
    @Query("SELECT c FROM ConfiguracionComision c WHERE c.estado = true ORDER BY c.fechaVigencia DESC")
    Optional<ConfiguracionComision> findActiveComision();
    
    /**
     * Buscar configuración activa
     */
    Optional<ConfiguracionComision> findFirstByEstadoTrueOrderByFechaVigenciaDesc();
}
