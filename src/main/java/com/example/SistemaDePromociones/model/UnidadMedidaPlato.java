package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad UnidadMedidaPlato - Representa las unidades de medida (porciones) de cada plato
 */
@Entity
@Table(name = "unidad_medida_plato")
public class UnidadMedidaPlato {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_plato", nullable = false)
    private PlatoMenu plato;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codigo_tipo_unidad", nullable = false)
    private TipoUnidadMedida tipoUnidad;
    
    @Column(name = "descripcion", length = 500)
    private String descripcion;
    
    @Column(name = "precio_original", nullable = false)
    private Double precioOriginal;
    
    @Column(name = "estado")
    private Boolean estado;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (estado == null) {
            estado = true;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getCodigo() {
        return codigo;
    }
    
    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }
    
    public TipoUnidadMedida getTipoUnidad() {
        return tipoUnidad;
    }
    
    public void setTipoUnidad(TipoUnidadMedida tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Double getPrecioOriginal() {
        return precioOriginal;
    }
    
    public void setPrecioOriginal(Double precioOriginal) {
        this.precioOriginal = precioOriginal;
    }
    
    public Boolean getEstado() {
        return estado;
    }
    
    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }
    
    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
    
    public PlatoMenu getPlato() {
        return plato;
    }
    
    public void setPlato(PlatoMenu plato) {
        this.plato = plato;
    }
}
