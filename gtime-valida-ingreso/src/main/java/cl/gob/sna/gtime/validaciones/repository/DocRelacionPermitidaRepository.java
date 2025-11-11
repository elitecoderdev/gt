package cl.gob.sna.gtime.validaciones.repository;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository 
public class DocRelacionPermitidaRepository {
	
 	private static final String QUERY_COUNT_DOCRELACIONPERMITIDA = 	  "SELECT"
																+ "          COUNT(*)"
																+ "        FROM"
																+ "          DOCUMENTOS.DOCRELACIONPERMITIDA drp"
																+ "          INNER JOIN DOCUMENTOS.DOCTIPORELACION dtr ON dtr.CODIGO = drp.TIPORELACION AND dtr.ACTIVA = 'S'"
																+ "        WHERE"
																+ "          drp.TIPODOCORIGEN = ?"
 																+ "          AND dtr.CODIGO = ?"
																+ "          AND drp.TIPODOCDESTINO = ?"
																+ "          AND drp.ACTIVO = 'S'"
																;
			 
 
 	@Autowired
	private DataSource dataSource;

	private DataSource getDataSource() {
		return dataSource;
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;
	 
	
	public boolean existdocRelacionPermitida(String tipoDocumentoOrigen,String tipoReferencia, String tipoDocumentoDestino) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
 
 		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(QUERY_COUNT_DOCRELACIONPERMITIDA, 
												 				tipoDocumentoOrigen, 
												 				tipoReferencia,
												 				tipoDocumentoDestino );
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		
		return id > 0;
	}
	
}
