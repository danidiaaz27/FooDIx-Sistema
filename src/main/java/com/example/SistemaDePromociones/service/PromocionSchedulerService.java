package com.example.SistemaDePromociones.service;

import com.example.SistemaDePromociones.model.Promocion;
import com.example.SistemaDePromociones.repository.PromocionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para gestionar automáticamente el ciclo de vida de las promociones
 * - Desactivar promociones expiradas
 * - Activar promociones cuya fecha de inicio ya llegó
 */
@Service
public class PromocionSchedulerService {
    
    @Autowired
    private PromocionRepository promocionRepository;
    
    /**
     * Tarea programada que se ejecuta cada 10 minutos
     * Verifica promociones expiradas y las marca como borrador
     */
    @Scheduled(fixedRate = 600000) // 10 minutos = 600,000 ms
    @Transactional
    public void verificarPromocionesExpiradas() {
        try {
            System.out.println("🕐 [SCHEDULER] Verificando promociones expiradas...");
            
            // Obtener todas las promociones activas
            List<Promocion> promocionesActivas = promocionRepository.findByEstadoOrderByFechaCreacionDesc("activa");
            
            int promocionesExpiradas = 0;
            Timestamp ahora = Timestamp.valueOf(LocalDateTime.now());
            
            for (Promocion promocion : promocionesActivas) {
                // Verificar si la promoción ya expiró
                if (promocion.getFechaFin() != null && promocion.getFechaFin().before(ahora)) {
                    System.out.println("⏰ [SCHEDULER] Promoción expirada detectada: " + promocion.getTitulo() + " (ID: " + promocion.getCodigo() + ")");
                    System.out.println("   └─ Fecha fin: " + promocion.getFechaFin() + " | Ahora: " + ahora);
                    
                    // Cambiar estado a borrador
                    promocion.setEstado("borrador");
                    promocion.setFechaModificacion(ahora);
                    promocionRepository.save(promocion);
                    
                    promocionesExpiradas++;
                    System.out.println("   └─ ✅ Estado cambiado a 'borrador'");
                }
            }
            
            if (promocionesExpiradas > 0) {
                System.out.println("✅ [SCHEDULER] " + promocionesExpiradas + " promoción(es) expirada(s) movida(s) a borrador");
            } else {
                System.out.println("✅ [SCHEDULER] No hay promociones expiradas en este momento");
            }
            
        } catch (Exception e) {
            System.err.println("❌ [SCHEDULER] Error al verificar promociones expiradas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Tarea programada que se ejecuta cada hora
     * Verifica promociones en borrador cuya fecha de inicio ya llegó
     * y las activa automáticamente
     */
    @Scheduled(fixedRate = 3600000) // 1 hora = 3,600,000 ms
    @Transactional
    public void activarPromocionesProgramadas() {
        try {
            System.out.println("🕐 [SCHEDULER] Verificando promociones programadas para activar...");
            
            // Obtener todas las promociones en borrador
            List<Promocion> promocionesBorrador = promocionRepository.findByEstadoOrderByFechaCreacionDesc("borrador");
            
            int promocionesActivadas = 0;
            Timestamp ahora = Timestamp.valueOf(LocalDateTime.now());
            
            for (Promocion promocion : promocionesBorrador) {
                // Verificar si la fecha de inicio ya llegó y la fecha fin aún no pasó
                boolean inicioValido = promocion.getFechaInicio() == null || !promocion.getFechaInicio().after(ahora);
                boolean finValido = promocion.getFechaFin() == null || promocion.getFechaFin().after(ahora);
                
                if (inicioValido && finValido) {
                    System.out.println("📅 [SCHEDULER] Promoción programada lista para activar: " + promocion.getTitulo() + " (ID: " + promocion.getCodigo() + ")");
                    
                    // Cambiar estado a activa
                    promocion.setEstado("activa");
                    promocion.setFechaModificacion(ahora);
                    promocionRepository.save(promocion);
                    
                    promocionesActivadas++;
                    System.out.println("   └─ ✅ Estado cambiado a 'activa'");
                }
            }
            
            if (promocionesActivadas > 0) {
                System.out.println("✅ [SCHEDULER] " + promocionesActivadas + " promoción(es) activada(s) automáticamente");
            } else {
                System.out.println("✅ [SCHEDULER] No hay promociones programadas para activar en este momento");
            }
            
        } catch (Exception e) {
            System.err.println("❌ [SCHEDULER] Error al activar promociones programadas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Método manual para forzar la verificación inmediata
     * Útil para pruebas o ejecución bajo demanda
     */
    @Transactional
    public void verificarAhora() {
        System.out.println("🔄 [SCHEDULER] Verificación manual iniciada");
        verificarPromocionesExpiradas();
        activarPromocionesProgramadas();
    }
}
