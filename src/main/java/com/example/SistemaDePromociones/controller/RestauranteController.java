package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.dto.RestauranteRegistroDTO;
import com.example.SistemaDePromociones.model.Categoria;
import com.example.SistemaDePromociones.model.Departamento;
import com.example.SistemaDePromociones.model.Usuario;
import com.example.SistemaDePromociones.repository.CategoriaRepository;
import com.example.SistemaDePromociones.repository.jdbc.DepartamentoJdbcRepository;
import com.example.SistemaDePromociones.security.CustomUserDetails;
import com.example.SistemaDePromociones.service.RestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.util.List;

/**
 * Controller para el registro de restaurantes
 */
@Controller
@RequestMapping("/registro-restaurante")
public class RestauranteController {
    
    @Autowired
    private DepartamentoJdbcRepository departamentoRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private RestauranteService restauranteService;
    
    /**
     * Mostrar formulario de registro de restaurante (PASO 2)
     * GET /registro-restaurante
     * 
     * Soporta dos flujos:
     * 1. Usuario nuevo (paso 1 completado): Requiere usuarioCodigoTemporal en sesión
     * 2. Usuario autenticado: Ya tiene cuenta y quiere agregar rol de restaurante
     */
    @GetMapping
    public String mostrarFormulario(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        System.out.println("🔍 [RESTAURANTE GET] Iniciando mostrarFormulario");
        
        // CASO 1: Usuario ya autenticado (obtener desde Spring Security Context)
        Usuario usuarioAutenticado = obtenerUsuarioAutenticado();
        
        System.out.println("🔍 [RESTAURANTE GET] Usuario autenticado: " + (usuarioAutenticado != null ? usuarioAutenticado.getCorreoElectronico() : "null"));
        
        if (usuarioAutenticado != null) {
            System.out.println("👤 [RESTAURANTE] Usuario autenticado accediendo: " + usuarioAutenticado.getCorreoElectronico() + " (Código: " + usuarioAutenticado.getCodigo() + ")");
            
            // Pasar datos del usuario autenticado al modelo
            model.addAttribute("usuarioCodigo", usuarioAutenticado.getCodigo());
            model.addAttribute("usuarioEmail", usuarioAutenticado.getCorreoElectronico());
            model.addAttribute("usuarioAutenticado", true);
            model.addAttribute("mostrarPaso", "restaurante"); // Ir directo al Paso 2
            
            // Cargar departamentos
            List<Departamento> departamentos = departamentoRepository.findAllActivos();
            System.out.println("🏪 [RESTAURANTE] Departamentos cargados: " + departamentos.size());
            model.addAttribute("departamentos", departamentos);
            
            // Cargar categorías
            List<Categoria> categorias = categoriaRepository.findByEstadoTrue();
            System.out.println("🏪 [RESTAURANTE] Categorías cargadas: " + categorias.size());
            model.addAttribute("categorias", categorias);
            
            return "registroRestaurante";
        }
        
        // CASO 2: Usuario nuevo (proceso de registro en 2 pasos)
        Long usuarioCodigo = (Long) session.getAttribute("usuarioCodigoTemporal");
        String usuarioEmail = (String) session.getAttribute("usuarioEmailTemporal");
        
        System.out.println("🔍 [RESTAURANTE GET] Usuario temporal - Código: " + usuarioCodigo + ", Email: " + usuarioEmail);
        
        if (usuarioCodigo == null || usuarioEmail == null) {
            System.out.println("⚠️ [RESTAURANTE PASO 2] No se encontró usuario temporal ni autenticado, redirigiendo a registro");
            redirectAttributes.addFlashAttribute("error", "Debes completar el registro de datos personales primero");
            return "redirect:/registroRestaurante";
        }
        
        System.out.println("🏪 [RESTAURANTE PASO 2] Usuario temporal encontrado: " + usuarioEmail + " (Código: " + usuarioCodigo + ")");
        
        // Pasar datos del usuario temporal al modelo
        model.addAttribute("usuarioCodigo", usuarioCodigo);
        model.addAttribute("usuarioEmail", usuarioEmail);
        model.addAttribute("usuarioAutenticado", false);
        model.addAttribute("mostrarPaso", "restaurante"); // Indicar qué paso mostrar
        
        // Cargar departamentos para los selects usando JDBC
        List<Departamento> departamentos = departamentoRepository.findAllActivos();
        System.out.println("🏪 [RESTAURANTE PASO 2] Departamentos cargados: " + departamentos.size());
        departamentos.forEach(d -> System.out.println("   - " + d.getCodigo() + ": " + d.getNombre()));
        model.addAttribute("departamentos", departamentos);
        
        // Cargar categorías para los checkboxes
        List<Categoria> categorias = categoriaRepository.findByEstadoTrue();
        System.out.println("🏪 [RESTAURANTE PASO 2] Categorías cargadas: " + categorias.size());
        model.addAttribute("categorias", categorias);
        
        return "registroRestaurante";
    }
    
