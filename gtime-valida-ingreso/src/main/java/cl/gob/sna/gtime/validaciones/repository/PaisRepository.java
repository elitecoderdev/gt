package cl.gob.sna.gtime.validaciones.repository;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class PaisRepository {
	
	private static final Logger LOG = LoggerFactory.getLogger(PaisRepository.class);

	private static final String QUERY_COUNT_COD_PAIS =  "SELECT COUNT(*) FROM LOCACIONES.LOCPAIS WHERE  CODIGO = ? and ACTIVA = 'S'"; 
 
	@Autowired
	private DataSource dataSource;

	private DataSource getDataSource() {
		return dataSource;
	}

 
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	 
	
	public boolean isPaisExistente(String codPais) {
		//LOG.debug("count pais cod: "+codPais);

		this.jdbcTemplate = new JdbcTemplate(getDataSource());

 		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(QUERY_COUNT_COD_PAIS, codPais);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return id > 0;
	}
	
}
