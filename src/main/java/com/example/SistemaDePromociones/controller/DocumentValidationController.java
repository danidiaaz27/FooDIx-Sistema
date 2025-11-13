package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.dto.ReniecResponseDTO;
import com.example.SistemaDePromociones.dto.SunatResponseDTO;
import com.example.SistemaDePromociones.service.DocumentValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para validación de documentos (DNI y RUC)
 */
@RestController
@RequestMapping("/api/validation")
public class DocumentValidationController {
    
    @Autowired
    private DocumentValidationService documentValidationService;
    
    /**
     * Endpoint para consultar DNI
     * GET /api/validation/dni/{numero}
     */
    @GetMapping("/dni/{numero}")
    public ResponseEntity<?> consultarDNI(@PathVariable String numero) {
        try {
            System.out.println("📥 [API] Recibida petición para DNI: " + numero);
            
            ReniecResponseDTO response = documentValidationService.consultarDNI(numero);
            
            if (response == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("DNI no encontrado"));
            }
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            System.err.println("❌ [API] Error en consulta DNI: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Error al consultar DNI: " + e.getMessage()));
        }
    }
    
    /**
     * Endpoint para consultar RUC
     * GET /api/validation/ruc/{numero}
     */
    @GetMapping("/ruc/{numero}")
    public ResponseEntity<?> consultarRUC(@PathVariable String numero) {
        try {
            System.out.println("📥 [API] Recibida petición para RUC: " + numero);
            
            SunatResponseDTO response = documentValidationService.consultarRUC(numero);
            
            if (response == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("RUC no encontrado"));
            }
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            System.err.println("❌ [API] Error en consulta RUC: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Error al consultar RUC: " + e.getMessage()));
        }
    }
    
    /**
     * Método auxiliar para crear respuestas de error
     */
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}
