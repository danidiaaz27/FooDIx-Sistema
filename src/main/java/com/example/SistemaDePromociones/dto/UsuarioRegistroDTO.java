package com.example.SistemaDePromociones.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * DTO para el registro de usuarios (datos personales - Paso 1)
 * Usado tanto para clientes, restaurantes y repartidores
 */
@Data
public class UsuarioRegistroDTO {
    
    // Datos personales
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private Integer codigoTipoDocumento; // 1=DNI, 2=Carnet Extranjería, 3=Pasaporte
    private String numeroDocumento;
    private LocalDate fechaNacimiento;
    private String correoElectronico;
    private String contrasena;
    private String telefono;
    private String direccion;
    
    // Ubicación
    private Long codigoDepartamento;
    private Long codigoProvincia;
    private Long codigoDistrito;
    
    // Rol del usuario
    private Integer codigoRol; // 1=Cliente, 2=Restaurante, 3=Repartidor
}
