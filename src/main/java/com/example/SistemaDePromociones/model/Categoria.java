package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad Categoria - Catálogo de categorías de restaurantes y productos
 */
@Entity
@Table(name = "Categoria")
@Data
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo")
    private Long codigo;
    
    @Column(name = "Nombre", nullable = false, length = 50)
    private String nombre;
    
    @Column(name = "Descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "Estado", nullable = false)
    private Boolean estado = true;
}
