package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para la entidad Departamento
 */
@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    
    /**
     * Listar todos los departamentos activos usando SQL nativo
     */
    @Query(value = "SELECT * FROM departamento WHERE estado = 1", nativeQuery = true)
    List<Departamento> findByEstadoTrue();
}
