package com.example.SistemaDePromociones.model;

import lombok.Data;
import java.io.Serializable;

/**
 * Clase de ID compuesta para CategoriaRestaurante
 */
@Data
public class CategoriaRestauranteId implements Serializable {
    private Long codigoRestaurante;
    private Long codigoCategoria;
    
    public CategoriaRestauranteId() {}
    
    public CategoriaRestauranteId(Long codigoRestaurante, Long codigoCategoria) {
        this.codigoRestaurante = codigoRestaurante;
        this.codigoCategoria = codigoCategoria;
    }
}
