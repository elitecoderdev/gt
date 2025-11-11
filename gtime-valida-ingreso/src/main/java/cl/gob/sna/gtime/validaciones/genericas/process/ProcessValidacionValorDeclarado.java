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

public class ProcessValidacionValorDeclarado implements Processor {

	Logger log = Logger.getLogger(ProcessValidacionValorDeclarado.class);
	public void process(Exchange exchange) throws Exception {	
		DocumentoResponse docResponse = exchange.getIn().getHeader("docResponse",DocumentoResponse.class);
		Validacion val = null;
		if (StringValidation.isNull(docResponse.getGtime().getValorDeclarado())) {
			val = new Validacion(TipoValidacion.VALOR_DECLARADO_NULL, false, new Date());

		} else {
			val = new Validacion(TipoValidacion.VALOR_DECLARADO, 
								 StringValidation.isNumeric(docResponse.getGtime().getValorDeclarado()) 
								 	&& new BigDecimal(docResponse.getGtime().getValorDeclarado()).compareTo(BigDecimal.ZERO) > 0,
								 new Date());
 		}
		docResponse.getListValidaciones().add(val);
		exchange.getIn().setHeader("docResponse", docResponse); 		
	}

}
