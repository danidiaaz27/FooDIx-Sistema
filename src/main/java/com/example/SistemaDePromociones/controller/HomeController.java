package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.dto.UsuarioRegistroDTO;
import com.example.SistemaDePromociones.model.Departamento;
import com.example.SistemaDePromociones.model.Usuario;
import com.example.SistemaDePromociones.repository.jdbc.DepartamentoJdbcRepository;
import com.example.SistemaDePromociones.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller principal para manejar las páginas estáticas del sistema
 */
@Controller
public class HomeController {
    
    @Autowired
    private DepartamentoJdbcRepository departamentoRepository;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Página principal / inicio
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    /**
     * Página de login
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    /**
     * Página de selección de tipo de registro (LEGACY - mantener compatibilidad)
     */
    @GetMapping("/registro")
    public String registro(Model model, HttpSession session) {
        System.out.println("👤 [REGISTRO] Cargando formulario de registro (legacy)");
        
        // Obtener email verificado de la sesión
        String verifiedEmail = (String) session.getAttribute("verifiedEmail");
        if (verifiedEmail != null) {
            model.addAttribute("verifiedEmail", verifiedEmail);
            System.out.println("📧 [REGISTRO] Email verificado encontrado: " + verifiedEmail);
        }
        
        List<Departamento> departamentos = departamentoRepository.findAllActivos();
        System.out.println("👤 [REGISTRO] Departamentos cargados: " + departamentos.size());
        departamentos.forEach(d -> System.out.println("   - " + d.getCodigo() + ": " + d.getNombre()));
        model.addAttribute("departamentos", departamentos);
        return "registro";
    }
    
    /**
     * Página de registro de usuario (cliente)
     * Requiere verificación de email previa
     */
    @GetMapping("/registroUsuario")
    public String registroUsuario(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        System.out.println("👤 [REGISTRO USUARIO] Cargando formulario de registro de usuario");
        
        // Verificar que el email esté verificado
        String verifiedEmail = (String) session.getAttribute("verifiedEmail");
        if (verifiedEmail == null) {
            System.out.println("⚠️ [REGISTRO USUARIO] Email no verificado, redirigiendo a /verificacion");
            redirectAttributes.addFlashAttribute("error", "Debes verificar tu correo electrónico primero");
            return "redirect:/verificacion?tipo=usuario";
        }
        
        model.addAttribute("verifiedEmail", verifiedEmail);
        System.out.println("📧 [REGISTRO USUARIO] Email verificado: " + verifiedEmail);
        
        List<Departamento> departamentos = departamentoRepository.findAllActivos();
        System.out.println("👤 [REGISTRO USUARIO] Departamentos cargados: " + departamentos.size());
        departamentos.forEach(d -> System.out.println("   - " + d.getCodigo() + ": " + d.getNombre()));
        model.addAttribute("departamentos", departamentos);
        return "registroUsuario";
    }
    
    /**
     * Página de registro de negocio (restaurante/repartidor)
     * Requiere verificación de email previa
     */
    @GetMapping("/registroNegocio")
    public String registroNegocio(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        System.out.println("🏪 [REGISTRO NEGOCIO] Cargando formulario de registro de negocio");
        
        // Verificar que el email esté verificado
        String verifiedEmail = (String) session.getAttribute("verifiedEmail");
        if (verifiedEmail == null) {
            System.out.println("⚠️ [REGISTRO NEGOCIO] Email no verificado, redirigiendo a /verificacion");
            redirectAttributes.addFlashAttribute("error", "Debes verificar tu correo electrónico primero");
            return "redirect:/verificacion?tipo=negocio";
        }
        
        model.addAttribute("verifiedEmail", verifiedEmail);
        System.out.println("📧 [REGISTRO NEGOCIO] Email verificado: " + verifiedEmail);
        
        List<Departamento> departamentos = departamentoRepository.findAllActivos();
        System.out.println("🏪 [REGISTRO NEGOCIO] Departamentos cargados: " + departamentos.size());
        departamentos.forEach(d -> System.out.println("   - " + d.getCodigo() + ": " + d.getNombre()));
        model.addAttribute("departamentos", departamentos);
        return "registroNegocio";
    }
    
