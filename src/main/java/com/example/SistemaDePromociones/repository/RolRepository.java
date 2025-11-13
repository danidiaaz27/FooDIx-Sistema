package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Rol
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    
    /**
     * Buscar rol por nombre
     */
    Optional<Rol> findByNombre(String nombre);
    
    /**
     * Buscar rol por nombre ignorando mayúsculas/minúsculas
     */
    Optional<Rol> findByNombreIgnoreCase(String nombre);
    
    /**
     * Verificar si existe un rol por nombre
     */
    boolean existsByNombre(String nombre);
}
