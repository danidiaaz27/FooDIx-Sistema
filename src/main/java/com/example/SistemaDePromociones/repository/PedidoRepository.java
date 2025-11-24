package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Pedidos pendientes y sin repartidor asignado
    List<Pedido> findByCodigoEstadoPedidoAndCodigoRepartidor(Long codigoEstadoPedido, Long codigoRepartidor);
    // Pedidos pendientes y sin repartidor (NULL)
    List<Pedido> findByCodigoEstadoPedidoAndCodigoRepartidorIsNull(Long codigoEstadoPedido);
}
