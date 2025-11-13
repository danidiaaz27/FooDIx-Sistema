package com.example.SistemaDePromociones.repository.mapper;

import com.example.SistemaDePromociones.model.Repartidor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RepartidorRowMapper implements RowMapper<Repartidor> {
    @Override
    public Repartidor mapRow(ResultSet rs, int rowNum) throws SQLException {
        Repartidor r = new Repartidor();
        r.setCodigo(rs.getLong("Codigo"));
        r.setCodigoUsuario(rs.getLong("CodigoUsuario"));
        r.setNumeroLicencia(rs.getString("NumeroLicencia"));
        r.setCodigoTipoVehiculo(rs.getLong("CodigoTipoVehiculo"));
        r.setDisponible(rs.getBoolean("Disponible"));
        r.setCodigoEstadoAprobacion(rs.getLong("CodigoEstadoAprobacion"));
        java.sql.Timestamp ts = rs.getTimestamp("FechaAprobacion");
        if (ts != null) r.setFechaAprobacion(ts.toLocalDateTime());
        long codigoAprobador = rs.getLong("CodigoAprobador");
        if (!rs.wasNull()) r.setCodigoAprobador(codigoAprobador);
        r.setMotivoRechazo(rs.getString("MotivoRechazo"));
        java.sql.Timestamp fc = rs.getTimestamp("FechaCreacion");
        if (fc != null) r.setFechaCreacion(fc.toLocalDateTime());
        r.setEstado(rs.getBoolean("Estado"));
        return r;
    }
}
