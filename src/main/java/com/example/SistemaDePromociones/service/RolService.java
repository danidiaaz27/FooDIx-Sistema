package com.example.SistemaDePromociones.service;

import com.example.SistemaDePromociones.model.Rol;
import com.example.SistemaDePromociones.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar roles del sistema
 */
@Service
public class RolService {
    
    @Autowired
    private RolRepository rolRepository;
    
    /**
     * Obtener todos los roles activos
     */
    public List<Rol> obtenerRolesActivos() {
        return rolRepository.findAll().stream()
                .filter(Rol::getEstado)
                .toList();
    }
    
    /**
     * Obtener rol por código
     */
    public Optional<Rol> obtenerPorCodigo(Long codigo) {
        return rolRepository.findById(codigo);
    }
    
    /**
     * Obtener rol por nombre
     */
    public Optional<Rol> obtenerPorNombre(String nombre) {
        return rolRepository.findByNombreIgnoreCase(nombre);
    }
    
    /**
     * Obtener rol de administrador
     */
    public Rol obtenerRolAdministrador() {
        return rolRepository.findById(Rol.ADMINISTRADOR)
                .orElseThrow(() -> new RuntimeException("Rol de administrador no encontrado"));
    }
    
    /**
     * Obtener rol de restaurante
     */
    public Rol obtenerRolRestaurante() {
        return rolRepository.findById(Rol.RESTAURANTE)
                .orElseThrow(() -> new RuntimeException("Rol de restaurante no encontrado"));
    }
    
    /**
     * Obtener rol de repartidor
     */
    public Rol obtenerRolRepartidor() {
        return rolRepository.findById(Rol.REPARTIDOR)
                .orElseThrow(() -> new RuntimeException("Rol de repartidor no encontrado"));
    }
    
    /**
     * Obtener rol de usuario/cliente
     */
    public Rol obtenerRolUsuario() {
        return rolRepository.findById(Rol.USUARIO)
                .orElseThrow(() -> new RuntimeException("Rol de usuario no encontrado"));
    }
    
    /**
     * Verificar si un rol existe
     */
    public boolean existeRol(Long codigo) {
        return rolRepository.existsById(codigo);
    }
    
    /**
     * Verificar si un rol es válido para registro
     * (Solo permite RESTAURANTE, REPARTIDOR, USUARIO - no ADMINISTRADOR)
     */
    public boolean esRolValidoParaRegistro(Long codigoRol) {
        return codigoRol != null && 
               !codigoRol.equals(Rol.ADMINISTRADOR) &&
               existeRol(codigoRol);
    }
}
