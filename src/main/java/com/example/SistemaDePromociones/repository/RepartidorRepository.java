package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.Repartidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
    Optional<Repartidor> findByNumeroLicencia(String numeroLicencia);
    
    /**
     * Verificar si existe un número de licencia
     */
    boolean existsByNumeroLicencia(String numeroLicencia);
}
