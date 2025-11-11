package cl.gob.sna.gtime.orchestrator.repo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cl.gob.sna.gtime.orchestrator.vo.queue.QueueGtime;

public class QueueMapper implements RowMapper<QueueGtime> {
	public QueueGtime mapRow(ResultSet rs, int rowNum) throws SQLException {
		QueueGtime queue = new QueueGtime();
		queue.setIdQueue(rs.getInt("ID"));
		queue.setXml(rs.getString("MSG"));
		return queue;
	}

}