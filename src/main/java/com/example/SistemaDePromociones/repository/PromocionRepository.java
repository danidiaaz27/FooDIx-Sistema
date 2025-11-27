package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Long> {
    List<Promocion> findByCodigoRestauranteAndEstado(Long codigoRestaurante, String estado);
    
    /**
     * Obtener todas las promociones activas (publicadas)
     */
    List<Promocion> findByEstadoOrderByFechaCreacionDesc(String estado);
    
    /**
     * Obtener promociones activas con fecha vigente
     */
    @Query("SELECT p FROM Promocion p WHERE p.estado = 'activa' " +
           "AND (p.fechaInicio IS NULL OR p.fechaInicio <= CURRENT_TIMESTAMP) " +
           "AND (p.fechaFin IS NULL OR p.fechaFin >= CURRENT_TIMESTAMP) " +
           "ORDER BY p.fechaCreacion DESC")
    List<Promocion> findPromocionesActivasVigentes();
    
    /**
     * Obtener promociones por restaurante
     */
    List<Promocion> findByCodigoRestauranteOrderByFechaCreacionDesc(Long codigoRestaurante);
}
