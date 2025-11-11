package cl.gob.sna.gtime.orchestrator.repo;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cl.gob.sna.gtime.orchestrator.vo.queue.QueueGtime;

@Repository
public class QueueRepository {

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

	private static final Logger log = LoggerFactory.getLogger(QueueRepository.class);

	public List<QueueGtime> buscarPendientes(int max) {
		String sql = "SELECT Q.ID, Q.MSG FROM ADMMENSAJES.QUEUE Q WHERE Q.TIPO_DOCUMENTO = 'GTIME' AND Q.TIPO_ACCION = 'I' AND Q.ESTADO ='ENQ' AND Q.STATUS = 0 AND Q.RETRY = 0 AND Q.PROC IS NULL AND rownum <= ? ";
		return jdbcTemplate.query(sql, new Object[] { max }, new QueueMapper());
	}
	
	public List<QueueGtime> buscarErrores() {
		String sql = "SELECT Q.ID, Q.MSG FROM ADMMENSAJES.QUEUE Q INNER JOIN ADMMENSAJES.QUEUE_PROPS P on Q.ID = P.id and P.NAME='numero-referencia' INNER JOIN DOCUMENTOS.DOCDOCUMENTOBASE b on B.NUMEROEXTERNO = P.CONTENT WHERE Q.TIPO_DOCUMENTO = 'GTIME' AND Q.TIPO_ACCION = 'I' AND Q.ESTADO ='ENQ' AND Q.STATUS = 0 AND Q.RETRY = 0 AND Q.PROC IS NULL";				
		return jdbcTemplate.query(sql, new QueueMapper());
	}

	public boolean cambiaEstado(Integer idQueue, String estado) {
		String sql = "update ADMMENSAJES.queue q set q.proc = sysdate, q.status = 2, q.estado = ? where q.id = ?";
		JdbcTemplate jdbcTemplateObject = new JdbcTemplate(getDataSource());
		return jdbcTemplateObject.update(sql,estado, idQueue) > 0;	
		
	}
}
