package cl.gob.sna.gtime.validaciones.repository;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class PerPersonaRepository {
	
	private static final Logger LOG = LoggerFactory.getLogger(PerPersonaRepository.class);

	private static final String QUERY_ID_BY_PER_VALORIDENTIFICADOR = "SELECT MIN(v.persona) "
																+ " FROM admpersonas.per_valoridentificador v "
																+ " INNER JOIN admpersonas.per_persona p ON p.id = v.persona AND p.activa = 'S' "
																+ " WHERE v.activa = 'S' "
																+ " AND v.tipoidentificador = ? "
																+ " AND v.nacionalidad = ? "
																+ " AND upper(v.valor)  = upper(?)"; 
	private static final String QUERY_COUNT_BY_PER_VALORIDENTIFICADOR = "SELECT"
																		+ "          COUNT(*)" 
																		+ "        FROM"
																		+ "          ADMPERSONAS.PER_VALORIDENTIFICADOR v"
																		+ "          INNER JOIN ADMPERSONAS.PER_PERSONA p ON p.ID = v.PERSONA AND p.ACTIVA = 'S'"
																		+ "        WHERE"
																		+ "          v.ACTIVA = 'S'"
																		+ "          AND v.NACIONALIDAD = ?"
																		+ "          AND v.TIPOIDENTIFICADOR = ?"
																		+ "          AND v.VALOR = ?";
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
	
	public long obtenerIdPersona(String tipoId, String valorId, String nacionId) {
		LOG.debug("buscando persona tipoId: {} - valorId: {} - nacionId: {}",tipoId, valorId, nacionId);
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

 		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(QUERY_ID_BY_PER_VALORIDENTIFICADOR, tipoId, nacionId, valorId);
 		sqlRowSet.next();
		return sqlRowSet.getLong(1);
	}
	
	public boolean existePersona(String tipoId, String valorId, String nacionId) {
		LOG.debug("buscando moneda tipoId: {} - valorId: {} - nacionId: {}",tipoId, valorId, nacionId);
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

 		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(QUERY_COUNT_BY_PER_VALORIDENTIFICADOR, nacionId, tipoId,  valorId);
 		sqlRowSet.next();
		return sqlRowSet.getLong(1) > 0;
	}
	
}
