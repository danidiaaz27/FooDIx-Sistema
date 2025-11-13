package com.example.SistemaDePromociones.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Repository para Restaurante usando JdbcTemplate (SQL directo)
 */
@Repository
public class RestauranteJdbcRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * Verificar si existe un RUC
     */
    public boolean existsByRuc(String ruc) {
        String sql = "SELECT COUNT(*) FROM restaurante WHERE ruc = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, ruc);
        return count != null && count > 0;
    }
    
    /**
     * Insertar un nuevo restaurante
     * Retorna el ID generado
     */
    public Long insertRestaurante(
            Long codigoUsuario,
            String ruc,
            String razonSocial,
            String nombre,
            String descripcion,
            String direccion,
            String telefono,
            String correoElectronico,
            Long codigoDistrito
    ) {
        String sql = "INSERT INTO restaurante " +
                    "(codigo_usuario, ruc, razon_social, nombre, descripcion, direccion, " +
                    "telefono, correo_electronico, codigo_distrito, codigo_estado_aprobacion, estado, fecha_creacion) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 7, 1, NOW())";  // 7 = Pendiente
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, codigoUsuario);
            ps.setString(2, ruc);
            ps.setString(3, razonSocial);
            ps.setString(4, nombre);
            ps.setString(5, descripcion);
            ps.setString(6, direccion);
            ps.setString(7, telefono);
            ps.setString(8, correoElectronico);
            ps.setLong(9, codigoDistrito);
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey().longValue();
    }
}
