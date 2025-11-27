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
        
        // Por ahora retornar el ID (simplificado)
        // TODO: Agregar categorías y documentos después
        return codigoRestaurante;
    }
    
    /*
    // CÓDIGO COMENTADO TEMPORALMENTE - Implementar con JDBC después
    public void guardarCategoriasYDocumentos(Long codigoRestaurante, RestauranteRegistroDTO dto) {
            dto.getCartaRestaurante(), 
            "restaurantes/" + restaurante.getCodigo() + "/documentos"
        );
        if (rutaRuc != null) {
            DocumentoRestaurante docRuc = new DocumentoRestaurante();
            docRuc.setCodigoRestaurante(restaurante.getCodigo());
            docRuc.setTipoDocumento(DocumentoRestaurante.TipoDocumentoRestaurante.CARTA_RESTAURANTE);
            docRuc.setRutaArchivo(rutaRuc);
            docRuc.setEstado(true);
            entityManager.persist(docRuc);
        }
        
        String rutaLicencia = fileStorageService.guardarArchivo(
            dto.getLicenciaFuncionamiento(), 
            "restaurantes/" + restaurante.getCodigo() + "/documentos"
        );
        if (rutaLicencia != null) {
            DocumentoRestaurante docLicencia = new DocumentoRestaurante();
            docLicencia.setCodigoRestaurante(restaurante.getCodigo());
            docLicencia.setTipoDocumento(DocumentoRestaurante.TipoDocumentoRestaurante.LicenciaFuncionamiento);
            docLicencia.setRutaArchivo(rutaLicencia);
            docLicencia.setEstado(true);
            entityManager.persist(docLicencia);
        }
        
        String rutaSanidad = fileStorageService.guardarArchivo(
            dto.getCarnetSanidad(), 
            "restaurantes/" + restaurante.getCodigo() + "/documentos"
        );
        if (rutaSanidad != null) {
            DocumentoRestaurante docSanidad = new DocumentoRestaurante();
            docSanidad.setCodigoRestaurante(restaurante.getCodigo());
            docSanidad.setTipoDocumento(DocumentoRestaurante.TipoDocumentoRestaurante.CarnetSanidad);
            docSanidad.setRutaArchivo(rutaSanidad);
            docSanidad.setEstado(true);
            entityManager.persist(docSanidad);
        }
        
        // 5. Guardar imágenes
        String rutaLogo = fileStorageService.guardarArchivo(
            dto.getLogo(), 
            "restaurantes/" + restaurante.getCodigo() + "/imagenes"
        );
        if (rutaLogo != null) {
            ImagenRestaurante imgLogo = new ImagenRestaurante();
            imgLogo.setCodigoRestaurante(restaurante.getCodigo());
            imgLogo.setTipoImagen(ImagenRestaurante.TipoImagenRestaurante.Logo);
            imgLogo.setRutaImagen(rutaLogo);
            imgLogo.setOrden(0);
            imgLogo.setEstado(true);
            entityManager.persist(imgLogo);
        }
        
        String rutaPortada = fileStorageService.guardarArchivo(
            dto.getPortada(), 
            "restaurantes/" + restaurante.getCodigo() + "/imagenes"
        );
        if (rutaPortada != null) {
            ImagenRestaurante imgPortada = new ImagenRestaurante();
            imgPortada.setCodigoRestaurante(restaurante.getCodigo());
            imgPortada.setTipoImagen(ImagenRestaurante.TipoImagenRestaurante.Portada);
            imgPortada.setRutaImagen(rutaPortada);
            imgPortada.setOrden(0);
            imgPortada.setEstado(true);
            entityManager.persist(imgPortada);
        }
        
        // Galería
        if (dto.getGaleria() != null && !dto.getGaleria().isEmpty()) {
            int orden = 1;
            for (MultipartFile imagen : dto.getGaleria()) {
                String rutaGaleria = fileStorageService.guardarArchivo(
                    imagen, 
                    "restaurantes/" + restaurante.getCodigo() + "/imagenes"
                );
                if (rutaGaleria != null) {
                    ImagenRestaurante imgGaleria = new ImagenRestaurante();
                    imgGaleria.setCodigoRestaurante(restaurante.getCodigo());
                    imgGaleria.setTipoImagen(ImagenRestaurante.TipoImagenRestaurante.Galeria);
                    imgGaleria.setRutaImagen(rutaGaleria);
                    imgGaleria.setOrden(orden++);
                    imgGaleria.setEstado(true);
                    entityManager.persist(imgGaleria);
                }
            }
        }
        
    */
}

