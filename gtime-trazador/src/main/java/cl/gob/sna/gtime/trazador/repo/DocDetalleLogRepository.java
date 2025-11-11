package cl.gob.sna.gtime.trazador.repo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class DocDetalleLogRepository {

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

	private static final Logger log = LoggerFactory.getLogger(DocDetalleLogRepository.class);

	private static final String DOCUMENTOS = "DOCUMENTOS";

	private static final String DOCDETALLELOG = "DOCDETALLELOG";

	@Transactional
	public Boolean insertaDetalleLog(String login, String nroReferencia, String mensaje, String nroEncabezado, String version, String tipoDocumento) {
		int result = jdbcTemplate.update(
				"INSERT INTO DOCUMENTOS.DOCDETALLELOG (" +
						"      TIPODOCORIGEN\n," +
						"      LOGIN\n, " +
						"      NUMEROREFERENCIA,\n" +
						"      ID,\n" +
						"      TEXTO,\n" +
						"      FECHACREACION,\n" +
						"      NROENCABEZADO,\n" +
						"      VERSIONTIPODOC) VALUES (?, ?, ?, DOCUMENTOS.SEC_DOCDETALLELOG.NEXTVAL, ?, ?, ?, ?)",
				tipoDocumento, login, nroReferencia,  mensaje, new Date(), nroEncabezado, version + ".0" );

		return (result > 0);
	}

	private long obtieneSecuenciaDocBase() {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		String sql = "select DOCUMENTOS.SEC_DOCDETALLELOG.NEXTVAL from dual";
		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return id;
	}


}
