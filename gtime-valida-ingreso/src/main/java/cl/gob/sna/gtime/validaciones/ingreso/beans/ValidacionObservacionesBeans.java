package cl.gob.sna.gtime.validaciones.ingreso.beans;

import cl.gob.sna.gtime.api.vo.Observaciones;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.api.vo.Observacion;
import cl.gob.sna.gtime.validaciones.genericas.service.ValidacionObservacionService;
import cl.gob.sna.gtime.vo.DocumentoResponse;

import java.util.ArrayList;
import java.util.List;

public class ValidacionObservacionesBeans implements Processor {

	Logger LOG = Logger.getLogger(ValidacionObservacionesBeans.class);
	
	@Autowired
	private ValidacionObservacionService valObsService;
	
	public void process(Exchange exchange) throws Exception {
		DocumentoResponse docResponse = exchange.getIn().getHeader("docResponse",DocumentoResponse.class);
		Integer seq = 1;

		Observaciones observaciones = docResponse.getGtime().getObservaciones();
		List<Observacion> obsList =  new ArrayList<>();

		if (observaciones != null){
			obsList = observaciones.getObservacion();
		}

		if (observaciones != null && obsList != null && obsList.size() > 0){
			for (Observacion obs : obsList) {
				docResponse.getListValidaciones().addAll(valObsService.validaContenido(seq, obs));

				docResponse.getListValidaciones().addAll(valObsService.validaNombre(seq, docResponse.getGtime().getTipo(), obs));
				seq++;
			}
		}
		exchange.getIn().setHeader("docResponse", docResponse);
	}

}
