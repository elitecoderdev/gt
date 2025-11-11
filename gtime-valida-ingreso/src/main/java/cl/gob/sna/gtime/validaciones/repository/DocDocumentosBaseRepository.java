package cl.gob.sna.gtime.validaciones.repository;

import java.util.Date;

import javax.sql.DataSource;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import cl.gob.sna.gtime.validaciones.repository.dto.DocumentoBaseTo;
import cl.gob.sna.gtime.validaciones.repository.mapper.DocumentoBaseMapper;

@Repository 
public class DocDocumentosBaseRepository {
	private final Logger LOG = Logger.getLogger(DocDocumentosBaseRepository.class);
//TODO:evaluar optimizacion de query
	private static final String QUERY_COUNT_DOCDOCUMENTOBASE = 	  "SELECT COUNT(*) "
																+ "FROM   documentos.docdocumentobase db "
																+ "WHERE  db.tipodocumento = ? "
																+ "AND db.numeroexterno = ? "
																+ "AND db.idemisor = ? "
																+ "AND trim(db.fechaemision) = trim(?) "
																+ "AND db.activo = 'S' "
																+ "AND ( "
																+ "    SELECT "
																+ "        COUNT(*) "
																+ "    FROM "
																+ "        documentos.docestados "
																+ "    WHERE documento = db.id "
																+ "    AND tipoestado IN ('REZ', 'ANU') "
																+ ") = 0"; 
	
	private static final String QUERY_BY_TIPO_DOC_NUM_EXTERNO_FEC_EMISION = "SELECT"
 																			+ "    db.ID ID,"
																			+ "            dp.TIPOID TIPO_ID,"
																			+ "            dp.NUMEROID NUMERO_ID,"
																			+ "            (SELECT COUNT(*) FROM DOCUMENTOS.DOCESTADOS WHERE DOCUMENTO = db.ID AND TIPOESTADO = 'ANU' AND ACTIVA = 'S') DOC_ESTADOS,"
																			+ "            dt.VIATRANSPORTE VIA_TRANSPORTE,"
																			+ "            dt.TIPOMANIFIESTO TIPO_MANIFIESTO,"
																			+ "            db.NUMEROEXTERNO NUMERO_EXTERNO"
																			+ "         FROM"
																			+ "            DOCUMENTOS.DOCDOCUMENTOBASE db"
																			+ "            INNER JOIN DOCUMENTOS.DOCTIPODOCUMENTO td ON td.CODIGO = db.TIPODOCUMENTO AND td.ACTIVO = 'S'"
																			+ "            INNER JOIN DOCUMENTOS.DOCPARTICIPACION dp ON dp.DOCUMENTO = db.ID AND dp.ACTIVA = 'S' AND dp.ROL = 'EMI'"
																			+ "            INNER JOIN DOCTRANSPORTE.DOCTRANMANIFIESTO dt on dt.ID = db.ID"
																			+ "          WHERE"
																			+ "            db.TIPODOCUMENTO = ?"
																			+ "            AND db.NUMEROEXTERNO = ?"
																			+ "            AND trim(db.FECHAEMISION) = trim(?)"
																			+ "            AND db.ACTIVO = 'S'";
 
 	@Autowired
	private DataSource dataSource;

	private DataSource getDataSource() {
		return dataSource;
	}

 
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	 
	
	public boolean existDocBaseByEmisor(String tipoDocumento,String nroReferencia, Long idPersonaEmisor, Date fem) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

		try{
			java.sql.Date fecha = new java.sql.Date(fem.getTime());

			final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(QUERY_COUNT_DOCDOCUMENTOBASE,
					tipoDocumento,
					nroReferencia,
					idPersonaEmisor,
					fecha );

			sqlRowSet.next();

			long id = sqlRowSet.getLong(1);

			return id > 0;
		}catch (Exception e){
			return false;
		}
	}
	
	public DocumentoBaseTo obtenerByTipoDocNumExternoFechaEmision(String tipoDocumento, String numeroExterno, Date fechaEmision) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

		DocumentoBaseTo docuBT = null;

		try{

			java.sql.Date fecha = new java.sql.Date(fechaEmision.getTime());
			docuBT =  jdbcTemplate.queryForObject(QUERY_BY_TIPO_DOC_NUM_EXTERNO_FEC_EMISION,
					new DocumentoBaseMapper(),
					tipoDocumento,
					numeroExterno,
					fecha );

			return docuBT;
		}catch (Exception e){
			return docuBT;
		}
	}
}
