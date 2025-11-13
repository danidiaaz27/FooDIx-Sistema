package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad DocumentoRestaurante - Almacena los documentos legales del restaurante
 * (Carta del Restaurante, Licencia de Funcionamiento, Carnet de Sanidad, etc.)
 */
@Entity
@Table(name = "DocumentoRestaurante")
@Data
public class DocumentoRestaurante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo")
    private Long codigo;
    
    @Column(name = "CodigoRestaurante", nullable = false)
    private Long codigoRestaurante;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "TipoDocumento", nullable = false)
    private TipoDocumentoRestaurante tipoDocumento;
    
    @Column(name = "RutaArchivo", nullable = false, length = 255)
    private String rutaArchivo;
    
    @Column(name = "FechaVencimiento")
    private LocalDate fechaVencimiento;
    
    @Column(name = "FechaSubida", nullable = false, updatable = false)
    private LocalDateTime fechaSubida;
    
    @Column(name = "Estado", nullable = false)
    private Boolean estado = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CodigoRestaurante", insertable = false, updatable = false)
    private Restaurante restaurante;
    
    @PrePersist
    protected void onCreate() {
        fechaSubida = LocalDateTime.now();
        if (estado == null) {
            estado = true;
        }
    }
    
    /**
     * Enum para los tipos de documentos de restaurante
     */
    public enum TipoDocumentoRestaurante {
        CARTA_RESTAURANTE,
        LicenciaFuncionamiento,
        CarnetSanidad,
        Otros
    }
}
