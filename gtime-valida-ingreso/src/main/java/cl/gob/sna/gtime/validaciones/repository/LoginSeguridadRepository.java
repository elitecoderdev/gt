package cl.gob.sna.gtime.validaciones.repository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class LoginSeguridadRepository {

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

    private static final Logger log = LoggerFactory.getLogger(LoginSeguridadRepository.class);

    public boolean existeLoginUsuarioSeguridad(String login) {
        this.jdbcTemplate = new JdbcTemplate(getDataSource());

        final SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(" SELECT\n" +
                "          count(*)\n" +
                "        FROM\n" +
                "          SEGURIDAD.SEGUSUARIO\n" +
                "        WHERE\n" +
                "          LOGIN = ?", login);
        sqlRowSet.next();
        long id = sqlRowSet.getLong(1);

        return id>0;
    }
}
