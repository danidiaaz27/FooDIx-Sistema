package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entidad ImagenRestaurante - Almacena las imágenes de los restaurantes
 * (Logo, Portada, Galería)
 */
@Entity
@Table(name = "ImagenRestaurante")
@Data
public class ImagenRestaurante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Codigo")
    private Long codigo;
    
    @Column(name = "CodigoRestaurante", nullable = false)
    private Long codigoRestaurante;
    
    @Column(name = "RutaImagen", nullable = false, length = 255)
    private String rutaImagen;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "TipoImagen", nullable = false)
    private TipoImagenRestaurante tipoImagen;
    
    @Column(name = "Orden")
    private Integer orden = 0;
    
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
        if (orden == null) {
            orden = 0;
        }
    }
    
    /**
     * Enum para los tipos de imágenes de restaurante
     */
    public enum TipoImagenRestaurante {
        Logo,
        Portada,
        Galeria
    }
}
