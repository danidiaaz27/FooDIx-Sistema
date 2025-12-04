package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    List<DetallePedido> findByCodigoPedido(Long codigoPedido);
}
