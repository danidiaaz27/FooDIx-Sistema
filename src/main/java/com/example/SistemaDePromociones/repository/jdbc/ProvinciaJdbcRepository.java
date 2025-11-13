package com.example.SistemaDePromociones.repository.jdbc;

import com.example.SistemaDePromociones.model.Provincia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para Provincia usando JdbcTemplate (SQL directo)
 */
@Repository
public class ProvinciaJdbcRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private final RowMapper<Provincia> rowMapper = (rs, rowNum) -> {
        Provincia prov = new Provincia();
        prov.setCodigo(rs.getLong("codigo"));
        prov.setNombre(rs.getString("nombre"));
        prov.setCodigoDepartamento(rs.getLong("codigo_departamento"));
        prov.setEstado(rs.getBoolean("estado"));
        return prov;
    };
    
    public List<Provincia> findByDepartamento(Long codigoDepartamento) {
        // Usar los nombres de las columnas en minúsculas (snake_case en MySQL)
        String sql = "SELECT codigo, nombre, codigo_departamento, estado " +
                    "FROM provincia " +
                    "WHERE codigo_departamento = ? AND estado = 1 " +
                    "ORDER BY nombre";
        return jdbcTemplate.query(sql, rowMapper, codigoDepartamento);
    }
    
    public Provincia findByCodigo(Long codigo) {
        String sql = "SELECT codigo, nombre, codigo_departamento, estado FROM provincia WHERE codigo = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, codigo);
    }
}
