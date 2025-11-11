package cl.gob.sna.gtime.validaciones.repository;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import cl.gob.sna.gtime.validaciones.repository.dto.UnidadMedidaTo;
import cl.gob.sna.gtime.validaciones.repository.mapper.UnidadMedidaMapper;

@Repository
public class UnidadMedidaRepository {
	private static final Logger LOG = LoggerFactory.getLogger(UnidadMedidaRepository.class);
	private static final String QUERY_BY_CODIGO = "SELECT CODIGO, DESCRIPCION FROM ADMINISTRACION.ADMUNIDADMEDIDA WHERE CODIGO = ? AND ACTIVA = 'S'"; 
	private static final String QUERY_COUNT_BY_CODIGO = "SELECT  COUNT(*) FROM ADMINISTRACION.ADMUNIDADMEDIDA WHERE CODIGO = ? AND ACTIVA = 'S'";
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
	
	public UnidadMedidaTo obtenerUnidadMedidaByCodigo(String codigo) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		try{
			return jdbcTemplate.queryForObject( QUERY_BY_CODIGO,
					new UnidadMedidaMapper(),
					codigo);
		}catch (Exception e){
			return null;
		}
	}

	public boolean isUnidadMedidaExistente(String codigo) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

 		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(QUERY_COUNT_BY_CODIGO, codigo);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return id > 0;
	}
}
