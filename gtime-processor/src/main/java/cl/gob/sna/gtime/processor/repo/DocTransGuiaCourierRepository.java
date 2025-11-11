package cl.gob.sna.gtime.processor.repo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import cl.gob.sna.gtime.api.vo.Gtime;

@Repository
public class DocTransGuiaCourierRepository {

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

	private static final Logger log = LoggerFactory.getLogger(DocTransGuiaCourierRepository.class);

	public Boolean persisteDocTransGuiaCourier(Long idDocBase, Gtime gtime) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

		int result = jdbcTemplate.update(
				"INSERT INTO DOCTRANSPORTE.DOCTRANGUIACOURIER (" +
						"      DOCUMENTOTRANSPORTE\n," +
						"      IDVEHICULO\n, " +
						"      PARCIAL\n, " +
						"      IDENTIFICACIONVEHICULO,\n" +
						"      VIAJE,\n" +
						"      VEHICULO) VALUES (?, ?, ?, ?, ?, ?)",
				idDocBase, 0, gtime.getParcial(), " "," ", " ");
		return (result > 0);
	}
}
