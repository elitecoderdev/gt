package cl.gob.sna.gtime.processor.repo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import cl.gob.sna.gtime.api.vo.Transbordo;
import cl.gob.sna.gtime.processor.util.FechaUtil;
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
public class DocTransbordosRepository {

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

	private static final Logger log = LoggerFactory.getLogger(DocTransbordosRepository.class);

	private long obtieneSecuenciaDocTranGcTransbordos(Long idDocBase) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		String sql = "SELECT NVL(MAX(ID), 0) FROM DOCTRANSPORTE.DOCTRANGCTRANSBORDOS WHERE DOCUMENTOTRANSPORTE = ? ";
		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, idDocBase);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return id;
	}

	public Boolean persisteDocTranGcTransbordos(Long idDocBase, Transbordo tra, int codLocacion) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		long id = obtieneSecuenciaDocTranGcTransbordos(idDocBase) + 1;
		String codLugarTrans = tra.getCodLugar().toUpperCase().trim();
		String lugarTrans = tra.getDescripcionLugar()!=null?tra.getDescripcionLugar():" ";
		Date fechaArr = FechaUtil.parseToFechaBD(tra.getFechaArribo());

		int result = jdbcTemplate.update(
				"INSERT INTO DOCTRANSPORTE.DOCTRANGCTRANSBORDOS (" +
						"      DOCUMENTOTRANSPORTE\n," +
						"      ID\n, " +
						"      IDVEHICULO\n, " +
						"      VIAJE\n, " +
						"      IDLUGARTRANSBORDO,\n" +
						"      CODLUGARTRANSBORDO,\n" +
						"      LUGARTRANSBORDO,\n" +
						"      CODMODOTRANSPORTE,\n" +
						"      MODOTRANSPORTE,\n" +
						"      IDENTIFICACIONVEHICULO,\n" +
						"      FECHAARRIBO,\n" +
						"      ACTIVA,\n" +
						"      FECHAACTIVA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				idDocBase, id, "", "",codLocacion, codLugarTrans, lugarTrans, " ", " ","", fechaArr,"S",new Date());
		return (result > 0);
	}
}
