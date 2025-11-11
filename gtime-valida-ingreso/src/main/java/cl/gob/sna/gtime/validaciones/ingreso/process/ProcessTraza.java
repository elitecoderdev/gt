package cl.gob.sna.gtime.validaciones.ingreso.process;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.modelo.vo.DetalleLog;
 

public class ProcessTraza implements Processor {
	Logger log = Logger.getLogger(ProcessTraza.class);
	public void process(Exchange exchange) throws Exception {			
 		Gtime documento = exchange.getIn().getHeader("documento",Gtime.class);
 		
 		exchange.getIn().setHeader("documento", documento);
		DetalleLog traza = new DetalleLog();
		traza.setLogin(documento.getUser());
		traza.setNroEncabezado(documento.getNroEncabezado());
		traza.setTipoDocumento(documento.getTipo());
		traza.setVersion(documento.getVersion());
		traza.setNroDocumento(documento.getNumeroReferencia());
		traza.setEvento(exchange.getIn().getHeader("evento",String.class));		
		exchange.getIn().setBody(traza);		
	}

}
