package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.Repartidor;
import com.example.SistemaDePromociones.repository.mapper.RepartidorRowMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class JdbcRepartidorRepository {

    private final JdbcTemplate jdbc;

    public JdbcRepartidorRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public boolean existsByNumeroLicencia(String numeroLicencia) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(1) FROM repartidor WHERE NumeroLicencia = ?",
                Integer.class, numeroLicencia);
        return count != null && count > 0;
    }

    public Optional<Repartidor> findByNumeroLicencia(String numeroLicencia) {
        var list = jdbc.query(
                "SELECT * FROM repartidor WHERE NumeroLicencia = ? LIMIT 1",
                new RepartidorRowMapper(), numeroLicencia);
        return list.stream().findFirst();
    }

    public Repartidor save(Repartidor r) {
        String sql = "INSERT INTO repartidor (CodigoUsuario, NumeroLicencia, CodigoTipoVehiculo, Disponible, CodigoEstadoAprobacion, FechaAprobacion, CodigoAprobador, MotivoRechazo, FechaCreacion, Estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbc.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, r.getCodigoUsuario());
                ps.setString(2, r.getNumeroLicencia());
                ps.setLong(3, r.getCodigoTipoVehiculo());
                ps.setBoolean(4, r.getDisponible() != null ? r.getDisponible() : Boolean.TRUE);
                ps.setLong(5, r.getCodigoEstadoAprobacion() != null ? r.getCodigoEstadoAprobacion() : 1L);
                if (r.getFechaAprobacion() != null) {
                    ps.setObject(6, java.sql.Timestamp.valueOf(r.getFechaAprobacion()));
                } else {
                    ps.setObject(6, null);
                }
                if (r.getCodigoAprobador() != null) {
                    ps.setLong(7, r.getCodigoAprobador());
                } else {
                    ps.setObject(7, null);
                }
                ps.setString(8, r.getMotivoRechazo());
                ps.setObject(9, java.sql.Timestamp.valueOf(r.getFechaCreacion() != null ? r.getFechaCreacion() : java.time.LocalDateTime.now()));
                ps.setBoolean(10, r.getEstado() != null ? r.getEstado() : Boolean.TRUE);
                return ps;
            }, keyHolder);
        } catch (DuplicateKeyException ex) {
            throw ex;
        }
        Number key = keyHolder.getKey();
        if (key != null) {
            r.setCodigo(key.longValue());
        }
        return r;
    }
}
