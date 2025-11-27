package com.example.SistemaDePromociones.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Servicio para almacenar archivos subidos
 */
@Service
public class FileStorageService {
    
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;
    
    @PostConstruct
    public void init() {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("📁 [FILE SERVICE] Directorio uploads creado: " + uploadPath.toAbsolutePath());
            } else {
                System.out.println("📁 [FILE SERVICE] Directorio uploads encontrado: " + uploadPath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("❌ [FILE SERVICE] Error al crear directorio uploads: " + e.getMessage());
        }
    }
    
    /**
     * Guardar un archivo en el sistema de archivos
     * @param file Archivo a guardar
     * @param subFolder Subcarpeta (ej: "repartidores", "restaurantes")
     * @return Ruta relativa del archivo guardado
     */
    public String guardarArchivo(MultipartFile file, String subFolder) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        
        // Crear directorio si no existe
        Path uploadPath = Paths.get(uploadDir, subFolder);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generar nombre único para el archivo
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String uniqueFilename = UUID.randomUUID().toString() + extension;
        
        // Guardar archivo
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        System.out.println("✅ [FILE SERVICE] Archivo guardado: " + subFolder + "/" + uniqueFilename);
        System.out.println("📂 [FILE SERVICE] Ubicación: " + filePath.toAbsolutePath());
        
        // Retornar ruta relativa
        return subFolder + "/" + uniqueFilename;
    }
    
    /**
     * Guardar archivo de restaurante con nombre específico
     * @param file Archivo a guardar
     * @param restauranteId ID del restaurante
     * @param tipoDocumento Tipo de documento (CARTA_RESTAURANTE, CARNET_SANIDAD, LICENCIA_FUNCIONAMIENTO)
     * @return Ruta relativa del archivo guardado
     */
    public String guardarArchivoRestaurante(MultipartFile file, Long restauranteId, String tipoDocumento) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        
        // Crear estructura: uploads/restaurante/{id}/
        String subFolder = "restaurante/" + restauranteId;
        Path uploadPath = Paths.get(uploadDir, subFolder);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Obtener extensión
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        // Nombre del archivo: TIPO_DOCUMENTO + extensión
        String filename = tipoDocumento + extension;
        Path filePath = uploadPath.resolve(filename);
        
        // Guardar archivo (reemplazar si existe)
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        System.out.println("✅ [FILE SERVICE] Documento restaurante guardado: " + subFolder + "/" + filename);
        System.out.println("📂 [FILE SERVICE] Ubicación: " + filePath.toAbsolutePath());
        
        // Retornar ruta relativa para la BD
        return subFolder + "/" + filename;
    }
    
    /**
     * Verificar si un archivo existe
     */
    public boolean archivoExiste(String rutaRelativa) {
        if (rutaRelativa == null || rutaRelativa.isEmpty()) {
            return false;
        }
        Path filePath = Paths.get(uploadDir, rutaRelativa);
        return Files.exists(filePath);
    }
    
    /**
     * Eliminar un archivo
     */
    public void eliminarArchivo(String rutaRelativa) {
        if (rutaRelativa == null || rutaRelativa.isEmpty()) {
            return;
        }
        try {
            Path filePath = Paths.get(uploadDir, rutaRelativa);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println("🗑️ [FILE SERVICE] Archivo eliminado: " + rutaRelativa);
            }
        } catch (IOException e) {
            System.err.println("❌ [FILE SERVICE] Error al eliminar archivo: " + e.getMessage());
        }
    }
    
    /**
     * Validar que el archivo no exceda el tamaño máximo (5MB)
     */
    public boolean validarTamano(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return true;
        }
        long maxSize = 5 * 1024 * 1024; // 5MB en bytes
        return file.getSize() <= maxSize;
    }
    
    /**
     * Validar que el archivo sea de tipo permitido
     */
    public boolean validarTipoArchivo(MultipartFile file, String[] tiposPermitidos) {
        if (file == null || file.isEmpty()) {
            return true;
        }
        String contentType = file.getContentType();
        if (contentType == null) {
            return false;
        }
        for (String tipo : tiposPermitidos) {
            if (contentType.contains(tipo)) {
                return true;
            }
        }
        return false;
    }
}
