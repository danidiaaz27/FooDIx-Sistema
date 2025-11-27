package com.example.SistemaDePromociones.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * ⚠️ CONTROLADOR TEMPORAL SOLO PARA DESARROLLO
 * Eliminar en producción
 */
@RestController
@RequestMapping("/api/test-password")
public class PasswordTestController {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Genera un hash BCrypt para una contraseña
     * GET /api/test-password/hash?password=tucontraseña
     */
    @GetMapping("/hash")
    public Map<String, String> generateHash(@RequestParam String password) {
        Map<String, String> response = new HashMap<>();
        String hash = passwordEncoder.encode(password);
        
        response.put("password", password);
        response.put("hash", hash);
        response.put("algorithm", "BCrypt");
        
        System.out.println("🔐 Hash generado para: " + password);
        System.out.println("   Hash: " + hash);
        
        return response;
    }
    
    /**
     * Verifica si una contraseña coincide con un hash
     * GET /api/test-password/verify?password=tucontraseña&hash=elhash
     */
    @GetMapping("/verify")
    public Map<String, Object> verifyPassword(
            @RequestParam String password,
            @RequestParam String hash) {
        
        Map<String, Object> response = new HashMap<>();
        boolean matches = passwordEncoder.matches(password, hash);
        
        response.put("password", password);
        response.put("hash", hash);
        response.put("matches", matches);
        
        System.out.println("🔍 Verificando contraseña: " + password);
        System.out.println("   Hash: " + hash);
        System.out.println("   Coincide: " + matches);
        
        return response;
    }
    
    /**
     * Prueba el hash por defecto de la BD
     * GET /api/test-password/test-default
     */
    @GetMapping("/test-default")
    public Map<String, Object> testDefaultPassword() {
        Map<String, Object> response = new HashMap<>();
        String defaultHash = "$2a$12$Nc5S.bEvkzigCcsXx8FRpeZc5jUi4JzXyOQLmjc2Wj51bZt1zj8Ge";
        
        // Probar contraseñas comunes
        String[] commonPasswords = {"password123", "123456", "admin123", "12345678"};
        
        for (String pwd : commonPasswords) {
            boolean matches = passwordEncoder.matches(pwd, defaultHash);
            response.put(pwd, matches);
            if (matches) {
                System.out.println("✅ Contraseña encontrada: " + pwd);
            }
        }
        
        return response;
    }
    
    /**
     * Genera hashes para múltiples contraseñas comunes
     * GET /api/test-password/generate-common
     */
    @GetMapping("/generate-common")
    public Map<String, String> generateCommonHashes() {
        Map<String, String> response = new HashMap<>();
        
        String[] commonPasswords = {"password123", "admin123", "12345678", "123456"};
        
        for (String pwd : commonPasswords) {
            String hash = passwordEncoder.encode(pwd);
            response.put(pwd, hash);
            System.out.println("🔐 " + pwd + " → " + hash);
        }
        
        return response;
    }
}
