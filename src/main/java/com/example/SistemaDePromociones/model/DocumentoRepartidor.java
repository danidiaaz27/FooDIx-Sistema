package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entidad DocumentoRepartidor - Representa la tabla DocumentoRepartidor en la base de datos
 * Almacena información sobre los documentos subidos por los repartidores
 */
@Entity
@Table(name = "DocumentoRepartidor")
@Data
public class DocumentoRepartidor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo")
    private Long codigo;
    
    @Column(name = "CodigoRepartidor", nullable = false)
    private Long codigoRepartidor;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "TipoDocumento", nullable = false)
    private TipoDocumentoRepartidor tipoDocumento;
    
    @Column(name = "RutaArchivo", nullable = false, length = 255)
    private String rutaArchivo;
    
    @Column(name = "FechaVencimiento")
    private LocalDateTime fechaVencimiento;
    
    @Column(name = "FechaSubida", nullable = false, updatable = false)
    private LocalDateTime fechaSubida;
    
    @Column(name = "Estado", nullable = false)
    private Boolean estado = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CodigoRepartidor", insertable = false, updatable = false)
    private Repartidor repartidor;
    
    @PrePersist
    protected void onCreate() {
        fechaSubida = LocalDateTime.now();
        if (estado == null) {
            estado = true;
        }
    }
    
    /**
     * Enum para los tipos de documentos de repartidor
     */
    public enum TipoDocumentoRepartidor {
        Licencia,
        SOAT,
        AntecedentesPolicial,
        TarjetaPropiedad
    }
}
