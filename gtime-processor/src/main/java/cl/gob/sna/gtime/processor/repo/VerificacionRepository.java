package cl.gob.sna.gtime.processor.repo;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class VerificacionRepository {

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

	private static final Logger log = LoggerFactory.getLogger(VerificacionRepository.class);

	public boolean isMftoConformado(Long idMfto) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		String sql = "SELECT COUNT(*) as v_ES_MFTOC_CONFORMADO FROM DOCUMENTOS.DOCESTADOS WHERE DOCUMENTO = ? AND TIPOESTADO = 'CMP' AND ACTIVA = 'S' ";
		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, idMfto);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return (id > 0);	
		}



}
