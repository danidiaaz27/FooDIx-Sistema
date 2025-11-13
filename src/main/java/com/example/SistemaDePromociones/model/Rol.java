package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad Rol - Representa los roles del sistema
 * (ADMINISTRADOR, RESTAURANTE, REPARTIDOR, USUARIO)
 */
@Entity
@Table(name = "rol")
@Data
public class Rol {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo;
    
    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;
    
    @Column(name = "descripcion", length = 100)
    private String descripcion;
    
    @Column(name = "estado", nullable = false)
    private Boolean estado = true;
    
    // Constantes para facilitar el manejo de roles
    public static final Long ADMINISTRADOR = 1L;
    public static final Long RESTAURANTE = 2L;
    public static final Long REPARTIDOR = 3L;
    public static final Long USUARIO = 4L;
    
    /**
     * Verifica si es rol de administrador
     */
    public boolean esAdministrador() {
        return ADMINISTRADOR.equals(this.codigo);
    }
    
    /**
     * Verifica si es rol de restaurante
     */
    public boolean esRestaurante() {
        return RESTAURANTE.equals(this.codigo);
    }
    
    /**
     * Verifica si es rol de repartidor
     */
    public boolean esRepartidor() {
        return REPARTIDOR.equals(this.codigo);
    }
    
    /**
     * Verifica si es rol de usuario/cliente
     */
    public boolean esUsuarioCliente() {
        return USUARIO.equals(this.codigo);
    }
}
