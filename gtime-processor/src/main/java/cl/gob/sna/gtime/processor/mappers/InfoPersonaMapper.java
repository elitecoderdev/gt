package cl.gob.sna.gtime.processor.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cl.gob.sna.gtime.processor.vo.InfoPersona;

public class InfoPersonaMapper implements RowMapper<InfoPersona> {
	public InfoPersona mapRow(ResultSet rs, int rowNum) throws SQLException {
		InfoPersona infoPersona = new InfoPersona();
		infoPersona.setIdPersona(rs.getLong("ID_PERSONA"));
		infoPersona.setEmisor(rs.getString("NOMBRE_EMISOR"));
		infoPersona.setIdDocBase(rs.getLong("DOC_BASE_ID"));
		return infoPersona;
	}
}