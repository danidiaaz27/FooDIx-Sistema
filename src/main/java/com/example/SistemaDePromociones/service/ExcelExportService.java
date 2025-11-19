package com.example.SistemaDePromociones.service;

import com.example.SistemaDePromociones.model.*;
import com.example.SistemaDePromociones.repository.UsuarioRepository;
import com.example.SistemaDePromociones.repository.RestauranteRepository;
import com.example.SistemaDePromociones.repository.RepartidorRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelExportService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private RepartidorRepository repartidorRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Exportar usuarios a Excel
     */
    public byte[] exportUsuarios() throws IOException {
        List<Usuario> usuarios = usuarioRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Usuarios");

            // Estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            // Encabezados
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Nombre", "Apellido Paterno", "Apellido Materno", "Email", "DNI", "Teléfono", "Estado", "Rol", "Fecha Registro"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos
            int rowNum = 1;
            for (Usuario usuario : usuarios) {
                Row row = sheet.createRow(rowNum++);
                
                createCell(row, 0, usuario.getCodigo(), dataStyle);
                createCell(row, 1, usuario.getNombre(), dataStyle);
                createCell(row, 2, usuario.getApellidoPaterno(), dataStyle);
                createCell(row, 3, usuario.getApellidoMaterno() != null ? usuario.getApellidoMaterno() : "", dataStyle);
                createCell(row, 4, usuario.getCorreoElectronico(), dataStyle);
                createCell(row, 5, usuario.getNumeroDocumento(), dataStyle);
                createCell(row, 6, usuario.getTelefono() != null ? usuario.getTelefono() : "N/A", dataStyle);
                createCell(row, 7, usuario.getEstado() ? "Activo" : "Inactivo", dataStyle);
                createCell(row, 8, usuario.getNombreRol() != null ? usuario.getNombreRol() : "Sin Rol", dataStyle);
                createCell(row, 9, usuario.getFechaCreacion() != null ? 
                    usuario.getFechaCreacion().format(DATE_FORMATTER) : "N/A", dataStyle);
            }

            // Ajustar anchos
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    /**
     * Exportar restaurantes a Excel
     */
    public byte[] exportRestaurantes() throws IOException {
        List<Restaurante> restaurantes = restauranteRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Restaurantes");

            // Estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            // Encabezados
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Nombre Comercial", "Razón Social", "RUC", "Email", "Teléfono", "Dirección", "Estado", "Aprobado", "Fecha Registro"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos
            int rowNum = 1;
            for (Restaurante restaurante : restaurantes) {
                Row row = sheet.createRow(rowNum++);
                
                createCell(row, 0, restaurante.getCodigo(), dataStyle);
                createCell(row, 1, restaurante.getNombre(), dataStyle);
                createCell(row, 2, restaurante.getRazonSocial() != null ? restaurante.getRazonSocial() : "N/A", dataStyle);
                createCell(row, 3, restaurante.getRuc(), dataStyle);
                createCell(row, 4, restaurante.getCorreoElectronico(), dataStyle);
                createCell(row, 5, restaurante.getTelefono() != null ? restaurante.getTelefono() : "N/A", dataStyle);
                createCell(row, 6, restaurante.getDireccion() != null ? restaurante.getDireccion() : "N/A", dataStyle);
                createCell(row, 7, restaurante.getEstado() ? "Activo" : "Inactivo", dataStyle);
                
                // Estado de aprobación
                String estadoAprobacion = "Pendiente";
                if (restaurante.getCodigoEstadoAprobacion() != null) {
                    if (restaurante.getCodigoEstadoAprobacion() == 8L) {
                        estadoAprobacion = "Aprobado";
                    } else if (restaurante.getCodigoEstadoAprobacion() == 9L) {
                        estadoAprobacion = "Rechazado";
                    }
                }
                createCell(row, 8, estadoAprobacion, dataStyle);
                createCell(row, 9, restaurante.getFechaCreacion() != null ? 
                    restaurante.getFechaCreacion().format(DATE_FORMATTER) : "N/A", dataStyle);
            }

            // Ajustar anchos
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    /**
     * Exportar repartidores a Excel
     */
    public byte[] exportDelivery() throws IOException {
        List<Repartidor> repartidores = repartidorRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Repartidores");

            // Estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            // Encabezados
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Nombre", "Apellido Paterno", "Apellido Materno", "DNI", "Email", "Teléfono", "Licencia", "Disponible", "Estado", "Aprobado", "Fecha Registro"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos
            int rowNum = 1;
            for (Repartidor repartidor : repartidores) {
                Row row = sheet.createRow(rowNum++);
                Usuario usuario = repartidor.getUsuario();
                
                createCell(row, 0, repartidor.getCodigo(), dataStyle);
                createCell(row, 1, usuario != null ? usuario.getNombre() : "N/A", dataStyle);
                createCell(row, 2, usuario != null ? usuario.getApellidoPaterno() : "N/A", dataStyle);
                createCell(row, 3, usuario != null && usuario.getApellidoMaterno() != null ? usuario.getApellidoMaterno() : "", dataStyle);
                createCell(row, 4, usuario != null ? usuario.getNumeroDocumento() : "N/A", dataStyle);
                createCell(row, 5, usuario != null ? usuario.getCorreoElectronico() : "N/A", dataStyle);
                createCell(row, 6, usuario != null && usuario.getTelefono() != null ? usuario.getTelefono() : "N/A", dataStyle);
                createCell(row, 7, repartidor.getNumeroLicencia() != null ? repartidor.getNumeroLicencia() : "N/A", dataStyle);
                createCell(row, 8, repartidor.getDisponible() ? "Sí" : "No", dataStyle);
                createCell(row, 9, repartidor.getEstado() ? "Activo" : "Inactivo", dataStyle);
                
                // Estado de aprobación
                String estadoAprobacion = "Pendiente";
                if (repartidor.getCodigoEstadoAprobacion() != null) {
                    if (repartidor.getCodigoEstadoAprobacion() == 8L) {
                        estadoAprobacion = "Aprobado";
                    } else if (repartidor.getCodigoEstadoAprobacion() == 9L) {
                        estadoAprobacion = "Rechazado";
                    }
                }
                createCell(row, 10, estadoAprobacion, dataStyle);
                createCell(row, 11, repartidor.getFechaCreacion() != null ? 
                    repartidor.getFechaCreacion().format(DATE_FORMATTER) : "N/A", dataStyle);
            }

            // Ajustar anchos
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    /**
     * Crear estilo para encabezados
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        // Color de fondo
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        // Fuente
        Font font = workbook.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        
        // Bordes
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        // Alineación
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        return style;
    }

    /**
     * Crear estilo para datos
     */
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        // Bordes
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        // Alineación
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        return style;
    }

    /**
     * Crear celda con valor
     */
    private void createCell(Row row, int column, Object value, CellStyle style) {
        Cell cell = row.createCell(column);
        
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }
        
        cell.setCellStyle(style);
    }
}
