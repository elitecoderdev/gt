package cl.gob.sna.gtime.validaciones.genericas.process;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cl.gob.sna.gtime.validaciones.utils.StringValidation;
import cl.gob.sna.gtime.vo.TipoValidacion;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.Validacion;

public class ProcessInicioValidacion implements Processor {

	Logger log = Logger.getLogger(ProcessInicioValidacion.class);
	public void process(Exchange exchange) throws Exception {	
		Gtime documento = exchange.getIn().getHeader("documento",Gtime.class);

		List<Validacion> validaciones = new ArrayList<>();
		DocumentoResponse docResponse = new DocumentoResponse();

		Validacion val = null;

		if (StringValidation.isNull(documento.getTipo())
			|| StringValidation.isNull(documento.getTipoAccion())
			|| !documento.getTipoAccion().equals("I")
			|| !documento.getTipo().equals("GTIME")){
			val = new Validacion(TipoValidacion.GTIME_TIPO, false, new Date());
			validaciones.add(val);
		}else{
			val = new Validacion(TipoValidacion.GTIME_TIPO, true, new Date());
			validaciones.add(val);
		}

		docResponse.setNroDocumento(documento.getNumeroReferencia());
		docResponse.setListValidaciones(validaciones);
		docResponse.setGtime(documento);

		exchange.getIn().setHeader("docResponse", docResponse);
	}
}
