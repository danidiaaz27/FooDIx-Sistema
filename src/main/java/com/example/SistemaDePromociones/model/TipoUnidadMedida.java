package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;

/**
 * Entidad TipoUnidadMedida - Catálogo simple de tipos de unidades
 */
@Entity
@Table(name = "tipo_unidad_medida")
public class TipoUnidadMedida {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo;
    
    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;
    
    // Getters y Setters
    public Long getCodigo() {
        return codigo;
    }
    
    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