    /**
     * Página de contacto
     */
    @GetMapping("/contacto")
    public String contacto() {
        System.out.println("📞 [CONTACTO] Mostrando página de contacto");
        return "contacto";
    }
    
    /**
     * Página de tutorial
     */
    @GetMapping("/tutorial")
    public String tutorial() {
        System.out.println("📚 [TUTORIAL] Mostrando página de tutorial");
        return "tutorial";
    }
    
    /**
     * Procesar registro de usuario (PASO 1 - Datos Personales)
     * POST /registro
     * Redirige según el rol seleccionado:
     * - Rol 1 (Cliente): Login directo
     * - Rol 2 (Restaurante): /registro-restaurante
     * - Rol 3 (Repartidor): /registro-repartidor
     */
    @PostMapping("/registro")
    public String registrarUsuario(
            @ModelAttribute UsuarioRegistroDTO dto,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        try {
            System.out.println("📝 [REGISTRO PASO 1] Procesando registro de usuario: " + dto.getCorreoElectronico());
            System.out.println("📝 [REGISTRO PASO 1] Rol seleccionado: " + dto.getCodigoRol());
            
            // Encriptar contraseña
            String contrasenaEncriptada = passwordEncoder.encode(dto.getContrasena());
            
            // Crear usuario en la base de datos
            Long codigoUsuario = usuarioService.crearUsuario(
                dto.getNombre(),
                dto.getApellidoPaterno(),
                dto.getApellidoMaterno(),
                dto.getNumeroDocumento(),
                dto.getFechaNacimiento(),
                dto.getCorreoElectronico(),
                contrasenaEncriptada,
                dto.getTelefono(),
                dto.getDireccion(),
                dto.getCodigoTipoDocumento(),
                dto.getCodigoRol(),
                dto.getCodigoDistrito()
            );
            
            System.out.println("✅ [REGISTRO PASO 1] Usuario creado exitosamente con código: " + codigoUsuario);
            
            // Guardar código de usuario en sesión para el paso 2
            session.setAttribute("usuarioCodigoTemporal", codigoUsuario);
            session.setAttribute("usuarioEmailTemporal", dto.getCorreoElectronico());
            
            // Redirigir según el rol
            if (dto.getCodigoRol() == 2) { // Restaurante
                System.out.println("🏪 [REGISTRO PASO 1] Redirigiendo a registro de restaurante");
                redirectAttributes.addFlashAttribute("mensaje", "Datos personales guardados. Completa los datos del restaurante.");
                return "redirect:/registro-restaurante";
                
            } else if (dto.getCodigoRol() == 3) { // Repartidor
                System.out.println("🚴 [REGISTRO PASO 1] Redirigiendo a registro de repartidor");
                redirectAttributes.addFlashAttribute("mensaje", "Datos personales guardados. Completa los datos del repartidor.");
                return "redirect:/registro-repartidor";
                
            } else { // Cliente (rol 1) u otro
                System.out.println("👤 [REGISTRO PASO 1] Usuario cliente registrado, redirigiendo a login");
                // Limpiar sesión temporal
                session.removeAttribute("usuarioCodigoTemporal");
                session.removeAttribute("usuarioEmailTemporal");
                
                redirectAttributes.addFlashAttribute("message", 
                    "¡Registro exitoso! Ya puedes iniciar sesión con tu cuenta.");
                return "redirect:/login";
            }
            
        } catch (Exception e) {
            System.err.println("❌ [REGISTRO PASO 1] Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
            
            redirectAttributes.addFlashAttribute("error", "Error al registrar: " + e.getMessage());
            return "redirect:/registroNegocio";
        }
    }

    
}
