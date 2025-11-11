package cl.gob.sna.gtime.validaciones.ingreso.process;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.validaciones.utils.AbstractValidation;
import cl.gob.sna.gtime.vo.Persistencia;
import cl.gob.sna.gtime.vo.Validacion;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

import cl.gob.sna.gtime.vo.DocumentoResponse;

import java.util.List;

public class ProcessResponse implements Processor {

	Logger log = Logger.getLogger(ProcessResponse.class);
	public void process(Exchange exchange) throws Exception {
		DocumentoResponse docResponse  =  exchange.getIn().getHeader("docResponse",DocumentoResponse.class);

		Gtime gtime
				= exchange.getIn().getHeader("documento", Gtime.class);

		//AbstractValidation.showAbstractProcess(docResponse.getListValidaciones());

		docResponse.setListValidaciones(docResponse.getListValidaciones());
		docResponse.setNroDocumento(gtime.getNumeroReferencia());
		docResponse.setGtime(gtime);

		exchange.getIn().setBody(docResponse, DocumentoResponse.class);
	}

}
