package cl.gob.sna.gtime.processor.repo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import cl.gob.sna.gtime.processor.util.FechaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import cl.gob.sna.gtime.api.vo.Observacion;

@Repository
public class DocObservacionRepository {

	private static final String DOCOBSERVACION = "DOCOBSERVACION";
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

	private static final Logger log = LoggerFactory.getLogger(DocObservacionRepository.class);

	private long obtieneSecuenciaDocObservacion(long idDocBase) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

		String sql = "SELECT NVL(MAX(D.ID), 0) as id FROM DOCUMENTOS.DOCOBSERVACION D WHERE D.DOCUMENTO = ?";
		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, idDocBase);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return id;
	}

	public boolean persisteObservacion(long idDocBase, Observacion obs, String login,
									   String tipoDocumento, int pos) {
		long id =  obtieneSecuenciaDocObservacion(idDocBase) + pos;
		String tipoObs = obs.getNombre()!=null?obs.getNombre().trim().toUpperCase():" ";
		int result = jdbcTemplate.update(
				"INSERT INTO DOCUMENTOS.DOCOBSERVACION (" +
						"      DOCUMENTO\n," +
						"      ID\n, " +
						"      FECHA,\n" +
						"      OBSERVACION,\n" +
						"      LOGINUSUARIO,\n" +
						"      TIPODOCUMENTO,\n" +
						"      TIPOOBSERVACION) VALUES (?, ?, ?, ?, ?, ?, ?)",
				idDocBase, id, FechaUtil.getFechaActualBD(), obs.getContenido(),login, tipoDocumento, tipoObs);
		return (result > 0);
	}

}
