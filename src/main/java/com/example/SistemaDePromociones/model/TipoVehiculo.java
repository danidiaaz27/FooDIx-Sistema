package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad TipoVehiculo - Catálogo de tipos de vehículos
 */
@Entity
@Table(name = "tipo_vehiculo")
@Data
public class TipoVehiculo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo;
    
    @Column(name = "nombre", nullable = false, length = 20)
    private String nombre;
    
    @Column(name = "estado", nullable = false)
    private Boolean estado = true;
}
