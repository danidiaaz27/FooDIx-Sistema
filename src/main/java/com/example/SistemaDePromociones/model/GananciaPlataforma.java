package com.example.SistemaDePromociones.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ganancia_plataforma")
public class GananciaPlataforma {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;
    
    @Column(name = "codigo_pedido", nullable = false)
    private Long codigoPedido;
    
    @JsonIgnore  // ✅ Evita ciclos de serialización
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_pedido", insertable = false, updatable = false)
    private Pedido pedido;
    
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();
    
    @Column(name = "subtotal", nullable = false)
    private BigDecimal subtotal;
    
    @Column(name = "comision", nullable = false)
    private BigDecimal comision;
    
    @Column(name = "porcentaje_aplicado", nullable = false)
    private BigDecimal porcentajeAplicado;
    
    @Column(name = "monto_restaurante", nullable = false)
    private BigDecimal montoRestaurante;

    // Constructors
    public GananciaPlataforma() {}

    public GananciaPlataforma(Long codigoPedido, BigDecimal subtotal, BigDecimal comision, 
                             BigDecimal porcentajeAplicado, BigDecimal montoRestaurante) {
        this.codigoPedido = codigoPedido;
        this.subtotal = subtotal;
        this.comision = comision;
        this.porcentajeAplicado = porcentajeAplicado;
        this.montoRestaurante = montoRestaurante;
    }

    // Getters y Setters
    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Long getCodigoPedido() {
        return codigoPedido;
    }

    public void setCodigoPedido(Long codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getComision() {
        return comision;
    }

    public void setComision(BigDecimal comision) {
        this.comision = comision;
    }

    public BigDecimal getPorcentajeAplicado() {
        return porcentajeAplicado;
    }

    public void setPorcentajeAplicado(BigDecimal porcentajeAplicado) {
        this.porcentajeAplicado = porcentajeAplicado;
    }

    public BigDecimal getMontoRestaurante() {
        return montoRestaurante;
    }

    public void setMontoRestaurante(BigDecimal montoRestaurante) {
        this.montoRestaurante = montoRestaurante;
    }
}
