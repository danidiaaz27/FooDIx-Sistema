package com.example.SistemaDePromociones.repository.jdbc;

import com.example.SistemaDePromociones.model.Distrito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para Distrito usando JdbcTemplate (SQL directo)
 */
@Repository
public class DistritoJdbcRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private final RowMapper<Distrito> rowMapper = (rs, rowNum) -> {
        Distrito dist = new Distrito();
        dist.setCodigo(rs.getLong("codigo"));
        dist.setNombre(rs.getString("nombre"));
        dist.setCodigoProvincia(rs.getLong("codigo_provincia"));
        dist.setEstado(rs.getBoolean("estado"));
        return dist;
    };
    
    public List<Distrito> findByProvincia(Long codigoProvincia) {
        // Usar los nombres de las columnas en minúsculas (snake_case en MySQL)
        String sql = "SELECT codigo, nombre, codigo_provincia, estado " +
                    "FROM distrito " +
                    "WHERE codigo_provincia = ? AND estado = 1 " +
                    "ORDER BY nombre";
        return jdbcTemplate.query(sql, rowMapper, codigoProvincia);
    }
    
    public Distrito findByCodigo(Long codigo) {
        String sql = "SELECT codigo, nombre, codigo_provincia, estado FROM distrito WHERE codigo = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, codigo);
    }
}
