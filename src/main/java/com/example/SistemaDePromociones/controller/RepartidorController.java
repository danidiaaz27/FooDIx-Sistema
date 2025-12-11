package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.dto.RepartidorRegistroDTO;
import com.example.SistemaDePromociones.model.Departamento;
import com.example.SistemaDePromociones.model.Repartidor;
import com.example.SistemaDePromociones.model.TipoVehiculo;
import com.example.SistemaDePromociones.model.Usuario;
import com.example.SistemaDePromociones.repository.jdbc.DepartamentoJdbcRepository;
import com.example.SistemaDePromociones.repository.TipoVehiculoRepository;
import com.example.SistemaDePromociones.security.CustomUserDetails;
import com.example.SistemaDePromociones.service.RepartidorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
     * 
     * Soporta dos flujos:
     * 1. Usuario nuevo (paso 1 completado): Requiere usuarioCodigoTemporal en sesión
     * 2. Usuario autenticado: Ya tiene cuenta y quiere agregar rol de repartidor
     */
    @GetMapping
    public String mostrarFormulario(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        System.out.println("🔍 [DELIVERY GET] Iniciando mostrarFormulario");
        
        // CASO 1: Usuario ya autenticado (obtener desde Spring Security Context)
        Usuario usuarioAutenticado = obtenerUsuarioAutenticado();
        
        System.out.println("🔍 [DELIVERY GET] Usuario autenticado: " + (usuarioAutenticado != null ? usuarioAutenticado.getCorreoElectronico() : "null"));
        
        if (usuarioAutenticado != null) {
            System.out.println("👤 [DELIVERY] Usuario autenticado accediendo: " + usuarioAutenticado.getCorreoElectronico() + " (Código: " + usuarioAutenticado.getCodigo() + ")");
            
            // Pasar datos del usuario autenticado al modelo
            model.addAttribute("usuarioCodigo", usuarioAutenticado.getCodigo());
            model.addAttribute("usuarioEmail", usuarioAutenticado.getCorreoElectronico());
            model.addAttribute("usuarioAutenticado", true);
            model.addAttribute("mostrarPaso", "delivery"); // Ir directo al Paso 2
            
            // Cargar departamentos
            List<Departamento> departamentos = departamentoRepository.findAllActivos();
            System.out.println("🚴 [DELIVERY] Departamentos cargados: " + departamentos.size());
            model.addAttribute("departamentos", departamentos);
            
            // Cargar tipos de vehículo
            List<TipoVehiculo> tiposVehiculo = tipoVehiculoRepository.findByEstadoTrue();
            model.addAttribute("tiposVehiculo", tiposVehiculo);
            
            return "registroDelivery";
        }
        
        // CASO 2: Usuario nuevo (proceso de registro en 2 pasos)
        Long usuarioCodigo = (Long) session.getAttribute("usuarioCodigoTemporal");
        String usuarioEmail = (String) session.getAttribute("usuarioEmailTemporal");
        
        System.out.println("🔍 [DELIVERY GET] Usuario temporal - Código: " + usuarioCodigo + ", Email: " + usuarioEmail);
        
        if (usuarioCodigo == null || usuarioEmail == null) {
            System.out.println("⚠️ [REPARTIDOR PASO 2] No se encontró usuario temporal ni autenticado, redirigiendo a registro");
            redirectAttributes.addFlashAttribute("error", "Debes completar el registro de datos personales primero");
            return "redirect:/registroDelivery";
        }
        
        System.out.println("🚴 [REPARTIDOR PASO 2] Usuario temporal encontrado: " + usuarioEmail + " (Código: " + usuarioCodigo + ")");
        
        // Pasar datos del usuario al modelo
        model.addAttribute("usuarioCodigo", usuarioCodigo);
        model.addAttribute("usuarioEmail", usuarioEmail);
        model.addAttribute("usuarioAutenticado", false);
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
     * 
     * Soporta dos flujos:
     * 1. Usuario nuevo: Usa usuarioCodigoTemporal de la sesión
     * 2. Usuario autenticado: Usa el usuario del SecurityContext de Spring Security
     */
    @PostMapping
    public String registrarRepartidor(
            @ModelAttribute RepartidorRegistroDTO dto,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            Long usuarioCodigo = null;
            boolean esUsuarioAutenticado = false;
            
            // CASO 1: Intentar obtener usuario autenticado primero
            Usuario usuarioAutenticado = obtenerUsuarioAutenticado();
            
            if (usuarioAutenticado != null) {
                usuarioCodigo = usuarioAutenticado.getCodigo();
                esUsuarioAutenticado = true;
                System.out.println("👤 [DELIVERY POST] Usuario autenticado: " + usuarioAutenticado.getCorreoElectronico() + " (Código: " + usuarioCodigo + ")");
            } else {
                // CASO 2: Usuario nuevo (de sesión temporal)
                usuarioCodigo = (Long) session.getAttribute("usuarioCodigoTemporal");
                System.out.println("🔍 [DELIVERY POST] Usuario temporal - Código: " + usuarioCodigo);
                
                if (usuarioCodigo == null) {
                    System.out.println("⚠️ [DELIVERY POST] No hay usuario temporal ni autenticado");
                    redirectAttributes.addFlashAttribute("error", "Debes completar el registro de datos personales primero");
                    return "redirect:/registroDelivery";
                }
            }
            
            System.out.println("🚴 [REPARTIDOR PASO 2] Registrando repartidor para usuario: " + usuarioCodigo);
            
            // Registrar el repartidor vinculándolo al usuario existente
            Repartidor repartidor = repartidorService.registrarRepartidor(dto, usuarioCodigo);
            
            System.out.println("✅ [REPARTIDOR PASO 2] Repartidor registrado exitosamente: " + repartidor.getCodigo());
            
            // Limpiar sesión temporal (solo si es usuario nuevo)
            if (!esUsuarioAutenticado) {
                session.removeAttribute("usuarioCodigoTemporal");
                session.removeAttribute("usuarioEmailTemporal");
                System.out.println("🗑️ [DELIVERY POST] Datos temporales eliminados de sesión");
            }
            
            // Mensaje de éxito
            if (esUsuarioAutenticado) {
                // Usuario autenticado: Mostrar modal de confirmación en menuUsuario
                redirectAttributes.addFlashAttribute("registroExitoso", true);
                redirectAttributes.addFlashAttribute("tipoRegistro", "delivery");
                redirectAttributes.addFlashAttribute("mensajeRegistro", 
                    "¡Solicitud de repartidor enviada correctamente! Tu cuenta se ha creado y está en proceso de revisión. Recibirás un correo electrónico con el resultado de la aprobación en las próximas 24-48 horas. Una vez aprobado, podrás acceder al panel de delivery.");
                return "redirect:/menuUsuario";
            } else {
                // Usuario nuevo: Dirigir a login
                redirectAttributes.addFlashAttribute("registroExitoso", true);
                redirectAttributes.addFlashAttribute("mensajeRegistro", 
                    "¡Registro exitoso! Tu solicitud está en revisión. Recibirás un correo con el resultado de la aprobación en las próximas 24-48 horas.");
                return "redirect:/login";
            }
            
        } catch (Exception e) {
            System.err.println("❌ [REPARTIDOR PASO 2] Error al registrar: " + e.getMessage());
            e.printStackTrace();
            
            redirectAttributes.addFlashAttribute("error", 
                "Error al registrar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
            return "redirect:/registro-repartidor";
        }
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
}
