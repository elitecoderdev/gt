package cl.gob.sna.gtime.validaciones.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cl.gob.sna.gtime.validaciones.repository.dto.LocacionTo;

public class LocacionMapper implements RowMapper<LocacionTo> {
	public LocacionTo mapRow(ResultSet rs, int rowNum) throws SQLException {
		LocacionTo locacion = new LocacionTo();
		locacion.setLocacion(rs.getInt("LOCACION"));
		locacion.setDescripcion(rs.getString("DESCRIPCION"));
		locacion.setCodigo(rs.getString("CODIGO"));
		
 		return locacion;
	}
}