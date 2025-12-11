package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.service.DatabaseBackupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para gestionar backups de base de datos
 * Solo accesible por usuarios con rol ADMINISTRADOR
 */
@RestController
@RequestMapping("/api/backup")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class BackupController {

    private static final Logger logger = LoggerFactory.getLogger(BackupController.class);

    @Autowired
    private DatabaseBackupService backupService;

    /**
     * Endpoint para ejecutar un backup manual
     * POST /api/backup/execute
     */
    @PostMapping("/execute")
    public ResponseEntity<Map<String, Object>> executeBackup() {
        logger.info("Solicitud de backup manual recibida");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String backupFileName = backupService.createBackup();
            
            if (backupFileName != null) {
                response.put("success", true);
                response.put("message", "Backup creado exitosamente");
                response.put("backupFile", backupFileName);
                response.put("timestamp", System.currentTimeMillis());
                
                logger.info("Backup manual completado: {}", backupFileName);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Error al crear el backup");
                
                logger.error("Fallo al crear backup manual");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (Exception e) {
            logger.error("Excepción durante backup manual", e);
            
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para verificar el estado del servicio de backup
     * GET /api/backup/status
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getBackupStatus() {
        Map<String, Object> response = new HashMap<>();
        
        response.put("backupServiceActive", true);
        response.put("scheduledBackupTime", "00:05 AM (America/Lima)");
        response.put("nextBackupInfo", "El próximo backup automático se ejecutará a las 00:05 AM");
        
        return ResponseEntity.ok(response);
    }
}
