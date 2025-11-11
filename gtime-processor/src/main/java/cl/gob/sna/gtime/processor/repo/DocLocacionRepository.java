package cl.gob.sna.gtime.processor.repo;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import cl.gob.sna.gtime.processor.util.FechaUtil;
import cl.gob.sna.gtime.processor.vo.Locacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;


@Repository
public class DocLocacionRepository {

	private static final String DOCLOCACIONDOCUMENTO = "DOCLOCACIONDOCUMENTO";
	private static final String DOCUMENTOS = "DOCUMENTOS";

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

	private static final Logger log = LoggerFactory.getLogger(DocLocacionRepository.class);

	public int obtieneInfoLocacion(String codLocacion, String viaTransporte) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		try {
			String sql = "SELECT ll.LOCACION " +
					"FROM  LOCACIONES.LOCLOCACION ll " +
					"LEFT JOIN LOCACIONES.LOCCODIGOVALOR tipo ON (tipo.LOCACION = ll.LOCACION AND tipo.ACTIVA = 'S' ) " +
					"LEFT JOIN LOCACIONES.LOCLOCACIONPORTIPO c ON (c.LOCACION = ll.LOCACION  AND c.ACTIVA = 'S'	) " +
					"WHERE c.TIPOLOCACION = DECODE('"+ viaTransporte +"', '7', 7, '07', 7, 5) " +
					"AND tipo.TIPOCODIGO =  DECODE('"+ viaTransporte +"', '7', 'UN', '07', 'UN', 'IATA') " +
					"AND (( '" + viaTransporte + "' IN ('7', '07') AND ll.CODIGO = '"+ codLocacion +"') " +
					"OR ( '" + viaTransporte + "' NOT IN ('7', '07') AND tipo.VALOR = '" + codLocacion + "'))";

			final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);

			sqlRowSet.next();
			int id = sqlRowSet.getInt(1);

			return id;
		}catch (Exception e){
			return 0;
		}

	}

	public void persisteLocaciones(final List<Locacion> lista) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		final int batchSize = 10;
		final String QUERY_SAVE = "INSERT INTO DOCUMENTOS.DOCLOCACIONDOCUMENTO values (?,?,?,?,?,?,?,?,?)";

		jdbcTemplate.batchUpdate(QUERY_SAVE, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Locacion locacion = lista.get(i);
				ps.setLong(1, locacion.getIdDocBase());
				ps.setString(2, locacion.getNombre());
				ps.setLong(3, locacion.getIdLocacion());
				ps.setString(4, locacion.getCodigo());
				ps.setString(5, locacion.getDescripcion());
				ps.setLong(6, 0);
				ps.setString(7, "S");
				ps.setTimestamp(8, FechaUtil.parseFechaHoyRS());
				ps.setTimestamp(9, FechaUtil.parseFechaFinVigenciaRS());
			}

			@Override
			public int getBatchSize() {
				if (batchSize > lista.size()) {
					return lista.size();
				}
				return batchSize;
			}
		});
	}
}
