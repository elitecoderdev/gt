package cl.gob.sna.gtime.validaciones.genericas.process;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

import cl.gob.sna.gtime.validaciones.utils.StringValidation;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;

public class ProcessValidacionTotalPeso implements Processor {
	Logger log = Logger.getLogger(ProcessValidacionTotalPeso.class);
	public void process(Exchange exchange) throws Exception {
		DocumentoResponse docResponse = exchange.getIn().getHeader("docResponse",DocumentoResponse.class);
		Validacion val = null;

		if (StringValidation.isNull(docResponse.getGtime().getTotalPeso())){
			val = new Validacion(TipoValidacion.TOTAL_PESO_NULL, false, new Date());
		}else{
			val = new Validacion(TipoValidacion.TOTAL_PESO, StringValidation.
					isNumeric(docResponse.getGtime().getTotalPeso()), new Date());
		}
		docResponse.getListValidaciones().add(val);
		exchange.getIn().setHeader("docResponse", docResponse);
	}

}
