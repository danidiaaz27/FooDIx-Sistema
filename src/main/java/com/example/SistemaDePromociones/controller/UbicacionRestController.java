package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.model.Distrito;
import com.example.SistemaDePromociones.model.Provincia;
import com.example.SistemaDePromociones.repository.jdbc.DistritoJdbcRepository;
import com.example.SistemaDePromociones.repository.jdbc.ProvinciaJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller para APIs de ubicación (provincias y distritos)
 * Usa JdbcTemplate para SQL directo sin Hibernate
 */
@RestController
@RequestMapping("/api")
public class UbicacionRestController {
    
    @Autowired
    private ProvinciaJdbcRepository provinciaRepository;
    
    @Autowired
    private DistritoJdbcRepository distritoRepository;
    
    /**
     * Obtener provincias por departamento
     * GET /api/provincias/{codigoDepartamento}
     */
    @GetMapping("/provincias/{codigoDepartamento}")
    public ResponseEntity<List<Provincia>> obtenerProvinciasPorDepartamento(
            @PathVariable Long codigoDepartamento) {
        System.out.println("🌍 API: Obteniendo provincias para departamento: " + codigoDepartamento);
        List<Provincia> provincias = provinciaRepository.findByDepartamento(codigoDepartamento);
        System.out.println("🌍 API: Provincias encontradas: " + provincias.size());
        provincias.forEach(p -> System.out.println("   - " + p.getCodigo() + ": " + p.getNombre()));
        return ResponseEntity.ok(provincias);
    }
    
    /**
     * Obtener distritos por provincia
     * GET /api/distritos/{codigoProvincia}
     */
    @GetMapping("/distritos/{codigoProvincia}")
    public ResponseEntity<List<Distrito>> obtenerDistritosPorProvincia(
            @PathVariable Long codigoProvincia) {
        System.out.println("🏘️ API: Obteniendo distritos para provincia: " + codigoProvincia);
        List<Distrito> distritos = distritoRepository.findByProvincia(codigoProvincia);
        System.out.println("🏘️ API: Distritos encontrados: " + distritos.size());
        distritos.forEach(d -> System.out.println("   - " + d.getCodigo() + ": " + d.getNombre()));
        return ResponseEntity.ok(distritos);
    }
}
