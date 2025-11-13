package com.example.SistemaDePromociones.service;

import com.example.SistemaDePromociones.model.Usuario;
import com.example.SistemaDePromociones.repository.UsuarioRepository;
import com.example.SistemaDePromociones.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio personalizado para cargar usuarios desde la base de datos
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Override
    public UserDetails loadUserByUsername(String correoElectronico) throws UsernameNotFoundException {
        System.out.println("🔐 [AUTH] Buscando usuario: " + correoElectronico);
        
        Usuario usuario = usuarioRepository.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> {
                    System.out.println("❌ [AUTH] Usuario NO encontrado: " + correoElectronico);
                    return new UsernameNotFoundException(
                            "Usuario no encontrado con email: " + correoElectronico);
                });
        
        System.out.println("✅ [AUTH] Usuario encontrado: " + usuario.getNombre() + " " + usuario.getApellidoPaterno());
        System.out.println("   - Email: " + usuario.getCorreoElectronico());
        System.out.println("   - Código Rol: " + usuario.getCodigoRol());
        System.out.println("   - Nombre Rol: " + usuario.getNombreRol());
        System.out.println("   - Estado: " + usuario.getEstado());
        System.out.println("   - Password hash: " + (usuario.getContrasena() != null ? "Presente" : "NULL"));
        
        return new CustomUserDetails(usuario);
    }
}
