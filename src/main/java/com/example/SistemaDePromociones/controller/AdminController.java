package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.model.Categoria;
import com.example.SistemaDePromociones.model.Restaurante;
import com.example.SistemaDePromociones.repository.CategoriaRepository;
import com.example.SistemaDePromociones.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador para el menú de administrador
 */
@Controller
@RequestMapping("/menuAdministrador")
public class AdminController {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private RestauranteRepository restauranteRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Mostrar el menú de administrador
     */
    @GetMapping
    public String mostrarMenuAdmin(Model model) {
        System.out.println("🔧 [ADMIN] Cargando menú de administrador");
        
        // Cargar todas las categorías para el modal
        List<Categoria> categorias = categoriaRepository.findAll();
        model.addAttribute("categories", categorias);
        
        System.out.println("🔧 [ADMIN] Categorías cargadas: " + categorias.size());
        
        // Cargar restaurantes pendientes de aprobación (Estado 7)
        List<Restaurante> pendingRestaurants = restauranteRepository.findByCodigoEstadoAprobacion(7L);
        model.addAttribute("pendingRestaurants", pendingRestaurants);
        System.out.println("🏪 [ADMIN] Restaurantes pendientes: " + pendingRestaurants.size());
        
        // Cargar restaurantes aprobados (Estado 8)
        List<Restaurante> approvedRestaurants = restauranteRepository.findByCodigoEstadoAprobacion(8L);
        model.addAttribute("approvedRestaurants", approvedRestaurants);
        System.out.println("✅ [ADMIN] Restaurantes aprobados: " + approvedRestaurants.size());
        
        // TODO: Cargar usuarios, delivery, etc.
        // model.addAttribute("users", usuarioRepository.findByRol("CUSTOMER"));
        // model.addAttribute("restaurantUsers", usuarioRepository.findByRol("RESTAURANT"));
        // model.addAttribute("deliveryUsers", usuarioRepository.findByRol("DELIVERY"));
        // model.addAttribute("adminUsers", usuarioRepository.findByRol("ADMIN"));
        
        return "menuAdministrador";
    }
    
