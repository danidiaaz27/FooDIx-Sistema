package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
// import lombok.Data; // Lombok eliminado
import java.time.LocalDateTime;

/**
 * Entidad Restaurante - Representa la tabla restaurante en la base de datos
 * Almacena información de los restaurantes registrados en la plataforma
 */
@Entity
@Table(name = "restaurante")
// @Data eliminado
public class Restaurante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo;

    public Long getId() {
        return codigo;
    }
    
    @Column(name = "codigo_usuario", nullable = false)
    private Long codigoUsuario;
    
    @Column(name = "nombre")
    private String nombre;

    @Column(name = "razon_social")
    private String razonSocial;

    @Column(name = "ruc")
    private String ruc;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "direccion")
    private String direccion;

        @Column(name = "codigo_distrito")
        private Long codigoDistrito;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "codigo_distrito", insertable = false, updatable = false)
        private Distrito distrito;


    @Column(name = "estado")
    private Boolean estado;

    @Column(name = "codigo_estado_aprobacion")
    private Long codigoEstadoAprobacion;

    @Column(name = "codigo_aprobador")
    private Long codigoAprobador;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_aprobacion")
    private LocalDateTime fechaAprobacion;

    @Column(name = "motivo_rechazo")
    private String motivoRechazo;

    @Column(name = "correo_electronico")
    private String correoElectronico;

    // Getters y setters solo para los campos existentes
    public Long getCodigo() { return codigo; }
    public void setCodigo(Long codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }


    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    public Long getCodigoEstadoAprobacion() { return codigoEstadoAprobacion; }
    public void setCodigoEstadoAprobacion(Long codigoEstadoAprobacion) { this.codigoEstadoAprobacion = codigoEstadoAprobacion; }

    public Long getCodigoAprobador() { return codigoAprobador; }
    public void setCodigoAprobador(Long codigoAprobador) { this.codigoAprobador = codigoAprobador; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaAprobacion() { return fechaAprobacion; }
    public void setFechaAprobacion(LocalDateTime fechaAprobacion) { this.fechaAprobacion = fechaAprobacion; }

    public String getMotivoRechazo() { return motivoRechazo; }
    public void setMotivoRechazo(String motivoRechazo) { this.motivoRechazo = motivoRechazo; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

        public Long getCodigoDistrito() {
            return codigoDistrito;
        }

        public void setCodigoDistrito(Long codigoDistrito) {
            this.codigoDistrito = codigoDistrito;
        }

        public Distrito getDistrito() {
            return distrito;
        }

        public void setDistrito(Distrito distrito) {
            this.distrito = distrito;
        }

    public Long getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(Long codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

}
