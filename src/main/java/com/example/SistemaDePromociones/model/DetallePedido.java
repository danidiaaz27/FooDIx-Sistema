package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_pedido")
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(name = "codigo_pedido")
    private Long codigoPedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_pedido", insertable = false, updatable = false)
    private Pedido pedido;

    @Column(name = "codigo_promocion")
    private Long codigoPromocion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codigo_promocion", insertable = false, updatable = false)
    private Promocion promocion;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "precio_unitario")
    private BigDecimal precioUnitario;

    @Column(name = "precio_total")
    private BigDecimal precioTotal;

    // @Column(name = "tipo_entrega")
    // private String tipoEntrega;

    // Constructores
    public DetallePedido() {}

    public DetallePedido(Long codigoPedido, Long codigoPromocion, Integer cantidad, BigDecimal precioUnitario, BigDecimal precioTotal) {
        this.codigoPedido = codigoPedido;
        this.codigoPromocion = codigoPromocion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.precioTotal = precioTotal;
    }

    // Getters y setters
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

    public Long getCodigoPromocion() {
        return codigoPromocion;
    }

    public void setCodigoPromocion(Long codigoPromocion) {
        this.codigoPromocion = codigoPromocion;
    }

    public Promocion getPromocion() {
        return promocion;
    }

    public void setPromocion(Promocion promocion) {
        this.promocion = promocion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    // public String getTipoEntrega() {
    //     return tipoEntrega;
    // }

    // public void setTipoEntrega(String tipoEntrega) {
    //     this.tipoEntrega = tipoEntrega;
    // }
}
