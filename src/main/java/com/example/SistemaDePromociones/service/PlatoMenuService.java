package com.example.SistemaDePromociones.service;

import com.example.SistemaDePromociones.dto.PlatoMenuDTO;
import com.example.SistemaDePromociones.model.PlatoMenu;
import com.example.SistemaDePromociones.model.UnidadMedidaPlato;
import com.example.SistemaDePromociones.repository.PlatoMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar los platos del menú de restaurantes
 */
@Service
@Transactional
public class PlatoMenuService {
    
    @Autowired
    private PlatoMenuRepository platoMenuRepository;
    
    /**
     * Obtener todos los platos activos de un restaurante
     */
    @Transactional(readOnly = true)
    public List<PlatoMenuDTO> obtenerPlatosPorRestaurante(Long codigoRestaurante) {
        List<PlatoMenu> platos = platoMenuRepository.findByCodigoRestauranteAndEstadoTrue(codigoRestaurante);
        return platos.stream()
                .map(this::convertirAPlatoMenuDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtener un plato por su código
     */
    @Transactional(readOnly = true)
    public PlatoMenuDTO obtenerPlatoPorId(Long codigo) {
        PlatoMenu plato = platoMenuRepository.findByIdWithUnidades(codigo);
        if (plato == null) {
            throw new RuntimeException("Plato no encontrado con código: " + codigo);
        }
        return convertirAPlatoMenuDTO(plato);
    }
    
    /**
     * Crear un nuevo plato
     */
    public PlatoMenuDTO crearPlato(PlatoMenuDTO platoDTO) {
        // Validaciones
        if (platoDTO.getNombre() == null || platoDTO.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del plato es obligatorio");
        }
        
        if (platoDTO.getCodigoRestaurante() == null) {
            throw new RuntimeException("El código del restaurante es obligatorio");
        }
        
        if (platoDTO.getUnidadesMedida() == null || platoDTO.getUnidadesMedida().isEmpty()) {
            throw new RuntimeException("Debe agregar al menos una unidad de medida");
        }
        
        // Crear entidad
        PlatoMenu plato = new PlatoMenu();
        plato.setCodigoRestaurante(platoDTO.getCodigoRestaurante());
        plato.setNombre(platoDTO.getNombre());
        plato.setDescripcion(platoDTO.getDescripcion());
        plato.setEstado(true);
        
        // Agregar unidades de medida
        for (PlatoMenuDTO.UnidadMedidaDTO unidadDTO : platoDTO.getUnidadesMedida()) {
            UnidadMedidaPlato unidad = new UnidadMedidaPlato();
            unidad.setNombre(unidadDTO.getNombre());
            unidad.setDescripcion(unidadDTO.getDescripcion());
            unidad.setPrecioOriginal(unidadDTO.getPrecioOriginal());
            unidad.setEstado(true);
            plato.addUnidadMedida(unidad);
        }
        
        // Guardar y forzar flush
        PlatoMenu platoGuardado = platoMenuRepository.saveAndFlush(plato);
        
        // Recargar el plato con sus unidades para asegurar que todo esté persistido
        PlatoMenu platoConUnidades = platoMenuRepository.findByIdWithUnidades(platoGuardado.getCodigo());
        return convertirAPlatoMenuDTO(platoConUnidades);
    }
    
    /**
     * Actualizar un plato existente
     */
    public PlatoMenuDTO actualizarPlato(Long codigo, PlatoMenuDTO platoDTO) {
        PlatoMenu platoExistente = platoMenuRepository.findByIdWithUnidades(codigo);
        
        if (platoExistente == null) {
            throw new RuntimeException("Plato no encontrado con código: " + codigo);
        }
        
        // Validaciones
        if (platoDTO.getNombre() == null || platoDTO.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del plato es obligatorio");
        }
        
        if (platoDTO.getUnidadesMedida() == null || platoDTO.getUnidadesMedida().isEmpty()) {
            throw new RuntimeException("Debe agregar al menos una unidad de medida");
        }
        
        // Actualizar datos básicos
        platoExistente.setNombre(platoDTO.getNombre());
        platoExistente.setDescripcion(platoDTO.getDescripcion());
        
        // Actualizar unidades de medida
        // Limpiar todas las unidades antiguas
        platoExistente.getUnidadesMedida().clear();
        
        // Forzar el flush para eliminar las unidades antiguas
        platoMenuRepository.saveAndFlush(platoExistente);
        
        // Agregar nuevas unidades
        for (PlatoMenuDTO.UnidadMedidaDTO unidadDTO : platoDTO.getUnidadesMedida()) {
            UnidadMedidaPlato unidad = new UnidadMedidaPlato();
            unidad.setNombre(unidadDTO.getNombre());
            unidad.setDescripcion(unidadDTO.getDescripcion());
            unidad.setPrecioOriginal(unidadDTO.getPrecioOriginal());
            unidad.setEstado(true);
            platoExistente.addUnidadMedida(unidad);
        }
        
        // Guardar las nuevas unidades
        PlatoMenu platoActualizado = platoMenuRepository.saveAndFlush(platoExistente);
        
        // Recargar el plato con sus unidades actualizadas
        PlatoMenu platoConUnidades = platoMenuRepository.findByIdWithUnidades(platoActualizado.getCodigo());
        return convertirAPlatoMenuDTO(platoConUnidades);
    }
    
    /**
     * Eliminar un plato (borrado lógico)
     */
    public void eliminarPlato(Long codigo) {
        Optional<PlatoMenu> platoOpt = platoMenuRepository.findById(codigo);
        
        if (!platoOpt.isPresent()) {
            throw new RuntimeException("Plato no encontrado con código: " + codigo);
        }
        
        PlatoMenu plato = platoOpt.get();
        plato.setEstado(false);
        platoMenuRepository.saveAndFlush(plato);
    }
    
    /**
     * Convertir entidad PlatoMenu a DTO
     */
    private PlatoMenuDTO convertirAPlatoMenuDTO(PlatoMenu plato) {
        PlatoMenuDTO dto = new PlatoMenuDTO();
        dto.setCodigo(plato.getCodigo());
        dto.setCodigoRestaurante(plato.getCodigoRestaurante());
        dto.setNombre(plato.getNombre());
        dto.setDescripcion(plato.getDescripcion());
        
        List<PlatoMenuDTO.UnidadMedidaDTO> unidadesDTO = plato.getUnidadesMedida().stream()
                .filter(u -> u.getEstado() != null && u.getEstado())
                .map(u -> {
                    PlatoMenuDTO.UnidadMedidaDTO unidadDTO = new PlatoMenuDTO.UnidadMedidaDTO();
                    unidadDTO.setCodigo(u.getCodigo());
                    unidadDTO.setNombre(u.getNombre());
                    unidadDTO.setDescripcion(u.getDescripcion());
                    unidadDTO.setPrecioOriginal(u.getPrecioOriginal());
                    return unidadDTO;
                })
                .collect(Collectors.toList());
        
        dto.setUnidadesMedida(unidadesDTO);
        return dto;
    }
}
