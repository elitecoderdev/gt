package cl.gob.sna.gtime.validaciones.repository;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class OperadorRepository {
	
	private static final Logger LOG = LoggerFactory.getLogger(OperadorRepository.class);

	public static final Integer TIPO_OPERADOR_COURIER = 10; 
	private static final String QUERY_COUNT_RUT_OPERADOR =  "SELECT COUNT(*)  FROM  ADMSIROTE.NWOP_OPERADOR op "
															+ "    INNER JOIN ADMSIROTE.NWOP_OPERACION_ADUANA top on top.OPADU_RUT_OPERADOR = op.RUT_OPERADOR "
															+ "    WHERE "
															+ "    top.TIPO_OPERADOR = ? " //COURIER
															+ "	   AND op.RUT_OPERADOR = ? "
															+ "    AND top.ESTADOOPER = 1 "
															+ "    AND top.ESTADO = '1' "
															; 
 
	@Autowired
	private DataSource dataSource;

	private DataSource getDataSource() {
		return dataSource;
	}

 
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	 
	
	public boolean isOperadorExistente(String rutOperador, Integer tipoOperador) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

 		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(QUERY_COUNT_RUT_OPERADOR, tipoOperador, rutOperador);
		if(sqlRowSet.next()) {
			long id = sqlRowSet.getLong(1);
			return id > 0;
		}else {
			return false;
		}
	}
	
}
