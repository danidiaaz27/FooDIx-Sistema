package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad Permiso - Representa los permisos de acceso a secciones del sistema
 * Cada permiso controla el acceso a una sección específica del menú administrador
 */
@Entity
@Table(name = "permiso")
@Data
public class Permiso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo;
    
    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;
    
    @Column(name = "descripcion", length = 200)
    private String descripcion;
    
    @Column(name = "seccion", nullable = false, length = 50)
    private String seccion; // usuarios, clientes, restaurantes, delivery, categorias, configuracion
    
    @Column(name = "estado", nullable = false)
    private Boolean estado = true;
    
    // Constantes para las secciones
    public static final String SECCION_USUARIOS = "usuarios";
    public static final String SECCION_CLIENTES = "clientes";
    public static final String SECCION_RESTAURANTES = "restaurantes";
    public static final String SECCION_DELIVERY = "delivery";
    public static final String SECCION_CATEGORIAS = "categorias";
    public static final String SECCION_CONFIGURACION = "configuracion";
    
    /**
     * Obtiene un nombre formateado del permiso para mostrar en UI
     * Ejemplo: "USUARIOS_CREAR" -> "Crear"
     */
    public String getAccion() {
        if (nombre == null || !nombre.contains("_")) {
            return nombre;
        }
        String[] partes = nombre.split("_");
        if (partes.length < 2) {
            return nombre;
        }
        String accion = partes[partes.length - 1].toLowerCase();
        return accion.substring(0, 1).toUpperCase() + accion.substring(1);
    }
    
    /**
     * Obtiene el grupo del permiso (primera parte antes del _)
     * Ejemplo: "USUARIOS_CREAR" -> "USUARIOS"
     */
    public String getGrupo() {
        if (nombre == null || !nombre.contains("_")) {
            return seccion;
        }
        return nombre.split("_")[0];
    }
}
