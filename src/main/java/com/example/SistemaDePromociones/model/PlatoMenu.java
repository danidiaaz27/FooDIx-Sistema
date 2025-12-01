package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad PlatoMenu - Representa los platos del menú de cada restaurante
 */
@Entity
@Table(name = "plato_menu")
public class PlatoMenu {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo;
    
    @Column(name = "codigo_restaurante", nullable = false)
    private Long codigoRestaurante;
    
    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "estado")
    private Boolean estado;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;
    
    @OneToMany(mappedBy = "plato", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UnidadMedidaPlato> unidadesMedida = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_restaurante", insertable = false, updatable = false)
    private Restaurante restaurante;
    
    // Métodos auxiliares para gestionar la relación bidireccional
    public void addUnidadMedida(UnidadMedidaPlato unidad) {
        unidadesMedida.add(unidad);
        unidad.setPlato(this);
    }
    
    public void removeUnidadMedida(UnidadMedidaPlato unidad) {
        unidadesMedida.remove(unidad);
        unidad.setPlato(null);
    }
    
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
    
    public Long getCodigoRestaurante() {
        return codigoRestaurante;
    }
    
    public void setCodigoRestaurante(Long codigoRestaurante) {
        this.codigoRestaurante = codigoRestaurante;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
    
    public List<UnidadMedidaPlato> getUnidadesMedida() {
        return unidadesMedida;
    }
    
    public void setUnidadesMedida(List<UnidadMedidaPlato> unidadesMedida) {
        this.unidadesMedida = unidadesMedida;
    }
    
    public Restaurante getRestaurante() {
        return restaurante;
    }
    
    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }
}
