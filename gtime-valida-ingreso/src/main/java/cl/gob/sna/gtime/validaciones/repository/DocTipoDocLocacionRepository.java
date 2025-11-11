package cl.gob.sna.gtime.validaciones.repository;

import javax.sql.DataSource;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class DocTipoDocLocacionRepository {
	
	private static final Logger LOG = Logger.getLogger(DocTipoDocLocacionRepository.class);

	private static final String QUERY_COUNT_DOCTIPODOCLOCACION =  "SELECT"
																+ "            COUNT(*)"
																+ " FROM"
																+ " 	DOCUMENTOS.DOCTIPODOCLOCACION tdl"
																+ "     INNER JOIN DOCUMENTOS.DOCTIPOLOCACION tl ON tl.CODIGO = tdl.TIPOLOCACION"
																+ " WHERE"
																+ "		tdl.TIPODOCUMENTO = ?"
																+ "		AND tdl.ACTIVA = 'S'"
																+ "		AND tl.ACTIVA = 'S'"
																+ "		AND tl.CODIGO = ?"; 
 
 
	
	@Autowired
	private DataSource dataSource;

	private DataSource getDataSource() {
		return dataSource;
	}

 
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	 
	
	public boolean isDocTipoDocLocacionExistente(String tipoDocumento, String cod) {
		LOG.debug("count DOCTIPODOCLOCACION tipoDocumento: "+tipoDocumento+" - cod: "+cod);

		this.jdbcTemplate = new JdbcTemplate(getDataSource());

 		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(QUERY_COUNT_DOCTIPODOCLOCACION, tipoDocumento, cod);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return id > 0;
	}
	
}
