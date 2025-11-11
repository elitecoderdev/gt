package cl.gob.sna.gtime.validaciones.genericas.process;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

import cl.gob.sna.gtime.validaciones.utils.StringValidation;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;

public class ProcessValidacionTipoOperacion implements Processor {

	Logger log = Logger.getLogger(ProcessValidacionTipoOperacion.class);
	public void process(Exchange exchange) throws Exception {	
		DocumentoResponse docResponse = exchange.getIn().getHeader("docResponse",DocumentoResponse.class);
		Validacion val = null;
		if (StringValidation.isNull(docResponse.getGtime().getTipoOperacion())) {
			val = new Validacion(TipoValidacion.TIPO_OPERACION_NULL, false, new Date());
		} else {
			val = new Validacion(TipoValidacion.TIPO_OPERACION, StringValidation.strInArray(docResponse.getGtime().getTipoOperacion(), new String[]{"I","S","TR","TRB"}), new Date());
  		}
		docResponse.getListValidaciones().add(val);
		exchange.getIn().setHeader("docResponse", docResponse);
		
 		
	}

}
