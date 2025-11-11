package cl.gob.sna.gtime.validaciones.repository;

import javax.sql.DataSource;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class DocTipoObservacionRepository {
	
	private static final Logger LOG = Logger.getLogger(DocTipoObservacionRepository.class);

	private static final String QUERY_COUNT_DOCTIPOOBSERVACION =  " SELECT COUNT(*) "
																+ " FROM DOCUMENTOS.DOCTIPOOBSERVACION "
																+ " WHERE TIPODOCUMENTO = ? "
																+ " AND CODIGO = ? "
																+ " AND ACTIVA = 'S'"; 
 
 
	
	@Autowired
	private DataSource dataSource;

	private DataSource getDataSource() {
		return dataSource;
	}

 
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	 
	
	public boolean isDocTipoDocObservacionExistente(String tipoDocumento, String nombre) {
		LOG.debug("count DOCTIPOOBSERVACION tipoDocumento: "+tipoDocumento+" - cod: "+nombre);

		this.jdbcTemplate = new JdbcTemplate(getDataSource());

 		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(QUERY_COUNT_DOCTIPOOBSERVACION, tipoDocumento, nombre);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return id > 0;
	}
	
}
