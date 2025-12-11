package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.dto.PedidoRequest;
import com.example.SistemaDePromociones.model.CarritoItem;
import com.example.SistemaDePromociones.model.Pedido;
import com.example.SistemaDePromociones.model.DetallePedido;
import com.example.SistemaDePromociones.model.Promocion;
import com.example.SistemaDePromociones.model.ConfiguracionComision;
import com.example.SistemaDePromociones.model.GananciaPlataforma;
import com.example.SistemaDePromociones.repository.PedidoRepository;
import com.example.SistemaDePromociones.repository.DetallePedidoRepository;
import com.example.SistemaDePromociones.repository.PromocionRepository;
import com.example.SistemaDePromociones.repository.ConfiguracionComisionRepository;
import com.example.SistemaDePromociones.repository.GananciaPlataformaRepository;
import com.example.SistemaDePromociones.service.CarritoService;
import com.example.SistemaDePromociones.security.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Controller REST para gestionar carrito y pedidos
 */
@RestController
@RequestMapping("/api/carrito")
public class CarritoController {
    
    @Autowired
    private CarritoService carritoService;
    
    @Autowired
    private PromocionRepository promocionRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private DetallePedidoRepository detallePedidoRepository;
    
    @Autowired
    private ConfiguracionComisionRepository configuracionComisionRepository;
    
    @Autowired
    private GananciaPlataformaRepository gananciaPlataformaRepository;
    
