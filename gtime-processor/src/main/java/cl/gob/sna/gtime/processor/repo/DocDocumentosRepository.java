package cl.gob.sna.gtime.processor.repo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import cl.gob.sna.gtime.processor.util.FechaUtil;
import cl.gob.sna.gtime.vo.Persistencia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cl.gob.sna.gtime.api.vo.Gtime;

@Repository
public class DocDocumentosRepository {

	private static final String DOCDOCUMENTOBASE = "DOCDOCUMENTOBASE";
	private static final String DOCUMENTOS = "DOCUMENTOS";

	private static final Date FECHA_FIN_VIGENCIA = new Date();
	private static final Date FIN_FECHA_VIGENCIA = FECHA_FIN_VIGENCIA;

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

	private static final Logger log = LoggerFactory.getLogger(DocDocumentosRepository.class);

	@Transactional
	public long insertaGtime(Gtime gtime, Date fechaEmision, long idEmisor, String emisor, String numeroAceptacion,
							 String user) {
		long id = obtieneSecuenciaDocBase();

		int result = jdbcTemplate.update(
				"INSERT INTO DOCUMENTOS.DOCDOCUMENTOBASE (" +
						"      ID\n, " +
						"      TIPODOCUMENTO\n, " +
						"      VERSION,\n" +
						"      VERSIONTIPODOC,\n" +
						"      NUMEROEXTERNO,\n" +
						"      LOGINCREADOR,\n" +
						"      LOGINDIGITADOR,\n" +
						"      CREADOR,\n" +
						"      FECHACREACION,\n" +
						"      FECHAEMISION,\n" +
						"      FECHAFINVIGENCIA,\n" +
						"      FECHAVERSION,\n" +
						"      ACTIVO,\n" +
						"      IDEMISOR,\n" +
						"      EMISOR,\n" +
						"      ARCHIVOXMLORIGEN,\n" +
						"      NUMEROACEPTACION,\n" +
						"      CODAREACREADOR) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				id, gtime.getTipo(), 0, gtime.getVersion() + ".0", gtime.getNumeroReferencia(),gtime.getLogin(),
				gtime.getLoginDigitador(), gtime.getUser(), new Date(), fechaEmision, FechaUtil.parseFechaFinVigencia(),
				new Date(), "S", idEmisor, emisor,  gtime.getNumeroReferencia() + ".XML",numeroAceptacion," ");

		if (result > 0) {
			log.info(" [ + ] GTIME ha sido insertado con id {}", id);
			return id;
		}
		return result;
	}

	private long obtieneSecuenciaDocBase() {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

		String sql = "SELECT DOCUMENTOS.NEXTID.NEXTVAL FROM dual";
		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return id;
	}

	public String obtieneViaTransporte(String tipoDocumento, String numeroAceptacion,  String fechaEmision) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

		String sql = "SELECT dt.VIATRANSPORTE "
				+ "FROM DOCUMENTOS.DOCDOCUMENTOBASE db "
				+ "INNER JOIN DOCUMENTOS.DOCTIPODOCUMENTO td ON td.CODIGO = db.TIPODOCUMENTO AND td.ACTIVO = 'S'	 "
				+ "INNER JOIN DOCUMENTOS.DOCPARTICIPACION dp ON dp.DOCUMENTO = db.ID AND dp.ACTIVA = 'S' AND dp.ROL = 'EMI' "
				+ "INNER JOIN DOCTRANSPORTE.DOCTRANMANIFIESTO dt on dt.ID = db.ID  "
				+ "WHERE db.TIPODOCUMENTO = '" + tipoDocumento + "'  "
				+ "  AND db.NUMEROACEPTACION = '" +  numeroAceptacion + " ' "
				+ "AND db.FECHAEMISION = ' " + fechaEmision + " '  "
				+  "AND db.ACTIVO = 'S' ";

		List<String> viaTransporteList = jdbcTemplate.query(sql,new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		} );

		if (viaTransporteList != null && !viaTransporteList.isEmpty()
				&& viaTransporteList.get(0) != null){
			return viaTransporteList.get(0);
		}else{
			return null;
		}
	}

	/**
	 *
	 * @param numeroReferencia
	 * @param fechaEmision
	 * @param numeroAceptacion
	 * @param idEmisor
	 * @return
	 */
	public boolean checkConstraintUniqueGtime(String tipoDocumento, String numeroReferencia, Date fechaEmision,
											  String numeroAceptacion, long idEmisor) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

		String sql = "SELECT COUNT(*) FROM " +
				"DOCUMENTOS.DOCDOCUMENTOBASE D " +
				"WHERE D.TIPODOCUMENTO = ?  " +
				"AND D.NUMEROEXTERNO = ?   " +
				"AND D.FECHAEMISION = ?  " +
				"AND D.VERSION = '0' " +
				"AND D.NUMEROACEPTACION = ? " +
				"AND D.IDEMISOR = ? ";

		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, tipoDocumento,
				numeroReferencia, fechaEmision, numeroAceptacion, idEmisor);
		sqlRowSet.next();

		long totalGtime = sqlRowSet.getLong(1);

		return (totalGtime > 0);
	}

	/**
	 *
	 * @param idDocBase
	 * @param persistencia
	 */
	public void deleteFromRollback(Long idDocBase, Persistencia persistencia){
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		if (idDocBase != null){
			this.jdbcTemplate.
					update("DELETE FROM " + persistencia.getSchemaTable() + "." + persistencia.getTipo().name() +" WHERE " + persistencia.getPrimariKey() +" = " + idDocBase);
		}

	}

	/**
	 *
	 * @param idDocBase
	 */
	public void deleteFromIdDocBase(Long idDocBase){
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		if (idDocBase != null){
			this.jdbcTemplate.
					update("DELETE FROM DOCUMENTOS.DOCDOCUMENTOBASE WHERE ID = " + idDocBase);

		}
	}
}