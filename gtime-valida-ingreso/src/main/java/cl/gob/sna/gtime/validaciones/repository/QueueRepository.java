package cl.gob.sna.gtime.validaciones.repository;

import cl.gob.sna.gtime.validaciones.repository.dto.QueueTo;
import cl.gob.sna.gtime.validaciones.repository.mapper.QueueMapper;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class QueueRepository {
    final Logger LOG = Logger.getLogger(QueueRepository.class);
    @Autowired
    private DataSource dataSource;
    private DataSource getDataSource() {
        return dataSource;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String QUERY_VALIDA_COLA = "SELECT\n" +
            "        q.MSG as XML,\n" +
            "        qp_login.content as LOGIN,\n" +
            "        q.tipo_documento,\n" +
            "        q.tipo_accion,\n" +
            "        qp_nombre_usuario.content as USUARIO,\n" +
            "        qp_num_ref.content as NUM_REF \n" +
            "      FROM\n" +
            "        ADMMENSAJES.QUEUE q\n" +
            "        LEFT JOIN ADMMENSAJES.QUEUE_PROPS qp_login on qp_login.ID = q.ID and qp_login.name = 'identificacion'\n" +
            "        LEFT JOIN ADMMENSAJES.QUEUE_PROPS qp_nombre_usuario on qp_nombre_usuario.ID = q.ID and qp_nombre_usuario.name = 'nombre-usuario'\n" +
            "        LEFT JOIN ADMMENSAJES.QUEUE_PROPS qp_num_ref on qp_num_ref.ID = q.ID and qp_num_ref.name = 'numero-referencia'\n" +
            "      WHERE q.ID = ?";

    public QueueTo obtenerQueuPorIDPayload(long idCola) {
        this.jdbcTemplate = new JdbcTemplate(getDataSource());
        try{
            QueueTo queueTo = jdbcTemplate.queryForObject( QUERY_VALIDA_COLA,
                    new QueueMapper(),
                    idCola);
            return queueTo;
        }catch (DataAccessException dae){
            return new QueueTo();
        }catch (Exception e){
            return new QueueTo();
        }
    }
}
