package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;

@Entity
@Table(name = "promocion")
public class Promocion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    private String titulo;
    private String descripcion;
    @Column(name = "codigo_restaurante")
    private Long codigoRestaurante;
    @Column(name = "precio_original")
    private Double precioOriginal;
    @Column(name = "precio_promocional")
    private Double precioPromocional;
    @Column(name = "tipo_descuento")
    private String tipoDescuento;
    @Column(name = "valor_descuento")
    private Double valorDescuento;
    @Column(name = "categoria_promocion")
    private String categoriaPromocion;
    @Column(name = "fecha_inicio")
    private java.sql.Timestamp fechaInicio;
    @Column(name = "fecha_fin")
    private java.sql.Timestamp fechaFin;
    @Column(name = "estado")
    private String estado;
    @Column(name = "contador_vistas")
    private Integer contadorVistas;
    @Column(name = "contador_pedidos")
    private Integer contadorPedidos;
    @Column(name = "ingresos_totales")
    private Double ingresosTotales;
    @Column(name = "fecha_creacion")
    private java.sql.Timestamp fechaCreacion;
    @Column(name = "fecha_modificacion")
    private java.sql.Timestamp fechaModificacion;

    // Getters y setters
    public Long getCodigo() { return codigo; }
    public void setCodigo(Long codigo) { this.codigo = codigo; }

    public Long getCodigoRestaurante() { return codigoRestaurante; }
    public void setCodigoRestaurante(Long codigoRestaurante) { this.codigoRestaurante = codigoRestaurante; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getPrecioOriginal() { return precioOriginal; }
    public void setPrecioOriginal(Double precioOriginal) { this.precioOriginal = precioOriginal; }

    public Double getPrecioPromocional() { return precioPromocional; }
    public void setPrecioPromocional(Double precioPromocional) { this.precioPromocional = precioPromocional; }

    public String getTipoDescuento() { return tipoDescuento; }
    public void setTipoDescuento(String tipoDescuento) { this.tipoDescuento = tipoDescuento; }

    public Double getValorDescuento() { return valorDescuento; }
    public void setValorDescuento(Double valorDescuento) { this.valorDescuento = valorDescuento; }

    public String getCategoriaPromocion() { return categoriaPromocion; }
    public void setCategoriaPromocion(String categoriaPromocion) { this.categoriaPromocion = categoriaPromocion; }

    public java.sql.Timestamp getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(java.sql.Timestamp fechaInicio) { this.fechaInicio = fechaInicio; }

    public java.sql.Timestamp getFechaFin() { return fechaFin; }
    public void setFechaFin(java.sql.Timestamp fechaFin) { this.fechaFin = fechaFin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Integer getContadorVistas() { return contadorVistas; }
    public void setContadorVistas(Integer contadorVistas) { this.contadorVistas = contadorVistas; }

    public Integer getContadorPedidos() { return contadorPedidos; }
    public void setContadorPedidos(Integer contadorPedidos) { this.contadorPedidos = contadorPedidos; }

    public Double getIngresosTotales() { return ingresosTotales; }
    public void setIngresosTotales(Double ingresosTotales) { this.ingresosTotales = ingresosTotales; }

    public java.sql.Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(java.sql.Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public java.sql.Timestamp getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(java.sql.Timestamp fechaModificacion) { this.fechaModificacion = fechaModificacion; }
}
