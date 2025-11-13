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
     * Mostrar formulario de registro de restaurante
     * GET /registro-restaurante
     */
    @GetMapping
    public String mostrarFormulario(Model model, HttpSession session) {
        // Obtener email verificado de la sesión
        String verifiedEmail = (String) session.getAttribute("verifiedEmail");
        if (verifiedEmail != null) {
            model.addAttribute("verifiedEmail", verifiedEmail);
            System.out.println("📧 [RESTAURANTE] Email verificado encontrado: " + verifiedEmail);
        }
        
        // Cargar departamentos para los selects usando JDBC
        List<Departamento> departamentos = departamentoRepository.findAllActivos();
        System.out.println("🏪 [RESTAURANTE] Departamentos cargados: " + departamentos.size());
        departamentos.forEach(d -> System.out.println("   - " + d.getCodigo() + ": " + d.getNombre()));
        model.addAttribute("departamentos", departamentos);
        
        // Cargar categorías para los checkboxes
        List<Categoria> categorias = categoriaRepository.findByEstadoTrue();
        System.out.println("🏪 [RESTAURANTE] Categorías cargadas: " + categorias.size());
        model.addAttribute("categorias", categorias);
        
        return "registro-Restaurante";
    }
    
    /**
     * Procesar registro de restaurante
     * POST /registro-restaurante
     */
    @PostMapping
    public String registrarRestaurante(
            @ModelAttribute RestauranteRegistroDTO dto,
            RedirectAttributes redirectAttributes) {
        try {
            Long codigoRestaurante = restauranteService.registrarRestaurante(dto);
            redirectAttributes.addFlashAttribute("mensaje", 
                "¡Registro exitoso! Tu solicitud está en revisión. Código: " + codigoRestaurante);
            redirectAttributes.addFlashAttribute("tipo", "success");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al registrar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
            return "redirect:/registro-restaurante";
        }
    }
}
