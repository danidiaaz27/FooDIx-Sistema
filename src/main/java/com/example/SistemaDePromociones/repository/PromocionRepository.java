package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Long> {
    List<Promocion> findByCodigoRestauranteAndEstado(Long codigoRestaurante, String estado);
}
