package cl.gob.sna.gtime.validaciones.genericas.process;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

import cl.gob.sna.gtime.validaciones.utils.StringValidation;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;

public class ProcessValidacionParcial implements Processor {

	Logger log = Logger.getLogger(ProcessValidacionParcial.class);
	public void process(Exchange exchange) throws Exception {	
		DocumentoResponse docResponse = exchange.getIn().getHeader("docResponse",DocumentoResponse.class);
		Validacion val = null;
		if (StringValidation.isNull(docResponse.getGtime().getParcial())) {
			val = new Validacion(TipoValidacion.PARCIAL_NULL, false, new Date());
		} else {
			val = new Validacion(TipoValidacion.PARCIAL, StringValidation.strInArray(docResponse.getGtime().getParcial(), new String[]{"S","N"}), new Date());
  		}
		docResponse.getListValidaciones().add(val);
		exchange.getIn().setHeader("docResponse", docResponse);
 		
	}

}
