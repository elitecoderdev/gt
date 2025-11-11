package cl.gob.sna.gtime.validaciones.ingreso.beans;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.validaciones.genericas.service.ValidacionEmisorService;
import cl.gob.sna.gtime.validaciones.repository.dto.EmisorTo;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;

public class ValidacionEmisorBeans implements Processor {

	private final Logger LOG = Logger.getLogger(ValidacionEmisorBeans.class);

	@Autowired
	private ValidacionEmisorService valEmisorService;
	
	public void process(Exchange exchange) throws Exception {
		EmisorTo emisor  =  exchange.getIn().getHeader("emisor", EmisorTo.class);

		DocumentoResponse docResponse = exchange.getIn().getHeader("docResponse",DocumentoResponse.class);

		if(docResponse.getFem() != null) {
			Long idPersonaEmisor = null;
			if (emisor != null) {
				idPersonaEmisor = valEmisorService.obtenerIdPersonaEmisor(emisor.getTipoId(), emisor.getValorId(),
						emisor.getNacion());
			}

			if (idPersonaEmisor != null && 
					valEmisorService.existDocBaseByEmisor(docResponse.getGtime().getTipo(),
															docResponse.getGtime().getNumeroReferencia(),
							idPersonaEmisor, docResponse.getFem())) {
				Validacion val = new Validacion(TipoValidacion.EMISOR_REFERENCIA_ASOCIADA, false, new Date());
				docResponse.getListValidaciones().add(val);
			}
		}
		exchange.getIn().setHeader("docResponse", docResponse);
	}
}
