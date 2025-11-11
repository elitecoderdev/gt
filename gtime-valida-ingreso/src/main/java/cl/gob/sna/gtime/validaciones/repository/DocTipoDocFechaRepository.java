package cl.gob.sna.gtime.validaciones.repository;

import javax.sql.DataSource;

import cl.gob.sna.gtime.validaciones.ingreso.beans.ValidacionFechasBeans;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository 
public class DocTipoDocFechaRepository {
	final Logger LOG = Logger.getLogger(DocTipoDocFechaRepository.class);

	private static final String QUERY_COUNT_TIPO_FECHA_GTIME = 	  "SELECT COUNT(*) FROM DOCUMENTOS.DOCTIPODOCFECHA WHERE  TIPODOCUMENTO = ? and TIPOFECHA = ? and ACTIVA = 'S'"; 
	public static final String FECHA_CREACION = "FECREACION";
	public static final String FECHA_FEM = "FEM";
	public static final String TIPO_DOC_GTIME = "GTIME";
	@Autowired
	private DataSource dataSource;

	private DataSource getDataSource() {
		return dataSource;
	}

 
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	 
	
	public boolean isFechaValidaTipoDocumento(String tipoDocumento, String tipoFecha) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
 		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(QUERY_COUNT_TIPO_FECHA_GTIME, tipoDocumento, tipoFecha);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		
		return id>0;
	}
	
}
