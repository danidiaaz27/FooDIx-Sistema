package com.example.SistemaDePromociones.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para la respuesta de MiAPI (DNI)
 * Estructura: { "success": true, "datos": {...} }
 */
public class MiApiDniResponseDTO {
    
    @JsonProperty("success")
    private boolean success;
    
    @JsonProperty("datos")
    private DatosPersona datos;
    
    // Clase interna para los datos de la persona
    public static class DatosPersona {
        @JsonProperty("dni")
        private String dni;
        
        @JsonProperty("nombres")
        private String nombres;
        
        @JsonProperty("ape_paterno")
        private String apePaterno;
        
        @JsonProperty("ape_materno")
        private String apeMaterno;
        
        // Constructor vacío
        public DatosPersona() {
        }
        
        // Getters y Setters
        public String getDni() {
            return dni;
        }
        
        public void setDni(String dni) {
            this.dni = dni;
        }
        
        public String getNombres() {
            return nombres;
        }
        
        public void setNombres(String nombres) {
            this.nombres = nombres;
        }
        
        public String getApePaterno() {
            return apePaterno;
        }
        
        public void setApePaterno(String apePaterno) {
            this.apePaterno = apePaterno;
        }
        
        public String getApeMaterno() {
            return apeMaterno;
        }
        
        public void setApeMaterno(String apeMaterno) {
            this.apeMaterno = apeMaterno;
        }
        
        @Override
        public String toString() {
            return "DatosPersona{" +
                    "dni='" + dni + '\'' +
                    ", nombres='" + nombres + '\'' +
                    ", apePaterno='" + apePaterno + '\'' +
                    ", apeMaterno='" + apeMaterno + '\'' +
                    '}';
        }
    }
    
    // Constructor vacío
    public MiApiDniResponseDTO() {
    }
    
    // Getters y Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public DatosPersona getDatos() {
        return datos;
    }
    
    public void setDatos(DatosPersona datos) {
        this.datos = datos;
    }
    
    /**
     * Convierte esta respuesta al formato ReniecResponseDTO para mantener compatibilidad
     */
    public ReniecResponseDTO toReniecResponse() {
        if (datos == null) {
            return null;
        }
        return new ReniecResponseDTO(
            datos.getDni(),
            datos.getNombres(),
            datos.getApePaterno(),
            datos.getApeMaterno()
        );
    }
    
    @Override
    public String toString() {
        return "MiApiDniResponseDTO{" +
                "success=" + success +
                ", datos=" + datos +
                '}';
    }
}
