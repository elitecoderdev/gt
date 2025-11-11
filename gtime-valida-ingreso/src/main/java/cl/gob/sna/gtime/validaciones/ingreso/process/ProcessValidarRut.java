package cl.gob.sna.gtime.validaciones.ingreso.process;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;

 

public class ProcessValidarRut implements Processor {

	
	Logger log = Logger.getLogger(ProcessValidarRut.class);
	@Override
	public void process(Exchange exchange) throws Exception {
		DocumentoResponse docResponse = (DocumentoResponse) exchange.getIn().getHeader("docResponse");

		Validacion val = new Validacion(TipoValidacion.RUT, true);

		docResponse.getListValidaciones().add(val);
		exchange.getIn().setHeader("docResponse", docResponse);

	}

}
