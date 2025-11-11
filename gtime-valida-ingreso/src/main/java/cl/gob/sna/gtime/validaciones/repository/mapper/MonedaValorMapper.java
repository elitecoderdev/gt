package cl.gob.sna.gtime.validaciones.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cl.gob.sna.gtime.validaciones.repository.dto.MonedaValorTo;

public class MonedaValorMapper implements RowMapper<MonedaValorTo> {
	public MonedaValorTo mapRow(ResultSet rs, int rowNum) throws SQLException {
		MonedaValorTo monedaValor = new MonedaValorTo();
		monedaValor.setCodigo(rs.getString("CODIGO"));
		monedaValor.setNombre(rs.getString("NOMBRE"));
 		return monedaValor;
	}
}