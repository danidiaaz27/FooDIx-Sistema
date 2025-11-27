package com.example.SistemaDePromociones.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración para servir archivos estáticos desde el directorio uploads
 */
@Configuration
public class FileStorageConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapear /uploads/** a la carpeta uploads en el directorio del proyecto
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
        
        System.out.println("📁 [FILE CONFIG] Configuración de archivos estáticos agregada");
        System.out.println("📁 [FILE CONFIG] URL: /uploads/** -> file:uploads/");
    }
}
