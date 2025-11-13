package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad CategoriaRestaurante - Relación muchos a muchos entre Categoria y Restaurante
 */
@Entity
@Table(name = "CategoriaRestaurante")
@Data
@IdClass(CategoriaRestauranteId.class)
public class CategoriaRestaurante {
    
    @Id
    @Column(name = "CodigoRestaurante")
    private Long codigoRestaurante;
    
    @Id
    @Column(name = "CodigoCategoria")
    private Long codigoCategoria;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CodigoRestaurante", insertable = false, updatable = false)
    private Restaurante restaurante;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CodigoCategoria", insertable = false, updatable = false)
    private Categoria categoria;
}
