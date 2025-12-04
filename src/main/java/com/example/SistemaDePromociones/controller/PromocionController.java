package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.model.Promocion;
import com.example.SistemaDePromociones.model.Restaurante;
import com.example.SistemaDePromociones.repository.PromocionRepository;
import com.example.SistemaDePromociones.repository.RestauranteRepository;
import com.example.SistemaDePromociones.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Controlador para gestionar promociones de restaurantes
 */
@Controller
@RequestMapping("/promociones")
public class PromocionController {

    @Autowired
    private PromocionRepository promocionRepository;
    
    @Autowired
    private RestauranteRepository restauranteRepository;

    /**
     * Crear nueva promoción
     * POST /promociones/crear
     */
    @PostMapping("/crear")
    public String crearPromocion(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam(value = "plato", required = false) Long codigoPlato,
            @RequestParam(value = "unidadMedida", required = false) Long codigoUnidadMedida,
            @RequestParam(value = "cantidad", required = false, defaultValue = "1") Integer cantidad,
            @RequestParam("precioOriginal") Double precioOriginal,
            @RequestParam("precioPromocional") Double precioPromocional,
            @RequestParam(value = "categoriaPromocion", required = false) String categoriaPromocion,
            @RequestParam(value = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(value = "fechaFin", required = false) String fechaFin,
            @RequestParam(value = "publicarInmediatamente", required = false) String publicarInmediatamente,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🍔 [PROMOCION] Creando nueva promoción: " + titulo);
            
            // Buscar restaurante del usuario autenticado
            Restaurante restaurante = restauranteRepository.findByCorreoElectronico(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));
            
            // Determinar estado inicial
            String estadoInicial = "on".equals(publicarInmediatamente) ? "activa" : "borrador";
            
            // Crear la promoción
            Promocion promocion = new Promocion();
            promocion.setTitulo(titulo);
            promocion.setDescripcion(descripcion);
            promocion.setCodigoRestaurante(restaurante.getCodigo());
            
            // Vincular con plato y unidad de medida del menú (si se seleccionaron)
            if (codigoPlato != null) {
                promocion.setCodigoPlato(codigoPlato);
            }
            if (codigoUnidadMedida != null) {
                promocion.setCodigoUnidadMedida(codigoUnidadMedida);
            }
            
            promocion.setPrecioOriginal(precioOriginal);
            promocion.setPrecioPromocional(precioPromocional);
            promocion.setCategoriaPromocion(categoriaPromocion);
            promocion.setEstado(estadoInicial);
            promocion.setContadorVistas(0);
            promocion.setContadorPedidos(0);
            promocion.setIngresosTotales(0.0);
            promocion.setFechaCreacion(Timestamp.valueOf(LocalDateTime.now()));
            
            // Calcular descuento
            if (precioOriginal != null && precioPromocional != null && precioOriginal > 0) {
                double descuento = ((precioOriginal - precioPromocional) / precioOriginal) * 100;
                promocion.setTipoDescuento("porcentaje");
                promocion.setValorDescuento(descuento);
            }
            
            // Fechas opcionales
            if (fechaInicio != null && !fechaInicio.isEmpty()) {
                promocion.setFechaInicio(Timestamp.valueOf(fechaInicio.replace("T", " ") + ":00"));
            }
            if (fechaFin != null && !fechaFin.isEmpty()) {
                promocion.setFechaFin(Timestamp.valueOf(fechaFin.replace("T", " ") + ":00"));
            }
            
            promocionRepository.save(promocion);
            
            String mensajeExito = "activa".equals(estadoInicial) 
                ? "Promoción creada y publicada exitosamente" 
                : "Promoción creada como borrador";
            redirectAttributes.addFlashAttribute("mensaje", mensajeExito);
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [PROMOCION] Promoción creada: " + promocion.getCodigo() + " (Estado: " + estadoInicial + ")");
            
        } catch (Exception e) {
            System.err.println("❌ [PROMOCION] Error al crear promoción: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al crear la promoción: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuRestaurante";
    }

    /**
     * Publicar promoción (cambiar de borrador a activa)
     * POST /promociones/{id}/publicar
     */
    @PostMapping("/{id}/publicar")
    public String publicarPromocion(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("📢 [PROMOCION] Publicando promoción: " + id);
            
            Promocion promocion = promocionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Promoción no encontrada"));
            
            // Verificar que pertenece al restaurante del usuario
            Restaurante restaurante = restauranteRepository.findByCorreoElectronico(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));
            
            if (!promocion.getCodigoRestaurante().equals(restaurante.getCodigo())) {
                throw new RuntimeException("No tienes permiso para publicar esta promoción");
            }
            
            // Cambiar estado a activa
            promocion.setEstado("activa");
            promocion.setFechaModificacion(Timestamp.valueOf(LocalDateTime.now()));
            
            // Si no tiene fecha de inicio, usar la actual
            if (promocion.getFechaInicio() == null) {
                promocion.setFechaInicio(Timestamp.valueOf(LocalDateTime.now()));
            }
            
            promocionRepository.save(promocion);
            
            redirectAttributes.addFlashAttribute("mensaje", "Promoción publicada exitosamente. Ahora es visible para todos los usuarios.");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [PROMOCION] Promoción publicada: " + id);
            
        } catch (Exception e) {
            System.err.println("❌ [PROMOCION] Error al publicar promoción: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al publicar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuRestaurante";
    }

    /**
     * Despublicar promoción (cambiar de activa a borrador)
     * POST /promociones/{id}/despublicar
     */
    @PostMapping("/{id}/despublicar")
    public String despublicarPromocion(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("📴 [PROMOCION] Despublicando promoción: " + id);
            
            Promocion promocion = promocionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Promoción no encontrada"));
            
            // Verificar que pertenece al restaurante del usuario
            Restaurante restaurante = restauranteRepository.findByCorreoElectronico(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));
            
            if (!promocion.getCodigoRestaurante().equals(restaurante.getCodigo())) {
                throw new RuntimeException("No tienes permiso para modificar esta promoción");
            }
            
            // Cambiar estado a borrador
            promocion.setEstado("borrador");
            promocion.setFechaModificacion(Timestamp.valueOf(LocalDateTime.now()));
            
            promocionRepository.save(promocion);
            
            redirectAttributes.addFlashAttribute("mensaje", "Promoción despublicada. Ya no es visible para los usuarios.");
            redirectAttributes.addFlashAttribute("tipo", "warning");
            
            System.out.println("✅ [PROMOCION] Promoción despublicada: " + id);
            
        } catch (Exception e) {
            System.err.println("❌ [PROMOCION] Error al despublicar promoción: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al despublicar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuRestaurante";
    }

    /**
     * Eliminar promoción
     * POST /promociones/{id}/eliminar
     */
    @PostMapping("/{id}/eliminar")
    public String eliminarPromocion(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🗑️ [PROMOCION] Eliminando promoción: " + id);
            
            Promocion promocion = promocionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Promoción no encontrada"));
            
            // Verificar que pertenece al restaurante del usuario
            Restaurante restaurante = restauranteRepository.findByCorreoElectronico(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));
            
            if (!promocion.getCodigoRestaurante().equals(restaurante.getCodigo())) {
                throw new RuntimeException("No tienes permiso para eliminar esta promoción");
            }
            
            promocionRepository.deleteById(id);
            
            redirectAttributes.addFlashAttribute("mensaje", "Promoción eliminada exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [PROMOCION] Promoción eliminada: " + id);
            
        } catch (Exception e) {
            System.err.println("❌ [PROMOCION] Error al eliminar promoción: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuRestaurante";
    }

    /**
     * Editar promoción
     * POST /promociones/{id}/editar
     */
    @PostMapping("/{id}/editar")
    public String editarPromocion(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precioOriginal") Double precioOriginal,
            @RequestParam("precioPromocional") Double precioPromocional,
            @RequestParam(value = "categoriaPromocion", required = false) String categoriaPromocion,
            @RequestParam(value = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(value = "fechaFin", required = false) String fechaFin,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("✏️ [PROMOCION] Editando promoción: " + id);
            
            Promocion promocion = promocionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Promoción no encontrada"));
            
            // Verificar que pertenece al restaurante del usuario
            Restaurante restaurante = restauranteRepository.findByCorreoElectronico(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));
            
            if (!promocion.getCodigoRestaurante().equals(restaurante.getCodigo())) {
                throw new RuntimeException("No tienes permiso para editar esta promoción");
            }
            
            // Actualizar datos
            promocion.setTitulo(titulo);
            promocion.setDescripcion(descripcion);
            promocion.setPrecioOriginal(precioOriginal);
            promocion.setPrecioPromocional(precioPromocional);
            promocion.setCategoriaPromocion(categoriaPromocion);
            promocion.setFechaModificacion(Timestamp.valueOf(LocalDateTime.now()));
            
            // Recalcular descuento
            if (precioOriginal != null && precioPromocional != null && precioOriginal > 0) {
                double descuento = ((precioOriginal - precioPromocional) / precioOriginal) * 100;
                promocion.setValorDescuento(descuento);
            }
            
            // Actualizar fechas
            if (fechaInicio != null && !fechaInicio.isEmpty()) {
                promocion.setFechaInicio(Timestamp.valueOf(fechaInicio.replace("T", " ") + ":00"));
            }
            if (fechaFin != null && !fechaFin.isEmpty()) {
                promocion.setFechaFin(Timestamp.valueOf(fechaFin.replace("T", " ") + ":00"));
            }
            
            promocionRepository.save(promocion);
            
            redirectAttributes.addFlashAttribute("mensaje", "Promoción actualizada exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [PROMOCION] Promoción actualizada: " + id);
            
        } catch (Exception e) {
            System.err.println("❌ [PROMOCION] Error al editar promoción: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al editar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuRestaurante";
    }
    
    /**
     * API REST: Obtener detalles de una promoción por código
     * GET /api/promociones/{codigo}
     */
    @GetMapping("/api/promociones/{codigo}")
    @ResponseBody
    public java.util.Map<String, Object> obtenerPromocionPorCodigo(@PathVariable Long codigo) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        
        try {
            Promocion promocion = promocionRepository.findById(codigo)
                    .orElseThrow(() -> new RuntimeException("Promoción no encontrada"));
            
            // Crear objeto con los datos necesarios para el frontend
            java.util.Map<String, Object> promocionData = new java.util.HashMap<>();
            promocionData.put("codigo", promocion.getCodigo());
            promocionData.put("titulo", promocion.getTitulo());
            promocionData.put("descripcion", promocion.getDescripcion());
            promocionData.put("categoriaPromocion", promocion.getCategoriaPromocion());
            promocionData.put("precioOriginal", promocion.getPrecioOriginal());
            promocionData.put("precioPromocional", promocion.getPrecioPromocional());
            promocionData.put("fechaInicio", promocion.getFechaInicio());
            promocionData.put("fechaFin", promocion.getFechaFin());
            promocionData.put("estado", promocion.getEstado());
            promocionData.put("contadorVistas", promocion.getContadorVistas());
            promocionData.put("contadorPedidos", promocion.getContadorPedidos());
            
            // Obtener información del restaurante
            if (promocion.getCodigoRestaurante() != null) {
                restauranteRepository.findById(promocion.getCodigoRestaurante()).ifPresent(restaurante -> {
                    promocionData.put("nombreRestaurante", restaurante.getNombre());
                    promocionData.put("direccionRestaurante", restaurante.getDireccion());
                    promocionData.put("telefonoRestaurante", restaurante.getTelefono());
                });
            }
            
            response.put("success", true);
            response.put("promocion", promocionData);
            
            System.out.println("✅ [API] Promoción " + codigo + " recuperada exitosamente");
            
        } catch (Exception e) {
            System.err.println("❌ [API] Error al obtener promoción: " + e.getMessage());
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        
        return response;
    }
}
