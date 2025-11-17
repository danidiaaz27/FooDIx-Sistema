package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.model.Departamento;
import com.example.SistemaDePromociones.repository.jdbc.DepartamentoJdbcRepository;
import com.example.SistemaDePromociones.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
     * Página de selección de tipo de registro
     */
    @GetMapping("/registro")
    public String registro(Model model, HttpSession session) {
        System.out.println("👤 [REGISTRO USUARIO] Cargando formulario de registro");
        
        // Obtener email verificado de la sesión
        String verifiedEmail = (String) session.getAttribute("verifiedEmail");
        if (verifiedEmail != null) {
            model.addAttribute("verifiedEmail", verifiedEmail);
            System.out.println("📧 [REGISTRO] Email verificado encontrado: " + verifiedEmail);
        }
        
        List<Departamento> departamentos = departamentoRepository.findAllActivos();
        System.out.println("👤 [REGISTRO USUARIO] Departamentos cargados: " + departamentos.size());
        departamentos.forEach(d -> System.out.println("   - " + d.getCodigo() + ": " + d.getNombre()));
        model.addAttribute("departamentos", departamentos);
        return "registro";
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
     * Procesar registro de usuario
     * POST /registro
     */
    @PostMapping("/registro")
    public String registrarUsuario(
            @RequestParam String Nombre,
            @RequestParam String ApellidoPaterno,
            @RequestParam String ApellidoMaterno,
            @RequestParam String NumeroDocumento,
            @RequestParam LocalDate FechaNacimiento,
            @RequestParam String CorreoElectronico,
            @RequestParam String Contrasena,
            @RequestParam(required = false) String Telefono,
            @RequestParam(required = false) String Direccion,
            @RequestParam Integer CodigoTipoDocumento,
            @RequestParam Integer CodigoRol,
            @RequestParam Long CodigoDistrito,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        try {
            System.out.println("📝 [REGISTRO] Procesando registro de usuario: " + CorreoElectronico);
            
            // Encriptar contraseña
            String contrasenaEncriptada = passwordEncoder.encode(Contrasena);
            
            // Crear usuario
            Long codigoUsuario = usuarioService.crearUsuario(
                Nombre,
                ApellidoPaterno,
                ApellidoMaterno,
                NumeroDocumento,
                FechaNacimiento,
                CorreoElectronico,
                contrasenaEncriptada,
                Telefono,
                Direccion,
                CodigoTipoDocumento,
                CodigoRol,
                CodigoDistrito
            );
            
            System.out.println("✅ [REGISTRO] Usuario creado exitosamente con código: " + codigoUsuario);
            
            redirectAttributes.addFlashAttribute("message", 
                "¡Registro exitoso! Ya puedes iniciar sesión con tu cuenta.");
            
            return "redirect:/login";
            
        } catch (Exception e) {
            System.err.println("❌ [REGISTRO] Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
            
            // Recargar departamentos
            List<Departamento> departamentos = departamentoRepository.findAllActivos();
            model.addAttribute("departamentos", departamentos);
            model.addAttribute("error", "Error al registrar: " + e.getMessage());
            
            return "registro";
        }
    }

    
}
