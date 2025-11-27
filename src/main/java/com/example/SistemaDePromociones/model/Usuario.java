package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad Usuario - Representa la tabla Usuario en la base de datos
 * Almacena información básica de todos los usuarios del sistema
 */
@Entity
@Table(name = "usuario")
@Data
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo;
    
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
    
    @Column(name = "apellido_paterno", nullable = false, length = 50)
    private String apellidoPaterno;
    
    @Column(name = "apellido_materno", nullable = false, length = 50)
    private String apellidoMaterno;
    
    @Column(name = "numero_documento", nullable = false, unique = true, length = 15)
    private String numeroDocumento;
    
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    
    @Column(name = "correo_electronico", nullable = false, unique = true, length = 50)
    private String correoElectronico;
    
    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "direccion", length = 100)
    private String direccion;
    
    @Column(name = "codigo_tipo_documento", nullable = false)
    private Long codigoTipoDocumento;
    
    @Column(name = "codigo_rol", nullable = false)
    private Long codigoRol;
    
    // Relación con Rol
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codigo_rol", referencedColumnName = "codigo", insertable = false, updatable = false)
    private Rol rol;
    
    @Column(name = "codigo_distrito", nullable = false)
    private Long codigoDistrito;
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;
    
    @Column(name = "estado", nullable = false)
    private Boolean estado = true;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (estado == null) {
            estado = true;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }
    
    // ============================================
    // Métodos de utilidad para roles
    // ============================================
    
    /**
     * Verifica si el usuario es administrador
     */
    public boolean esAdministrador() {
        return Rol.ADMINISTRADOR.equals(this.codigoRol);
    }
    
    /**
     * Verifica si el usuario es propietario de restaurante
     */
    public boolean esRestaurante() {
        return Rol.RESTAURANTE.equals(this.codigoRol);
    }
    
    /**
     * Verifica si el usuario es repartidor
     */
    public boolean esRepartidor() {
        return Rol.REPARTIDOR.equals(this.codigoRol);
    }
    
    /**
     * Verifica si el usuario es cliente
     */
    public boolean esCliente() {
        return Rol.USUARIO.equals(this.codigoRol);
    }
    
    /**
     * Obtiene el nombre del rol
     */
    public String getNombreRol() {
        return rol != null ? rol.getNombre() : "DESCONOCIDO";
    }
    
    /**
     * Obtiene el nombre completo del usuario
     */
    public String getNombreCompleto() {
        return nombre + " " + apellidoPaterno + " " + apellidoMaterno;
    }
}
