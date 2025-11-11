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

public class ProcessValidacionTotalBultos implements Processor {

	Logger log = Logger.getLogger(ProcessValidacionTotalBultos.class);

	public void process(Exchange exchange) throws Exception {
		DocumentoResponse docResponse = exchange.getIn().getHeader("docResponse",DocumentoResponse.class);
		Validacion val = null;
		if (StringValidation.isNull(docResponse.getGtime().getTotalBultos())) {
			val = new Validacion(TipoValidacion.TOTAL_BULTOS_NULL, false, new Date());
		} else {
			val = new Validacion(TipoValidacion.TOTAL_BULTOS, 
								 StringValidation.isNumeric(docResponse.getGtime().getTotalBultos()) 
								 	&& new BigDecimal(docResponse.getGtime().getTotalBultos()).compareTo(BigDecimal.ZERO) > 0,
								 new Date());
 		}
		docResponse.getListValidaciones().add(val);
		exchange.getIn().setHeader("docResponse", docResponse);
	}

}
