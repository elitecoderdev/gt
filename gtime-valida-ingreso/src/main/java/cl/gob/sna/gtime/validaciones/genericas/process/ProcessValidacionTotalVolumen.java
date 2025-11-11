package cl.gob.sna.gtime.validaciones.genericas.process;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

import cl.gob.sna.gtime.validaciones.utils.StringValidation;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;

public class ProcessValidacionTotalVolumen implements Processor {

	Logger log = Logger.getLogger(ProcessValidacionTotalVolumen.class);
	public void process(Exchange exchange) throws Exception {
		DocumentoResponse docResponse = exchange.getIn().getHeader("docResponse",DocumentoResponse.class);

		if(!StringValidation.isNull(docResponse.getGtime().getTotalVolumen())) {
			Validacion val = new Validacion(TipoValidacion.TOTAL_VOLUMEN, 
					 StringValidation.isNumeric(docResponse.getGtime().getTotalVolumen()) 
					 	&& new BigDecimal(docResponse.getGtime().getTotalVolumen()).compareTo(BigDecimal.ZERO) > 0,
					 new Date());
 			docResponse.getListValidaciones().add(val);
 		}
 
		exchange.getIn().setHeader("docResponse", docResponse);
 	}

}
