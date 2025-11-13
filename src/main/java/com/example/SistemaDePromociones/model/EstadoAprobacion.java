package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad EstadoAprobacion - Catálogo de estados de aprobación
 */
@Entity
@Table(name = "EstadoAprobacion")
@Data
public class EstadoAprobacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo")
    private Long codigo;
    
    @Column(name = "Nombre", nullable = false, length = 50)
    private String nombre;
    
    @Column(name = "Descripcion", length = 100)
    private String descripcion;
    
    @Column(name = "Estado", nullable = false)
    private Boolean estado = true;
}
