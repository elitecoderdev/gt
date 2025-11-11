package cl.gob.sna.gtime.validaciones.genericas.process;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

import cl.gob.sna.gtime.validaciones.utils.StringValidation;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;

public class ProcessValidacionNumeroReferencia implements Processor {

	Logger log = Logger.getLogger(ProcessValidacionNumeroReferencia.class);
	
	public void process(Exchange exchange) throws Exception {	
		DocumentoResponse docResponse  =  exchange.getIn().getHeader("docResponse",DocumentoResponse.class);
 	
 		Validacion val = null;
		if(StringValidation.isNull(docResponse.getGtime().getNumeroReferencia())) {
			val = new Validacion(TipoValidacion.NUMERO_REFERENCIA_NULL, false, new Date());
		}else {
			val = new Validacion(TipoValidacion.NUMERO_REFERENCIA, StringValidation.
					largoMaximo(docResponse.getGtime().getNumeroReferencia(), 200), new Date());
		}		
		docResponse.getListValidaciones().add(val);
		exchange.getIn().setHeader("docResponse", docResponse);
  		
	}

}
