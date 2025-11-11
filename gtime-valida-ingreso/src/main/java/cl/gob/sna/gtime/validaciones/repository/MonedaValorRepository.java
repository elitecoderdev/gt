package cl.gob.sna.gtime.validaciones.repository;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
 import org.springframework.stereotype.Repository;

import cl.gob.sna.gtime.validaciones.repository.dto.MonedaValorTo;
import cl.gob.sna.gtime.validaciones.repository.mapper.MonedaValorMapper;

@Repository
public class MonedaValorRepository {
	
	private static final Logger LOG = LoggerFactory.getLogger(MonedaValorRepository.class);

	private static final String QUERY_MONEDA_VALOR_BY_CODIGO = "SELECT CODIGO, NOMBRE FROM ADMINISTRACION.ADMVALORFINANCIERO WHERE CODIGO = ? AND ACTIVA = 'S'"; 
	
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
	
	public MonedaValorTo obtenerMonedaValorByCodigo(String codMoneda) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

		try{
			return jdbcTemplate.queryForObject( QUERY_MONEDA_VALOR_BY_CODIGO,
					new MonedaValorMapper(),
					codMoneda);
		}catch (Exception e){
			return null;
		}
	}
	
}
