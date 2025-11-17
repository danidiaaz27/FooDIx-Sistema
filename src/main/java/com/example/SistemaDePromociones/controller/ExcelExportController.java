package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.service.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/export")
public class ExcelExportController {

    @Autowired
    private ExcelExportService excelExportService;

    private static final DateTimeFormatter FILE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Exportar usuarios a Excel
     */
    @GetMapping("/usuarios")
    @PreAuthorize("hasAnyAuthority('USUARIOS_VER', 'USUARIOS_LISTAR')")
    public ResponseEntity<byte[]> exportUsuarios() {
        try {
            byte[] excelData = excelExportService.exportUsuarios();
            
            String fileName = "FooDix_Usuarios_" + LocalDateTime.now().format(FILE_DATE_FORMATTER) + ".xlsx";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exportar restaurantes a Excel
     */
    @GetMapping("/restaurantes")
    @PreAuthorize("hasAnyAuthority('RESTAURANTES_VER', 'RESTAURANTES_LISTAR')")
    public ResponseEntity<byte[]> exportRestaurantes() {
        try {
            byte[] excelData = excelExportService.exportRestaurantes();
            
            String fileName = "FooDix_Restaurantes_" + LocalDateTime.now().format(FILE_DATE_FORMATTER) + ".xlsx";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exportar repartidores a Excel
     */
    @GetMapping("/delivery")
    @PreAuthorize("hasAnyAuthority('DELIVERY_VER', 'DELIVERY_LISTAR')")
    public ResponseEntity<byte[]> exportDelivery() {
        try {
            byte[] excelData = excelExportService.exportDelivery();
            
            String fileName = "FooDix_Repartidores_" + LocalDateTime.now().format(FILE_DATE_FORMATTER) + ".xlsx";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
