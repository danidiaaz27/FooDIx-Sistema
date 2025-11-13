package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.Distrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para la entidad Distrito
 */
@Repository
public interface DistritoRepository extends JpaRepository<Distrito, Long> {
    
    /**
     * Listar distritos por provincia (activos) usando SQL nativo
     */
    @Query(value = "SELECT * FROM distrito WHERE codigo_provincia = :codigoProvincia AND estado = 1", nativeQuery = true)
    List<Distrito> findByCodigoProvinciaAndEstadoTrue(@Param("codigoProvincia") Long codigoProvincia);
}
