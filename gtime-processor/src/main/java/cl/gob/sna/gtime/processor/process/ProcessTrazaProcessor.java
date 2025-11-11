package cl.gob.sna.gtime.processor.process;

import cl.gob.sna.gtime.vo.Validacion;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.modelo.vo.DetalleLog;
import cl.gob.sna.gtime.vo.DocumentoResponse;

import java.util.List;


public class ProcessTrazaProcessor implements Processor {

	Logger log = Logger.getLogger(ProcessTrazaProcessor.class);
	public void process(Exchange exchange) throws Exception {	
		DocumentoResponse response ;
		try {
			 response = (DocumentoResponse) exchange.getIn().getBody();	
		} catch (Exception e) {
			 response = (DocumentoResponse) exchange.getIn().getHeader("documento");
		}
		DetalleLog traza = new DetalleLog();
		Gtime documento = response.getGtime();

		traza.setLogin(documento.getLogin());
		traza.setNroEncabezado(documento.getNroEncabezado());
		traza.setTipoDocumento(documento.getTipoDocumento());
		traza.setVersion(documento.getVersion());
		traza.setNroDocumento(documento.getNumeroReferencia());
		traza.setEvento(exchange.getIn().getHeader("evento",String.class));

		exchange.getIn().setBody(traza);
		
	}

}
