package cl.gob.sna.gtime.processor.repo;

import cl.gob.sna.gtime.processor.util.DocEstadoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.Date;

public class DocPropagacionBatchRepository {
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

    private static final Logger log = LoggerFactory.getLogger(DocPropagacionBatchRepository.class);

    public Boolean persistPropagacionBach(Long idDocBase, DocEstadoUtil docEstado) {
        this.jdbcTemplate = new JdbcTemplate(getDataSource());
        String tipoDocumento = "GTIME";
        //Long id = obtieneSecuenciaDocPropagacionBach();

        int result = jdbcTemplate.update(
                "INSERT INTO DOCUMENTOS.DOCPROPAGACIONESTADOBATCH (" +
                        "      ID\n," +
                        "      FECHACREACION\n, " +
                        "      LOGIN,\n" +
                        "      IDDOCUMENTO,\n" +
                        "      TIPODOCUMENTO,\n" +
                        "      CODIGOESTADO) VALUES (DOCUMENTOS.SEC_DOCPROPAGACIONESTADOBATCH.NEXTVAL, ?, ?, ?, ?, ?)",
               new Date(), docEstado.getDocEstadoLogin(), idDocBase, tipoDocumento, docEstado.getDocEstado());

        return (result > 0);
    }

    /**
     *
     * @return
     */
    private long obtieneSecuenciaDocPropagacionBach(){
        this.jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "SELECT DOCUMENTOS.SEC_DOCPROPAGACIONESTADOBATCH.NEXTVAL from dual";
        final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        sqlRowSet.next();
        long id = sqlRowSet.getLong(1);
        return id + 1;
    }
}
