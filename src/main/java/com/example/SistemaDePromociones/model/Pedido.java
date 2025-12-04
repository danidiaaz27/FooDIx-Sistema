package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(name = "codigo_usuario")
    private Long codigoUsuario;

    @Column(name = "codigo_restaurante")
    private Long codigoRestaurante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_restaurante", insertable = false, updatable = false)
    private Restaurante restaurante;

    @Column(name = "codigo_repartidor")
    private Long codigoRepartidor;

    @Column(name = "codigo_estado_pedido")
    private Long codigoEstadoPedido;

    // @Column(name = "codigo_tipo_entrega")
    // private Long codigoTipoEntrega;

    // @Column(name = "numero_pedido")
    // private String numeroPedido;

    @Column(name = "subtotal")
    private BigDecimal subtotal;

    @Column(name = "costo_delivery")
    private BigDecimal costoDelivery;

    @Column(name = "descuento")
    private BigDecimal descuento;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "codigo_metodo_pago")
    private Long codigoMetodoPago;

    @Column(name = "direccion_entrega")
    private String direccionEntrega;

    @Column(name = "referencia_direccion")
    private String referenciaDireccion;

    @Column(name = "telefono_contacto")
    private String telefonoContacto;

    @Column(name = "notas")
    private String notas;

    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido;

    @Column(name = "fecha_confirmacion")
    private LocalDateTime fechaConfirmacion;

    // @Column(name = "fecha_entrega_estimada")
    // private LocalDateTime fechaEntregaEstimada;

    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

    @Column(name = "calificacion_restaurante")
    private Integer calificacionRestaurante;

    @Column(name = "calificacion_repartidor")
    private Integer calificacionRepartidor;

    @Column(name = "comentario_calificacion")
    private String comentarioCalificacion;

    // @Column(name = "ganancia_repartidor")
    // private BigDecimal gananciaRepartidor;
    
    @Column(name = "estado")
    private Boolean estado = true;
    
    @Column(name = "codigo_verificacion", length = 4)
    private String codigoVerificacion;
    
    @Column(name = "verificado")
    private Boolean verificado = false;

    // Getters y setters
    public Long getCodigo() { return codigo; }
    public void setCodigo(Long codigo) { this.codigo = codigo; }

    public Long getCodigoUsuario() { return codigoUsuario; }
    public void setCodigoUsuario(Long codigoUsuario) { this.codigoUsuario = codigoUsuario; }

    public Long getCodigoRestaurante() { return codigoRestaurante; }
    public void setCodigoRestaurante(Long codigoRestaurante) { this.codigoRestaurante = codigoRestaurante; }

    public Long getCodigoRepartidor() { return codigoRepartidor; }
    public void setCodigoRepartidor(Long codigoRepartidor) { this.codigoRepartidor = codigoRepartidor; }

    public Long getCodigoEstadoPedido() { return codigoEstadoPedido; }
    public void setCodigoEstadoPedido(Long codigoEstadoPedido) { this.codigoEstadoPedido = codigoEstadoPedido; }

    // public Long getCodigoTipoEntrega() { return codigoTipoEntrega; }
    // public void setCodigoTipoEntrega(Long codigoTipoEntrega) { this.codigoTipoEntrega = codigoTipoEntrega; }

    // public String getNumeroPedido() { return numeroPedido; }
    // public void setNumeroPedido(String numeroPedido) { this.numeroPedido = numeroPedido; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getCostoDelivery() { return costoDelivery; }
    public void setCostoDelivery(BigDecimal costoDelivery) { this.costoDelivery = costoDelivery; }

    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }
    
        public Restaurante getRestaurante() {
            return restaurante;
        }
    
        public void setRestaurante(Restaurante restaurante) {
            this.restaurante = restaurante;
        }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public Long getCodigoMetodoPago() { return codigoMetodoPago; }
    public void setCodigoMetodoPago(Long codigoMetodoPago) { this.codigoMetodoPago = codigoMetodoPago; }

    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }

    public String getReferenciaDireccion() { return referenciaDireccion; }
    public void setReferenciaDireccion(String referenciaDireccion) { this.referenciaDireccion = referenciaDireccion; }

    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String telefonoContacto) { this.telefonoContacto = telefonoContacto; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }

    public LocalDateTime getFechaConfirmacion() { return fechaConfirmacion; }
    public void setFechaConfirmacion(LocalDateTime fechaConfirmacion) { this.fechaConfirmacion = fechaConfirmacion; }

    // public LocalDateTime getFechaEntregaEstimada() { return fechaEntregaEstimada; }
    // public void setFechaEntregaEstimada(LocalDateTime fechaEntregaEstimada) { this.fechaEntregaEstimada = fechaEntregaEstimada; }

    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDateTime fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public Integer getCalificacionRestaurante() { return calificacionRestaurante; }
    public void setCalificacionRestaurante(Integer calificacionRestaurante) { this.calificacionRestaurante = calificacionRestaurante; }

    public Integer getCalificacionRepartidor() { return calificacionRepartidor; }
    public void setCalificacionRepartidor(Integer calificacionRepartidor) { this.calificacionRepartidor = calificacionRepartidor; }

    public String getComentarioCalificacion() { return comentarioCalificacion; }
    public void setComentarioCalificacion(String comentarioCalificacion) { this.comentarioCalificacion = comentarioCalificacion; }

    // public BigDecimal getGananciaRepartidor() { return gananciaRepartidor; }
    // public void setGananciaRepartidor(BigDecimal gananciaRepartidor) { this.gananciaRepartidor = gananciaRepartidor; }
    
    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
    
    public String getCodigoVerificacion() { return codigoVerificacion; }
    public void setCodigoVerificacion(String codigoVerificacion) { this.codigoVerificacion = codigoVerificacion; }
    
    public Boolean getVerificado() { return verificado; }
    public void setVerificado(Boolean verificado) { this.verificado = verificado; }
}
