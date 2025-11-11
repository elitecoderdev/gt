package cl.gob.sna.gtime.validaciones.repository;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class DocTipoDocumentoRepository {
	
	private static final Logger LOG = LoggerFactory.getLogger(DocTipoDocumentoRepository.class);

	private static final String QUERY_COUNT_DOCTIPODOCUMENTO =  "SELECT COUNT(*) FROM DOCUMENTOS.DOCTIPODOCUMENTO dtd WHERE dtd.CODIGO = ? and dtd.ACTIVO = 'S'"; 
 
 
	
	@Autowired
	private DataSource dataSource;

	private DataSource getDataSource() {
		return dataSource;
	}

 
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	 
	
	public boolean isDocTipoDocumentoExistente(String tipoDocumento) {
		LOG.debug("count DOCROLTIPODOC tipoDocumento: {}",tipoDocumento);

		this.jdbcTemplate = new JdbcTemplate(getDataSource());

 		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(QUERY_COUNT_DOCTIPODOCUMENTO, tipoDocumento);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return id > 0;
	}
	
}
