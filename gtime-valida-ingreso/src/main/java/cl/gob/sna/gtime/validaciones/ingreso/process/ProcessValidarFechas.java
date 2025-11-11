package cl.gob.sna.gtime.validaciones.ingreso.process;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;

public class ProcessValidarFechas implements Processor {

	Logger log = Logger.getLogger(ProcessValidarFechas.class);
	public void process(Exchange exchange) throws Exception {	
//		log.info("inicio processor");
//		log.info("-- body "+exchange.getIn().getBody());
 		Validacion val = new Validacion(TipoValidacion.FECHAS, true);
		exchange.getIn().setBody(val);
		 
		Gtime documento = (Gtime) exchange.getIn().getHeader("documento");
		DocumentoResponse docResponse = new DocumentoResponse();
		docResponse.setNroDocumento(documento.getNumeroReferencia());
		exchange.getIn().setHeader("docResponse", docResponse);
	}

}
