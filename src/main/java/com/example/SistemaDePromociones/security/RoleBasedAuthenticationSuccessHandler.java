package com.example.SistemaDePromociones.security;

import com.example.SistemaDePromociones.model.Rol;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Manejador de autenticación exitosa con redirección basada en roles
 * Utiliza las constantes de la entidad Rol para determinar la redirección
 */
@Component
public class RoleBasedAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                       HttpServletResponse response,
                                       Authentication authentication) throws IOException, ServletException {
        
        // Obtener el rol del usuario autenticado
        String redirectUrl = determineTargetUrl(authentication);
        
        // Redirigir al usuario
        response.sendRedirect(redirectUrl);
    }
    
    private String determineTargetUrl(Authentication authentication) {
        // Obtener las autoridades (roles) del usuario
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();
            
            return switch (role) {
                case "ROLE_ADMIN" -> "/menuAdministrador";
                case "ROLE_RESTAURANT" -> "/menuRestaurante";
                case "ROLE_DELIVERY" -> "/menuDelivery";
                case "ROLE_CUSTOMER" -> "/menuUsuario";
                default -> "/";
            };
        }
        
        // Fallback por defecto
        return "/";
    }
}
