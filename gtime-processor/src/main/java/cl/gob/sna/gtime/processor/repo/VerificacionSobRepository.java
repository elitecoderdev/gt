package cl.gob.sna.gtime.processor.repo;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class VerificacionSobRepository {

	@Autowired
	DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final Logger log = LoggerFactory.getLogger(VerificacionSobRepository.class);

	public boolean isSobrante(Long idMfto) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		String sql = "SELECT COUNT(*) as v_EXISTE FROM DOCUMENTOS.DOCOBSERVACION WHERE DOCUMENTO = ? AND TIPOOBSERVACION = 'SOB' ";
		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, idMfto);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return (id > 0);	
		}



}
