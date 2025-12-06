package com.example.SistemaDePromociones.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Utilidad para generar y validar tokens JWT
 * Basado en la librería JJWT (io.jsonwebtoken)
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationMs}")
    private int expMs;

    /**
     * Genera un token JWT para un usuario autenticado
     * El token incluye el username (email) y los roles del usuario
     * 
     * @param user UserDetails del usuario autenticado
     * @return Token JWT como String
     */
    public String generateToken(UserDetails user) {
        String roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("rol", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expMs))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrae el username (email) del token JWT
     * 
     * @param token Token JWT
     * @return Username (email) del usuario
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }
    
    /**
     * Extrae el rol del usuario del token JWT
     * 
     * @param token Token JWT
     * @return Roles del usuario (ej: "ROLE_ADMIN")
     */
    public String extractRole(String token) {
        return extractClaims(token).get("rol", String.class);
    }
    
    /**
     * Extrae todos los claims del token JWT
     * 
     * @param token Token JWT
     * @return Claims del token
     */
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
