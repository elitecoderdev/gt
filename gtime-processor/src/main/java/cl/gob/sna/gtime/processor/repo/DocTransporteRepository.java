package cl.gob.sna.gtime.processor.repo;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import cl.gob.sna.gtime.api.vo.Gtime;

@Repository
public class DocTransporteRepository {

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

	private static final Logger log = LoggerFactory.getLogger(DocTransporteRepository.class);

	private long obtieneSecuenciaDocItemTransporte(Long idDocBase) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		String sql = "SELECT NVL(MAX(ID), 0) as v_MAX_ID FROM DOCTRANSPORTE.DOCTRANITEMTRANSPORTE T WHERE T.DOCUMENTOTRANSPORTE = ? ";
		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, idDocBase);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return id;
	}

	public Boolean persisteDocTransporte(Long idDocBase, Gtime gtime) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		List<MapSqlParameterSource> params = new ArrayList<>();
		SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(getDataSource()).withSchemaName("DOCTRANSPORTE")
				.withTableName("DOCTRANDOCTRANSPORTE");

		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("ID", idDocBase);
		source.addValue("TOORDER", "N");
		source.addValue("TOTALBULTOS", gtime.getTotalBultos());
		source.addValue("TOTALPESO", gtime.getTotalPeso());
		source.addValue("UNIDADPESO", gtime.getUnidadPeso());
		source.addValue("TOTALVOLUMEN", gtime.getTotalVolumen()!=null?gtime.getTotalVolumen():0);
		source.addValue("UNIDADVOLUMEN", gtime.getUnidadVolumen()!=null?gtime.getUnidadVolumen():" ");
		source.addValue("VALORDECLARADO", gtime.getValorDeclarado());
		source.addValue("MONEDAVALORDECLARADO", gtime.getMonedaValor());
		source.addValue("TOTALITEM", gtime.getTotalItem());
		source.addValue("SENTIDO", gtime.getTipoOperacion());
		source.addValue("TOTALPESOBRUTO", 0);
		source.addValue("INSTRUCCIONES", " ");
		source.addValue("NOTIFICACION", " ");

		params.add(source);
		int result = jdbcInsert.execute(source);

		return (result > 0);
	}
}
