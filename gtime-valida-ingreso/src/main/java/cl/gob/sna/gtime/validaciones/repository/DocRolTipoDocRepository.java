package cl.gob.sna.gtime.validaciones.repository;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class DocRolTipoDocRepository {
	
	private static final Logger LOG = LoggerFactory.getLogger(DocRolTipoDocRepository.class);

	private static final String QUERY_COUNT_DOCROLTIPODOC =  "SELECT COUNT(*) FROM DOCUMENTOS.DOCROLTIPODOC WHERE TIPODOCUMENTO = ? and ROL = ? and ACTIVO = 'S'"; 
 
	public static final String ALMACENISTA  = "ALM";
	public static final String TRANSPORTISTA  = "TRA";
	public static final String EMISOR  = "EMI";
	public static final String CONSIGNANTE = "CNTE";
	public static final String CONSIGNATARIO = "CONS";
	
	@Autowired
	private DataSource dataSource;

	private DataSource getDataSource() {
		return dataSource;
	}

 
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	 
	
	public boolean isDocRolTipoDocExistente(String rol, String tipoDocumento) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

 		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(QUERY_COUNT_DOCROLTIPODOC, tipoDocumento, rol);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return id > 0;
	}
	
}
