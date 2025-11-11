package cl.gob.sna.gtime.validaciones.repository;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class DINRepository {

    final Logger LOG = Logger.getLogger(DINRepository.class);
    @Autowired
    private DataSource dataSource;

    private String SCHEMA = "DIN";
    private String PACKAGE = "VALRUT_RCEI";
    private String STORE_PROCEDURE = "ValidaRUTDocumento";

    private DataSource getDataSource() {
        return dataSource;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, String> getDinConsigatarioRes(String numReferencia, String tipoDoc, String tipoAccion, String rutCons,
                                        String dvRutCons, Date fechaEmision){

        this.jdbcTemplate = new JdbcTemplate(getDataSource());

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName(SCHEMA)
                .withCatalogName(PACKAGE)
                .withProcedureName(STORE_PROCEDURE);

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("numdocumento", numReferencia)
                .addValue("TipoDOC", tipoDoc)
                .addValue("p_tipingr", tipoAccion)
                .addValue("par_RUT_RCEI", rutCons)
                .addValue("par_DVRUT_RCEI", dvRutCons)
                .addValue("par_validacion", fechaEmision);

        Map<String, Object> out = simpleJdbcCall.execute(in);

        Map<String, String> response = new HashMap<>();

        response.put("COD_MSJ", (String) out.get("P_COD_ERROR"));
        response.put("MSJ", (String) out.get("PAR_ERRORES"));

        return response;
    }
}
