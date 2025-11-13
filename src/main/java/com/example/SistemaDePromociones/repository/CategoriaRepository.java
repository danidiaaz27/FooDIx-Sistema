package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para la entidad Categoria
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    /**
     * Listar categorías activas
     */
    List<Categoria> findByEstadoTrue();
}
