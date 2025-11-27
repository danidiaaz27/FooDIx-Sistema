package com.example.SistemaDePromociones.service;

import com.example.SistemaDePromociones.dto.RestauranteRegistroDTO;
import com.example.SistemaDePromociones.repository.jdbc.RestauranteJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Servicio para gestionar restaurantes - USANDO JDBC
 */
@Service
public class RestauranteService {
    
    @Autowired
    private RestauranteJdbcRepository restauranteRepository;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    /**
     * Registrar un nuevo restaurante usando SQL directo (PASO 2)
     * El usuario ya debe existir (creado en PASO 1)
     */
    @Transactional
    public Long registrarRestaurante(RestauranteRegistroDTO dto, Long codigoUsuario) throws Exception {
        
        // Validar que el usuario existe
        if (codigoUsuario == null) {
            throw new RuntimeException("El código de usuario es requerido");
        }
        
        // Validar que no exista el RUC
        if (restauranteRepository.existsByRuc(dto.getRuc())) {
            throw new RuntimeException("El RUC ya está registrado");
        }
        
        System.out.println("🏪 [SERVICE] Registrando restaurante para usuario: " + codigoUsuario);
        
        // Crear restaurante usando JDBC vinculándolo al usuario existente
        Long codigoRestaurante = restauranteRepository.insertRestaurante(
            codigoUsuario,
            dto.getRuc(),
            dto.getRazonSocial(),
            dto.getNombreComercial(),
            dto.getDescripcion(),
            dto.getDireccionNegocio(),
            dto.getTelefonoNegocio(),
            dto.getCorreoNegocio(),
            dto.getCodigoDistritoNegocio()
        );
        
        System.out.println("✅ [SERVICE] Restaurante registrado con código: " + codigoRestaurante);
        
        // Guardar documentos del restaurante
        guardarDocumentosRestaurante(codigoRestaurante, dto);
        
        return codigoRestaurante;
    }
    
    /**
     * Guardar documentos del restaurante (CARTA, Licencia, Sanidad)
     */
    private void guardarDocumentosRestaurante(Long codigoRestaurante, RestauranteRegistroDTO dto) {
        System.out.println("📄 [SERVICE] Guardando documentos del restaurante: " + codigoRestaurante);
        
        try {
            // 1. Carta del Restaurante
            if (dto.getCartaRestaurante() != null && !dto.getCartaRestaurante().isEmpty()) {
                String rutaCarta = fileStorageService.guardarArchivoRestaurante(
                    dto.getCartaRestaurante(), 
                    codigoRestaurante,
                    "CARTA_RESTAURANTE"
                );
                if (rutaCarta != null) {
                    restauranteRepository.insertDocumento(
                        codigoRestaurante,
                        "CARTA_RESTAURANTE",
                        rutaCarta
                    );
                    System.out.println("   ✓ Carta guardada: " + rutaCarta);
                }
            }
            
            // 2. Licencia de Funcionamiento
            if (dto.getLicenciaFuncionamiento() != null && !dto.getLicenciaFuncionamiento().isEmpty()) {
                String rutaLicencia = fileStorageService.guardarArchivoRestaurante(
                    dto.getLicenciaFuncionamiento(), 
                    codigoRestaurante,
                    "LicenciaFuncionamiento"
                );
                if (rutaLicencia != null) {
                    restauranteRepository.insertDocumento(
                        codigoRestaurante,
                        "LicenciaFuncionamiento",
                        rutaLicencia
                    );
                    System.out.println("   ✓ Licencia guardada: " + rutaLicencia);
                }
            }
            
            // 3. Carnet de Sanidad
            if (dto.getCarnetSanidad() != null && !dto.getCarnetSanidad().isEmpty()) {
                String rutaSanidad = fileStorageService.guardarArchivoRestaurante(
                    dto.getCarnetSanidad(), 
                    codigoRestaurante,
                    "CarnetSanidad"
                );
                if (rutaSanidad != null) {
                    restauranteRepository.insertDocumento(
                        codigoRestaurante,
                        "CarnetSanidad",
                        rutaSanidad
                    );
                    System.out.println("   ✓ Carnet de Sanidad guardado: " + rutaSanidad);
                }
            }
            
            System.out.println("✅ [SERVICE] Documentos guardados exitosamente");
            
        } catch (Exception e) {
            System.err.println("❌ [SERVICE] Error al guardar documentos: " + e.getMessage());
            e.printStackTrace();
            // No lanzar excepción para no interrumpir el registro
        }
    }
}

