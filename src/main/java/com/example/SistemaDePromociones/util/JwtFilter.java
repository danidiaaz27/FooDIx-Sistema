package com.example.SistemaDePromociones.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro JWT que intercepta todas las peticiones HTTP
 * para validar el token JWT y autenticar al usuario
 */
@Component
public class JwtFilter extends OncePerRequestFilter {
    
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    
    public JwtFilter(JwtUtil jwtUtil, @Qualifier("customUserDetailsService") UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Método principal del filtro que se ejecuta en cada request
     * 
     * @param req Request HTTP
     * @param res Response HTTP
     * @param chain FilterChain para continuar la cadena de filtros
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
        throws ServletException, IOException {

        String token = null;
        String auth = req.getHeader("Authorization");
        
        // Intentar obtener token del header Authorization
        if (auth != null && auth.startsWith("Bearer ")) {
            token = auth.substring(7);
        }
        // Si no hay token en header, intentar obtenerlo de cookies
        else if (req.getCookies() != null) {
            for (var cookie : req.getCookies()) {
                if ("jwt_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        
        // Si encontramos token, autenticar
        if (token != null) {
            try {
                String user = jwtUtil.extractUsername(token);
                if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails ud = userDetailsService.loadUserByUsername(user);
                    UsernamePasswordAuthenticationToken at = new UsernamePasswordAuthenticationToken(
                        ud, null, ud.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(at);
                }
            } catch (Exception e) {
                // Token inválido o expirado, continuar sin autenticación
                System.out.println("⚠️ [JWT] Token inválido o expirado: " + e.getMessage());
            }
        }
        
        chain.doFilter(req, res);
    }
}
