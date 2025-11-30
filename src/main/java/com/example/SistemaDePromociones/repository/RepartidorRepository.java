package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.Repartidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad Repartidor
 */
@Repository
public interface RepartidorRepository extends JpaRepository<Repartidor, Long> {
    
    /**
     * Buscar repartidor por código de usuario
     */
    Optional<Repartidor> findByCodigoUsuario(Long codigoUsuario);
    
    /**
     * Buscar repartidor por número de licencia
     */
    @Query("SELECT r FROM Repartidor r WHERE r.numeroLicencia = :numeroLicencia")
    Optional<Repartidor> findByNumeroLicencia(@Param("numeroLicencia") String numeroLicencia);
    
    /**
     * Verificar si existe un número de licencia
     */
    @Query("SELECT COUNT(r) > 0 FROM Repartidor r WHERE r.numeroLicencia = :numeroLicencia")
    boolean existsByNumeroLicencia(@Param("numeroLicencia") String numeroLicencia);
    
    /**
     * Buscar repartidores por estado de aprobación
     */
    List<Repartidor> findByCodigoEstadoAprobacion(Long codigoEstadoAprobacion);
    
    /**
     * Buscar repartidores por estado de aprobación ordenados por fecha de creación
     */
    List<Repartidor> findByCodigoEstadoAprobacionOrderByFechaCreacionAsc(Long codigoEstadoAprobacion);
}
