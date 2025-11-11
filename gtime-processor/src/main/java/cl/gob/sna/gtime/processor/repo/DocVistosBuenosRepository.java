package cl.gob.sna.gtime.processor.repo;

import java.util.Date;


import javax.sql.DataSource;

import cl.gob.sna.gtime.api.vo.Vb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;


@Repository
public class DocVistosBuenosRepository {

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

	private static final Logger log = LoggerFactory.getLogger(DocVistosBuenosRepository.class);

	private long obtieneSecuenciaDocItemTransporte(Long idDocBase) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		String sql = " SELECT NVL(MAX(ID), 0) as v_MAX_ID FROM DOCTRANSPORTE.DOCTRANVISTOSBUENOS WHERE DOCUMENTOTRANSPORTE = ? ";
		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, idDocBase);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return id +1;
	}

	public Boolean persisteDocTransVistoBuenos(Long idDocBase, Vb v) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

		long id =  obtieneSecuenciaDocItemTransporte(idDocBase) + 1;
		int result = jdbcTemplate.update(
				"INSERT INTO DOCTRANSPORTE.DOCTRANVISTOSBUENOS (" +
						"      DOCUMENTOTRANSPORTE\n," +
						"      ID\n, " +
						"      CODIGO\n, " +
						"      ACTIVA,\n" +
						"      FECHAACTIVA,\n" +
						"      DESCRIPCION) VALUES (?, ?, ?, ?, ?, ?)",
				idDocBase, id,  v.getTipoVb(), "S", new Date()," ");

		return (result > 0);
	}
}
