package com.example.SistemaDePromociones.config;

import com.example.SistemaDePromociones.security.RoleBasedAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad de Spring Security
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private RoleBasedAuthenticationSuccessHandler successHandler;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // ========================================
                // RUTAS PÚBLICAS - Sin autenticación
                // ========================================
                .requestMatchers(
                    "/",                        // Página principal
                    "/login",                   // Login
                    "/registro",                // Registro de usuarios
                    "/registro-repartidor",     // Registro de repartidores
                    "/registro-restaurante",    // Registro de restaurantes
                    "/verificacion",            // Verificación de email
                    "/auth/send-code",          // Envío de código de verificación
                    "/auth/verify-code",        // Verificación de código
                    "/contacto",                // Página de contacto
                    "/tutorial",                // Tutorial
                    "/recuperar-password",      // Recuperación de contraseña
                    "/api/roles",               // API de roles (para formularios)
                    "/api/roles/**",            // API de roles (específicos)
                    "/api/provincias/**",       // API de provincias
                    "/api/distritos/**",        // API de distritos
                    "/api/tipos-vehiculo",      // API de tipos de vehículo
                    "/api/validation/**",       // API de validación
                    "/menuAdministrador/test-password",  // Test de passwords
                    "/css/**",                  // Recursos estáticos CSS
                    "/js/**",                   // Recursos estáticos JS
                    "/img/**",                  // Imágenes
                    "/images/**"                // Imágenes alternativo
                ).permitAll()
                
                // ========================================
                // RUTAS PROTEGIDAS POR ROL
                // Basadas en la tabla 'rol' de la BD:
                // 1 = ADMINISTRADOR  → ROLE_ADMIN
                // 2 = RESTAURANTE    → ROLE_RESTAURANT
                // 3 = REPARTIDOR     → ROLE_DELIVERY
                // 4 = USUARIO        → ROLE_CUSTOMER
                // ========================================
                .requestMatchers("/menuAdministrador/**").hasRole("ADMIN")
                .requestMatchers("/menuRestaurante/**").hasRole("RESTAURANT")
                .requestMatchers("/menuDelivery/**").hasRole("DELIVERY")
                .requestMatchers("/menuUsuario/**").hasRole("CUSTOMER")
                
                // ========================================
                // FALLBACK - Cualquier otra ruta requiere autenticación
                // ========================================
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(successHandler)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    "/auth/send-code",
                    "/auth/verify-code",
                    "/api/**"
                )
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            );
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
