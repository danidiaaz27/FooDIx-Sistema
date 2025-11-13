package com.example.SistemaDePromociones.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para la respuesta de la API de SUNAT (RUC)
 */
public class SunatResponseDTO {
    
    @JsonProperty("numeroDocumento")
    private String numeroDocumento;
    
    @JsonProperty("razonSocial")
    private String razonSocial;
    
    @JsonProperty("nombreComercial")
    private String nombreComercial;
    
    @JsonProperty("estado")
    private String estado;
    
    @JsonProperty("condicion")
    private String condicion;
    
    @JsonProperty("direccion")
    private String direccion;
    
    @JsonProperty("ubigeo")
    private String ubigeo;
    
    @JsonProperty("viaTipo")
    private String viaTipo;
    
    @JsonProperty("viaNombre")
    private String viaNombre;
    
    @JsonProperty("departamento")
    private String departamento;
    
    @JsonProperty("provincia")
    private String provincia;
    
    @JsonProperty("distrito")
    private String distrito;
    
    // Constructores
    public SunatResponseDTO() {
    }
    
    // Getters y Setters
    public String getNumeroDocumento() {
        return numeroDocumento;
    }
    
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
    
    public String getRazonSocial() {
        return razonSocial;
    }
    
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }
    
    public String getNombreComercial() {
        return nombreComercial;
    }
    
    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getCondicion() {
        return condicion;
    }
    
    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getUbigeo() {
        return ubigeo;
    }
    
    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }
    
    public String getViaTipo() {
        return viaTipo;
    }
    
    public void setViaTipo(String viaTipo) {
        this.viaTipo = viaTipo;
    }
    
    public String getViaNombre() {
        return viaNombre;
    }
    
    public void setViaNombre(String viaNombre) {
        this.viaNombre = viaNombre;
    }
    
    public String getDepartamento() {
        return departamento;
    }
    
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    
    public String getProvincia() {
        return provincia;
    }
    
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
    
    public String getDistrito() {
        return distrito;
    }
    
    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }
    
    @Override
    public String toString() {
        return "SunatResponseDTO{" +
                "numeroDocumento='" + numeroDocumento + '\'' +
                ", razonSocial='" + razonSocial + '\'' +
                ", estado='" + estado + '\'' +
                ", condicion='" + condicion + '\'' +
                ", direccion='" + direccion + '\'' +
                ", distrito='" + distrito + '\'' +
                '}';
    }
}
