package cl.gob.sna.gtime.processor.process;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

import cl.gob.sna.gtime.queue.vo.QueueRecord;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.Persistencia;

public class ProcessPersistResult implements Processor {
	Logger log = Logger.getLogger(ProcessPersistResult.class);

	public void process(Exchange exchange) throws Exception {
		Boolean isPersistenciaError = false;
		DocumentoResponse documento = (DocumentoResponse) exchange.getIn().getHeader("documento");

		documento.setEstadoFinal(QueueRecord.RECHAZADO);

		try {
			List<Persistencia> listPersistenias = (List<Persistencia>) exchange.getIn().getBody();
			isPersistenciaError = existeErrorPersistencia(listPersistenias);

			if (!isPersistenciaError){
				documento.setEstadoFinal(QueueRecord.ACEPTADO);
			}
		} catch (Exception e) {
			documento.setEstadoFinal(QueueRecord.RECHAZADO);
		} finally {
			exchange.getIn().setHeader("execTime", documento.getGtime().executionTimeMsg());
			exchange.getIn().setHeader("persistenciaExitosa", !isPersistenciaError);
			exchange.getIn().setBody(exchange.getIn().getHeader("documento"));
		}
	}

	/**
	 *
	 * @param persistencias
	 * @return
	 */
	private boolean existeErrorPersistencia(List<Persistencia> persistencias){
		if (persistencias == null){
			return true;
		}
		for (Persistencia persistencia : persistencias) {
			if (persistencia == null){
				return true;
			}
			if (!persistencia.isValido()){
				return true;
			}
		}
		return false;
	}

}
