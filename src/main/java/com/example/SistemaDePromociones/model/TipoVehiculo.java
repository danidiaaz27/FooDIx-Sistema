package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad TipoVehiculo - Catálogo de tipos de vehículos
 */
@Entity
@Table(name = "TipoVehiculo")
@Data
public class TipoVehiculo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo")
    private Long codigo;
    
    @Column(name = "Nombre", nullable = false, length = 20)
    private String nombre;
    
    @Column(name = "Estado", nullable = false)
    private Boolean estado = true;
}
