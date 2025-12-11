package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "configuracion_comision")
public class ConfiguracionComision {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;
    
    @Column(name = "porcentaje_comision", nullable = false)
    private BigDecimal porcentajeComision = new BigDecimal("5.00");
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "fecha_vigencia", nullable = false)
    private LocalDate fechaVigencia;
    
    @Column(name = "estado", nullable = false)
    private Boolean estado = true;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // Constructors
    public ConfiguracionComision() {}

    public ConfiguracionComision(BigDecimal porcentajeComision, String descripcion, LocalDate fechaVigencia) {
        this.porcentajeComision = porcentajeComision;
        this.descripcion = descripcion;
        this.fechaVigencia = fechaVigencia;
    }

    // Getters y Setters
    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getPorcentajeComision() {
        return porcentajeComision;
    }

    public void setPorcentajeComision(BigDecimal porcentajeComision) {
        this.porcentajeComision = porcentajeComision;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(LocalDate fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
