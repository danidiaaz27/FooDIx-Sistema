package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entidad Repartidor - Representa la tabla Repartidor en la base de datos
 * Almacena información específica de los repartidores registrados
 */
@Entity
@Table(name = "repartidor")
@Data
public class Repartidor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo;
    
    @Column(name = "codigo_usuario", nullable = false)
    private Long codigoUsuario;
    
    @Column(name = "numero_licencia", nullable = false, unique = true, length = 15)
    private String numeroLicencia;
    
    @Column(name = "codigo_tipo_vehiculo", nullable = false)
    private Long codigoTipoVehiculo;
    
    @Column(name = "disponible", nullable = false)
    private Boolean disponible = true;
    
    @Column(name = "codigo_estado_aprobacion", nullable = false)
    private Long codigoEstadoAprobacion = 7L; // 7 = Pendiente, 8 = Aprobado, 9 = Rechazado
    
    @Column(name = "fecha_aprobacion")
    private LocalDateTime fechaAprobacion;
    
    @Column(name = "codigo_aprobador")
    private Long codigoAprobador;
    
    @Column(name = "motivo_rechazo", columnDefinition = "TEXT")
    private String motivoRechazo;
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "estado", nullable = false)
    private Boolean estado = true;
    
    // Relaciones (opcional, si deseas usar JPA relationships)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_usuario", insertable = false, updatable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_tipo_vehiculo", insertable = false, updatable = false)
    private TipoVehiculo tipoVehiculo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_estado_aprobacion", insertable = false, updatable = false)
    private EstadoAprobacion estadoAprobacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (estado == null) {
            estado = true;
        }
        if (disponible == null) {
            disponible = false;
        }
        if (codigoEstadoAprobacion == null) {
            codigoEstadoAprobacion = 7L; // 7 = Pendiente
        }
    }
}
