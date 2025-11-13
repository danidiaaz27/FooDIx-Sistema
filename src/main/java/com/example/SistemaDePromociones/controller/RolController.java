package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.model.Rol;
import com.example.SistemaDePromociones.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar roles
 */
@RestController
@RequestMapping("/api/roles")
public class RolController {
    
    @Autowired
    private RolService rolService;
    
    /**
     * Obtener todos los roles activos
     */
    @GetMapping
    public ResponseEntity<List<Rol>> obtenerRoles() {
        List<Rol> roles = rolService.obtenerRolesActivos();
        return ResponseEntity.ok(roles);
    }
    
    /**
     * Obtener rol por código
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<Rol> obtenerRolPorCodigo(@PathVariable Long codigo) {
        return rolService.obtenerPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtener roles disponibles para registro
     * (Excluye ADMINISTRADOR)
     */
    @GetMapping("/registro")
    public ResponseEntity<List<Rol>> obtenerRolesParaRegistro() {
        List<Rol> roles = rolService.obtenerRolesActivos().stream()
                .filter(rol -> !rol.getCodigo().equals(Rol.ADMINISTRADOR))
                .toList();
        return ResponseEntity.ok(roles);
    }
}
