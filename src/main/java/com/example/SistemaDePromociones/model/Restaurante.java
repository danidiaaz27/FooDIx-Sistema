package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entidad Restaurante - Representa la tabla restaurante en la base de datos
 * Almacena información de los restaurantes registrados en la plataforma
 */
@Entity
@Table(name = "restaurante")
@Data
public class Restaurante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo;
    
    @Column(name = "codigo_usuario", nullable = false)
    private Long codigoUsuario;
    
    @Column(name = "ruc", nullable = false, unique = true, length = 15)
    private String ruc;
    
    @Column(name = "razon_social", nullable = false, length = 150)
    private String razonSocial;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "direccion", length = 200)
    private String direccion;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "correo_electronico", length = 150)
    private String correoElectronico;
    
    @Column(name = "codigo_distrito", nullable = false)
    private Long codigoDistrito;
    
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
    
    // Relaciones (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_usuario", insertable = false, updatable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_distrito", insertable = false, updatable = false)
    private Distrito distrito;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_estado_aprobacion", insertable = false, updatable = false)
    private EstadoAprobacion estadoAprobacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (estado == null) {
            estado = true;
        }
        if (codigoEstadoAprobacion == null) {
            codigoEstadoAprobacion = 7L; // Pendiente
        }
    }
}
