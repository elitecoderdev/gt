package cl.gob.sna.gtime.validaciones.repository;

import javax.sql.DataSource;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class DocTranTipoBultoRepository {
	
	private static final Logger LOG = Logger.getLogger(DocTranTipoBultoRepository.class);

	private static final String QUERY_COUNT_DOCTRANTIPOBULTO =		"SELECT"
																+ "          COUNT(*)"
																+ "        FROM"
																+ "          DOCTRANSPORTE.DOCTRANTIPOBULTO"
																+ "        WHERE"
																+ "          CODIGO = ? "
																+ "          AND ACTIVO = 'S'"; 

	
	@Autowired
	private DataSource dataSource;

	private DataSource getDataSource() {
		return dataSource;
	}

 
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	 
	
	public boolean isDocTranTipoBultoExistente(String tipoBulto) {
		LOG.debug("count DOCTRANTIPOBULTO tipoBulto: "+tipoBulto);

		this.jdbcTemplate = new JdbcTemplate(getDataSource());

 		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(QUERY_COUNT_DOCTRANTIPOBULTO, tipoBulto);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return id > 0;
	}
	
}
