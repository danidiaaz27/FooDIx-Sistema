package com.example.SistemaDePromociones.service;

import com.example.SistemaDePromociones.model.EstadisticasRestaurante;

public interface EstadisticasRestauranteService {
    EstadisticasRestaurante calcularEstadisticas(Long restauranteId);
}
