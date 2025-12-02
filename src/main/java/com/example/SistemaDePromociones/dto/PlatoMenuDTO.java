package com.example.SistemaDePromociones.dto;

import java.util.List;

/**
 * DTO para crear/actualizar platos del menú
 */
public class PlatoMenuDTO {
    
    private Long codigo;
    private Long codigoRestaurante;
    private String nombre;
    private String descripcion;
    private List<UnidadMedidaDTO> unidadesMedida;
    
    // Constructor vacío
    public PlatoMenuDTO() {
    }
    
    // Constructor completo
    public PlatoMenuDTO(Long codigo, Long codigoRestaurante, String nombre, String descripcion, List<UnidadMedidaDTO> unidadesMedida) {
        this.codigo = codigo;
        this.codigoRestaurante = codigoRestaurante;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.unidadesMedida = unidadesMedida;
    }
    
    // Getters y Setters
    public Long getCodigo() {
        return codigo;
    }
    
    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }
    
    public Long getCodigoRestaurante() {
        return codigoRestaurante;
    }
    
    public void setCodigoRestaurante(Long codigoRestaurante) {
        this.codigoRestaurante = codigoRestaurante;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public List<UnidadMedidaDTO> getUnidadesMedida() {
        return unidadesMedida;
    }
    
    public void setUnidadesMedida(List<UnidadMedidaDTO> unidadesMedida) {
        this.unidadesMedida = unidadesMedida;
    }
    
    /**
     * DTO interno para las unidades de medida
     */
    public static class UnidadMedidaDTO {
        private Long codigo;
        private Long codigoTipoUnidad;  // Ahora usa FK en lugar de nombre
        private String nombreTipoUnidad; // Solo para lectura desde el frontend
        private String descripcion;
        private Double precioOriginal;
        
        public UnidadMedidaDTO() {
        }
        
        public UnidadMedidaDTO(Long codigo, Long codigoTipoUnidad, String nombreTipoUnidad, String descripcion, Double precioOriginal) {
            this.codigo = codigo;
            this.codigoTipoUnidad = codigoTipoUnidad;
            this.nombreTipoUnidad = nombreTipoUnidad;
            this.descripcion = descripcion;
            this.precioOriginal = precioOriginal;
        }
        
        public Long getCodigo() {
            return codigo;
        }
        
        public void setCodigo(Long codigo) {
            this.codigo = codigo;
        }
        
        public Long getCodigoTipoUnidad() {
            return codigoTipoUnidad;
        }
        
        public void setCodigoTipoUnidad(Long codigoTipoUnidad) {
            this.codigoTipoUnidad = codigoTipoUnidad;
        }
        
        public String getNombreTipoUnidad() {
            return nombreTipoUnidad;
        }
        
        public void setNombreTipoUnidad(String nombreTipoUnidad) {
            this.nombreTipoUnidad = nombreTipoUnidad;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
        
        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public Double getPrecioOriginal() {
            return precioOriginal;
        }
        
        public void setPrecioOriginal(Double precioOriginal) {
            this.precioOriginal = precioOriginal;
        }
    }
}
