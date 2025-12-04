package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.model.Repartidor;
import com.example.SistemaDePromociones.model.Usuario;
import com.example.SistemaDePromociones.model.Pedido;
import com.example.SistemaDePromociones.repository.RepartidorRepository;
import com.example.SistemaDePromociones.repository.UsuarioRepository;
import com.example.SistemaDePromociones.repository.PedidoRepository;
import com.example.SistemaDePromociones.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
            
            // Estadísticas - valores por defecto si el servicio falla
            try {
                model.addAttribute("estadisticas", estadisticasRepartidorService.calcularEstadisticas(repartidor.getCodigo()));
            } catch (Exception e) {
                System.out.println("⚠️ [MENU DELIVERY] Error al cargar estadísticas: " + e.getMessage());
                Map<String, Object> statsDefault = new java.util.HashMap<>();
                statsDefault.put("gananciasHoy", 0.0);
                statsDefault.put("entregasHoy", 0);
                statsDefault.put("gananciasSemana", 0.0);
                statsDefault.put("calificacionPromedio", 0.0);
                statsDefault.put("tasaExito", 0);
                statsDefault.put("progresoSemanal", 0);
                statsDefault.put("metaSemanal", 500.0);
                statsDefault.put("totalEntregas", 0);
                statsDefault.put("promedioEntrega", 0.0);
                statsDefault.put("tiempoPromedio", 0);
                model.addAttribute("estadisticas", statsDefault);
            }
            
            // Valores simples para el header
            model.addAttribute("gananciasHoy", 0.0);
            model.addAttribute("entregasHoy", 0);

            // Pedidos disponibles: estado pendiente y sin repartidor asignado
            try {
                java.util.List<com.example.SistemaDePromociones.model.Pedido> pedidosDisponibles = 
                    pedidoRepository.findByCodigoEstadoPedidoAndCodigoRepartidorIsNull(1L);
                model.addAttribute("pedidosDisponibles", pedidosDisponibles);
                System.out.println("📦 [MENU DELIVERY] Pedidos disponibles: " + (pedidosDisponibles != null ? pedidosDisponibles.size() : 0));
            } catch (Exception e) {
                System.out.println("⚠️ [MENU DELIVERY] Error al cargar pedidos disponibles: " + e.getMessage());
                model.addAttribute("pedidosDisponibles", new java.util.ArrayList<>());
            }
            
            // Mis entregas: pedidos asignados a este repartidor
            try {
                java.util.List<com.example.SistemaDePromociones.model.Pedido> misEntregas = 
                    pedidoRepository.findByCodigoRepartidor(repartidor.getCodigo());
                model.addAttribute("misEntregas", misEntregas);
                System.out.println("🚴 [MENU DELIVERY] Mis entregas: " + (misEntregas != null ? misEntregas.size() : 0));
            } catch (Exception e) {
                System.out.println("⚠️ [MENU DELIVERY] Error al cargar mis entregas: " + e.getMessage());
                model.addAttribute("misEntregas", new java.util.ArrayList<>());
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
    
    /**
     * Verificar código de entrega y marcar pedido como entregado
     * POST /delivery/pedidos/{id}/verificar-entrega
     */
    @PostMapping("/delivery/pedidos/{id}/verificar-entrega")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verificarEntrega(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String codigoIngresado = payload.get("codigoVerificacion");
            
            if (codigoIngresado == null || codigoIngresado.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "El código es requerido");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Buscar el pedido
            Pedido pedido = pedidoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
            
            // Verificar que el código coincida
            if (!codigoIngresado.equals(pedido.getCodigoVerificacion())) {
                response.put("success", false);
                response.put("message", "Código de verificación incorrecto");
                return ResponseEntity.ok(response);
            }
            
            // Verificar que el delivery sea el asignado al pedido
            Usuario usuario = usuarioRepository.findByCorreoElectronico(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            Repartidor repartidor = repartidorRepository.findByCodigoUsuario(usuario.getCodigo())
                    .orElseThrow(() -> new RuntimeException("Repartidor no encontrado"));
            
            if (!pedido.getCodigoRepartidor().equals(repartidor.getCodigo())) {
                response.put("success", false);
                response.put("message", "Este pedido no está asignado a ti");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Marcar como verificado y entregado
            pedido.setVerificado(true);
            pedido.setEstado(false); // Pedido completado
            pedido.setFechaEntrega(java.time.LocalDateTime.now());
            
            // Guardar cambios
            pedidoRepository.save(pedido);
            
            System.out.println("✅ [DELIVERY] Pedido #" + pedido.getCodigo() + " verificado y entregado correctamente");
            
            response.put("success", true);
            response.put("message", "Entrega verificada correctamente");
            response.put("ganancia", 0.00); // Por definir ganancia
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("❌ [DELIVERY] Error al verificar entrega: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al procesar la verificación");
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Aceptar un pedido disponible y asignarlo al delivery
     * POST /delivery/pedidos/{id}/aceptar
     */
    @PostMapping("/delivery/pedidos/{id}/aceptar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> aceptarPedido(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Buscar el pedido
            Pedido pedido = pedidoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
            
            // Verificar que el pedido esté disponible (sin repartidor asignado)
            if (pedido.getCodigoRepartidor() != null) {
                response.put("success", false);
                response.put("message", "Este pedido ya está asignado a otro delivery");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Obtener el repartidor actual
            Usuario usuario = usuarioRepository.findByCorreoElectronico(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            Repartidor repartidor = repartidorRepository.findByCodigoUsuario(usuario.getCodigo())
                    .orElseThrow(() -> new RuntimeException("Repartidor no encontrado"));
            
            // Asignar el pedido al repartidor
            pedido.setCodigoRepartidor(repartidor.getCodigo());
            pedidoRepository.save(pedido);
            
            System.out.println("✅ [DELIVERY] Pedido #" + pedido.getCodigo() + " aceptado por " + usuario.getNombre());
            
            response.put("success", true);
            response.put("message", "Pedido aceptado correctamente");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("❌ [DELIVERY] Error al aceptar pedido: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al aceptar el pedido");
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Rechazar un pedido disponible
     * POST /delivery/pedidos/{id}/rechazar
     */
    @PostMapping("/delivery/pedidos/{id}/rechazar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> rechazarPedido(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Buscar el pedido
            Pedido pedido = pedidoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
            
            // Por ahora solo retornamos mensaje de rechazo
            // En el futuro podríamos registrar esto en una tabla de rechazos
            
            System.out.println("⚠️ [DELIVERY] Pedido #" + pedido.getCodigo() + " rechazado por " + userDetails.getUsername());
            
            response.put("success", true);
            response.put("message", "Pedido rechazado");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("❌ [DELIVERY] Error al rechazar pedido: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al rechazar el pedido");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
