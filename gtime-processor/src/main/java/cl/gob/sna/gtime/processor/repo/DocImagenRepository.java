package cl.gob.sna.gtime.processor.repo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import cl.gob.sna.gtime.processor.vo.Imagen;

@Repository
public class DocImagenRepository {

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

	private static final Logger log = LoggerFactory.getLogger(DocImagenRepository.class);

	public long obtieneSecuenciaDocImagen(Long idDocBase) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		String sql = "SELECT NVL(MAX(ID), 0) as v_MAX_ID FROM DOCUMENTOS.DOCIMAGEN WHERE DOCUMENTO = ? ";
		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, idDocBase);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return id +1;
	}

	public void persisteDocImagen(final List<Imagen> lista) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		final int batchSize = 1;
		final String QUERY_SAVE = "INSERT INTO DOCUMENTOS.DOCIMAGEN values (?,?,?,?,?,?)";

		jdbcTemplate.batchUpdate(QUERY_SAVE, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Imagen imagen = lista.get(i);
				ps.setLong(1, imagen.getIdDocBase());
				ps.setLong(2, imagen.getId());
				ps.setString(3, imagen.getTipo());
				ps.setString(4, imagen.getXml());
				ps.setString(5, null);
				ps.setString(6, null);
			}

			@Override
			public int getBatchSize() {
				if (batchSize > lista.size()) {
					return lista.size();
				}
				return batchSize;
			}
		});
	}
}
