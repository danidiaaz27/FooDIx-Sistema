package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.service.EmailService;
import com.example.SistemaDePromociones.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class VerificationController {

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/verificacion")
    public String showVerificationPage(@RequestParam(required = false, defaultValue = "usuario") String tipo, Model model) {
        model.addAttribute("tipoRegistro", tipo);
        return "verificacion";
    }

    @PostMapping("/auth/send-code")
    @ResponseBody
    public String sendVerificationCode(@RequestParam String email, 
                                      @RequestParam(required = false, defaultValue = "usuario") String tipo,
                                      HttpSession session) {
        try {
            if (email == null || email.trim().isEmpty()) {
                return "{\"success\": false, \"error\": \"El correo electrónico es requerido\"}";
            }
            String code = verificationService.generateCode(email);
            emailService.sendVerificationCode(email, code);
            session.setAttribute("verificationEmail", email);
            session.setAttribute("tipoRegistro", tipo); // Guardar el tipo
            return "{\"success\": true, \"message\": \"Código enviado exitosamente\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\": false, \"error\": \"Error al enviar el código: " + e.getMessage() + "\"}";
        }
    }

    @PostMapping("/auth/verify-code")
    @ResponseBody
    public String verifyCode(@RequestParam String code, HttpSession session) {
        String email = (String) session.getAttribute("verificationEmail");
        String tipo = (String) session.getAttribute("tipoRegistro");
        
        if (email == null) {
            return "{\"success\": false, \"error\": \"Sesión expirada\"}";
        }
        
        if (tipo == null) {
            tipo = "usuario"; // Por defecto
        }

        boolean isValid = verificationService.verifyCode(email, code);
        if (isValid) {
            session.setAttribute("verifiedEmail", email);
            
            // Redirigir según el tipo de registro
            String redirectUrl = switch (tipo) {
                case "negocio", "restaurante", "repartidor" -> "/registroNegocio";
                default -> "/registroUsuario";
            };
            
            return "{\"success\": true, \"redirectUrl\": \"" + redirectUrl + "\"}";
        } else {
            return "{\"success\": false, \"error\": \"Código inválido\"}";
        }
    }
}