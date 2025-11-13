package com.example.SistemaDePromociones.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para la respuesta de la API de RENIEC (DNI)
 */
public class ReniecResponseDTO {
    
    @JsonProperty("numeroDocumento")
    private String numeroDocumento;
    
    @JsonProperty("nombres")
    private String nombres;
    
    @JsonProperty("apellidoPaterno")
    private String apellidoPaterno;
    
    @JsonProperty("apellidoMaterno")
    private String apellidoMaterno;
    
    // Constructores
    public ReniecResponseDTO() {
    }
    
    public ReniecResponseDTO(String numeroDocumento, String nombres, String apellidoPaterno, String apellidoMaterno) {
        this.numeroDocumento = numeroDocumento;
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
    }
    
    // Getters y Setters
    public String getNumeroDocumento() {
        return numeroDocumento;
    }
    
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
    
    public String getNombres() {
        return nombres;
    }
    
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    
    public String getApellidoPaterno() {
        return apellidoPaterno;
    }
    
    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }
    
    public String getApellidoMaterno() {
        return apellidoMaterno;
    }
    
    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }
    
    @Override
    public String toString() {
        return "ReniecResponseDTO{" +
                "numeroDocumento='" + numeroDocumento + '\'' +
                ", nombres='" + nombres + '\'' +
                ", apellidoPaterno='" + apellidoPaterno + '\'' +
                ", apellidoMaterno='" + apellidoMaterno + '\'' +
                '}';
    }
}
