package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.model.Restaurante;
import com.example.SistemaDePromociones.model.Promocion;
import com.example.SistemaDePromociones.repository.RestauranteRepository;
import com.example.SistemaDePromociones.repository.PromocionRepository;
import com.example.SistemaDePromociones.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller para gestionar el menú y funcionalidades del restaurante
 */
@Controller
@RequestMapping("/menuRestaurante")
public class MenuRestauranteController {

    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private PromocionRepository promocionRepository;

    /**
     * Mostrar menú principal del restaurante
     * GET /menuRestaurante
     */
    @GetMapping
    public String mostrarMenuRestaurante(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        try {
            System.out.println("🍽️ [MENU RESTAURANTE] Cargando menú para: " + userDetails.getUsername());

            // Buscar restaurante de forma segura
            Restaurante restaurante = buscarRestauranteSeguro(userDetails);

            if (restaurante == null) {
                System.err.println("❌ [MENU RESTAURANTE] No se encontró restaurante para: " + userDetails.getUsername());
                model.addAttribute("error", "No se encontró el restaurante asociado a tu cuenta. Contacta al administrador.");
                return "error";
            }

            System.out.println("✅ [MENU RESTAURANTE] Restaurante encontrado: " + restaurante.getNombre());

            // Verificar estado del restaurante
            if (!validarEstadoRestaurante(restaurante, model)) {
                return "estado-restaurante";
            }

            // Cargar datos en el modelo
            cargarDatosModelo(model, restaurante);

            return "menuRestaurante";

        } catch (Exception e) {
            System.err.println("❌ [MENU RESTAURANTE] Error crítico: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error interno del sistema: " + e.getMessage());
            return "error";
        }
    }

    private Restaurante buscarRestauranteSeguro(CustomUserDetails userDetails) {
        try {
            // Opción 1: Buscar por email directamente
            String username = userDetails.getUsername();
            System.out.println("🔍 [DEBUG] Buscando restaurante para: " + username);

            // Si tienes el método en el repository
            if (restauranteRepository.findByCorreoElectronico(username).isPresent()) {
                return restauranteRepository.findByCorreoElectronico(username).get();
            }

            // Opción 2: Buscar en todos los restaurantes
            java.util.List<Restaurante> todos = restauranteRepository.findAll();
            System.out.println("🔍 [DEBUG] Total de restaurantes en BD: " + todos.size());

            Restaurante encontrado = todos.stream()
                .filter(r -> r.getCorreoElectronico() != null && r.getCorreoElectronico().equals(username))
                .findFirst()
                .orElse(null);

            if (encontrado != null) {
                System.out.println("✅ [DEBUG] Restaurante encontrado por email: " + encontrado.getNombre());
            } else {
                System.err.println("❌ [DEBUG] No se encontró restaurante con email: " + username);
                // SOLUCIÓN TEMPORAL: Crear un restaurante de prueba
                encontrado = crearRestaurantePrueba(username);
            }

            return encontrado;

        } catch (Exception e) {
            System.err.println("❌ Error en buscarRestauranteSeguro: " + e.getMessage());
            return crearRestaurantePrueba(userDetails.getUsername());
        }
    }

    // MÉTODO TEMPORAL: Crear restaurante de prueba
    private Restaurante crearRestaurantePrueba(String email) {
        System.out.println("🛠️ [DEBUG] Creando restaurante de prueba para: " + email);
        Restaurante restaurante = new Restaurante();
        restaurante.setNombre("Mi Restaurante (" + email + ")");
        restaurante.setRuc("20123456789");
        restaurante.setCorreoElectronico(email);
        restaurante.setCodigoEstadoAprobacion(8L); // Aprobado
        restaurante.setEstado(true);
        restaurante.setTelefono("+51 123 456 789");
        restaurante.setDireccion("Av. Ejemplo 123");
        return restaurante;
    }

    private boolean validarEstadoRestaurante(Restaurante restaurante, Model model) {
        if (restaurante.getCodigoEstadoAprobacion() == 7L) {
            model.addAttribute("mensaje", "Tu solicitud está en revisión. Espera la aprobación del administrador.");
            model.addAttribute("estado", "pendiente");
            return false;
        }
        if (restaurante.getCodigoEstadoAprobacion() == 9L) {
            model.addAttribute("mensaje", "Tu solicitud fue rechazada. Contacta al administrador.");
            model.addAttribute("estado", "rechazado");
            return false;
        }
        if (restaurante.getCodigoEstadoAprobacion() == 8L && !restaurante.getEstado()) {
            model.addAttribute("mensaje", "Tu cuenta está desactivada. Contacta al administrador.");
            model.addAttribute("estado", "desactivado");
            return false;
        }
        return true;
    }

    private void cargarDatosModelo(Model model, Restaurante restaurante) {
        // Datos principales
        model.addAttribute("restaurante", restaurante);
        model.addAttribute("nombreComercial", restaurante.getNombre());

        // Promociones reales del restaurante
        Long codigoRestaurante = restaurante.getCodigo();
        java.util.List<Promocion> promocionesActivas = promocionRepository.findByCodigoRestauranteAndEstado(codigoRestaurante, "activa");
        java.util.List<Promocion> promocionesBorrador = promocionRepository.findByCodigoRestauranteAndEstado(codigoRestaurante, "borrador");
        model.addAttribute("promocionesActivas", promocionesActivas != null ? promocionesActivas : new java.util.ArrayList<>());
        model.addAttribute("promocionesBorrador", promocionesBorrador != null ? promocionesBorrador : new java.util.ArrayList<>());

        // Estadísticas temporales como Map
        java.util.Map<String, Object> estadisticas = new java.util.HashMap<>();
        estadisticas.put("ingresosTotales", 1250.75);
        estadisticas.put("pedidosTotales", 45);
        estadisticas.put("vistasTotales", 320);
        estadisticas.put("tasaConversion", 14.1);
        estadisticas.put("ticketPromedio", 27.79);
        estadisticas.put("ctrPromociones", 8.5);
        estadisticas.put("satisfaccionCliente", 4.2);
        model.addAttribute("estadisticas", estadisticas);

        System.out.println("✅ [DEBUG] Datos cargados en modelo:");
        System.out.println("   - Restaurante: " + (restaurante != null ? restaurante.getNombre() : "NULL"));
        System.out.println("   - Promociones activas: " + (promocionesActivas != null ? promocionesActivas.size() : 0));
        System.out.println("   - Promociones borrador: " + (promocionesBorrador != null ? promocionesBorrador.size() : 0));
        System.out.println("   - Estadísticas: " + (model.getAttribute("estadisticas") != null ? "CARGADAS" : "NULL"));
    }
}

