package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.model.Repartidor;
import com.example.SistemaDePromociones.model.Usuario;
import com.example.SistemaDePromociones.repository.RepartidorRepository;
import com.example.SistemaDePromociones.repository.UsuarioRepository;
import com.example.SistemaDePromociones.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller para gestionar el menú y funcionalidades del repartidor (delivery)
 */
@Controller
@RequestMapping("/menuDelivery")
public class MenuDeliveryController {
    
    @Autowired
    private RepartidorRepository repartidorRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    /**
     * Mostrar menú principal del repartidor
     * GET /menuDelivery
     */
    @GetMapping
    public String mostrarMenuDelivery(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        try {
            System.out.println("🚴 [MENU DELIVERY] Cargando menú para: " + userDetails.getUsername());
            
            // Obtener usuario primero
            Usuario usuario = usuarioRepository.findByCorreoElectronico(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Obtener datos del repartidor por código de usuario
            Repartidor repartidor = repartidorRepository.findByCodigoUsuario(usuario.getCodigo())
                    .orElseThrow(() -> new RuntimeException("Repartidor no encontrado"));
            
            // Verificar estado de aprobación
            if (repartidor.getCodigoEstadoAprobacion() == 7L) {
                model.addAttribute("mensaje", "Tu solicitud está en revisión. Espera la aprobación del administrador.");
                model.addAttribute("estado", "pendiente");
            } else if (repartidor.getCodigoEstadoAprobacion() == 9L) {
                model.addAttribute("mensaje", "Tu solicitud fue rechazada. Contacta al administrador.");
                model.addAttribute("estado", "rechazado");
            } else if (repartidor.getCodigoEstadoAprobacion() == 8L && !repartidor.getEstado()) {
                model.addAttribute("mensaje", "Tu cuenta está desactivada. Contacta al administrador.");
                model.addAttribute("estado", "desactivado");
            }
            
            // Agregar datos al modelo
            model.addAttribute("repartidor", repartidor);
            model.addAttribute("usuario", usuario);
            model.addAttribute("nombreCompleto", 
                usuario.getNombre() + " " + usuario.getApellidoPaterno());
            
            System.out.println("✅ [MENU DELIVERY] Repartidor cargado: " + usuario.getNombre());
            
            return "menuDelivery";
            
        } catch (Exception e) {
            System.err.println("❌ [MENU DELIVERY] Error al cargar menú: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar el menú");
            return "error";
        }
    }
}
