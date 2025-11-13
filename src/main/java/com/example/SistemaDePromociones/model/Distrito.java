package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad Distrito - Catálogo de distritos del Perú
 */
@Entity
@Table(name = "distrito")
@Data
public class Distrito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "codigo_provincia", nullable = false)
    private Long codigoProvincia;
    
    @Column(nullable = false)
    private Boolean estado = true;
}
