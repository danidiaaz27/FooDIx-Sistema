package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad Usuario
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Buscar usuario por correo electrónico
     */
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);
    
    /**
     * Buscar usuario por número de documento
     */
    Optional<Usuario> findByNumeroDocumento(String numeroDocumento);
    
    /**
     * Verificar si existe un correo electrónico
     */
    boolean existsByCorreoElectronico(String correoElectronico);
    
    /**
     * Verificar si existe un número de documento
     */
    boolean existsByNumeroDocumento(String numeroDocumento);
    
    /**
     * Buscar usuarios por código de rol
     */
    List<Usuario> findByCodigoRol(Long codigoRol);
}
