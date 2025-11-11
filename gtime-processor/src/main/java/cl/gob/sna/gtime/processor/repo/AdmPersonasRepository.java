package cl.gob.sna.gtime.processor.repo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cl.gob.sna.gtime.api.vo.Participacion;
import cl.gob.sna.gtime.api.vo.Referencia;
import cl.gob.sna.gtime.processor.mappers.InfoPersonaMapper;
import cl.gob.sna.gtime.processor.vo.InfoPersona;

@Repository
public class AdmPersonasRepository {

	private static final String DOCRELACIONDOCUMENTO = "DOCRELACIONDOCUMENTO";
	private static final String DOCOBSERVACION = "DOCOBSERVACION";
	private static final String DOCLOCACIONDOCUMENTO = "DOCLOCACIONDOCUMENTO";
	private static final String DOCFECHADOCUMENTO = "DOCFECHADOCUMENTO";
	private static final String DOCPARTICIPACION = "DOCPARTICIPACION";
	private static final String DOCDOCUMENTOBASE = "DOCDOCUMENTOBASE";
	private static final String DOCUMENTOS = "DOCUMENTOS";
	private static final int PORCENTAJE_PARTICIPACION_0 = 0;
	private static final String ACTIVO_N = "N";
	private static final String ACTIVO_S = "S";
	private static final Date FECHA_FIN_VIGENCIA = new Date();
	private static final Date FIN_FECHA_VIGENCIA = FECHA_FIN_VIGENCIA;

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

	private static final Logger log = LoggerFactory.getLogger(AdmPersonasRepository.class);

	public String obtieneIdPersonaParticipante(Participacion participacion) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

		String sql = "SELECT p.ID FROM ADMPERSONAS.PER_VALORIDENTIFICADOR v " +
				"LEFT JOIN ADMPERSONAS.PER_PERSONA p ON p.ID = v.PERSONA AND p.ACTIVA = 'S'" +
				"WHERE v.TIPOIDENTIFICADOR = '" + participacion.getTipoId() + "' " +
				"AND v.NACIONALIDAD = '" + participacion.getNacionId() + "' " +
				"AND UPPER(v.VALOR) = UPPER('" + participacion.getValorId() + "' ) " +
				"AND v.ACTIVA = 'S' and p.ID IS NULL OR p.ID = (SELECT MIN(PERSONA) " +
				"FROM ADMPERSONAS.PER_VALORIDENTIFICADOR pv " +
				"WHERE pv.TIPOIDENTIFICADOR = '" + participacion.getTipoId() + "' " +
				"AND pv.NACIONALIDAD = '" + participacion.getNacionId() + "' " +
				"AND UPPER(pv.VALOR) = UPPER('" + participacion.getValorId() + "') " +
				"AND ACTIVA = 'S')";

		List<String> admPersonaList = jdbcTemplate.query(sql,new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		} );

		if (admPersonaList != null && !admPersonaList.isEmpty()
				&& admPersonaList.get(0) != null){
			return admPersonaList.get(0);
		}else{
			return null;
		}
	}

	public InfoPersona obtieneInfoPersona(Referencia referencia) {
		this.jdbcTemplate = new JdbcTemplate(getDataSource());

		String sql = "SELECT p.ID as ID_PERSONA, db.EMISOR as NOMBRE_EMISOR, db.ID as DOC_BASE_ID " +
				"FROM ADMPERSONAS.PER_VALORIDENTIFICADOR v " +
				"LEFT JOIN ADMPERSONAS.PER_PERSONA p ON (p.ID = v.PERSONA AND p.ACTIVA = 'S') " +
				"LEFT JOIN DOCUMENTOS.DOCDOCUMENTOBASE db ON (db.IDEMISOR = p.ID AND db.ACTIVO = 'S') " +
				"WHERE " +
				"v.NACIONALIDAD = '" + referencia.getNacIdEmisor() + "' " +
				"AND v.TIPOIDENTIFICADOR = '" + referencia.getTipoIdEmisor() + "' " +
				"AND v.VALOR = '" + referencia.getValorIdEmisor() + "'  " +
				"AND v.ACTIVA = 'S' " +
				"AND db.TIPODOCUMENTO = '" + referencia.getTipoDocumento() + "'  " +
				"AND db.NUMEROEXTERNO = '" + referencia.getNumero() + "'  " +
				"AND db.FECHAEMISION = TO_DATE(SUBSTR('" + referencia.getFecha() + "', 1, 10), 'dd-mm-yyyy')";

		List<InfoPersona> infoPersonaList = jdbcTemplate.query(sql, new InfoPersonaMapper());

		if (infoPersonaList != null && !infoPersonaList.isEmpty()
				&& infoPersonaList.size() > 0){
			return infoPersonaList.get(0);
		}else{
			return new InfoPersona();
		}
	}
}
