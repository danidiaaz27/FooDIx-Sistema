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
@Table(name = "Usuario")
@Data
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo")
    private Long codigo;
    
    @Column(name = "Nombre", nullable = false, length = 50)
    private String nombre;
    
    @Column(name = "ApellidoPaterno", nullable = false, length = 50)
    private String apellidoPaterno;
    
    @Column(name = "ApellidoMaterno", nullable = false, length = 50)
    private String apellidoMaterno;
    
    @Column(name = "NumeroDocumento", nullable = false, unique = true, length = 15)
    private String numeroDocumento;
    
    @Column(name = "FechaNacimiento", nullable = false)
    private LocalDate fechaNacimiento;
    
    @Column(name = "CorreoElectronico", nullable = false, unique = true, length = 50)
    private String correoElectronico;
    
    @Column(name = "Contrasena", nullable = false, length = 255)
    private String contrasena;
    
    @Column(name = "Telefono", length = 20)
    private String telefono;
    
    @Column(name = "Direccion", length = 100)
    private String direccion;
    
    @Column(name = "CodigoTipoDocumento", nullable = false)
    private Long codigoTipoDocumento;
    
    @Column(name = "CodigoRol", nullable = false, insertable = false, updatable = false)
    private Long codigoRol;
    
    // Relación con Rol
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CodigoRol", referencedColumnName = "codigo")
    private Rol rol;
    
    @Column(name = "CodigoDistrito", nullable = false)
    private Long codigoDistrito;
    
    @Column(name = "FechaCreacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "FechaModificacion")
    private LocalDateTime fechaModificacion;
    
    @Column(name = "Estado", nullable = false)
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
