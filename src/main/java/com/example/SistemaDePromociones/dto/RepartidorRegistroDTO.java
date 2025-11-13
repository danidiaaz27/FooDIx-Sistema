package com.example.SistemaDePromociones.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

/**
 * DTO para el registro de repartidores
 */
@Data
public class RepartidorRegistroDTO {
    
    // Datos personales del usuario
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String tipoDocumento; // "DNI" o "CE"
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
    
    // Datos específicos del repartidor
    private String numeroLicencia;
    private Long codigoTipoVehiculo;
    
    // Archivos
    private MultipartFile licenciaConducir;
    private MultipartFile soat;
    private MultipartFile antecedentesPolicial;
    private MultipartFile tarjetaPropiedad;
}
