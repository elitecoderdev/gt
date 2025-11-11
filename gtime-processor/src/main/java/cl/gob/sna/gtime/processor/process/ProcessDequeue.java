package cl.gob.sna.gtime.processor.process;

import java.util.Date;

import cl.gob.sna.gtime.api.vo.Gtime;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

import cl.gob.sna.gtime.processor.util.FechaUtil;
import cl.gob.sna.gtime.queue.vo.QueueRecord;
import cl.gob.sna.gtime.vo.DocumentoResponse;

public class ProcessDequeue implements Processor {

	Logger log = Logger.getLogger(ProcessDequeue.class);

	public void process(Exchange exchange) throws Exception {
		DocumentoResponse documento = (DocumentoResponse) exchange.getIn().getBody();
		Gtime gtime = documento.getGtime();

		if (documento != null) {
			QueueRecord queueRecord = new QueueRecord();

			queueRecord.setEstado(gtime.getEstadoGtime());
			queueRecord.setIdPayload(documento.getGtime().getIdPayload());
			queueRecord.setIdentificacion(documento.getGtime().getLogin());
			queueRecord.setTipoDocumento(documento.getGtime().getTipoDocumento());
			queueRecord.setNumeroReferencia(documento.getGtime().getNumeroReferencia());
			queueRecord.setIdPersistencia(documento.getGtime().getIdPersistencia());


			exchange.getIn().setBody(queueRecord, QueueRecord.class);
		}
	}

}
