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
    private UsuarioService usuarioService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    /**
     * Registrar un nuevo restaurante usando SQL directo
     */
    @Transactional
    public Long registrarRestaurante(RestauranteRegistroDTO dto) throws Exception {
        
        // Validar campos requeridos
        if (dto.getTipoDocumento() == null || dto.getTipoDocumento().isEmpty()) {
            throw new RuntimeException("El tipo de documento es requerido");
        }
        
        // Validar que no exista el RUC
        if (restauranteRepository.existsByRuc(dto.getRuc())) {
            throw new RuntimeException("El RUC ya está registrado");
        }
        
        // 1. Crear usuario (representante del restaurante)
    Integer codigoTipoDocumento = "DNI".equals(dto.getTipoDocumento()) ? 1 : 2;
        Integer codigoRol = 4; // 4 = Restaurante
        
        Long codigoUsuario = usuarioService.crearUsuario(
            dto.getNombre(),
            dto.getApellidoPaterno(),
            dto.getApellidoMaterno(),
            dto.getNumeroDocumento(),
            dto.getFechaNacimiento(),
            dto.getCorreoElectronico(),
            dto.getContrasena(),
            dto.getTelefono(),
            dto.getDireccionPersonal(),
            codigoTipoDocumento,
            codigoRol,
            dto.getCodigoDistritoPersonal()
        );
        
        // 2. Crear restaurante usando JDBC
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

