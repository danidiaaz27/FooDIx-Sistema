package com.example.SistemaDePromociones.config;

import com.example.SistemaDePromociones.model.Usuario;
import com.example.SistemaDePromociones.security.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Controlador global que inyecta el usuario autenticado en todos los modelos
 */
@ControllerAdvice
public class GlobalControllerAdvice {
    
    @ModelAttribute("currentUser")
    public Usuario getCurrentUser(HttpSession session) {
        // Primero intentar obtener de la sesión HTTP
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        if (usuario != null) {
            return usuario;
        }
        
        // Si no está en sesión, intentar obtener del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() 
                && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            usuario = userDetails.getUsuario();
            
            // Guardar en sesión para próximas peticiones
            session.setAttribute("usuario", usuario);
            
            return usuario;
        }
        
        return null;
    }
}
