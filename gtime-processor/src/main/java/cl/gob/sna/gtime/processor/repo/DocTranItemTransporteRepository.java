package cl.gob.sna.gtime.processor.repo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import cl.gob.sna.gtime.api.vo.Proditem;
import cl.gob.sna.gtime.processor.util.FechaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import cl.gob.sna.gtime.api.vo.Item;

@Repository
public class DocTranItemTransporteRepository {

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

	private static final Logger log = LoggerFactory.getLogger(DocTranItemTransporteRepository.class);

	private long obtieneSecuenciaDocItemTransporte(Long idDocBase) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

		String sql = "SELECT NVL(MAX(ID), 0) as v_MAX_ID " +
				"FROM DOCTRANSPORTE.DOCTRANITEMTRANSPORTE T " +
				"WHERE T.DOCUMENTOTRANSPORTE = ? ";

		final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, idDocBase);
		sqlRowSet.next();
		long id = sqlRowSet.getLong(1);
		return id;
	}

	public Boolean persisteItem(Long idDocBase, int idMfto, Item item, int pos){
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

		try{
			int numItem = Integer.valueOf(item.getNumeroItem());

			BigDecimal cantidad = BigDecimal.ZERO;
			BigDecimal pesoBruto = BigDecimal.ZERO;

			cantidad  = BigDecimal.valueOf(Double.valueOf(item.getCantidad()));
			pesoBruto  = BigDecimal.valueOf(Double.valueOf(item.getPesoBruto()));
			String unidPeso = item.getUnidadPeso()!=null?item.getUnidadPeso():" ";
			String tipoBulto = item.getTipoBulto()!=null?item.getTipoBulto():" ";
			String desc = item.getDescripcion() !=null ?item.getDescripcion():" ";

			long id = obtieneSecuenciaDocItemTransporte(idDocBase) + pos;

			int result = jdbcTemplate.update(
					"INSERT INTO DOCTRANSPORTE.DOCTRANITEMTRANSPORTE (" +
							"      DOCUMENTOTRANSPORTE\n," +
							"      ID\n, " +
							"      NUMERO,\n" +
							"      CANTIDAD,\n" +
							"      PESOBRUTO,\n" +
							"      PESONETO,\n" +
							"      UNIDADPESO,\n" +
							"      VOLUMENNETO,\n" +
							"      VOLUMENBRUTO,\n" +
							"      UNIDADVOLUMEN,\n" +
							"      CARGAPELIGROSA,\n" +
							"      ALTO,\n" +
							"      ANCHO,\n" +
							"      LARGO,\n" +
							"      VENTILACION,\n" +
							"      UNIDADDIMENSION,\n" +
							"      CARGACONTENEDORIZADA,\n" +
							"      FECHAACTIVA,\n" +
							"      ACTIVO,\n" +
							"      TIPOBULTO,\n" +
							"      INSTRUCCIONESESTIBA,\n" +
							"      TEMPMAXIMA,\n" +
							"      TEMPMINIMA,\n" +
							"      ESCALA,\n" +
							"      DESCRIPCION,\n" +
							"      MARCASCONTRAMARCAS,\n" +
							"      IDDOCTRANMANIFIESTO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					idDocBase, id, numItem, cantidad,pesoBruto, 0, unidPeso, 0, 0 , " ", " ", 0 , 0, 0,
					0," ", "N", new Date(), "S", tipoBulto, " ", " ", " ", " ", desc, item.getMarcas(), idMfto);
			return (result > 0);
		}catch (Exception e){
			log.info(" exception {}", e.getMessage());
			e.getStackTrace();
			return false;
		}
	}
}
