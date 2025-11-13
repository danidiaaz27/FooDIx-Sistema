package com.example.SistemaDePromociones.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para el registro de restaurantes
 */
@Data
public class RestauranteRegistroDTO {
    
    // Datos personales del representante (usuario)
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String tipoDocumento; // "DNI" o "CE"
    private String numeroDocumento;
    private LocalDate fechaNacimiento;
    private String correoElectronico;
    private String contrasena;
    private String telefono;
    
    // Ubicación personal
    private String direccionPersonal;
    private Long codigoDepartamentoPersonal;
    private Long codigoProvinciaPersonal;
    private Long codigoDistritoPersonal;
    
    // Datos del restaurante
    private String ruc;
    private String razonSocial;
    private String nombreComercial;
    private String descripcion;
    
    // Ubicación del negocio
    private String direccionNegocio;
    private String telefonoNegocio;
    private String correoNegocio;
    private Long codigoDepartamentoNegocio;
    private Long codigoProvinciaNegocio;
    private Long codigoDistritoNegocio;
    
    // Categorías (pueden ser múltiples)
    private List<Long> categorias;
    
    // Archivos - Documentos
    private MultipartFile cartaRestaurante;
    private MultipartFile licenciaFuncionamiento;
    private MultipartFile carnetSanidad;
    
    // Archivos - Imágenes
    private MultipartFile logo;
    private MultipartFile portada;
    private List<MultipartFile> galeria;
}
