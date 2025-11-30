package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.dto.RestauranteRegistroDTO;
import com.example.SistemaDePromociones.model.Categoria;
import com.example.SistemaDePromociones.model.Departamento;
import com.example.SistemaDePromociones.repository.CategoriaRepository;
import com.example.SistemaDePromociones.repository.jdbc.DepartamentoJdbcRepository;
import com.example.SistemaDePromociones.service.RestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
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
     * Requiere que el usuario haya completado el paso 1 (datos personales)
     */
    @GetMapping
    public String mostrarFormulario(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        // Verificar que existe un usuario temporal (paso 1 completado)
        Long usuarioCodigo = (Long) session.getAttribute("usuarioCodigoTemporal");
        String usuarioEmail = (String) session.getAttribute("usuarioEmailTemporal");
        
        if (usuarioCodigo == null || usuarioEmail == null) {
            System.out.println("⚠️ [RESTAURANTE PASO 2] No se encontró usuario temporal, redirigiendo a registro");
            redirectAttributes.addFlashAttribute("error", "Debes completar el registro de datos personales primero");
            return "redirect:/registroRestaurante";
        }
        
        System.out.println("🏪 [RESTAURANTE PASO 2] Usuario temporal encontrado: " + usuarioEmail + " (Código: " + usuarioCodigo + ")");
        
        // Pasar datos del usuario al modelo
        model.addAttribute("usuarioCodigo", usuarioCodigo);
        model.addAttribute("usuarioEmail", usuarioEmail);
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
     * Procesar registro de restaurante (PASO 2)
     * POST /registro-restaurante
     */
    @PostMapping
    public String registrarRestaurante(
            @ModelAttribute RestauranteRegistroDTO dto,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            // Verificar que existe el usuario temporal
            Long usuarioCodigo = (Long) session.getAttribute("usuarioCodigoTemporal");
            
            if (usuarioCodigo == null) {
                System.out.println("⚠️ [RESTAURANTE PASO 2] No se encontró usuario temporal");
                redirectAttributes.addFlashAttribute("error", "Sesión expirada. Debes volver a registrarte.");
                return "redirect:/registroRestaurante";
            }
            
            System.out.println("🏪 [RESTAURANTE PASO 2] Registrando restaurante para usuario: " + usuarioCodigo);
            
            // Registrar el restaurante vinculándolo al usuario existente
            Long codigoRestaurante = restauranteService.registrarRestaurante(dto, usuarioCodigo);
            
            System.out.println("✅ [RESTAURANTE PASO 2] Restaurante registrado exitosamente: " + codigoRestaurante);
            
            // Limpiar sesión temporal
            session.removeAttribute("usuarioCodigoTemporal");
            session.removeAttribute("usuarioEmailTemporal");
            
            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("registroExitoso", true);
            redirectAttributes.addFlashAttribute("mensajeRegistro", 
                "¡Registro exitoso! Tu solicitud está en revisión. Recibirás un correo electrónico con el resultado de la aprobación en las próximas 24-48 horas.");
            
            return "redirect:/login";
            
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
