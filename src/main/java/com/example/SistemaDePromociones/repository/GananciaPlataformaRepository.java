package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.GananciaPlataforma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GananciaPlataformaRepository extends JpaRepository<GananciaPlataforma, Long> {
    
    /**
     * Obtener todas las ganancias ordenadas por fecha descendente
     */
    List<GananciaPlataforma> findAllByOrderByFechaRegistroDesc();
    
    /**
     * Calcular ganancias totales
     */
    @Query("SELECT COALESCE(SUM(g.comision), 0) FROM GananciaPlataforma g")
    BigDecimal calcularGananciasTotales();
    
    /**
     * Calcular ganancias en un rango de fechas
     */
    @Query("SELECT COALESCE(SUM(g.comision), 0) FROM GananciaPlataforma g WHERE g.fechaRegistro BETWEEN :inicio AND :fin")
    BigDecimal calcularGananciasPorPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
    
    /**
     * Contar pedidos procesados
     */
    @Query("SELECT COUNT(g) FROM GananciaPlataforma g")
    Long contarPedidosProcesados();
    
    /**
     * Obtener ganancias por mes (últimos 12 meses)
     */
    @Query(value = "SELECT DATE_FORMAT(fecha_registro, '%Y-%m') as mes, SUM(comision) as ganancia " +
                   "FROM ganancia_plataforma " +
                   "WHERE fecha_registro >= DATE_SUB(NOW(), INTERVAL 12 MONTH) " +
                   "GROUP BY mes " +
                   "ORDER BY mes DESC", 
           nativeQuery = true)
    List<Object[]> obtenerGananciasPorMes();
    
    /**
     * Buscar ganancias por código de pedido
     */
    List<GananciaPlataforma> findByCodigoPedido(Long codigoPedido);
}
