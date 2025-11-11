package cl.gob.sna.gtime.processor.repo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import cl.gob.sna.gtime.api.vo.Fecha;
import cl.gob.sna.gtime.processor.util.FechaUtil;

@Repository
public class DocFechaDocumentoRepository {


	private static final String DOCUMENTOS = "DOCUMENTOS";

	private static final String DOCFECHADOCUMENTO = "DOCFECHADOCUMENTO";

	private static final String ACTIVO_S = "S";

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

	private static final Logger log = LoggerFactory.getLogger(DocFechaDocumentoRepository.class);

	public boolean persisteFechas(long idDocBase, Date valorFecha, String tipoFecha) {
		int result = jdbcTemplate.update(
				"INSERT INTO DOCUMENTOS.DOCFECHADOCUMENTO (" +
						"      DOCUMENTO\n," +
						"      TIPOFECHA\n, " +
						"      FECHA,\n" +
						"      ACTIVA,\n" +
						"      FECHAACTIVA,\n" +
						"      FECHADESACTIVA) VALUES (?, ?, ?, ?, ?, ?)",
				idDocBase, tipoFecha, valorFecha, ACTIVO_S,new Date(),  FechaUtil.parseFechaFinVigencia());

		return (result > 0);
	}


	public void saveFechaList(final Long idDocBase, final List<Fecha> listaFechas) {
		final String QUERY_SAVE = "INSERT INTO DOCUMENTOS.DOCFECHADOCUMENTO values (?,?,?,?,?,?)";

		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		final int batchSize = 10;
		java.util.Date date = Calendar.getInstance().getTime();
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());

		jdbcTemplate.batchUpdate(QUERY_SAVE, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Fecha fecha = listaFechas.get(i);

				java.sql.Date valorFecha = null;

				if (fecha.getNombre().trim().equalsIgnoreCase("FEM")){
					valorFecha = FechaUtil.parseFechaDoc(fecha.getValor());
				}else{
					valorFecha = FechaUtil.parseFechaDocAlt(fecha.getValor());
				}

				ps.setLong(1, idDocBase);
				ps.setString(2, fecha.getNombre());
				ps.setDate(3, valorFecha);
				ps.setString(4, ACTIVO_S);
				ps.setDate(5, FechaUtil.getFechaHoy());
				ps.setDate(6,  FechaUtil.getFechaFinVigencia());

			}

			@Override
			public int getBatchSize() {
				if (batchSize > listaFechas.size()) {
					return listaFechas.size();
				}
				return batchSize;
			}
		});
	}

	public boolean generaFechaCreacion(Long idDocBase) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		List<MapSqlParameterSource> params = new ArrayList<>();
		SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(getDataSource()).withSchemaName(DOCUMENTOS)
				.withTableName(DOCFECHADOCUMENTO);

		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("DOCUMENTO", idDocBase);
		source.addValue("TIPOFECHA", "FECREACION");
		source.addValue("FECHA", new Date());
		source.addValue("ACTIVA", "S");
		source.addValue("FECHAACTIVA", new Date());
		source.addValue("FECHADESACTIVA", FechaUtil.getFechaFinVigencia());
		params.add(source);
		int result = jdbcInsert.execute(source);

		return (result > 0);
	}

}
