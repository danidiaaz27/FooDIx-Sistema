package com.example.SistemaDePromociones.service;

import com.example.SistemaDePromociones.repository.jdbc.UsuarioJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Servicio para gestionar usuarios - USANDO JDBC
 */
@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioJdbcRepository usuarioRepository;
    
    /**
     * Crear un nuevo usuario usando SQL directo
     */
    @Transactional
    public Long crearUsuario(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            String numeroDocumento,
            LocalDate fechaNacimiento,
            String correoElectronico,
            String contrasena,
            String telefono,
            String direccion,
            Integer codigoTipoDocumento,
            Integer codigoRol,
            Long codigoDistrito
    ) {
        // Validar que no exista el correo
        if (usuarioRepository.existsByCorreoElectronico(correoElectronico)) {
            throw new RuntimeException("El correo electrónico ya está registrado");
        }
        
        // Validar que no exista el documento
        if (usuarioRepository.existsByNumeroDocumento(numeroDocumento)) {
            throw new RuntimeException("El número de documento ya está registrado");
        }
        
        // Insertar usuario y retornar el ID generado
        return usuarioRepository.insertUsuario(
            nombre,
            apellidoPaterno,
            apellidoMaterno,
            numeroDocumento,
            fechaNacimiento,
            correoElectronico,
            contrasena,  // TODO: Implementar BCrypt
            telefono,
            direccion,
            Long.valueOf(codigoTipoDocumento),
            Long.valueOf(codigoRol),
            codigoDistrito
        );
    }
}
