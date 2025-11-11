package cl.gob.sna.gtime.processor.repo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import cl.gob.sna.gtime.processor.util.FechaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import cl.gob.sna.gtime.api.vo.Cargo;
import cl.gob.sna.gtime.api.vo.Gtime;

@Repository
public class DocCargosRepository {

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

	private static final Logger log = LoggerFactory.getLogger(DocCargosRepository.class);

	/**
	 *
	 * @param cargo
	 * @return
	 */
	public Boolean persisteDocTransTipoCargo(Cargo cargo) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		int result = jdbcTemplate.update(
				"INSERT INTO DOCTRANSPORTE.DOCTRANTIPOCARGO (" +
						"      CODIGO\n," +
						"      GLOSA\n, " +
						"      DESCRIPCION,\n" +
						"      ACTIVA,\n" +
						"      FECHAACTIVA,\n" +
						"      CREACIONAUTOMATICA) VALUES (?, ?, ?, ?, ?, ?)",
				cargo.getTipoCargo(), cargo.getDescripcion(), "Creado Automáticamente", "S", new Date(), "");
		return (result > 0);
	}

	/**
	 *
	 * @param idDocBase
	 * @return
	 */
	private long obtieneSecuenciaDocTransCargo(Long idDocBase) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		String sql = "SELECT NVL(MAX(ID), 0) as v_MAX_ID FROM DOCTRANSPORTE.DOCTRANCARGO WHERE DOCUMENTOTRANSPORTE = ? ";
		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, idDocBase);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return id;
	}

	/**
	 *
	 * @param idDocBase
	 * @param gtime
	 * @return
	 */
	public List<Boolean> persisteDocTransCargo(Long idDocBase, Gtime gtime) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

		List<Boolean> listaResultado = new ArrayList<>();

		List<Cargo> listaCargos = gtime.getCargos().getCargo();
		int i = 0;
		for (Cargo cargo : listaCargos) {
			i++;
			long id = obtieneSecuenciaDocTransCargo(idDocBase) + i;
			String desc = cargo.getDescripcion();
			BigDecimal monto = BigDecimal.valueOf(Double.valueOf(cargo.getMonto()));
			String condPago = cargo.getCondPago();
			String moneda = cargo.getMoneda();
			String tipoCargo = cargo.getTipoCargo();
			//BigDecimal tarifa = cargo.getTarifa() != null ? BigDecimal.valueOf(Double.valueOf(cargo.getTarifa())) : 0);
			String obs = cargo.getObservaciones()!=null? cargo.getObservaciones() : " ";


			int result = jdbcTemplate.update(
					"INSERT INTO DOCTRANSPORTE.DOCTRANCARGO (" +
							"      DOCUMENTOTRANSPORTE\n," +
							"      ID\n, " +
							"      NUMERO,\n" +
							"      DESCRIPCION,\n" +
							"      MONTO,\n" +
							"      CONDICIONPAGO,\n" +
							"      MONEDA,\n" +
							"      ACTIVO,\n" +
							"      FECHAACTIVA,\n" +
							"      TIPOCARGO,\n" +
							"      TARIFA,\n" +
							"      OBSERVACIONES,\n" +
							"      UNIDADCOBRO,\n" +
							"      CODIGOLUGARPAGO,\n" +
							"LUGARPAGO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					idDocBase, id, i, desc, monto, condPago,moneda , "S", new Date(), tipoCargo ,
					cargo.getTarifa() != null ? BigDecimal.valueOf(Double.valueOf(cargo.getTarifa())) : 0, obs, " ", " ", " ");

			listaResultado.add((result > 0));

			if (!checkCargoTipo(cargo.getTipoCargo())){
				persisteDocTransTipoCargo(cargo);
			}
		}
		return listaResultado;

	}

	/**
	 *
	 * @param tipoCargo
	 * @return
	 */
	public boolean checkCargoTipo(String tipoCargo){
		if (tipoCargo == null || tipoCargo.isEmpty()) return true;
		this.jdbcTemplate = new JdbcTemplate(getDataSource());
		String sql = "SELECT COUNT(*) FROM DOCTRANSPORTE.DOCTRANTIPOCARGO WHERE CODIGO = UPPER(TRIM(?))";
		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, tipoCargo);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return (id > 0);

	}

}
