package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.model.TipoUnidadMedida;
import com.example.SistemaDePromociones.repository.TipoUnidadMedidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-unidad-medida")
public class TipoUnidadMedidaController {
    
    @Autowired
    private TipoUnidadMedidaRepository tipoUnidadMedidaRepository;
    
    /**
     * Obtiene todos los tipos de unidades de medida
     * GET /api/tipos-unidad-medida
     */
    @GetMapping
    public ResponseEntity<List<TipoUnidadMedida>> listarTipos() {
        try {
            List<TipoUnidadMedida> tipos = tipoUnidadMedidaRepository.findAllByOrderByNombreAsc();
            return ResponseEntity.ok(tipos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
