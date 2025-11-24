package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;

@Entity
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String nombre;
    private String descripcion;

    @Column(name = "codigo_restaurante")
    private Long codigoRestaurante;

    // Si tu tabla producto tiene más campos, agrégalos aquí

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Long getCodigoRestaurante() { return codigoRestaurante; }
    public void setCodigoRestaurante(Long codigoRestaurante) { this.codigoRestaurante = codigoRestaurante; }
}
