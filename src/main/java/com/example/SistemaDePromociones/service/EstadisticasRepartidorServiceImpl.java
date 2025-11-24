package com.example.SistemaDePromociones.service;

import com.example.SistemaDePromociones.model.EstadisticasRepartidor;
import org.springframework.stereotype.Service;

@Service
public class EstadisticasRepartidorServiceImpl implements EstadisticasRepartidorService {
    @Override
    public EstadisticasRepartidor calcularEstadisticas(Long codigoRepartidor) {
        // Aquí puedes implementar la lógica real de cálculo
        // Por ahora, retorna un objeto vacío para evitar errores en el template
        return new EstadisticasRepartidor();
    }
}
