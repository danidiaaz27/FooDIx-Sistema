package com.example.SistemaDePromociones.repository.jdbc;

import com.example.SistemaDePromociones.model.Departamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para Departamento usando JdbcTemplate (SQL directo)
 */
@Repository
public class DepartamentoJdbcRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private final RowMapper<Departamento> rowMapper = (rs, rowNum) -> {
        Departamento dept = new Departamento();
        dept.setCodigo(rs.getLong("codigo"));
        dept.setNombre(rs.getString("nombre"));
        dept.setEstado(rs.getBoolean("estado"));
        return dept;
    };
    
    public List<Departamento> findAllActivos() {
        // Usar los nombres de las columnas en minúsculas (snake_case en MySQL)
        String sql = "SELECT codigo, nombre, estado FROM departamento WHERE estado = 1 ORDER BY nombre";
        return jdbcTemplate.query(sql, rowMapper);
    }
    
    public Departamento findByCodigo(Long codigo) {
        String sql = "SELECT codigo, nombre, estado FROM departamento WHERE codigo = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, codigo);
    }
}
