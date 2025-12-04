package com.example.SistemaDePromociones.service;

import com.example.SistemaDePromociones.model.CarritoItem;
import com.example.SistemaDePromociones.model.Promocion;
import com.example.SistemaDePromociones.repository.PromocionRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar el carrito de compras en sesión
 */
@Service
public class CarritoService {
    
    private static final String CARRITO_SESSION_KEY = "carrito";
    
    @Autowired
    private PromocionRepository promocionRepository;
    
    /**
     * Obtener items del carrito
     */
    public List<CarritoItem> obtenerCarrito(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute(CARRITO_SESSION_KEY);
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute(CARRITO_SESSION_KEY, carrito);
            System.out.println("🛒 [CARRITO] Nuevo carrito creado");
        } else {
            System.out.println("🛒 [CARRITO] Carrito existente con " + carrito.size() + " items");
        }
        return carrito;
    }
    
    /**
     * Agregar item al carrito
     */
    public boolean agregarItem(HttpSession session, Long codigoPromocion, Integer cantidad) {
        try {
            System.out.println("🛒 [CARRITO] Agregando promoción: " + codigoPromocion + " cantidad: " + cantidad);
            
            // Verificar que la promoción existe y está activa
            Optional<Promocion> promocionOpt = promocionRepository.findById(codigoPromocion);
            if (promocionOpt.isEmpty()) {
                System.out.println("❌ [CARRITO] Promoción no encontrada");
                return false;
            }
            
            Promocion promocion = promocionOpt.get();
            
            if (!"activa".equals(promocion.getEstado())) {
                System.out.println("❌ [CARRITO] Promoción no está activa: " + promocion.getEstado());
                return false;
            }
            
            // Verificar stock disponible
            Integer disponible = promocion.getCantidadDisponible();
            System.out.println("📦 [CARRITO] Stock disponible: " + disponible);
            
            if (disponible != null && disponible < cantidad) {
                System.out.println("❌ [CARRITO] Stock insuficiente");
                return false; // No hay suficiente stock
            }
            
            List<CarritoItem> carrito = obtenerCarrito(session);
            System.out.println("🛒 [CARRITO] Items actuales en carrito: " + carrito.size());
            
            // Verificar si ya existe en el carrito
            Optional<CarritoItem> itemExistente = carrito.stream()
                .filter(item -> item.getCodigoPromocion().equals(codigoPromocion))
                .findFirst();
            
            if (itemExistente.isPresent()) {
                // Actualizar cantidad
                CarritoItem item = itemExistente.get();
                int nuevaCantidad = item.getCantidad() + cantidad;
                
                // Verificar stock para la nueva cantidad
                if (disponible != null && disponible < nuevaCantidad) {
                    System.out.println("❌ [CARRITO] Stock insuficiente para nueva cantidad");
                    return false;
                }
                
                item.setCantidad(nuevaCantidad);
                System.out.println("✅ [CARRITO] Cantidad actualizada a: " + nuevaCantidad);
            } else {
                // Obtener nombre del restaurante
                String nombreRestaurante = "Restaurante";
                try {
                    if (promocion.getCodigoRestaurante() != null) {
                        nombreRestaurante = promocionRepository.findById(codigoPromocion)
                            .map(p -> p.getRestaurante() != null ? p.getRestaurante().getNombre() : "Restaurante")
                            .orElse("Restaurante");
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ [CARRITO] No se pudo obtener nombre del restaurante");
                }
                
                // Crear nuevo item
                CarritoItem nuevoItem = new CarritoItem(
                    promocion.getCodigo(),
                    promocion.getTitulo(),
                    nombreRestaurante,
                    promocion.getCodigoRestaurante(),
                    promocion.getPrecioPromocional(),
                    cantidad
                );
                carrito.add(nuevoItem);
                System.out.println("✅ [CARRITO] Nuevo item agregado: " + promocion.getTitulo());
            }
            
            session.setAttribute(CARRITO_SESSION_KEY, carrito);
            System.out.println("✅ [CARRITO] Total items en carrito: " + carrito.size());
            return true;
            
        } catch (Exception e) {
            System.out.println("❌ [CARRITO] Error al agregar item: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Actualizar cantidad de un item
     */
    public boolean actualizarCantidad(HttpSession session, Long codigoPromocion, Integer nuevaCantidad) {
        List<CarritoItem> carrito = obtenerCarrito(session);
        
        Optional<CarritoItem> itemOpt = carrito.stream()
            .filter(item -> item.getCodigoPromocion().equals(codigoPromocion))
            .findFirst();
        
        if (itemOpt.isPresent()) {
            if (nuevaCantidad <= 0) {
                carrito.remove(itemOpt.get());
            } else {
                // Verificar stock
                Optional<Promocion> promocionOpt = promocionRepository.findById(codigoPromocion);
                if (promocionOpt.isPresent()) {
                    Integer disponible = promocionOpt.get().getCantidadDisponible();
                    if (disponible != null && disponible < nuevaCantidad) {
                        return false;
                    }
                }
                itemOpt.get().setCantidad(nuevaCantidad);
            }
            session.setAttribute(CARRITO_SESSION_KEY, carrito);
            return true;
        }
        return false;
    }
    
    /**
     * Eliminar item del carrito
     */
    public boolean eliminarItem(HttpSession session, Long codigoPromocion) {
        List<CarritoItem> carrito = obtenerCarrito(session);
        boolean eliminado = carrito.removeIf(item -> item.getCodigoPromocion().equals(codigoPromocion));
        if (eliminado) {
            session.setAttribute(CARRITO_SESSION_KEY, carrito);
        }
        return eliminado;
    }
    
    /**
     * Limpiar carrito
     */
    public void limpiarCarrito(HttpSession session) {
        session.removeAttribute(CARRITO_SESSION_KEY);
    }
    
    /**
     * Calcular totales del carrito
     */
    public java.util.Map<String, Object> calcularTotales(HttpSession session) {
        List<CarritoItem> carrito = obtenerCarrito(session);
        
        double subtotal = carrito.stream()
            .mapToDouble(CarritoItem::getSubtotal)
            .sum();
        
        // Calcular delivery (S/ 5.90 si hay items con delivery)
        boolean tieneDelivery = carrito.stream()
            .anyMatch(item -> "DELIVERY".equals(item.getTipoEntrega()));
        double costoDelivery = tieneDelivery ? 5.90 : 0.0;
        
        double total = subtotal + costoDelivery;
        int itemCount = carrito.stream().mapToInt(CarritoItem::getCantidad).sum();
        
        java.util.Map<String, Object> totales = new java.util.HashMap<>();
        totales.put("subtotal", subtotal);
        totales.put("costoDelivery", costoDelivery);
        totales.put("total", total);
        totales.put("itemCount", itemCount);
        
        return totales;
    }
}
