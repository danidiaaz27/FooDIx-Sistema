package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.dto.PlatoMenuDTO;
import com.example.SistemaDePromociones.service.PlatoMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar el menú de platos de los restaurantes
 */
@RestController
@RequestMapping("/api/platos")
public class PlatoMenuController {
    
    @Autowired
    private PlatoMenuService platoMenuService;
    
    /**
     * Obtener todos los platos de un restaurante
     */
    @GetMapping("/restaurante/{codigoRestaurante}")
    public ResponseEntity<?> obtenerPlatosPorRestaurante(@PathVariable Long codigoRestaurante) {
        try {
            List<PlatoMenuDTO> platos = platoMenuService.obtenerPlatosPorRestaurante(codigoRestaurante);
            return ResponseEntity.ok(platos);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error al obtener los platos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Obtener platos con sus unidades para el formulario de promociones
     * Endpoint específico para cargar platos en el modal de crear promoción
     */
    @GetMapping("/restaurante/{codigoRestaurante}/para-promocion")
    public ResponseEntity<?> obtenerPlatosParaPromocion(@PathVariable Long codigoRestaurante) {
        try {
            List<PlatoMenuDTO> platos = platoMenuService.obtenerPlatosPorRestaurante(codigoRestaurante);
            return ResponseEntity.ok(platos);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error al obtener los platos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Obtener un plato por su código
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<?> obtenerPlatoPorId(@PathVariable Long codigo) {
        try {
            PlatoMenuDTO plato = platoMenuService.obtenerPlatoPorId(codigo);
            return ResponseEntity.ok(plato);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error al obtener el plato: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Crear un nuevo plato
     */
    @PostMapping
    public ResponseEntity<?> crearPlato(@RequestBody PlatoMenuDTO platoDTO) {
        try {
            PlatoMenuDTO platoCreado = platoMenuService.crearPlato(platoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(platoCreado);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error al crear el plato: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Actualizar un plato existente
     */
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizarPlato(@PathVariable Long codigo, @RequestBody PlatoMenuDTO platoDTO) {
        try {
            PlatoMenuDTO platoActualizado = platoMenuService.actualizarPlato(codigo, platoDTO);
            return ResponseEntity.ok(platoActualizado);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error al actualizar el plato: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Eliminar un plato (borrado lógico)
     */
    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> eliminarPlato(@PathVariable Long codigo) {
        try {
            platoMenuService.eliminarPlato(codigo);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Plato eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error al eliminar el plato: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
