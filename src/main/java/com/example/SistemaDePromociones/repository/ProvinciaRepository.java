package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para la entidad Provincia
 */
@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia, Long> {
    
    /**
     * Listar provincias por departamento (activas) usando SQL nativo
     */
    @Query(value = "SELECT * FROM provincia WHERE codigo_departamento = :codigoDepartamento AND estado = 1", nativeQuery = true)
    List<Provincia> findByCodigoDepartamentoAndEstadoTrue(@Param("codigoDepartamento") Long codigoDepartamento);
}
