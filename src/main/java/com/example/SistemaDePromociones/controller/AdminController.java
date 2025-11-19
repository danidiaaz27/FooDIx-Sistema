package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.model.Categoria;
import com.example.SistemaDePromociones.model.Restaurante;
import com.example.SistemaDePromociones.model.Usuario;
import com.example.SistemaDePromociones.model.Repartidor;
import com.example.SistemaDePromociones.model.Rol;
import com.example.SistemaDePromociones.model.Permiso;
import com.example.SistemaDePromociones.repository.CategoriaRepository;
import com.example.SistemaDePromociones.repository.RestauranteRepository;
import com.example.SistemaDePromociones.repository.UsuarioRepository;
import com.example.SistemaDePromociones.repository.RepartidorRepository;
import com.example.SistemaDePromociones.repository.RolRepository;
import com.example.SistemaDePromociones.repository.PermisoRepository;
import com.example.SistemaDePromociones.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RepartidorRepository repartidorRepository;
    
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private PermisoRepository permisoRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;
    
    /**
     * Mostrar el menú de administrador
     */
    @GetMapping
    public String mostrarMenuAdmin(Model model) {
        System.out.println("🔧 [ADMIN] Cargando menú de administrador");
        
        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuario = authentication.getName();
        System.out.println("👤 [ADMIN] Usuario autenticado: " + emailUsuario);
        
        // Obtener permisos del usuario
        Set<String> permisosUsuario = usuarioRepository.findByCorreoElectronico(emailUsuario)
                .map(usuario -> {
                    if (usuario.getRol() != null && usuario.getRol().getPermisos() != null) {
                        System.out.println("🎭 [ADMIN] Rol del usuario: " + usuario.getRol().getNombre());
                        Set<String> secciones = usuario.getRol().getPermisos().stream()
                                .map(Permiso::getSeccion)
                                .collect(Collectors.toSet());
                        System.out.println("🛡️ [ADMIN] Permisos (secciones): " + secciones);
                        return secciones;
                    }
                    return Set.<String>of();
                })
                .orElse(Set.of());
        
        // Pasar permisos al template
        model.addAttribute("permisosUsuario", permisosUsuario);
        
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
        
        // Cargar solo clientes (rol 4 = USUARIO/CLIENTE)
        List<Usuario> clientes = usuarioRepository.findByCodigoRol(4L);
        model.addAttribute("clientes", clientes);
        System.out.println("👥 [ADMIN] Clientes cargados: " + clientes.size());
        
        // Cargar repartidores
        List<Repartidor> repartidores = repartidorRepository.findAll();
        model.addAttribute("repartidores", repartidores);
        System.out.println("🚚 [ADMIN] Repartidores cargados: " + repartidores.size());
        
        // Cargar todos los roles
        List<Rol> roles = rolRepository.findAll();
        model.addAttribute("roles", roles);
        System.out.println("🎭 [ADMIN] Roles cargados: " + roles.size());
        
        // Cargar todos los permisos
        List<Permiso> permisos = permisoRepository.findByEstadoTrue();
        model.addAttribute("permisos", permisos);
        System.out.println("🛡️ [ADMIN] Permisos cargados: " + permisos.size());
        
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
            
            // Enviar correo de notificación al restaurante
            try {
                emailService.sendRestaurantRejectionNotification(
                    restaurante.getCorreoElectronico(),
                    restaurante.getNombre(),
                    motivo
                );
                System.out.println("✅ [ADMIN] Correo de rechazo enviado a: " + restaurante.getCorreoElectronico());
            } catch (Exception emailError) {
                System.err.println("⚠️ [ADMIN] No se pudo enviar el correo: " + emailError.getMessage());
                // Continuar aunque falle el envío de correo
            }
            
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
    
    // ============================================
    // CRUD DE USUARIOS DEL SISTEMA
    // ============================================
    
    /**
     * Crear nuevo usuario del sistema (administradores, supervisores, etc.)
     */
    @PostMapping("/create-admin")
    public String crearUsuarioSistema(
            @RequestParam("name") String nombre,
            @RequestParam("lastName") String apellido,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "phone", required = false) String telefono,
            @RequestParam("codigoRol") Long codigoRol,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("👤 [ADMIN] Creando usuario del sistema: " + email + " con rol: " + codigoRol);
            
            // Verificar que el email no exista
            if (usuarioRepository.findByCorreoElectronico(email).isPresent()) {
                throw new RuntimeException("El email ya está registrado");
            }
            
            // Verificar que el rol exista
            Rol rol = rolRepository.findById(codigoRol)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            
            System.out.println("   ✓ Rol asignado: " + rol.getNombre());
            
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setApellidoPaterno(apellido);
            usuario.setApellidoMaterno(apellido);  // Por defecto igual al paterno
            usuario.setCorreoElectronico(email);
            usuario.setContrasena(passwordEncoder.encode(password));
            usuario.setTelefono(telefono);
            usuario.setRol(rol);
            usuario.setEstado(true);
            usuario.setNumeroDocumento("00000000"); // Valor por defecto
            usuario.setFechaNacimiento(LocalDate.now().minusYears(18)); // Fecha por defecto
            usuario.setCodigoTipoDocumento(1L); // Tipo documento por defecto
            usuario.setCodigoDistrito(1L); // Distrito por defecto
            
            usuarioRepository.save(usuario);
            
            redirectAttributes.addFlashAttribute("mensaje", "Usuario creado exitosamente con rol: " + rol.getNombre());
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [ADMIN] Usuario creado: " + usuario.getCodigo());
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al crear usuario: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al crear usuario: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
    
    // ============================================
    // CRUD DE ROLES
    // ============================================
    
    /**
     * Crear nuevo rol
     */
    @PostMapping("/role/create")
    public String crearRol(
            @RequestParam("nombre") String nombre,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam(value = "permisos", required = false) List<Long> permisosIds,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🎭 [ADMIN] Creando rol: " + nombre);
            System.out.println("🛡️ [ADMIN] Permisos seleccionados: " + (permisosIds != null ? permisosIds.size() : 0));
            
            Rol rol = new Rol();
            rol.setNombre(nombre);
            rol.setDescripcion(descripcion);
            rol.setEstado(true);
            
            // Asignar permisos si se seleccionaron
            if (permisosIds != null && !permisosIds.isEmpty()) {
                rol.getPermisos().clear();
                for (Long permisoId : permisosIds) {
                    permisoRepository.findById(permisoId).ifPresent(permiso -> {
                        rol.getPermisos().add(permiso);
                        System.out.println("   ✓ Permiso asignado: " + permiso.getNombre());
                    });
                }
            }
            
            rolRepository.save(rol);
            
            redirectAttributes.addFlashAttribute("mensaje", "Rol creado exitosamente con " + rol.getPermisos().size() + " permisos");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [ADMIN] Rol creado: " + rol.getCodigo());
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al crear rol: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al crear el rol: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
    
    /**
     * Actualizar rol existente
     */
    @PostMapping("/role/{id}/update")
    public String actualizarRol(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam(value = "permisos", required = false) List<Long> permisosIds,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🎭 [ADMIN] Actualizando rol: " + id);
            System.out.println("🛡️ [ADMIN] Permisos seleccionados: " + (permisosIds != null ? permisosIds.size() : 0));
            
            Rol rol = rolRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            
            rol.setNombre(nombre);
            rol.setDescripcion(descripcion);
            
            // Actualizar permisos
            rol.getPermisos().clear();
            if (permisosIds != null && !permisosIds.isEmpty()) {
                for (Long permisoId : permisosIds) {
                    permisoRepository.findById(permisoId).ifPresent(permiso -> {
                        rol.getPermisos().add(permiso);
                        System.out.println("   ✓ Permiso asignado: " + permiso.getNombre());
                    });
                }
            }
            
            rolRepository.save(rol);
            
            redirectAttributes.addFlashAttribute("mensaje", "Rol actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [ADMIN] Rol actualizado: " + id);
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al actualizar rol: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
    
    /**
     * Cambiar estado del rol (activar/desactivar)
     */
    @PostMapping("/role/{id}/toggle-status")
    public String cambiarEstadoRol(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🎭 [ADMIN] Cambiando estado del rol: " + id);
            
            Rol rol = rolRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            
            rol.setEstado(!rol.getEstado());
            rolRepository.save(rol);
            
            redirectAttributes.addFlashAttribute("mensaje", 
                "Rol " + (rol.getEstado() ? "activado" : "desactivado") + " exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [ADMIN] Estado del rol cambiado: " + id);
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al cambiar estado: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
    
    /**
     * Obtener permisos de un rol (para AJAX)
     */
    @GetMapping("/role/{id}/permisos")
    @ResponseBody
    public List<Long> obtenerPermisosRol(@PathVariable Long id) {
        System.out.println("🔍 [ADMIN] Obteniendo permisos del rol: " + id);
        
        return rolRepository.findById(id)
                .map(rol -> rol.getPermisos().stream()
                        .map(Permiso::getCodigo)
                        .toList())
                .orElse(List.of());
    }
    
    /**
     * Eliminar rol
     */
    @PostMapping("/role/{id}/delete")
    public String eliminarRol(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🗑️ [ADMIN] Eliminando rol: " + id);
            
            // Verificar que no sea un rol del sistema (1-4)
            if (id <= 4) {
                throw new RuntimeException("No se pueden eliminar los roles predefinidos del sistema");
            }
            
            rolRepository.deleteById(id);
            
            redirectAttributes.addFlashAttribute("mensaje", "Rol eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [ADMIN] Rol eliminado: " + id);
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al eliminar rol: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
}
