package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.TipoUnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoUnidadMedidaRepository extends JpaRepository<TipoUnidadMedida, Long> {
    
    /**
     * Obtiene todos los tipos de unidades ordenados por nombre
     */
    List<TipoUnidadMedida> findAllByOrderByNombreAsc();
}
