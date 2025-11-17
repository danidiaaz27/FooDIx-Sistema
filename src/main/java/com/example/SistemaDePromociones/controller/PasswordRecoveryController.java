package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.service.EmailService;
import com.example.SistemaDePromociones.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Controlador para recuperación de contraseña
 */
@Controller
public class PasswordRecoveryController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;
    
    /**
     * Mostrar página de recuperación de contraseña
     */
    @GetMapping("/recuperar-password")
    public String mostrarRecuperarPassword() {
        System.out.println("🔑 [RECOVERY] Mostrando página de recuperación de contraseña");
        return "recuperar-password";
    }
    
    /**
     * Mostrar página de verificación de código
     */
    @GetMapping("/verificar-codigo")
    public String mostrarVerificarCodigo() {
        System.out.println("🔑 [RECOVERY] Mostrando página de verificación de código");
        return "verificar-codigo";
    }
    
    /**
     * Mostrar página para cambiar contraseña
     */
    @GetMapping("/cambiar-password")
    public String mostrarCambiarPassword() {
        System.out.println("🔐 [RECOVERY] Mostrando página para cambiar contraseña");
        return "cambiar-password";
    }
    
    /**
     * Verificar si el correo existe y enviar código de verificación
     */
    @PostMapping("/auth/recovery/verify-email")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verificarEmailExiste(
            @RequestParam String email,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("📧 [RECOVERY] Verificando si existe email: " + email);
            
            boolean existe = usuarioService.existeEmail(email);
            
            if (existe) {
                System.out.println("✅ [RECOVERY] Email encontrado: " + email);
                
                // Generar código de 6 dígitos
                String codigo = generarCodigoVerificacion();
                
                // Guardar en sesión
                session.setAttribute("verificationCode", codigo);
                session.setAttribute("verificationEmail", email);
                session.setAttribute("verificationExpiry", LocalDateTime.now().plusMinutes(10));
                
                System.out.println("🔑 [RECOVERY] Código generado: " + codigo + " para " + email);
                
                // Enviar código por correo
                emailService.enviarCodigoVerificacion(email, codigo);
                
                response.put("success", true);
                response.put("message", "Código enviado a tu correo electrónico");
            } else {
                System.out.println("❌ [RECOVERY] Email no encontrado: " + email);
                response.put("success", false);
                response.put("message", "El correo ingresado no está registrado en el sistema");
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("❌ [RECOVERY] Error al verificar email: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al procesar la solicitud");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * Enviar código de verificación (para reenvío)
     */
    @PostMapping("/auth/recovery/send-code")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> enviarCodigoVerificacion(
            @RequestParam String email,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("📧 [RECOVERY] Reenviando código a: " + email);
            
            // Validar que el email existe
            if (!usuarioService.existeEmail(email)) {
                response.put("success", false);
                response.put("message", "El correo no está registrado");
                return ResponseEntity.ok(response);
            }
            
            // Generar nuevo código
            String codigo = generarCodigoVerificacion();
            
            // Actualizar sesión
            session.setAttribute("verificationCode", codigo);
            session.setAttribute("verificationEmail", email);
            session.setAttribute("verificationExpiry", LocalDateTime.now().plusMinutes(10));
            
            System.out.println("🔑 [RECOVERY] Nuevo código generado: " + codigo);
            
            // Enviar código por correo
            emailService.enviarCodigoVerificacion(email, codigo);
            
            response.put("success", true);
            response.put("message", "Código reenviado correctamente");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("❌ [RECOVERY] Error al reenviar código: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al reenviar el código");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * Verificar código ingresado por el usuario
     */
    @PostMapping("/auth/recovery/verify-code")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verificarCodigo(
            @RequestParam String email,
            @RequestParam String code,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("🔍 [RECOVERY] Verificando código para: " + email);
            
            String storedCode = (String) session.getAttribute("verificationCode");
            String storedEmail = (String) session.getAttribute("verificationEmail");
            LocalDateTime expiry = (LocalDateTime) session.getAttribute("verificationExpiry");
            
            // Validaciones
            if (storedCode == null || storedEmail == null || expiry == null) {
                System.out.println("❌ [RECOVERY] No hay código en sesión");
                response.put("success", false);
                response.put("message", "Sesión expirada. Solicita un nuevo código");
                return ResponseEntity.ok(response);
            }
            
            if (!storedEmail.equals(email)) {
                System.out.println("❌ [RECOVERY] Email no coincide");
                response.put("success", false);
                response.put("message", "Email no válido");
                return ResponseEntity.ok(response);
            }
            
            if (LocalDateTime.now().isAfter(expiry)) {
                System.out.println("❌ [RECOVERY] Código expirado");
                response.put("success", false);
                response.put("message", "Código expirado. Solicita uno nuevo");
                return ResponseEntity.ok(response);
            }
            
            if (!storedCode.equals(code)) {
                System.out.println("❌ [RECOVERY] Código incorrecto");
                response.put("success", false);
                response.put("message", "Código incorrecto");
                return ResponseEntity.ok(response);
            }
            
            // Código válido
            System.out.println("✅ [RECOVERY] Código verificado correctamente");
            session.setAttribute("codeVerified", true);
            
            response.put("success", true);
            response.put("message", "Código verificado correctamente");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("❌ [RECOVERY] Error al verificar código: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al verificar el código");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * Actualizar contraseña del usuario
     */
    @PostMapping("/auth/recovery/update-password")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> actualizarPassword(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("🔐 [RECOVERY] Actualizando contraseña para: " + email);
            
            // Validar que el código fue verificado
            Boolean codeVerified = (Boolean) session.getAttribute("codeVerified");
            if (codeVerified == null || !codeVerified) {
                System.out.println("❌ [RECOVERY] Código no verificado");
                response.put("success", false);
                response.put("message", "Debe verificar el código primero");
                return ResponseEntity.ok(response);
            }
            
            // Validar que el email coincide
            String sessionEmail = (String) session.getAttribute("verificationEmail");
            if (!email.equals(sessionEmail)) {
                System.out.println("❌ [RECOVERY] Email no coincide con la sesión");
                response.put("success", false);
                response.put("message", "Email no válido");
                return ResponseEntity.ok(response);
            }
            
            // Validar que el email existe
            if (!usuarioService.existeEmail(email)) {
                System.out.println("❌ [RECOVERY] Email no encontrado: " + email);
                response.put("success", false);
                response.put("message", "El correo no está registrado en el sistema");
                return ResponseEntity.ok(response);
            }
            
            // Encriptar la nueva contraseña
            String passwordEncriptada = passwordEncoder.encode(password);
            
            // Actualizar contraseña
            usuarioService.actualizarPassword(email, passwordEncriptada);
            
            // Limpiar datos de sesión
            session.removeAttribute("verificationCode");
            session.removeAttribute("verificationEmail");
            session.removeAttribute("verificationExpiry");
            session.removeAttribute("codeVerified");
            
            System.out.println("✅ [RECOVERY] Contraseña actualizada correctamente para: " + email);
            response.put("success", true);
            response.put("message", "Contraseña actualizada correctamente");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("❌ [RECOVERY] Error al actualizar contraseña: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al actualizar la contraseña");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * Generar código de verificación de 6 dígitos
     */
    private String generarCodigoVerificacion() {
        Random random = new Random();
        int codigo = 100000 + random.nextInt(900000);
        return String.valueOf(codigo);
    }
}
