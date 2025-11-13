package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.model.Usuario;
import com.example.SistemaDePromociones.repository.UsuarioRepository;
import com.example.SistemaDePromociones.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller para gestionar el menú y funcionalidades del usuario (cliente)
 */
@Controller
@RequestMapping("/menuUsuario")
public class UsuarioController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    /**
     * Mostrar menú principal del usuario
     * GET /menuUsuario
     */
    @GetMapping
    public String mostrarMenuUsuario(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        try {
            System.out.println("👤 [MENU USUARIO] Cargando menú para: " + userDetails.getUsername());
            
            // Obtener datos completos del usuario
            Usuario usuario = usuarioRepository.findByCorreoElectronico(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Crear objeto currentUser con los datos que espera la vista
            java.util.Map<String, Object> currentUser = new java.util.HashMap<>();
            currentUser.put("name", usuario.getNombre());
            currentUser.put("lastName", usuario.getApellidoPaterno());
            currentUser.put("email", usuario.getCorreoElectronico());
            currentUser.put("phone", usuario.getTelefono());
            currentUser.put("address", usuario.getDireccion());
            
            // Agregar datos al modelo
            model.addAttribute("usuario", usuario);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("nombreCompleto", 
                usuario.getNombre() + " " + usuario.getApellidoPaterno());
            
            // Datos simulados para las vistas (por ahora vacíos)
            model.addAttribute("cartItemCount", 0);
            model.addAttribute("categories", new java.util.HashMap<>());
            model.addAttribute("products", new java.util.ArrayList<>());
            model.addAttribute("cartItems", new java.util.ArrayList<>());
            model.addAttribute("orders", new java.util.ArrayList<>());
            
            System.out.println("✅ [MENU USUARIO] Usuario cargado: " + usuario.getNombre());
            
            return "menuUsuario";
            
        } catch (Exception e) {
            System.err.println("❌ [MENU USUARIO] Error al cargar menú: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar el menú");
            return "error";
        }
    }
}
