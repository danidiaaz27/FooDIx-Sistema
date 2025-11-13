package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entidad Repartidor - Representa la tabla Repartidor en la base de datos
 * Almacena información específica de los repartidores registrados
 */
@Entity
@Table(name = "Repartidor")
@Data
public class Repartidor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo")
    private Long codigo;
    
    @Column(name = "CodigoUsuario", nullable = false)
    private Long codigoUsuario;
    
    @Column(name = "NumeroLicencia", nullable = false, unique = true, length = 15)
    private String numeroLicencia;
    
    @Column(name = "CodigoTipoVehiculo", nullable = false)
    private Long codigoTipoVehiculo;
    
    @Column(name = "Disponible", nullable = false)
    private Boolean disponible = true;
    
    @Column(name = "CodigoEstadoAprobacion", nullable = false)
    private Long codigoEstadoAprobacion = 1L; // 1 = Pendiente, 2 = Aprobado, 3 = Rechazado
    
    @Column(name = "FechaAprobacion")
    private LocalDateTime fechaAprobacion;
    
    @Column(name = "CodigoAprobador")
    private Long codigoAprobador;
    
    @Column(name = "MotivoRechazo", columnDefinition = "TEXT")
    private String motivoRechazo;
    
    @Column(name = "FechaCreacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "Estado", nullable = false)
    private Boolean estado = true;
    
    // Relaciones (opcional, si deseas usar JPA relationships)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CodigoUsuario", insertable = false, updatable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CodigoTipoVehiculo", insertable = false, updatable = false)
    private TipoVehiculo tipoVehiculo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CodigoEstadoAprobacion", insertable = false, updatable = false)
    private EstadoAprobacion estadoAprobacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (estado == null) {
            estado = true;
        }
        if (disponible == null) {
            disponible = true;
        }
        if (codigoEstadoAprobacion == null) {
            codigoEstadoAprobacion = 1L; // Pendiente
        }
    }
}
