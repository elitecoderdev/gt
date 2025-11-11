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

import cl.gob.sna.gtime.api.vo.Referencia;

@Repository
public class DocRelacionRepository {

	private static final String DOCRELACIONDOCUMENTO = "DOCRELACIONDOCUMENTO";
	private static final String DOCUMENTOS = "DOCUMENTOS";
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

	private static final Logger log = LoggerFactory.getLogger(DocRelacionRepository.class);

	public boolean persisteRelacion(long idDocBase, Referencia ref, String login,
			Long emisorDocDestino, Long docDestino, String emisorB, Date fem) {

		java.sql.Date fechaDocDestino = FechaUtil.parseToFechaBD(ref.getFecha());
		Date fechaDesactiva = FechaUtil.parseFechaFinVigencia();
		java.sql.Date fecha = new java.sql.Date(fem.getTime());
		long id = obtieneSecuenciaDocRelacion();
		String tipoRerencia = ref.getTipoReferencia();
		String obs = ref.getObservaciones();
		String tipoDoc = ref.getTipoDocumento().trim().toUpperCase();
		String numDocDest = ref.getNumero().trim();
		String nombreEmi = ref.getEmisor();

		int result = jdbcTemplate.update(
				"INSERT INTO DOCUMENTOS.DOCRELACIONDOCUMENTO (" +
						"      TIPORELACION\n," +
						"      DOCORIGEN\n, " +
						"      ID,\n" +
						"      FECHA,\n" +
						"      OBSERVACION,\n" +
						"      IDUSUARIO,\n" +
						"      TIPODOCDESTINO,\n" +
						"      NUMDOCDESTINO,\n" +
						"      EMISORDOCDESTINO,\n" +
						"      NOMBREEMISOR,\n" +
						"      FECHADOCDESTINO,\n" +
						"      DOCDESTINO,\n" +
						"      ACTIVO,\n" +
						"      FECHAACTIVA,\n" +
						"      FECHADESACTIVA) VALUES (?, ?, DOCUMENTOS.SEC_DOCRELACIONDOCUMENTO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				tipoRerencia, idDocBase, fecha, obs, login, tipoDoc,  numDocDest, emisorDocDestino!=null?emisorDocDestino:emisorB,
				nombreEmi,fechaDocDestino, docDestino,ACTIVO_S,new Date(), fechaDesactiva);
		return (result > 0);
	}

	private long obtieneSecuenciaDocRelacion() {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

		String sql = "select DOCUMENTOS.SEC_DOCRELACIONDOCUMENTO.NEXTVAL from dual";
		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return id;
	}

}
