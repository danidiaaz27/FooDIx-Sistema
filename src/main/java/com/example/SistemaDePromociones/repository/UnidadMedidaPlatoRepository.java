package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.UnidadMedidaPlato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para gestionar unidades de medida de platos
 */
@Repository
public interface UnidadMedidaPlatoRepository extends JpaRepository<UnidadMedidaPlato, Long> {
}
