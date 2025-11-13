package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad Departamento - Catálogo de departamentos del Perú
 */
@Entity
@Table(name = "departamento")
@Data
public class Departamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;
    
    @Column(nullable = false, length = 50)
    private String nombre;
    
    @Column(nullable = false)
    private Boolean estado = true;
}
