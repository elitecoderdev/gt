package cl.gob.sna.gtime.processor.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.Date;

public class DocNotificacionBatchRepository {

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

    private static final Logger log = LoggerFactory.getLogger(DocNotificacionBatchRepository.class);

    public Boolean persistNotificacionBach(Long idDocBase, String tipoEstado) {
        this.jdbcTemplate = new JdbcTemplate(getDataSource());


        int result = jdbcTemplate.update(
                "INSERT INTO DOCUMENTOS.DOCNOTIFICACIONBATCH (" +
                        "      ID\n," +
                        "      FECHACREACION\n, " +
                        "      TIPOEVENTO,\n" +
                        "      CODIGOESTADO,\n" +
                        "      DOCUMENTO) VALUES (DOCUMENTOS.SEC_DOCNOTIFICACIONBATCH.NEXTVAL, ?, ?, ?, ?)",
                new Date(), "SC", tipoEstado, idDocBase);
        return (result > 0);
    }

    /**
     *
     * @return
     */
    private long obtieneSecuenciaDocNotificacionBach() {
        this.jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "SELECT DOCUMENTOS.SEC_DOCNOTIFICACIONBATCH.NEXTVAL from dual";
        final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        sqlRowSet.next();
        long id = sqlRowSet.getLong(1);
        return id + 1;
    }


}
