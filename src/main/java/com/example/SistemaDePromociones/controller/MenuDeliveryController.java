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
        private com.example.SistemaDePromociones.service.EstadisticasRepartidorService estadisticasRepartidorService;
    
    @Autowired
    private RepartidorRepository repartidorRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

        @Autowired
        private com.example.SistemaDePromociones.repository.PedidoRepository pedidoRepository;
    
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
            
            // Verificar estado de aprobación - BLOQUEAR ACCESO si no está aprobado
            if (repartidor.getCodigoEstadoAprobacion() == 7L) {
                // Pendiente de aprobación
                System.out.println("⚠️ [MENU DELIVERY] Repartidor pendiente de aprobación");
                model.addAttribute("mensaje", "Tu solicitud está en revisión. Espera la aprobación del administrador.");
                model.addAttribute("estado", "pendiente");
                model.addAttribute("usuario", usuario);
                return "estadoAprobacionDelivery"; // Vista especial para pendientes
            } else if (repartidor.getCodigoEstadoAprobacion() == 9L) {
                // Rechazado
                System.out.println("❌ [MENU DELIVERY] Repartidor rechazado");
                model.addAttribute("mensaje", "Tu solicitud fue rechazada. Motivo: " + (repartidor.getMotivoRechazo() != null ? repartidor.getMotivoRechazo() : "No especificado"));
                model.addAttribute("estado", "rechazado");
                model.addAttribute("usuario", usuario);
                return "estadoAprobacionDelivery"; // Vista especial para rechazados
            } else if (repartidor.getCodigoEstadoAprobacion() == 8L && !repartidor.getEstado()) {
                // Desactivado
                System.out.println("⚠️ [MENU DELIVERY] Repartidor desactivado");
                model.addAttribute("mensaje", "Tu cuenta está desactivada. Contacta al administrador.");
                model.addAttribute("estado", "desactivado");
                model.addAttribute("usuario", usuario);
                return "estadoAprobacionDelivery"; // Vista especial para desactivados
            }
            
            // Si llegamos aquí, el repartidor está aprobado (código 8) y activo
            System.out.println("✅ [MENU DELIVERY] Repartidor aprobado y activo");
            


            // Agregar datos al modelo
            model.addAttribute("repartidor", repartidor);
            model.addAttribute("usuario", usuario);
            model.addAttribute("currentUser", usuario);
            model.addAttribute("nombreCompleto", usuario.getNombre() + " " + usuario.getApellidoPaterno());
            model.addAttribute("estadisticas", estadisticasRepartidorService.calcularEstadisticas(repartidor.getCodigo()));

            // Pedidos disponibles: estado pendiente (1) y sin repartidor asignado
            try {
                java.util.List<com.example.SistemaDePromociones.model.Pedido> pedidosDisponibles = pedidoRepository.findByCodigoEstadoPedidoAndCodigoRepartidorIsNull(1L);
                model.addAttribute("pedidosDisponibles", pedidosDisponibles);
                System.out.println("📦 [MENU DELIVERY] Pedidos disponibles: " + (pedidosDisponibles != null ? pedidosDisponibles.size() : 0));
            } catch (Exception e) {
                System.out.println("⚠️ [MENU DELIVERY] No se pudieron cargar pedidos (tabla no existe aún): " + e.getMessage());
                model.addAttribute("pedidosDisponibles", new java.util.ArrayList<>());
            }

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