    /**
     * Obtener el usuario autenticado desde el SecurityContext de Spring Security
     */
    private Usuario obtenerUsuarioAutenticado() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication != null && authentication.isAuthenticated() 
                && !"anonymousUser".equals(authentication.getPrincipal())) {
                
                Object principal = authentication.getPrincipal();
                
                if (principal instanceof CustomUserDetails) {
                    CustomUserDetails userDetails = (CustomUserDetails) principal;
                    return userDetails.getUsuario();
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error al obtener usuario autenticado: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Procesar registro de restaurante (PASO 2)
     * POST /registro-restaurante
     * 
     * Soporta dos flujos:
     * 1. Usuario nuevo: Usa usuarioCodigoTemporal de la sesión
     * 2. Usuario autenticado: Usa el usuario del SecurityContext de Spring Security
     */
    @PostMapping
    public String registrarRestaurante(
            @ModelAttribute RestauranteRegistroDTO dto,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            Long usuarioCodigo = null;
            boolean esUsuarioAutenticado = false;
            
            // CASO 1: Usuario autenticado (obtener desde Spring Security Context)
            Usuario usuarioAutenticado = obtenerUsuarioAutenticado();
            
            if (usuarioAutenticado != null) {
                usuarioCodigo = usuarioAutenticado.getCodigo();
                esUsuarioAutenticado = true;
                System.out.println("👤 [RESTAURANTE REGISTRO] Usuario autenticado: " + usuarioAutenticado.getCorreoElectronico() + " (Código: " + usuarioCodigo + ")");
            } else {
                // CASO 2: Usuario nuevo (temporal)
                usuarioCodigo = (Long) session.getAttribute("usuarioCodigoTemporal");
                System.out.println("🏪 [RESTAURANTE PASO 2] Usuario temporal: " + usuarioCodigo);
            }
            
            if (usuarioCodigo == null) {
                System.out.println("⚠️ [RESTAURANTE PASO 2] No se encontró usuario temporal ni autenticado");
                redirectAttributes.addFlashAttribute("error", "Sesión expirada. Debes volver a registrarte.");
                return "redirect:/registroRestaurante";
            }
            
            System.out.println("🏪 [RESTAURANTE PASO 2] Registrando restaurante para usuario: " + usuarioCodigo);
            
            // Registrar el restaurante vinculándolo al usuario existente
            Long codigoRestaurante = restauranteService.registrarRestaurante(dto, usuarioCodigo);
            
            System.out.println("✅ [RESTAURANTE PASO 2] Restaurante registrado exitosamente: " + codigoRestaurante);
            
            // Limpiar sesión temporal solo si no es usuario autenticado
            if (!esUsuarioAutenticado) {
                session.removeAttribute("usuarioCodigoTemporal");
                session.removeAttribute("usuarioEmailTemporal");
            }
            
            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("registroExitoso", true);
            
            if (esUsuarioAutenticado) {
                // Usuario autenticado: Mostrar modal de confirmación en menuUsuario
                redirectAttributes.addFlashAttribute("tipoRegistro", "restaurante");
                redirectAttributes.addFlashAttribute("mensajeRegistro", 
                    "¡Solicitud de restaurante enviada correctamente! Tu cuenta se ha creado y está en proceso de revisión. Recibirás un correo electrónico con el resultado de la aprobación en las próximas 24-48 horas. Una vez aprobado, podrás acceder al panel de restaurante.");
                return "redirect:/menuUsuario";
            } else {
                // Usuario nuevo: Dirigir a login
                redirectAttributes.addFlashAttribute("mensajeRegistro", 
                    "¡Registro exitoso! Tu solicitud está en revisión. Recibirás un correo electrónico con el resultado de la aprobación en las próximas 24-48 horas.");
                return "redirect:/login";
            }
            
        } catch (Exception e) {
            System.err.println("❌ [RESTAURANTE PASO 2] Error al registrar: " + e.getMessage());
            e.printStackTrace();
            
            redirectAttributes.addFlashAttribute("error", 
                "Error al registrar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
            return "redirect:/registro-restaurante";
        }
    }
}
