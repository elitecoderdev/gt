package cl.gob.sna.gtime.validaciones.repository.mapper;

import cl.gob.sna.gtime.validaciones.repository.dto.QueueTo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QueueMapper implements RowMapper<QueueTo> {
    @Override
    public QueueTo mapRow(ResultSet rs, int rowNum) throws SQLException {
        QueueTo queue = new QueueTo();

        queue.setLogin(rs.getString("LOGIN"));
        queue.setTipoDocumento(rs.getString("tipo_documento"));
        queue.setTipoAccion(rs.getString("tipo_accion"));
        queue.setNombreUsuario(rs.getString("USUARIO"));
        queue.setNumeroReferencia(rs.getString("NUM_REF"));
        queue.setXML(rs.getString("XML"));

        return queue;
    }
}
