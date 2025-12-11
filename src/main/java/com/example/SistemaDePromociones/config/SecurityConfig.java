package com.example.SistemaDePromociones.config;

import com.example.SistemaDePromociones.security.RoleBasedAuthenticationSuccessHandler;
import com.example.SistemaDePromociones.util.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad de Spring Security con JWT
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private RoleBasedAuthenticationSuccessHandler successHandler;
    
    @Autowired
    private JwtFilter jwtFilter;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Configurar sesiones: permitir sesiones para compatibilidad con JWT
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )
            .authorizeHttpRequests(auth -> auth
                // ========================================
                // RUTAS PÚBLICAS - Sin autenticación
                // ========================================
                .requestMatchers(
                    "/",                        // Página principal
                    "/login",                   // Login
                    "/logout",                  // Logout (renderiza vista que limpia JWT)
                    "/dashboard",               // Dashboard (renderiza vista que guarda JWT)
                    "/registro",                // Registro de usuarios (legacy)
                    "/registroUsuario",         // Registro de usuario/cliente
                    // "/registroNegocio",      // ❌ DEPRECADO - Ya no se usa
                    "/registroRestaurante",     // Registro de restaurantes
                    "/registroDelivery",        // Registro de delivery/repartidores
                    "/registro-repartidor",     // Registro de repartidores (legacy)
                    "/registro-restaurante",    // Registro de restaurantes (legacy)
                    "/verificacion",            // Verificación de email
                    "/auth/send-code",          // Envío de código de verificación
                    "/auth/verify-code",        // Verificación de código
                    "/recuperar-password",      // Recuperar contraseña
                    "/verificar-codigo",        // Verificar código de recuperación
                    "/cambiar-password",        // Cambiar contraseña
                    "/auth/recovery/**",        // Endpoints de recuperación de contraseña
                    "/contacto",                // Página de contacto
                    "/tutorial",                // Tutorial
                    "/docs/**",                 // ✅ Documentos legales (términos, privacidad, PDFs)
                    "/api/**",                  // ✅ TODAS las APIs públicas (roles, provincias, distritos, validación, etc.)
                    "/menuAdministrador/api/ganancias/**", // ✅ TEMPORAL: API de ganancias sin autenticación para debug
                    "/menuAdministrador/test-password",  // Test de passwords
                    "/css/**",                  // Recursos estáticos CSS
                    "/js/**",                   // Recursos estáticos JS
                    "/img/**",                  // Imágenes
                    "/images/**",               // Imágenes alternativo
                    "/favicon.ico",             // ✅ Icono del sitio web
                    "/uploads/**",              // ✅ Archivos subidos (documentos, imágenes de promociones)
                    "/files/**",                // ✅ Controlador de archivos (view/download)
                    "/actuator/**"              // ✅ Endpoints de Spring Actuator para monitoreo
                ).permitAll()
                
                // ========================================
                // RUTAS PROTEGIDAS POR ROL
                // Basadas en la tabla 'rol' de la BD:
                // 1 = ADMINISTRADOR  → ROLE_ADMIN
                // 2 = RESTAURANTE    → ROLE_RESTAURANT
                // 3 = REPARTIDOR     → ROLE_DELIVERY
                // 4 = USUARIO        → ROLE_CUSTOMER
                // ========================================
                .requestMatchers("/menuAdministrador/api/ganancias/**").hasRole("ADMIN") // ✅ API de ganancias
                .requestMatchers("/menuAdministrador/**").hasRole("ADMIN")
                .requestMatchers("/menuRestaurante/**").hasRole("RESTAURANT")
                .requestMatchers("/menuDelivery/**").hasRole("DELIVERY")
                .requestMatchers("/menuUsuario/**").hasRole("CUSTOMER")
                .requestMatchers("/promociones/api/**").authenticated() // ✅ API de promociones para usuarios autenticados
                .requestMatchers("/api/carrito/**").authenticated() // ✅ API del carrito y pedidos
                
                // ========================================
                // FALLBACK - Cualquier otra ruta requiere autenticación
                // ========================================
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    "/login",                   // ✅ Login con JWT
                    "/auth/send-code",
                    "/auth/verify-code",
                    "/auth/recovery/**",
                    "/api/**",
                    "/menuAdministrador/**",    // ✅ TODAS las acciones del administrador
                    "/menuRestaurante/**",      // ✅ Acciones de restaurante
                    "/menuDelivery/**",         // ✅ Acciones de delivery
                    "/menuUsuario/**",          // ✅ Acciones de usuario
                    "/promociones/**"            // ✅ Gestión de promociones con JWT
                )
            )
            // Agregar filtro JWT antes del filtro de autenticación estándar
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    /**
     * AuthenticationManager bean para autenticación manual en el login
     * Usa el CustomUserDetailsService existente para cargar usuarios
     */
    @Bean
    public AuthenticationManager authManager(
            @Qualifier("customUserDetailsService") UserDetailsService userDetailsService, 
            PasswordEncoder passwordEncoder) {
        return authentication -> {
            UserDetails user = userDetailsService.loadUserByUsername(authentication.getName());
            if (!passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
                throw new BadCredentialsException("Credenciales inválidas");
            }
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        };
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
