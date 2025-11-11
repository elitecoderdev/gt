package cl.gob.sna.gtime.processor.repo;

import java.util.Date;
import javax.sql.DataSource;
import cl.gob.sna.gtime.processor.util.FechaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;


@Repository
public class DocEstadoRepository {

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

	private static final Logger log = LoggerFactory.getLogger(DocEstadoRepository.class);

	private long obtieneSecuenciaDocEstado(Long idDocBase, String tipoDocumento, String tipoEstado) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		String sql = "SELECT NVL(MAX(D.ID), 0) as v_MAX_ID FROM DOCUMENTOS.DOCESTADOS D WHERE D.DOCUMENTO = ? AND D.TIPODOCUMENTO = ? AND D.TIPOESTADO =  ? ";
		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, idDocBase, tipoDocumento, tipoEstado);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return id + 1;
	}

	/**
	 * @param idDocBase
	 * @param tipoEstado
	 * @param observacion
	 * @param login
	 * @return
	 */
	public Boolean persisteDocEstado(Long idDocBase, String tipoEstado,	String observacion, String login) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		String tipoDocumento = "GTIME";

		Date fechaEstado = new Date();
		//Long id = obtieneSecuenciaDocEstado(idDocBase, tipoDocumento, tipoEstado);
		String obs = observacion != null ? observacion : " ";

		int result = jdbcTemplate.update(
				"INSERT INTO DOCUMENTOS.DOCESTADOS (" +
						"      DOCUMENTO\n," +
						"      TIPODOCUMENTO\n, " +
						"      TIPOESTADO,\n" +
						"      ID,\n" +
						"      FECHA,\n" +
						"      OBSERVACION,\n" +
						"      IDUSUARIO,\n" +
						"      ACTIVA,\n" +
						"      FECHAACTIVA,\n" +
						"      FECHADESACTIVA) VALUES (?, ?, ?, DOCUMENTOS.SEC_DOCESTADOS.NEXTVAL, ?, ?, ?, ?, ?, ?)",
				idDocBase, tipoDocumento, tipoEstado, fechaEstado, obs, login, "S", new Date(),
				FechaUtil.parseFechaFinVigencia());
		return (result > 0);
	}

	/**
	 *
	 * @param idMfto
	 * @return
	 */
	public boolean esMftoConformado(String idMfto){
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		String sql
				= "SELECT COUNT(*) FROM DOCUMENTOS.DOCESTADOS WHERE DOCUMENTO = ? AND TIPOESTADO = 'CMP' AND ACTIVA = 'S'";

		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, idMfto);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);

		return (id > 0);
	}

	public boolean esMftoSob(String idMfto){
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		String sql
				= "SELECT COUNT(*) FROM DOCUMENTOS.DOCOBSERVACION WHERE DOCUMENTO = ? AND TIPOOBSERVACION = 'SOB'";

		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, idMfto);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);

		return (id > 0);
	}
}
