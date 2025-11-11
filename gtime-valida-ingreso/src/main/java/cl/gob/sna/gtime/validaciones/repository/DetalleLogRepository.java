package cl.gob.sna.gtime.validaciones.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class DetalleLogRepository {

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

    private static final Logger log = LoggerFactory.getLogger(DetalleLogRepository.class);

    public void deleteFromDocLogRecepcion(String tipoDoc, String numRef, String login) {
        if (numRef != null && tipoDoc != null){
            String sql = "DELETE FROM DOCUMENTOS.DOCLOGRECEPCION WHERE LOGIN = '" + login + "' AND TIPODOCORIGEN = '" +tipoDoc+ "' AND NUMEROREFERENCIA = '" + numRef + "'";
            this.jdbcTemplate.update(sql);
        }
    }

    public void deleteFromDetalleLogByNumRef(String tipoDoc, String numRef, String login){
        if (numRef != null && tipoDoc != null){
            String sql = "DELETE FROM DOCUMENTOS.DOCDETALLELOG WHERE LOGIN = '" + login + "'  AND TIPODOCORIGEN = '" + tipoDoc + "' AND NUMEROREFERENCIA = '" + numRef + "'";
            this.jdbcTemplate.
                    update(sql);
        }
    }
}
