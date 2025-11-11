package cl.gob.sna.gtime.validaciones.repository;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cl.gob.sna.gtime.validaciones.repository.dto.LocacionTo;
import cl.gob.sna.gtime.validaciones.repository.mapper.LocacionMapper;

@Repository
public class LocacionRepository {
	private static final Logger LOG = LoggerFactory.getLogger(LocacionRepository.class);
	private static final String QUERY_UN_BY_CODIGO = "SELECT"
													+ "            LL.LOCACION LOCACION,"
													+ "            LL.DESCRIPCION DESCRIPCION,"
													+ "            LP.CODIGO CODIGO"
													+ "          FROM"
													+ "            LOCACIONES.LOCLOCACION ll"
													+ "            INNER JOIN LOCACIONES.LOCCODIGOVALOR tipo ON tipo.LOCACION = ll.LOCACION AND tipo.ACTIVA = 'S'"
													+ "            INNER JOIN LOCACIONES.LOCLOCACIONPORTIPO c ON c.LOCACION = ll.LOCACION AND c.ACTIVA = 'S'"
													+ "            LEFT JOIN LOCACIONES.LOCPAIS lp ON lp.PAIS = ll.PAIS AND lp.ACTIVA = 'S'"
													+ "          WHERE"
													+ "            c.TIPOLOCACION = 7"
													+ "            AND tipo.TIPOCODIGO = 'UN'"
													+ "            AND ll.CODIGO = ?"
													+ "            AND ll.ACTIVA = 'S'"
													+ "            AND ROWNUM <= 1";
	private static final String QUERY_IATA_BY_CODIGO = " SELECT"
													+ "            LL.LOCACION LOCACION,"
													+ "            LL.DESCRIPCION DESCRIPCION,"
													+ "            LP.CODIGO CODIGO"
													+ "          FROM"
													+ "            LOCACIONES.LOCLOCACION ll"
													+ "            INNER JOIN LOCACIONES.LOCCODIGOVALOR tipo ON tipo.LOCACION = ll.LOCACION AND tipo.ACTIVA = 'S'"
													+ "            INNER JOIN LOCACIONES.LOCLOCACIONPORTIPO c ON c.LOCACION = ll.LOCACION AND c.ACTIVA = 'S'"
													+ "            LEFT JOIN LOCACIONES.LOCPAIS lp ON lp.PAIS = ll.PAIS AND lp.ACTIVA = 'S' "
													+ "          WHERE"
													+ "            c.TIPOLOCACION = 5"
													+ "            AND tipo.TIPOCODIGO = 'IATA'"
													+ "            AND tipo.VALOR = ?"
													+ "            AND ll.ACTIVA = 'S'"
													+ "            AND ROWNUM <= 1";
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
	
	public LocacionTo obtenerLocacionUnByCodigo(String codigo) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		try{
			return jdbcTemplate.queryForObject( QUERY_UN_BY_CODIGO,
					new LocacionMapper(),
					codigo);
		}catch (Exception e){
			return null;
		}
	}
	public LocacionTo obtenerLocacionIataByCodigo(String codigo) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		try{
			return jdbcTemplate.queryForObject( QUERY_IATA_BY_CODIGO,
					new LocacionMapper(),
					codigo);
		}catch (Exception e){
			return null;
		}

	}
}
