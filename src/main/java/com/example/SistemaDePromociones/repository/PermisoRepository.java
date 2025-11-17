package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Permiso
 */
@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Long> {
    
    /**
     * Busca permisos por sección
     */
    List<Permiso> findBySeccion(String seccion);
    
    /**
     * Busca permisos activos
     */
    List<Permiso> findByEstadoTrue();
}
