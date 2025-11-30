package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.dto.RepartidorRegistroDTO;
import com.example.SistemaDePromociones.model.Departamento;
import com.example.SistemaDePromociones.model.Repartidor;
import com.example.SistemaDePromociones.model.TipoVehiculo;
import com.example.SistemaDePromociones.repository.jdbc.DepartamentoJdbcRepository;
import com.example.SistemaDePromociones.repository.TipoVehiculoRepository;
import com.example.SistemaDePromociones.service.RepartidorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller para el registro de repartidores
 */
@Controller
@RequestMapping("/registro-repartidor")
public class RepartidorController {
    
    @Autowired
    private DepartamentoJdbcRepository departamentoRepository;
    
    @Autowired
    private TipoVehiculoRepository tipoVehiculoRepository;
    
    @Autowired
    private RepartidorService repartidorService;
    
    /**
     * Mostrar formulario de registro de repartidor (PASO 2)
     * GET /registro-repartidor
     * Requiere que el usuario haya completado el paso 1 (datos personales)
     */
    @GetMapping
    public String mostrarFormulario(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        // Verificar que existe un usuario temporal (paso 1 completado)
        Long usuarioCodigo = (Long) session.getAttribute("usuarioCodigoTemporal");
        String usuarioEmail = (String) session.getAttribute("usuarioEmailTemporal");
        
        if (usuarioCodigo == null || usuarioEmail == null) {
            System.out.println("⚠️ [REPARTIDOR PASO 2] No se encontró usuario temporal, redirigiendo a registro");
            redirectAttributes.addFlashAttribute("error", "Debes completar el registro de datos personales primero");
            return "redirect:/registroDelivery";
        }
        
        System.out.println("🚴 [REPARTIDOR PASO 2] Usuario temporal encontrado: " + usuarioEmail + " (Código: " + usuarioCodigo + ")");
        
        // Pasar datos del usuario al modelo
        model.addAttribute("usuarioCodigo", usuarioCodigo);
        model.addAttribute("usuarioEmail", usuarioEmail);
        model.addAttribute("mostrarPaso", "repartidor"); // Indicar qué paso mostrar
        
        // Cargar departamentos para el select usando JDBC
        List<Departamento> departamentos = departamentoRepository.findAllActivos();
        System.out.println("🚴 [REPARTIDOR PASO 2] Departamentos cargados: " + departamentos.size());
        departamentos.forEach(d -> System.out.println("   - " + d.getCodigo() + ": " + d.getNombre()));
        model.addAttribute("departamentos", departamentos);
        
        // Cargar tipos de vehículo para el select
        List<TipoVehiculo> tiposVehiculo = tipoVehiculoRepository.findByEstadoTrue();
        System.out.println("🚴 [REPARTIDOR PASO 2] Tipos de vehículo cargados: " + tiposVehiculo.size());
        model.addAttribute("tiposVehiculo", tiposVehiculo);
        
        return "registroDelivery";
    }
    
    /**
     * Procesar registro de repartidor (PASO 2)
     * POST /registro-repartidor
     */
    @PostMapping
    public String registrarRepartidor(
            @ModelAttribute RepartidorRegistroDTO dto,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            // Verificar que existe el usuario temporal
            Long usuarioCodigo = (Long) session.getAttribute("usuarioCodigoTemporal");
            
            if (usuarioCodigo == null) {
                System.out.println("⚠️ [REPARTIDOR PASO 2] No se encontró usuario temporal");
                redirectAttributes.addFlashAttribute("error", "Sesión expirada. Debes volver a registrarte.");
                return "redirect:/registroDelivery";
            }
            
            System.out.println("🚴 [REPARTIDOR PASO 2] Registrando repartidor para usuario: " + usuarioCodigo);
            
            // Registrar el repartidor vinculándolo al usuario existente
            Repartidor repartidor = repartidorService.registrarRepartidor(dto, usuarioCodigo);
            
            System.out.println("✅ [REPARTIDOR PASO 2] Repartidor registrado exitosamente: " + repartidor.getCodigo());
            
            // Limpiar sesión temporal
            session.removeAttribute("usuarioCodigoTemporal");
            session.removeAttribute("usuarioEmailTemporal");
            
            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("registroExitoso", true);
            redirectAttributes.addFlashAttribute("mensajeRegistro", 
                "¡Registro exitoso! Tu solicitud está en revisión. Recibirás un correo electrónico con el resultado de la aprobación en las próximas 24-48 horas.");
            
            return "redirect:/login";
            
        } catch (Exception e) {
            System.err.println("❌ [REPARTIDOR PASO 2] Error al registrar: " + e.getMessage());
            e.printStackTrace();
            
            redirectAttributes.addFlashAttribute("error", 
                "Error al registrar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
            return "redirect:/registro-repartidor";
        }
    }
}