    /**
     * Crear nueva categoría
     * POST /menuAdministrador/create-category
     */
    @PostMapping("/create-category")
    public String crearCategoria(
            @RequestParam("nombre") String nombre,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam(value = "icono", required = false, defaultValue = "fa-utensils") String icono,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🏷️ [ADMIN] Creando categoría: " + nombre);
            
            Categoria categoria = new Categoria();
            categoria.setNombre(nombre);
            categoria.setDescripcion(descripcion);
            categoria.setIcono(icono);
            categoria.setEstado(true);
            
            categoriaRepository.save(categoria);
            
            redirectAttributes.addFlashAttribute("mensaje", "Categoría creada exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [ADMIN] Categoría creada: " + categoria.getCodigo());
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al crear categoría: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al crear la categoría: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
    
    /**
     * Cambiar estado de una categoría (activar/desactivar)
     * POST /menuAdministrador/category/{id}/toggle-status
     */
    @PostMapping("/category/{id}/toggle-status")
    public String cambiarEstadoCategoria(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🔄 [ADMIN] Cambiando estado de categoría: " + id);
            
            Categoria categoria = categoriaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            
            categoria.setEstado(!categoria.getEstado());
            categoriaRepository.save(categoria);
            
            String nuevoEstado = categoria.getEstado() ? "activada" : "desactivada";
            redirectAttributes.addFlashAttribute("mensaje", "Categoría " + nuevoEstado + " exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [ADMIN] Categoría " + nuevoEstado + ": " + id);
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al cambiar estado: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
    
    /**
     * Eliminar una categoría
     * POST /menuAdministrador/category/{id}/delete
     */
    @PostMapping("/category/{id}/delete")
    public String eliminarCategoria(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🗑️ [ADMIN] Eliminando categoría: " + id);
            
            categoriaRepository.deleteById(id);
            
            redirectAttributes.addFlashAttribute("mensaje", "Categoría eliminada exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [ADMIN] Categoría eliminada: " + id);
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al eliminar categoría: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
    
    /**
     * ENDPOINT TEMPORAL PARA DEBUG - Verificar password BCrypt
     * GET /menuAdministrador/test-password
     */
    @GetMapping("/test-password")
    @ResponseBody
    public String testPassword(@RequestParam(defaultValue = "525224Da!") String password) {
        String existingHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
        
        boolean matches = passwordEncoder.matches(password, existingHash);
        String newHash = passwordEncoder.encode(password);
        
        return """
                <h2>Test de Password BCrypt</h2>
                <p><strong>Password a verificar:</strong> %s</p>
                <p><strong>Hash existente en BD:</strong><br/>%s</p>
                <p><strong>¿Coincide?:</strong> %s</p>
                <hr/>
                <p><strong>Nuevo hash generado:</strong><br/>%s</p>
                <hr/>
                <p>Si coincide = true, el login debería funcionar</p>
                <p>Si coincide = false, necesitas actualizar la BD con el nuevo hash</p>
                """.formatted(password, existingHash, matches ? "✅ SÍ" : "❌ NO", newHash);
    }
    
    /**
     * ENDPOINT DEBUG - Ver restaurantes
     */
    @GetMapping("/debug-restaurantes")
    @ResponseBody
    public String debugRestaurantes() {
        List<Restaurante> todos = restauranteRepository.findAll();
        List<Restaurante> pendientes = restauranteRepository.findByCodigoEstadoAprobacion(7L);
        List<Restaurante> aprobados = restauranteRepository.findByCodigoEstadoAprobacion(8L);
        
        StringBuilder html = new StringBuilder();
        html.append("<h1>Debug Restaurantes</h1>");
        html.append("<h2>Total: ").append(todos.size()).append("</h2>");
        html.append("<h2>Pendientes (7): ").append(pendientes.size()).append("</h2>");
        html.append("<h2>Aprobados (8): ").append(aprobados.size()).append("</h2>");
        
        html.append("<h3>Todos los restaurantes:</h3><ul>");
        for (Restaurante r : todos) {
            html.append("<li>ID: ").append(r.getCodigo())
                .append(" | Nombre: ").append(r.getNombre())
                .append(" | RUC: ").append(r.getRuc())
                .append(" | Estado Aprobación: ").append(r.getCodigoEstadoAprobacion())
                .append(" | Email: ").append(r.getCorreoElectronico())
                .append("</li>");
        }
        html.append("</ul>");
        
        return html.toString();
    }
    
    /**
     * Aprobar un restaurante
     * POST /menuAdministrador/restaurant/{id}/approve
     */
    @PostMapping("/restaurant/{id}/approve")
    public String aprobarRestaurante(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("✅ [ADMIN] Aprobando restaurante: " + id);
            
            Restaurante restaurante = restauranteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));
            
            // Verificar que esté pendiente
            if (!restaurante.getCodigoEstadoAprobacion().equals(7L)) {
                throw new RuntimeException("El restaurante no está en estado pendiente");
            }
            
            // Obtener el admin actual (para codigoAprobador)
            // TODO: Obtener del usuario autenticado
            Long adminId = 1L;
            
            // Cambiar estado a aprobado (8)
            restaurante.setCodigoEstadoAprobacion(8L);
            restaurante.setFechaAprobacion(LocalDateTime.now());
            restaurante.setCodigoAprobador(adminId);
            restaurante.setMotivoRechazo(null); // Limpiar cualquier rechazo previo
            
            restauranteRepository.save(restaurante);
            
            redirectAttributes.addFlashAttribute("mensaje", 
                "Restaurante '" + restaurante.getNombre() + "' aprobado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [ADMIN] Restaurante aprobado: " + restaurante.getNombre());
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al aprobar restaurante: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al aprobar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
    
    /**
     * Rechazar un restaurante
     * POST /menuAdministrador/restaurant/{id}/reject
     */
    @PostMapping("/restaurant/{id}/reject")
    public String rechazarRestaurante(
            @PathVariable Long id,
            @RequestParam(required = false) String motivo,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("❌ [ADMIN] Rechazando restaurante: " + id);
            
            Restaurante restaurante = restauranteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));
            
            // Verificar que esté pendiente
            if (!restaurante.getCodigoEstadoAprobacion().equals(7L)) {
                throw new RuntimeException("El restaurante no está en estado pendiente");
            }
            
            // Validar motivo
            if (motivo == null || motivo.trim().isEmpty()) {
                throw new RuntimeException("Debe proporcionar un motivo de rechazo");
            }
            
            // Obtener el admin actual
            // TODO: Obtener del usuario autenticado
            Long adminId = 1L;
            
            // Cambiar estado a rechazado (9)
            restaurante.setCodigoEstadoAprobacion(9L);
            restaurante.setFechaAprobacion(LocalDateTime.now());
            restaurante.setCodigoAprobador(adminId);
            restaurante.setMotivoRechazo(motivo);
            
            restauranteRepository.save(restaurante);
            
            redirectAttributes.addFlashAttribute("mensaje", 
                "Restaurante '" + restaurante.getNombre() + "' rechazado");
            redirectAttributes.addFlashAttribute("tipo", "warning");
            
            System.out.println("❌ [ADMIN] Restaurante rechazado: " + restaurante.getNombre());
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al rechazar restaurante: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al rechazar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
    
    /**
     * Cambiar estado de un restaurante (activar/desactivar)
     * POST /menuAdministrador/restaurant/{id}/toggle-status
     */
    @PostMapping("/restaurant/{id}/toggle-status")
    public String cambiarEstadoRestaurante(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🔄 [ADMIN] Cambiando estado de restaurante: " + id);
            
            Restaurante restaurante = restauranteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));
            
            restaurante.setEstado(!restaurante.getEstado());
            restauranteRepository.save(restaurante);
            
            String nuevoEstado = restaurante.getEstado() ? "activado" : "desactivado";
            redirectAttributes.addFlashAttribute("mensaje", "Restaurante " + nuevoEstado + " exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [ADMIN] Restaurante " + nuevoEstado + ": " + id);
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al cambiar estado: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
    
    /**
     * Eliminar un restaurante
     * POST /menuAdministrador/restaurant/{id}/delete
     */
    @PostMapping("/restaurant/{id}/delete")
    public String eliminarRestaurante(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🗑️ [ADMIN] Eliminando restaurante: " + id);
            
            restauranteRepository.deleteById(id);
            
            redirectAttributes.addFlashAttribute("mensaje", "Restaurante eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [ADMIN] Restaurante eliminado: " + id);
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al eliminar restaurante: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
}
