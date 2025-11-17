package com.example.SistemaDePromociones.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * Repository para Usuario usando JdbcTemplate (SQL directo)
 * Usa los nombres EXACTOS de las columnas como están en la base de datos
 */
@Repository
public class UsuarioJdbcRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * Verificar si existe un correo electrónico
     */
    public boolean existsByCorreoElectronico(String correo) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE correo_electronico = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, correo);
        return count != null && count > 0;
    }
    
    /**
     * Verificar si existe un número de documento
     */
    public boolean existsByNumeroDocumento(String numeroDoc) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE numero_documento = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, numeroDoc);
        return count != null && count > 0;
    }
    
    /**
     * Insertar un nuevo usuario
     * Retorna el ID generado
     */
    public Long insertUsuario(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            String numeroDocumento,
            LocalDate fechaNacimiento,
            String correoElectronico,
            String contrasena,
            String telefono,
            String direccion,
            Long codigoTipoDocumento,
            Long codigoRol,
            Long codigoDistrito
    ) {
        String sql = "INSERT INTO usuario " +
                    "(nombre, apellido_paterno, apellido_materno, numero_documento, fecha_nacimiento, " +
                    "correo_electronico, contrasena, telefono, direccion, codigo_tipo_documento, " +
                    "codigo_rol, codigo_distrito, estado, fecha_creacion, fecha_modificacion) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1, NOW(), NOW())";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nombre);
            ps.setString(2, apellidoPaterno);
            ps.setString(3, apellidoMaterno);
            ps.setString(4, numeroDocumento);
            ps.setObject(5, fechaNacimiento);
            ps.setString(6, correoElectronico);
            ps.setString(7, contrasena);
            ps.setString(8, telefono);
            ps.setString(9, direccion);
            ps.setLong(10, codigoTipoDocumento);
            ps.setLong(11, codigoRol);
            ps.setLong(12, codigoDistrito);
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey().longValue();
    }
    
    /**
     * Actualizar la contraseña de un usuario
     */
    public int actualizarPassword(String correoElectronico, String nuevaPasswordEncriptada) {
        String sql = "UPDATE usuario SET contrasena = ?, fecha_modificacion = NOW() " +
                    "WHERE correo_electronico = ?";
        
        int filasActualizadas = jdbcTemplate.update(sql, nuevaPasswordEncriptada, correoElectronico);
        System.out.println("📝 [REPOSITORY] Contraseña actualizada. Filas afectadas: " + filasActualizadas);
        
        return filasActualizadas;
    }
}
