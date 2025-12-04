package com.example.SistemaDePromociones.model;

import java.io.Serializable;

/**
 * Representa un item en el carrito de compras (sesión)
 */
public class CarritoItem implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long codigoPromocion;
    private String tituloPromocion;
    private String nombreRestaurante;
    private Long codigoRestaurante;
    private Double precioUnitario;
    private Integer cantidad;
    private String tipoEntrega; // "DELIVERY" o "RECOJO"
    
    public CarritoItem() {
        this.cantidad = 1;
        this.tipoEntrega = "DELIVERY";
    }
    
    public CarritoItem(Long codigoPromocion, String tituloPromocion, String nombreRestaurante, 
                       Long codigoRestaurante, Double precioUnitario, Integer cantidad) {
        this.codigoPromocion = codigoPromocion;
        this.tituloPromocion = tituloPromocion;
        this.nombreRestaurante = nombreRestaurante;
        this.codigoRestaurante = codigoRestaurante;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.tipoEntrega = "DELIVERY";
    }
    
    public Double getSubtotal() {
        return precioUnitario * cantidad;
    }
    
    // Getters y setters
    public Long getCodigoPromocion() { return codigoPromocion; }
    public void setCodigoPromocion(Long codigoPromocion) { this.codigoPromocion = codigoPromocion; }
    
    public String getTituloPromocion() { return tituloPromocion; }
    public void setTituloPromocion(String tituloPromocion) { this.tituloPromocion = tituloPromocion; }
    
    public String getNombreRestaurante() { return nombreRestaurante; }
    public void setNombreRestaurante(String nombreRestaurante) { this.nombreRestaurante = nombreRestaurante; }
    
    public Long getCodigoRestaurante() { return codigoRestaurante; }
    public void setCodigoRestaurante(Long codigoRestaurante) { this.codigoRestaurante = codigoRestaurante; }
    
    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }
    
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    
    public String getTipoEntrega() { return tipoEntrega; }
    public void setTipoEntrega(String tipoEntrega) { this.tipoEntrega = tipoEntrega; }
}
