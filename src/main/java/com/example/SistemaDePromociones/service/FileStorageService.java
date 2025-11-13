package com.example.SistemaDePromociones.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
        
        // Retornar ruta relativa
        return subFolder + "/" + uniqueFilename;
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
