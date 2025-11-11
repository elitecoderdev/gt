package cl.gob.sna.gtime.validaciones.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cl.gob.sna.gtime.validaciones.repository.dto.UnidadMedidaTo;

public class UnidadMedidaMapper implements RowMapper<UnidadMedidaTo> {
	public UnidadMedidaTo mapRow(ResultSet rs, int rowNum) throws SQLException {
		UnidadMedidaTo infoPersona = new UnidadMedidaTo();
		infoPersona.setCodigo(rs.getString("CODIGO"));
		infoPersona.setDescripcion(rs.getString("DESCRIPCION"));
 		return infoPersona;
	}
}