package com.example.SistemaDePromociones.service;

import com.example.SistemaDePromociones.dto.RepartidorRegistroDTO;
import com.example.SistemaDePromociones.model.DocumentoRepartidor;
import com.example.SistemaDePromociones.model.Repartidor;
import com.example.SistemaDePromociones.repository.JdbcRepartidorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Servicio para gestionar repartidores
 */
@Service
public class RepartidorService {
    
    @Autowired
    private JdbcRepartidorRepository repartidorRepository;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    /**
     * Registrar un nuevo repartidor
     */
    @Transactional
    public Repartidor registrarRepartidor(RepartidorRegistroDTO dto) throws Exception {
        
        // Validar que no exista la licencia
        if (repartidorRepository.existsByNumeroLicencia(dto.getNumeroLicencia())) {
            throw new RuntimeException("El número de licencia ya está registrado");
        }
        
        // 1. Crear usuario
    Integer codigoTipoDocumento = "DNI".equals(dto.getTipoDocumento()) ? 1 : 2;
        Integer codigoRol = 3; // 3 = Repartidor
        
        Long codigoUsuario = usuarioService.crearUsuario(
            dto.getNombre(),
            dto.getApellidoPaterno(),
            dto.getApellidoMaterno(),
            dto.getNumeroDocumento(),
            dto.getFechaNacimiento(),
            dto.getCorreoElectronico(),
            dto.getContrasena(),
            dto.getTelefono(),
            dto.getDireccion(),
            codigoTipoDocumento,
            codigoRol,
            dto.getCodigoDistrito()
        );
        
        // 2. Crear repartidor
        Repartidor repartidor = new Repartidor();
        repartidor.setCodigoUsuario(codigoUsuario);
        repartidor.setNumeroLicencia(dto.getNumeroLicencia());
        repartidor.setCodigoTipoVehiculo(dto.getCodigoTipoVehiculo());
        repartidor.setDisponible(true);
        repartidor.setCodigoEstadoAprobacion(1L); // Pendiente
        repartidor.setEstado(true);
        
        repartidor = repartidorRepository.save(repartidor);
        
        // 3. Guardar documentos
        String rutaLicencia = fileStorageService.guardarArchivo(
            dto.getLicenciaConducir(), 
            "repartidores/" + repartidor.getCodigo()
        );
        if (rutaLicencia != null) {
            DocumentoRepartidor docLicencia = new DocumentoRepartidor();
            docLicencia.setCodigoRepartidor(repartidor.getCodigo());
            docLicencia.setTipoDocumento(DocumentoRepartidor.TipoDocumentoRepartidor.Licencia);
            docLicencia.setRutaArchivo(rutaLicencia);
            docLicencia.setEstado(true);
            entityManager.persist(docLicencia);
        }
        
        String rutaSoat = fileStorageService.guardarArchivo(
            dto.getSoat(), 
            "repartidores/" + repartidor.getCodigo()
        );
        if (rutaSoat != null) {
            DocumentoRepartidor docSoat = new DocumentoRepartidor();
            docSoat.setCodigoRepartidor(repartidor.getCodigo());
            docSoat.setTipoDocumento(DocumentoRepartidor.TipoDocumentoRepartidor.SOAT);
            docSoat.setRutaArchivo(rutaSoat);
            docSoat.setEstado(true);
            entityManager.persist(docSoat);
        }
        
        String rutaAntecedentes = fileStorageService.guardarArchivo(
            dto.getAntecedentesPolicial(), 
            "repartidores/" + repartidor.getCodigo()
        );
        if (rutaAntecedentes != null) {
            DocumentoRepartidor docAntecedentes = new DocumentoRepartidor();
            docAntecedentes.setCodigoRepartidor(repartidor.getCodigo());
            docAntecedentes.setTipoDocumento(DocumentoRepartidor.TipoDocumentoRepartidor.AntecedentesPolicial);
            docAntecedentes.setRutaArchivo(rutaAntecedentes);
            docAntecedentes.setEstado(true);
            entityManager.persist(docAntecedentes);
        }
        
        String rutaTarjeta = fileStorageService.guardarArchivo(
            dto.getTarjetaPropiedad(), 
            "repartidores/" + repartidor.getCodigo()
        );
        if (rutaTarjeta != null) {
            DocumentoRepartidor docTarjeta = new DocumentoRepartidor();
            docTarjeta.setCodigoRepartidor(repartidor.getCodigo());
            docTarjeta.setTipoDocumento(DocumentoRepartidor.TipoDocumentoRepartidor.TarjetaPropiedad);
            docTarjeta.setRutaArchivo(rutaTarjeta);
            docTarjeta.setEstado(true);
            entityManager.persist(docTarjeta);
        }
        
        return repartidor;
    }
}
