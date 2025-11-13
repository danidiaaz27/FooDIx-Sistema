package com.example.SistemaDePromociones.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para la respuesta de MiAPI (RUC)
 * Estructura: { "success": true, "datos": {...} }
 */
public class MiApiRucResponseDTO {
    
    @JsonProperty("success")
    private boolean success;
    
    @JsonProperty("datos")
    private DatosEmpresa datos;
    
    // Clase interna para los datos de la empresa
    public static class DatosEmpresa {
        @JsonProperty("ruc")
        private String ruc;
        
        @JsonProperty("razon_social")
        private String razonSocial;
        
        @JsonProperty("nombre_comercial")
        private String nombreComercial;
        
        @JsonProperty("tipo_contribuyente")
        private String tipoContribuyente;
        
        @JsonProperty("estado")
        private String estado;
        
        @JsonProperty("condicion")
        private String condicion;
        
        @JsonProperty("direccion")
        private String direccion;
        
        @JsonProperty("domicilio_fiscal")
        private DomicilioFiscal domicilioFiscal;
        
        @JsonProperty("ubigeo")
        private String ubigeo;
        
        @JsonProperty("departamento")
        private String departamento;
        
        @JsonProperty("provincia")
        private String provincia;
        
        @JsonProperty("distrito")
        private String distrito;
        
        @JsonProperty("fecha_inscripcion")
        private String fechaInscripcion;
        
        @JsonProperty("fecha_inicio_actividades")
        private String fechaInicioActividades;
        
        // Constructor vacío
        public DatosEmpresa() {
        }
        
        // Getters y Setters
        public String getRuc() {
            return ruc;
        }
        
        public void setRuc(String ruc) {
            this.ruc = ruc;
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
        
        public String getTipoContribuyente() {
            return tipoContribuyente;
        }
        
        public void setTipoContribuyente(String tipoContribuyente) {
            this.tipoContribuyente = tipoContribuyente;
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
        
        public String getFechaInscripcion() {
            return fechaInscripcion;
        }
        
        public void setFechaInscripcion(String fechaInscripcion) {
            this.fechaInscripcion = fechaInscripcion;
        }
        
        public String getFechaInicioActividades() {
            return fechaInicioActividades;
        }
        
        public void setFechaInicioActividades(String fechaInicioActividades) {
            this.fechaInicioActividades = fechaInicioActividades;
        }
        
        @Override
        public String toString() {
            return "DatosEmpresa{" +
                    "ruc='" + ruc + '\'' +
                    ", razonSocial='" + razonSocial + '\'' +
                    ", nombreComercial='" + nombreComercial + '\'' +
                    ", estado='" + estado + '\'' +
                    ", direccion='" + direccion + '\'' +
                    ", distrito='" + distrito + '\'' +
                    '}';
        }
        
        public DomicilioFiscal getDomicilioFiscal() {
            return domicilioFiscal;
        }
        
        public void setDomicilioFiscal(DomicilioFiscal domicilioFiscal) {
            this.domicilioFiscal = domicilioFiscal;
        }
    }
    
    // Clase interna para domicilio fiscal
    public static class DomicilioFiscal {
        @JsonProperty("direccion")
        private String direccion;
        
        @JsonProperty("distrito")
        private String distrito;
        
        @JsonProperty("provincia")
        private String provincia;
        
        @JsonProperty("departamento")
        private String departamento;
        
        public String getDireccion() {
            return direccion;
        }
        
        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }
        
        public String getDistrito() {
            return distrito;
        }
        
        public void setDistrito(String distrito) {
            this.distrito = distrito;
        }
        
        public String getProvincia() {
            return provincia;
        }
        
        public void setProvincia(String provincia) {
            this.provincia = provincia;
        }
        
        public String getDepartamento() {
            return departamento;
        }
        
        public void setDepartamento(String departamento) {
            this.departamento = departamento;
        }
    }
    
    // Constructor vacío
    public MiApiRucResponseDTO() {
    }
    
    // Getters y Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public DatosEmpresa getDatos() {
        return datos;
    }
    
    public void setDatos(DatosEmpresa datos) {
        this.datos = datos;
    }
    
    /**
     * Convierte esta respuesta al formato SunatResponseDTO para mantener compatibilidad
     */
    public SunatResponseDTO toSunatResponse() {
        if (datos == null) {
            return null;
        }
        SunatResponseDTO response = new SunatResponseDTO();
        response.setNumeroDocumento(datos.getRuc());
        response.setRazonSocial(datos.getRazonSocial());
        response.setNombreComercial(datos.getNombreComercial());
        response.setEstado(datos.getEstado());
        response.setCondicion(datos.getCondicion());
        
        // Priorizar domicilio_fiscal sobre direccion simple
        if (datos.getDomicilioFiscal() != null) {
            response.setDireccion(datos.getDomicilioFiscal().getDireccion());
            response.setDistrito(datos.getDomicilioFiscal().getDistrito());
            response.setProvincia(datos.getDomicilioFiscal().getProvincia());
            response.setDepartamento(datos.getDomicilioFiscal().getDepartamento());
        } else {
            response.setDireccion(datos.getDireccion());
            response.setDistrito(datos.getDistrito());
            response.setProvincia(datos.getProvincia());
            response.setDepartamento(datos.getDepartamento());
        }
        
        response.setUbigeo(datos.getUbigeo());
        return response;
    }
    
    @Override
    public String toString() {
        return "MiApiRucResponseDTO{" +
                "success=" + success +
                ", datos=" + datos +
                '}';
    }
}
