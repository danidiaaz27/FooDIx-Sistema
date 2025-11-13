package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad Provincia - Catálogo de provincias del Perú
 */
@Entity
@Table(name = "provincia")
@Data
public class Provincia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "codigo_departamento", nullable = false)
    private Long codigoDepartamento;
    
    @Column(nullable = false)
    private Boolean estado = true;
}
