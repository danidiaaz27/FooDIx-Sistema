package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.model.Restaurante;
import com.example.SistemaDePromociones.repository.RestauranteRepository;
import com.example.SistemaDePromociones.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller para gestionar el menú y funcionalidades del restaurante
 */
@Controller
@RequestMapping("/menuRestaurante")
public class MenuRestauranteController {
    
    @Autowired
    private RestauranteRepository restauranteRepository;
    
    /**
     * Mostrar menú principal del restaurante
     * GET /menuRestaurante
     */
    @GetMapping
    public String mostrarMenuRestaurante(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        try {
            System.out.println("🍽️ [MENU RESTAURANTE] Cargando menú para: " + userDetails.getUsername());
            
            // Obtener datos del restaurante por RUC del usuario (necesitamos mejorar esto)
            // Por ahora, buscar por correo requiere agregar el método al repository
            Restaurante restaurante = restauranteRepository.findByRuc("20123456789")
                    .orElse(restauranteRepository.findAll().stream()
                            .filter(r -> r.getCorreoElectronico() != null && 
                                       r.getCorreoElectronico().equals(userDetails.getUsername()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Restaurante no encontrado")));
            
            // Verificar estado de aprobación
            if (restaurante.getCodigoEstadoAprobacion() == 7L) {
                model.addAttribute("mensaje", "Tu solicitud está en revisión. Espera la aprobación del administrador.");
                model.addAttribute("estado", "pendiente");
            } else if (restaurante.getCodigoEstadoAprobacion() == 9L) {
                model.addAttribute("mensaje", "Tu solicitud fue rechazada. Contacta al administrador.");
                model.addAttribute("estado", "rechazado");
            } else if (restaurante.getCodigoEstadoAprobacion() == 8L && !restaurante.getEstado()) {
                model.addAttribute("mensaje", "Tu cuenta está desactivada. Contacta al administrador.");
                model.addAttribute("estado", "desactivado");
            }
            
            // Agregar datos al modelo
            model.addAttribute("restaurante", restaurante);
            model.addAttribute("nombreComercial", restaurante.getNombre());
            
            System.out.println("✅ [MENU RESTAURANTE] Restaurante cargado: " + restaurante.getNombre());
            
            return "menuRestaurante";
            
        } catch (Exception e) {
            System.err.println("❌ [MENU RESTAURANTE] Error al cargar menú: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar el menú");
            return "error";
        }
    }
}