    /**
     * Obtener resumen del carrito
     */
    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> obtenerResumen(HttpSession session) {
        try {
            List<CarritoItem> items = carritoService.obtenerCarrito(session);
            Map<String, Object> totales = carritoService.calcularTotales(session);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("items", items);
            response.put("totales", totales);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al obtener carrito: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Agregar item al carrito
     */
    @PostMapping("/agregar")
    public ResponseEntity<Map<String, Object>> agregarItem(
            HttpSession session,
            @RequestParam Long codigoPromocion,
            @RequestParam(defaultValue = "1") Integer cantidad) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean agregado = carritoService.agregarItem(session, codigoPromocion, cantidad);
            
            if (agregado) {
                Map<String, Object> totales = carritoService.calcularTotales(session);
                response.put("success", true);
                response.put("message", "Promoción agregada al carrito");
                response.put("totales", totales);
            } else {
                response.put("success", false);
                response.put("message", "No se pudo agregar la promoción. Verifica el stock disponible.");
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Actualizar cantidad de un item
     */
    @PutMapping("/actualizar/{codigoPromocion}")
    public ResponseEntity<Map<String, Object>> actualizarCantidad(
            HttpSession session,
            @PathVariable Long codigoPromocion,
            @RequestParam Integer cantidad) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean actualizado = carritoService.actualizarCantidad(session, codigoPromocion, cantidad);
            
            if (actualizado) {
                Map<String, Object> totales = carritoService.calcularTotales(session);
                response.put("success", true);
                response.put("totales", totales);
            } else {
                response.put("success", false);
                response.put("message", "No se pudo actualizar la cantidad");
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Eliminar item del carrito
     */
    @DeleteMapping("/eliminar/{codigoPromocion}")
    public ResponseEntity<Map<String, Object>> eliminarItem(
            HttpSession session,
            @PathVariable Long codigoPromocion) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean eliminado = carritoService.eliminarItem(session, codigoPromocion);
            
            if (eliminado) {
                Map<String, Object> totales = carritoService.calcularTotales(session);
                response.put("success", true);
                response.put("message", "Item eliminado del carrito");
                response.put("totales", totales);
            } else {
                response.put("success", false);
                response.put("message", "No se pudo eliminar el item");
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Crear pedido desde el carrito
     */
    @PostMapping("/crear-pedido")
    public ResponseEntity<Map<String, Object>> crearPedido(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpSession session,
            @RequestBody PedidoRequest pedidoRequest) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<CarritoItem> items = carritoService.obtenerCarrito(session);
            
            if (items.isEmpty()) {
                response.put("success", false);
                response.put("message", "El carrito está vacío");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Validar stock antes de crear el pedido
            for (CarritoItem item : items) {
                Promocion promocion = promocionRepository.findById(item.getCodigoPromocion())
                    .orElseThrow(() -> new RuntimeException("Promoción no encontrada"));
                
                Integer disponible = promocion.getCantidadDisponible();
                if (disponible != null && disponible < item.getCantidad()) {
                    response.put("success", false);
                    response.put("message", "Stock insuficiente para: " + item.getTituloPromocion());
                    return ResponseEntity.badRequest().body(response);
                }
            }
            
            // Calcular totales
            Map<String, Object> totales = carritoService.calcularTotales(session);
            BigDecimal subtotal = BigDecimal.valueOf((Double) totales.get("subtotal"));
            BigDecimal costoDelivery = BigDecimal.valueOf((Double) totales.get("costoDelivery"));
            
            // ============================================
            // 💰 CALCULAR COMISIÓN DE LA PLATAFORMA
            // ============================================
            // Obtener porcentaje de comisión configurado (default 5%)
            BigDecimal porcentajeComision = configuracionComisionRepository
                .findFirstByEstadoTrueOrderByFechaVigenciaDesc()
                .map(ConfiguracionComision::getPorcentajeComision)
                .orElse(new BigDecimal("5.00"));
            
            // Calcular comisión sobre el subtotal (sin incluir delivery)
            BigDecimal comision = subtotal
                .multiply(porcentajeComision)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            
            // Calcular monto que recibe el restaurante
            BigDecimal montoRestaurante = subtotal.subtract(comision);
            
            BigDecimal total = subtotal.add(costoDelivery);
            
            System.out.println("💰 [COMISIÓN] Subtotal: S/ " + subtotal);
            System.out.println("💰 [COMISIÓN] Comisión FooDIx (" + porcentajeComision + "%): S/ " + comision);
            System.out.println("💰 [COMISIÓN] Monto Restaurante: S/ " + montoRestaurante);
            System.out.println("💰 [COMISIÓN] Total pedido: S/ " + total);
            
            // Crear pedido
            Pedido pedido = new Pedido();
            pedido.setCodigoUsuario(userDetails.getUsuario().getCodigo());
            pedido.setCodigoRestaurante(items.get(0).getCodigoRestaurante()); // Todos del mismo restaurante
            // pedido.setNumeroPedido(generarNumeroPedido()); // Campo no existe en BD
            pedido.setCodigoVerificacion(generarCodigoVerificacion());
            pedido.setSubtotal(subtotal);
            pedido.setDescuento(BigDecimal.ZERO); // Sin descuento por ahora
            pedido.setCostoDelivery(costoDelivery);
            pedido.setTotal(total);
            
            // ⚡ NUEVOS CAMPOS DE COMISIÓN
            pedido.setComisionPlataforma(comision);
            pedido.setPorcentajeComision(porcentajeComision);
            pedido.setMontoRestaurante(montoRestaurante);
            
            pedido.setCodigoMetodoPago(obtenerCodigoMetodoPago(pedidoRequest.getMetodoPago()));
            pedido.setDireccionEntrega(pedidoRequest.getDireccionEntrega());
            pedido.setReferenciaDireccion(pedidoRequest.getReferencia());
            pedido.setTelefonoContacto(pedidoRequest.getTelefono());
            pedido.setNotas(pedidoRequest.getNotas());
            pedido.setFechaPedido(LocalDateTime.now());
            pedido.setCodigoEstadoPedido(1L); // Pendiente
            pedido.setEstado(true); // Activo
            pedido.setVerificado(false);
            
            Pedido pedidoGuardado = pedidoRepository.save(pedido);
            
            // ============================================
            // 📊 REGISTRAR GANANCIA DE LA PLATAFORMA
            // ============================================
            GananciaPlataforma ganancia = new GananciaPlataforma();
            ganancia.setCodigoPedido(pedidoGuardado.getCodigo());
            ganancia.setSubtotal(subtotal);
            ganancia.setComision(comision);
            ganancia.setPorcentajeAplicado(porcentajeComision);
            ganancia.setMontoRestaurante(montoRestaurante);
            ganancia.setFechaRegistro(LocalDateTime.now());
            
            gananciaPlataformaRepository.save(ganancia);
            
            System.out.println("✅ [GANANCIA] Registrada para pedido #" + pedidoGuardado.getCodigo());
            
            // Crear detalles del pedido y actualizar stock
            for (CarritoItem item : items) {
                DetallePedido detalle = new DetallePedido();
                detalle.setCodigoPedido(pedidoGuardado.getCodigo());
                detalle.setCodigoPromocion(item.getCodigoPromocion());
                detalle.setCantidad(item.getCantidad());
                detalle.setPrecioUnitario(BigDecimal.valueOf(item.getPrecioUnitario()));
                detalle.setPrecioTotal(BigDecimal.valueOf(item.getSubtotal()));
                
                detallePedidoRepository.save(detalle);
                
                // Actualizar stock de la promoción
                Promocion promocion = promocionRepository.findById(item.getCodigoPromocion()).get();
                if (promocion.getCantidadDisponible() != null) {
                    promocion.setCantidadDisponible(promocion.getCantidadDisponible() - item.getCantidad());
                }
                if (promocion.getCantidadVendida() == null) {
                    promocion.setCantidadVendida(item.getCantidad());
                } else {
                    promocion.setCantidadVendida(promocion.getCantidadVendida() + item.getCantidad());
                }
                promocion.setContadorPedidos((promocion.getContadorPedidos() != null ? promocion.getContadorPedidos() : 0) + 1);
                
                promocionRepository.save(promocion);
            }
            
            // Limpiar carrito
            carritoService.limpiarCarrito(session);
            
            // Respuesta exitosa
            response.put("success", true);
            response.put("message", "Pedido creado exitosamente");
            response.put("codigoPedido", pedidoGuardado.getCodigo());
            response.put("codigoVerificacion", pedidoGuardado.getCodigoVerificacion());
            
            System.out.println("✅ [PEDIDO] Pedido creado #" + pedidoGuardado.getCodigo() + 
                             " | Código verificación: " + pedidoGuardado.getCodigoVerificacion());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al crear pedido: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Generar número de pedido único
     */
    // private String generarNumeroPedido() {
    //     return "PED-" + System.currentTimeMillis();
    // }
    
    /**
     * Generar código de verificación de 4 dígitos
     */
    private String generarCodigoVerificacion() {
        Random random = new Random();
        int codigo = 1000 + random.nextInt(9000); // Genera número entre 1000 y 9999
        return String.valueOf(codigo);
    }
    
    /**
     * Obtener historial de pedidos del usuario
     */
    @GetMapping("/historial")
    public ResponseEntity<List<Map<String, Object>>> obtenerHistorial(@AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Long codigoUsuario = userDetails.getUsuario().getCodigo();
            List<Pedido> pedidos = pedidoRepository.findByCodigoUsuarioOrderByFechaPedidoDesc(codigoUsuario);
            
            List<Map<String, Object>> historial = pedidos.stream().map(pedido -> {
                Map<String, Object> pedidoMap = new HashMap<>();
                pedidoMap.put("codigo", pedido.getCodigo());
                pedidoMap.put("codigoVerificacion", pedido.getCodigoVerificacion());
                pedidoMap.put("fechaPedido", pedido.getFechaPedido());
                pedidoMap.put("total", pedido.getTotal());
                pedidoMap.put("estado", pedido.getEstado());
                pedidoMap.put("direccionEntrega", pedido.getDireccionEntrega());
                pedidoMap.put("telefonoContacto", pedido.getTelefonoContacto());
                pedidoMap.put("verificado", pedido.getVerificado());
                
                // Obtener detalles del pedido
                List<DetallePedido> detalles = detallePedidoRepository.findByCodigoPedido(pedido.getCodigo());
                List<Map<String, Object>> items = detalles.stream().map(detalle -> {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("cantidad", detalle.getCantidad());
                    itemMap.put("precioUnitario", detalle.getPrecioUnitario());
                    itemMap.put("precioTotal", detalle.getPrecioTotal());
                    
                    // Obtener nombre de la promoción
                    promocionRepository.findById(detalle.getCodigoPromocion()).ifPresent(promo -> {
                        itemMap.put("nombrePromocion", promo.getTitulo());
                    });
                    
                    return itemMap;
                }).toList();
                
                pedidoMap.put("items", items);
                
                return pedidoMap;
            }).toList();
            
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Convertir nombre de método de pago a código
     */
    private Long obtenerCodigoMetodoPago(String metodoPago) {
        if (metodoPago == null) return 1L; // Efectivo por defecto
        
        return switch (metodoPago.toLowerCase()) {
            case "efectivo" -> 1L;
            case "tarjeta" -> 2L;
            case "yape" -> 3L;
            case "plin" -> 4L;
            default -> 1L; // Efectivo por defecto
        };
    }
}
