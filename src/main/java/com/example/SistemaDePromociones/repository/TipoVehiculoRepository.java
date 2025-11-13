package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.TipoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para la entidad TipoVehiculo
 */
@Repository
public interface TipoVehiculoRepository extends JpaRepository<TipoVehiculo, Long> {
    
    /**
     * Listar tipos de vehículo activos
     */
    List<TipoVehiculo> findByEstadoTrue();
}
