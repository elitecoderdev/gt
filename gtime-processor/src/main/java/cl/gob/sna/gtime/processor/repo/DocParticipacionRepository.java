package cl.gob.sna.gtime.processor.repo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import cl.gob.sna.gtime.api.vo.Participacion;
import cl.gob.sna.gtime.processor.util.FechaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;


@Repository
public class DocParticipacionRepository {


	private static final String DOCPARTICIPACION = "DOCPARTICIPACION";
	private static final String DOCUMENTOS = "DOCUMENTOS";
	private static final int PORCENTAJE_PARTICIPACION_0 = 0;
	private static final String ACTIVO_N = "N";
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

	private static final Logger log = LoggerFactory.getLogger(DocParticipacionRepository.class);

	private long obtieneSecuenciaDocParticipacion() {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

		String sql = "select DOCUMENTOS.SEC_DOCPARTICIPACION.NEXTVAL from dual";
		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return id;
	}

	public boolean persisteParticipacion(long idDocBase, Participacion participacion, String idPersonaParticipante) {
		Integer idPersistPart = null;

		if (idPersonaParticipante != null) idPersistPart = Integer.valueOf(idPersonaParticipante);

		int result = jdbcTemplate.update(
				"INSERT INTO DOCUMENTOS.DOCPARTICIPACION (" +
						"      DOCUMENTO\n," +
						"      ROL\n, " +
						"      ID,\n" +
						"      IDPERSONAPARTICIPANTE,\n" +
						"      NOMBREPARTICIPANTE,\n" +
						"      CODIGOPAIS,\n" +
						"      TIPOID,\n" +
						"      NACID,\n" +
						"      NUMEROID,\n" +
						"      DIRECCION,\n" +
						"      PORCENTAJEPARTICIPACION,\n" +
						"      FECHAPARTICIPACION,\n" +
						"      ACTIVA,\n" +
						"      FECHAACTIVA,\n" +
						"      FECHADESACTIVA,\n" +
						"      COMUNA,\n" +
						"      DOCUMENTAL) VALUES (?, ?, DOCUMENTOS.SEC_DOCPARTICIPACION.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				idDocBase, participacion.getNombre(), idPersistPart, participacion.getNombres(), participacion.getCodigoPais(),
				participacion.getTipoId(), participacion.getNacionId(), participacion.getValorId(),
				participacion.getDireccion(),PORCENTAJE_PARTICIPACION_0, new Date(), ACTIVO_S, new Date(),
				FechaUtil.parseFechaFinVigencia(), participacion.getComuna(), ACTIVO_N);

		return (result > 0);
	}

	public long getIdPersonaEmisor(String tipoId, String nacionId, String valorId){
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

	    String sql = "SELECT MIN(v.persona) "
				+ " FROM admpersonas.per_valoridentificador v "
				+ " INNER JOIN admpersonas.per_persona p ON p.id = v.persona AND p.activa = 'S' "
				+ " WHERE v.activa = 'S' "
				+ " AND v.tipoidentificador = ? "
				+ " AND v.nacionalidad = ? "
				+ " AND upper(v.valor)  = upper(?) ";

		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, tipoId, nacionId, valorId);

		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);

		return id;
	}



}
