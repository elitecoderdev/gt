package cl.gob.sna.gtime.validaciones.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cl.gob.sna.gtime.validaciones.repository.dto.DocumentoBaseTo;

public class DocumentoBaseMapper implements RowMapper<DocumentoBaseTo> {
	public DocumentoBaseTo mapRow(ResultSet rs, int rowNum) throws SQLException {
		DocumentoBaseTo to = new DocumentoBaseTo();
		to.setId(rs.getInt("ID"));
		to.setDocEstados(rs.getString("DOC_ESTADOS"));
		to.setNumeroExterno(rs.getString("NUMERO_EXTERNO"));
		to.setNumeroId(rs.getString("NUMERO_ID"));
		to.setTipoId(rs.getString("TIPO_ID"));
		to.setTipoManifiesto(rs.getString("TIPO_MANIFIESTO"));
		to.setViaTransporte(rs.getString("VIA_TRANSPORTE"));
 
		return to;
	}
}