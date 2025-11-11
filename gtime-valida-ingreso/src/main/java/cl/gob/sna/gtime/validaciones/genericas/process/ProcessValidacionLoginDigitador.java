package cl.gob.sna.gtime.validaciones.genericas.process;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

import cl.gob.sna.gtime.validaciones.utils.StringValidation;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;

public class ProcessValidacionLoginDigitador implements Processor {

	Logger log = Logger.getLogger(ProcessValidacionLoginDigitador.class);
	public void process(Exchange exchange) throws Exception {	
		DocumentoResponse docResponse  =  exchange.getIn().getHeader("docResponse",DocumentoResponse.class);

		Validacion vLogin = null;

		if (docResponse.getGtime() != null && docResponse.getGtime().getLogin() != null){
			vLogin = new Validacion(TipoValidacion.LOGIN_DIGITADOR, StringValidation.
					largoMaximo(docResponse.getGtime().getLogin(), 50), new Date());
		}else{
			vLogin = new Validacion(TipoValidacion.LOGIN_DIGITADOR_NULL, false,  new Date());
		}

		docResponse.getListValidaciones().add(vLogin);
		exchange.getIn().setHeader("docResponse", docResponse);
	}

}
