package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.model.Categoria;
import com.example.SistemaDePromociones.model.Restaurante;
import com.example.SistemaDePromociones.model.Usuario;
import com.example.SistemaDePromociones.model.Repartidor;
import com.example.SistemaDePromociones.model.Rol;
import com.example.SistemaDePromociones.model.Permiso;
import com.example.SistemaDePromociones.model.ConfiguracionComision;
import com.example.SistemaDePromociones.model.GananciaPlataforma;
import com.example.SistemaDePromociones.repository.CategoriaRepository;
import com.example.SistemaDePromociones.repository.RestauranteRepository;
import com.example.SistemaDePromociones.repository.UsuarioRepository;
import com.example.SistemaDePromociones.repository.RepartidorRepository;
import com.example.SistemaDePromociones.repository.RolRepository;
import com.example.SistemaDePromociones.repository.PermisoRepository;
import com.example.SistemaDePromociones.repository.ConfiguracionComisionRepository;
import com.example.SistemaDePromociones.repository.GananciaPlataformaRepository;
import com.example.SistemaDePromociones.service.EmailService;
import com.example.SistemaDePromociones.service.FileStorageService;
import com.example.SistemaDePromociones.repository.jdbc.RestauranteJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;

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
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @Autowired
    private RestauranteJdbcRepository restauranteJdbcRepository;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private ConfiguracionComisionRepository configuracionComisionRepository;
    
    @Autowired
    private GananciaPlataformaRepository gananciaPlataformaRepository;
    
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
        
        // Cargar usuarios del sistema (roles 1=ADMIN, 2=RESTAURANTE, 3=REPARTIDOR)
        List<Usuario> adminUsers = usuarioRepository.findAll().stream()
                .filter(u -> u.getCodigoRol() != null && u.getCodigoRol() <= 3L)
                .collect(Collectors.toList());
        model.addAttribute("adminUsers", adminUsers);
        System.out.println("👨‍💼 [ADMIN] Usuarios del sistema cargados: " + adminUsers.size());
        
        // Cargar repartidores pendientes de aprobación
        List<Repartidor> pendingRepartidores = repartidorRepository.findByCodigoEstadoAprobacion(7L);
        model.addAttribute("pendingRepartidores", pendingRepartidores);
        System.out.println("🚚 [ADMIN] Repartidores pendientes: " + pendingRepartidores.size());
        
        // Cargar repartidores aprobados
        List<Repartidor> approvedRepartidores = repartidorRepository.findByCodigoEstadoAprobacion(8L);
        model.addAttribute("approvedRepartidores", approvedRepartidores);
        System.out.println("✅ [ADMIN] Repartidores aprobados: " + approvedRepartidores.size());
        
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
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🏷️ [ADMIN] Creando categoría: " + nombre);
            
            Categoria categoria = new Categoria();
            categoria.setNombre(nombre);
            categoria.setDescripcion(descripcion);
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
     * Actualizar información de restaurante
     * POST /menuAdministrador/restaurant/{id}/update
     */
    @PostMapping("/restaurant/{id}/update")
    public String actualizarRestaurante(
            @PathVariable Long id,
            @RequestParam("name") String nombre,
            @RequestParam("lastName") String apellido,
            @RequestParam("email") String email,
            @RequestParam(value = "phone", required = false) String telefono,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🏪 [ADMIN] Actualizando restaurante: " + id);
            
            Restaurante restaurante = restauranteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));
            
            // Verificar que el email no esté en uso por otro restaurante
            restauranteRepository.findByCorreoElectronico(email).ifPresent(existente -> {
                if (!existente.getCodigo().equals(id)) {
                    throw new RuntimeException("El email ya está registrado por otro restaurante");
                }
            });
            
            restaurante.setNombre(nombre);
            restaurante.setCorreoElectronico(email);
            restaurante.setTelefono(telefono);
            
            restauranteRepository.save(restaurante);
            
            redirectAttributes.addFlashAttribute("mensaje", "Restaurante actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [ADMIN] Restaurante actualizado: " + id);
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al actualizar restaurante: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
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
     * Actualizar usuario cliente
     * POST /menuAdministrador/user/{id}/update
     */
    @PostMapping("/user/{id}/update")
    public String actualizarUsuario(
            @PathVariable Long id,
            @RequestParam("name") String nombre,
            @RequestParam("lastName") String apellido,
            @RequestParam("email") String email,
            @RequestParam(value = "phone", required = false) String telefono,
            @RequestParam(value = "address", required = false) String direccion,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("👤 [ADMIN] Actualizando usuario: " + id);
            
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Verificar que el email no esté en uso por otro usuario
            usuarioRepository.findByCorreoElectronico(email).ifPresent(existente -> {
                if (!existente.getCodigo().equals(id)) {
                    throw new RuntimeException("El email ya está registrado por otro usuario");
                }
            });
            
            usuario.setNombre(nombre);
            usuario.setApellidoPaterno(apellido);
            usuario.setApellidoMaterno(apellido);
            usuario.setCorreoElectronico(email);
            usuario.setTelefono(telefono);
            usuario.setDireccion(direccion);
            
            usuarioRepository.save(usuario);
            
            redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [ADMIN] Usuario actualizado: " + id);
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al actualizar usuario: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al actualizar usuario: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
    
    /**
     * Cambiar estado de un usuario (activar/desactivar)
     * POST /menuAdministrador/user/{id}/toggle-status
     */
    @PostMapping("/user/{id}/toggle-status")
    public String cambiarEstadoUsuario(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🔄 [ADMIN] Cambiando estado de usuario: " + id);
            
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Cambiar el estado (si es true lo pasa a false y viceversa)
            usuario.setEstado(!usuario.getEstado());
            usuarioRepository.save(usuario);
            
            String nuevoEstado = usuario.getEstado() ? "activado" : "desactivado";
            redirectAttributes.addFlashAttribute("mensaje", "Usuario " + nuevoEstado + " exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [ADMIN] Usuario " + nuevoEstado + ": " + id);
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al cambiar estado de usuario: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
    
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
    
    // ============================================
    // GESTIÓN DE REPARTIDORES (DELIVERY)
    // ============================================
    
    /**
     * Aprobar un repartidor
     * POST /menuAdministrador/repartidor/{id}/approve
     */
    @PostMapping("/repartidor/{id}/approve")
    public String aprobarRepartidor(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("✅ [ADMIN] Aprobando repartidor: " + id);
            
            Repartidor repartidor = repartidorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Repartidor no encontrado"));
            
            // Verificar que esté pendiente
            if (!repartidor.getCodigoEstadoAprobacion().equals(7L)) {
                throw new RuntimeException("El repartidor no está en estado pendiente");
            }
            
            // Obtener el admin actual
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Usuario admin = usuarioRepository.findByCorreoElectronico(authentication.getName())
                    .orElse(null);
            Long adminId = admin != null ? admin.getCodigo() : 1L;
            
            // Cambiar estado a aprobado (8)
            repartidor.setCodigoEstadoAprobacion(8L);
            repartidor.setFechaAprobacion(LocalDateTime.now());
            repartidor.setCodigoAprobador(adminId);
            repartidor.setMotivoRechazo(null); // Limpiar cualquier rechazo previo
            repartidor.setDisponible(true); // Activar disponibilidad
            
            repartidorRepository.save(repartidor);
            
            // Obtener nombre del repartidor
            String nombreRepartidor = repartidor.getUsuario() != null ? 
                    repartidor.getUsuario().getNombre() + " " + repartidor.getUsuario().getApellidoPaterno() : 
                    "Repartidor #" + id;
            
            redirectAttributes.addFlashAttribute("mensaje", 
                "Repartidor '" + nombreRepartidor + "' aprobado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [ADMIN] Repartidor aprobado: " + nombreRepartidor);
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al aprobar repartidor: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al aprobar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
    
    /**
     * Rechazar un repartidor
     * POST /menuAdministrador/repartidor/{id}/reject
     */
    @PostMapping("/repartidor/{id}/reject")
    public String rechazarRepartidor(
            @PathVariable Long id,
            @RequestParam(required = false) String motivo,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("❌ [ADMIN] Rechazando repartidor: " + id);
            
            Repartidor repartidor = repartidorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Repartidor no encontrado"));
            
            // Verificar que esté pendiente
            if (!repartidor.getCodigoEstadoAprobacion().equals(7L)) {
                throw new RuntimeException("El repartidor no está en estado pendiente");
            }
            
            // Validar motivo
            if (motivo == null || motivo.trim().isEmpty()) {
                throw new RuntimeException("Debe proporcionar un motivo de rechazo");
            }
            
            // Obtener el admin actual
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Usuario admin = usuarioRepository.findByCorreoElectronico(authentication.getName())
                    .orElse(null);
            Long adminId = admin != null ? admin.getCodigo() : 1L;
            
            // Cambiar estado a rechazado (9)
            repartidor.setCodigoEstadoAprobacion(9L);
            repartidor.setFechaAprobacion(LocalDateTime.now());
            repartidor.setCodigoAprobador(adminId);
            repartidor.setMotivoRechazo(motivo);
            repartidor.setDisponible(false);
            
            repartidorRepository.save(repartidor);
            
            // Obtener nombre del repartidor
            String nombreRepartidor = repartidor.getUsuario() != null ? 
                    repartidor.getUsuario().getNombre() + " " + repartidor.getUsuario().getApellidoPaterno() : 
                    "Repartidor #" + id;
            
            // Enviar correo de notificación
            try {
                if (repartidor.getUsuario() != null && repartidor.getUsuario().getCorreoElectronico() != null) {
                    emailService.sendDeliveryRejectionNotification(
                        repartidor.getUsuario().getCorreoElectronico(),
                        nombreRepartidor,
                        motivo
                    );
                    System.out.println("✅ [ADMIN] Correo de rechazo enviado a: " + repartidor.getUsuario().getCorreoElectronico());
                }
            } catch (Exception emailError) {
                System.err.println("⚠️ [ADMIN] No se pudo enviar el correo: " + emailError.getMessage());
                // Continuar aunque falle el envío de correo
            }
            
            redirectAttributes.addFlashAttribute("mensaje", 
                "Repartidor '" + nombreRepartidor + "' rechazado");
            redirectAttributes.addFlashAttribute("tipo", "warning");
            
            System.out.println("❌ [ADMIN] Repartidor rechazado: " + nombreRepartidor);
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al rechazar repartidor: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al rechazar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
    
    /**
     * Cambiar disponibilidad de un repartidor
     * POST /menuAdministrador/repartidor/{id}/toggle-disponibilidad
     */
    @PostMapping("/repartidor/{id}/toggle-disponibilidad")
    public String cambiarDisponibilidadRepartidor(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🔄 [ADMIN] Cambiando disponibilidad de repartidor: " + id);
            
            Repartidor repartidor = repartidorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Repartidor no encontrado"));
            
            repartidor.setDisponible(!repartidor.getDisponible());
            repartidorRepository.save(repartidor);
            
            String nuevoEstado = repartidor.getDisponible() ? "disponible" : "no disponible";
            redirectAttributes.addFlashAttribute("mensaje", "Repartidor marcado como " + nuevoEstado);
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [ADMIN] Repartidor ahora " + nuevoEstado + ": " + id);
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al cambiar disponibilidad: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al cambiar disponibilidad: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
    
    /**
     * Cambiar estado de un repartidor (activar/desactivar)
     * POST /menuAdministrador/repartidor/{id}/toggle-status
     */
    @PostMapping("/repartidor/{id}/toggle-status")
    public String cambiarEstadoRepartidor(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🔄 [ADMIN] Cambiando estado de repartidor: " + id);
            
            Repartidor repartidor = repartidorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Repartidor no encontrado"));
            
            repartidor.setEstado(!repartidor.getEstado());
            repartidorRepository.save(repartidor);
            
            String nuevoEstado = repartidor.getEstado() ? "activado" : "desactivado";
            redirectAttributes.addFlashAttribute("mensaje", "Repartidor " + nuevoEstado + " exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [ADMIN] Repartidor " + nuevoEstado + ": " + id);
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al cambiar estado: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
    
    /**
     * Actualizar información de repartidor
     * POST /menuAdministrador/delivery/{id}/update
     */
    @PostMapping("/delivery/{id}/update")
    public String actualizarRepartidor(
            @PathVariable Long id,
            @RequestParam("name") String nombre,
            @RequestParam("lastName") String apellido,
            @RequestParam("email") String email,
            @RequestParam(value = "phone", required = false) String telefono,
            @RequestParam(value = "address", required = false) String direccion,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🚚 [ADMIN] Actualizando repartidor: " + id);
            
            Repartidor repartidor = repartidorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Repartidor no encontrado"));
            
            if (repartidor.getUsuario() != null) {
                Usuario usuario = repartidor.getUsuario();
                
                // Verificar que el email no esté en uso por otro usuario
                usuarioRepository.findByCorreoElectronico(email).ifPresent(existente -> {
                    if (!existente.getCodigo().equals(usuario.getCodigo())) {
                        throw new RuntimeException("El email ya está registrado por otro usuario");
                    }
                });
                
                usuario.setNombre(nombre);
                usuario.setApellidoPaterno(apellido);
                usuario.setApellidoMaterno(apellido);
                usuario.setCorreoElectronico(email);
                usuario.setTelefono(telefono);
                usuario.setDireccion(direccion);
                
                usuarioRepository.save(usuario);
            }
            
            repartidorRepository.save(repartidor);
            
            redirectAttributes.addFlashAttribute("mensaje", "Repartidor actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [ADMIN] Repartidor actualizado: " + id);
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al actualizar repartidor: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
    
    /**
     * Obtener documentos de un restaurante (JSON)
     * GET /menuAdministrador/restaurant/{id}/documents
     */
    @GetMapping("/restaurant/{id}/documents")
    @ResponseBody
    public ResponseEntity<Map<String, String>> obtenerDocumentosRestaurante(@PathVariable Long id) {
        try {
            System.out.println("📄 [ADMIN] Obteniendo documentos del restaurante: " + id);
            
            String sql = "SELECT tipo_documento, ruta_archivo FROM documento_restaurante " +
                        "WHERE codigo_restaurante = ? AND estado = 1";
            
            Map<String, String> documentos = new HashMap<>();
            
            jdbcTemplate.query(sql, rs -> {
                documentos.put(rs.getString("tipo_documento"), rs.getString("ruta_archivo"));
            }, id);
            
            System.out.println("✅ [ADMIN] Documentos encontrados: " + documentos.size());
            return ResponseEntity.ok(documentos);
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al obtener documentos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new HashMap<>()); // Retornar vacío en caso de error
        }
    }
    
    /**
     * Subir/actualizar documentos de un restaurante
     * POST /menuAdministrador/restaurant/{id}/upload-documents
     */
    @PostMapping("/restaurant/{id}/upload-documents")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> subirDocumentosRestaurante(
            @PathVariable Long id,
            @RequestParam(required = false) MultipartFile cartaRestaurante,
            @RequestParam(required = false) MultipartFile carnetSanidad,
            @RequestParam(required = false) MultipartFile licenciaFuncionamiento) {
        
        Map<String, Object> response = new HashMap<>();
        int documentosSubidos = 0;
        
        try {
            System.out.println("📤 [ADMIN] Subiendo documentos para restaurante: " + id);
            
            // Verificar que el restaurante existe
            if (!restauranteRepository.existsById(id)) {
                response.put("success", false);
                response.put("message", "Restaurante no encontrado");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Subir Carta del Restaurante
            if (cartaRestaurante != null && !cartaRestaurante.isEmpty()) {
                String rutaCarta = fileStorageService.guardarArchivoRestaurante(
                    cartaRestaurante, id, "CARTA_RESTAURANTE"
                );
                
                // Eliminar documento anterior si existe
                jdbcTemplate.update(
                    "DELETE FROM documento_restaurante WHERE codigo_restaurante = ? AND tipo_documento = 'CARTA_RESTAURANTE'",
                    id
                );
                
                // Insertar nuevo documento
                restauranteJdbcRepository.insertDocumento(id, "CARTA_RESTAURANTE", rutaCarta);
                documentosSubidos++;
                System.out.println("   ✓ Carta subida: " + rutaCarta);
            }
            
            // Subir Carnet de Sanidad
            if (carnetSanidad != null && !carnetSanidad.isEmpty()) {
                String rutaSanidad = fileStorageService.guardarArchivoRestaurante(
                    carnetSanidad, id, "CarnetSanidad"
                );
                
                jdbcTemplate.update(
                    "DELETE FROM documento_restaurante WHERE codigo_restaurante = ? AND tipo_documento = 'CarnetSanidad'",
                    id
                );
                
                restauranteJdbcRepository.insertDocumento(id, "CarnetSanidad", rutaSanidad);
                documentosSubidos++;
                System.out.println("   ✓ Carnet de Sanidad subido: " + rutaSanidad);
            }
            
            // Subir Licencia de Funcionamiento
            if (licenciaFuncionamiento != null && !licenciaFuncionamiento.isEmpty()) {
                String rutaLicencia = fileStorageService.guardarArchivoRestaurante(
                    licenciaFuncionamiento, id, "LicenciaFuncionamiento"
                );
                
                jdbcTemplate.update(
                    "DELETE FROM documento_restaurante WHERE codigo_restaurante = ? AND tipo_documento = 'LicenciaFuncionamiento'",
                    id
                );
                
                restauranteJdbcRepository.insertDocumento(id, "LicenciaFuncionamiento", rutaLicencia);
                documentosSubidos++;
                System.out.println("   ✓ Licencia de Funcionamiento subida: " + rutaLicencia);
            }
            
            if (documentosSubidos == 0) {
                response.put("success", false);
                response.put("message", "No se seleccionó ningún archivo");
                return ResponseEntity.badRequest().body(response);
            }
            
            response.put("success", true);
            response.put("message", documentosSubidos + " documento(s) subido(s) exitosamente");
            response.put("count", documentosSubidos);
            
            System.out.println("✅ [ADMIN] Total de documentos subidos: " + documentosSubidos);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al subir documentos: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al subir documentos: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // =====================================================
    // 💰 ENDPOINTS DE GANANCIAS DE LA PLATAFORMA
    // =====================================================
    
    /**
     * Obtener resumen de ganancias
     */
    @GetMapping("/api/ganancias/resumen")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerResumenGanancias() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Ganancias totales
            java.math.BigDecimal gananciasTotales = gananciaPlataformaRepository.calcularGananciasTotales();
            
            // Ganancias del mes actual
            LocalDateTime inicioMes = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime finMes = LocalDateTime.now().withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
            java.math.BigDecimal gananciasMes = gananciaPlataformaRepository.calcularGananciasPorPeriodo(inicioMes, finMes);
            
            // Ganancias de hoy
            LocalDateTime inicioHoy = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
            LocalDateTime finHoy = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
            java.math.BigDecimal gananciasHoy = gananciaPlataformaRepository.calcularGananciasPorPeriodo(inicioHoy, finHoy);
            
            // Pedidos procesados
            Long pedidosProcesados = gananciaPlataformaRepository.contarPedidosProcesados();
            
            // Porcentaje de comisión actual
            java.math.BigDecimal porcentajeActual = configuracionComisionRepository
                .findFirstByEstadoTrueOrderByFechaVigenciaDesc()
                .map(ConfiguracionComision::getPorcentajeComision)
                .orElse(new java.math.BigDecimal("5.00"));
            
            response.put("success", true);
            response.put("gananciasTotales", gananciasTotales);
            response.put("gananciasMes", gananciasMes);
            response.put("gananciasHoy", gananciasHoy);
            response.put("pedidosProcesados", pedidosProcesados);
            response.put("porcentajeComision", porcentajeActual);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al obtener resumen: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * Obtener historial de ganancias
     */
    @GetMapping("/api/ganancias/historial")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerHistorialGanancias() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<GananciaPlataforma> ganancias = gananciaPlataformaRepository.findAllByOrderByFechaRegistroDesc();
            
            response.put("success", true);
            response.put("ganancias", ganancias);
            response.put("total", ganancias.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al obtener historial: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * Obtener ganancias por mes (gráfico)
     */
    @GetMapping("/api/ganancias/por-mes")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerGananciasPorMes() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Object[]> gananciasPorMes = gananciaPlataformaRepository.obtenerGananciasPorMes();
            
            response.put("success", true);
            response.put("datos", gananciasPorMes);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al obtener datos: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * Actualizar porcentaje de comisión
     */
    @PostMapping("/api/ganancias/actualizar-comision")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> actualizarComision(@RequestParam java.math.BigDecimal porcentaje) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar porcentaje
            if (porcentaje.compareTo(java.math.BigDecimal.ZERO) <= 0 || 
                porcentaje.compareTo(new java.math.BigDecimal("100")) > 0) {
                response.put("success", false);
                response.put("message", "El porcentaje debe estar entre 0 y 100");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Desactivar configuraciones anteriores
            configuracionComisionRepository.findAll().forEach(config -> {
                config.setEstado(false);
                configuracionComisionRepository.save(config);
            });
            
            // Crear nueva configuración
            ConfiguracionComision nuevaConfig = new ConfiguracionComision();
            nuevaConfig.setPorcentajeComision(porcentaje);
            nuevaConfig.setDescripcion("Comisión actualizada desde panel de administración");
            nuevaConfig.setFechaVigencia(LocalDate.now());
            nuevaConfig.setEstado(true);
            
            configuracionComisionRepository.save(nuevaConfig);
            
            response.put("success", true);
            response.put("message", "Comisión actualizada a " + porcentaje + "%");
            response.put("porcentaje", porcentaje);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al actualizar comisión: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
