package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Controlador para servir archivos subidos
 */
@Controller
@RequestMapping("/files")
public class FileController {
    
    @Autowired
    private FileStorageService fileStorageService;
    
    /**
     * Busca un archivo sin extensión probando extensiones comunes
     */
    private Resource buscarArchivoConExtension(Path rutaBase) {
        String[] extensiones = {".pdf", ".PDF", ".jpg", ".JPG", ".jpeg", ".JPEG", ".png", ".PNG"};
        
        for (String ext : extensiones) {
            try {
                Path rutaConExt = Paths.get(rutaBase.toString() + ext);
                Resource resource = new UrlResource(rutaConExt.toUri());
                if (resource.exists() && resource.isReadable()) {
                    System.out.println("✅ [FILE CONTROLLER] Archivo encontrado: " + rutaConExt);
                    return resource;
                }
            } catch (Exception e) {
                // Continuar con la siguiente extensión
            }
        }
        return null;
    }
    
    /**
     * Ver archivo en línea (PDF, imágenes)
     * GET /files/view/restaurante/1/CARTA_RESTAURANTE.pdf
     * GET /files/view/restaurante/1/CARTA_RESTAURANTE (sin extensión)
     */
    @GetMapping("/view/**")
    public ResponseEntity<Resource> verArchivo(jakarta.servlet.http.HttpServletRequest request) {
        try {
            // Obtener la ruta completa después de /files/view/
            String rutaCompleta = request.getRequestURI().substring("/files/view/".length());
            
            // Construir ruta del archivo
            Path archivoPath = Paths.get("uploads", rutaCompleta);
            Resource resource = new UrlResource(archivoPath.toUri());
            
            // Si el archivo no existe con la ruta exacta, intentar con extensiones comunes
            if (!resource.exists() || !resource.isReadable()) {
                System.out.println("🔍 [FILE CONTROLLER] Archivo no encontrado con ruta exacta, buscando con extensiones...");
                resource = buscarArchivoConExtension(archivoPath);
                if (resource == null) {
                    System.err.println("❌ [FILE CONTROLLER] Archivo no encontrado: " + rutaCompleta);
                    return ResponseEntity.notFound().build();
                }
                // Actualizar archivoPath con el archivo encontrado
                archivoPath = Paths.get(resource.getURI());
            }
            
            // Determinar tipo de contenido
            String contentType = "application/octet-stream";
            String filename = archivoPath.getFileName().toString().toLowerCase();
            
            if (filename.endsWith(".pdf")) {
                contentType = "application/pdf";
            } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (filename.endsWith(".png")) {
                contentType = "image/png";
            } else if (filename.endsWith(".gif")) {
                contentType = "image/gif";
            }
            
            System.out.println("📄 [FILE CONTROLLER] Sirviendo archivo: " + archivoPath);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + archivoPath.getFileName() + "\"")
                    .body(resource);
                    
        } catch (Exception e) {
            System.err.println("❌ [FILE CONTROLLER] Error al servir archivo: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Descargar archivo
     * GET /files/download/restaurante/1/CARTA_RESTAURANTE.pdf
     */
    @GetMapping("/download/**")
    public ResponseEntity<Resource> descargarArchivo(jakarta.servlet.http.HttpServletRequest request) {
        try {
            // Obtener la ruta completa después de /files/download/
            String rutaCompleta = request.getRequestURI().substring("/files/download/".length());
            
            // Construir ruta del archivo
            Path archivoPath = Paths.get("uploads", rutaCompleta);
            Resource resource = new UrlResource(archivoPath.toUri());
            
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }
            
            System.out.println("⬇️ [FILE CONTROLLER] Descargando archivo: " + rutaCompleta);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivoPath.getFileName() + "\"")
                    .body(resource);
                    
        } catch (Exception e) {
            System.err.println("❌ [FILE CONTROLLER] Error al descargar archivo: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
