package cl.gob.sna.gtime.validaciones.ingreso.beans;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.api.vo.Locacion;
import cl.gob.sna.gtime.validaciones.genericas.service.ValidacionLocacionesService;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;

public class ValidacionLocacionesBeans implements Processor {

	private final Logger LOG = Logger.getLogger(ValidacionLocacionesBeans.class);

	@Autowired
	private ValidacionLocacionesService validacionLocacionesService;
	
 
 	public void process(Exchange exchange) throws Exception {
		DocumentoResponse docResponse = exchange.getIn().getHeader("docResponse",DocumentoResponse.class);
		Integer seq = 1;
		Boolean existePe = Boolean.FALSE;
		Boolean existePd = Boolean.FALSE;

		for (Locacion locacion : docResponse.getGtime().getLocaciones().getLocacion()) {
			
			docResponse.getListValidaciones().addAll(validacionLocacionesService.validaNombreLocacion(seq,
																									  docResponse.getGtime().getTipo(),
																									  locacion,
																									  existePe,
																									  existePd));
			docResponse.getListValidaciones().addAll(validacionLocacionesService.validaCodigoLocacion(seq,locacion));
			if("2.0".equals(docResponse.getGtime().getVersion().trim())) {
				docResponse.getListValidaciones().addAll(validacionLocacionesService.validaDescripcionLocacion_VersionNot2_0(seq,locacion));
			}
			docResponse.getListValidaciones().addAll(validacionLocacionesService.validaExistenciaLocacionByViaTransporte(seq,
																														 docResponse.getViaTransporte(),
																														 docResponse.getTipoManifiesto(),
																														 docResponse.getGtime().getTipo(),
																														 locacion));
			seq++;
		}
		
		if(docResponse.getGtime().getLocaciones() == null || 
				docResponse.getGtime().getLocaciones().getLocacion() == null || 
					docResponse.getGtime().getLocaciones().getLocacion().isEmpty()) {
			Validacion val = new Validacion(TipoValidacion.LOCACION_DESCRIPCION_LARGO, false, new Date());
			val.setTexto(TipoValidacion.LOCACION_DESCRIPCION_LARGO.getTexto().replace("[1?]",docResponse.getGtime().getTipo()));
			docResponse.getListValidaciones().add(val);
		}
		
		if("I".equals( docResponse.getTipoManifiesto())) {
			if(existePe.booleanValue()) {
				Validacion val = new Validacion(TipoValidacion.LOCACION_PUERTO_EMBARQUE_OBLIGATORIO, false, new Date());
				val.setTexto(TipoValidacion.LOCACION_PUERTO_EMBARQUE_OBLIGATORIO.getTexto().replace("[1?]",docResponse.getGtime().getTipo()));
				docResponse.getListValidaciones().add(val);
			}
			if(existePd.booleanValue()) {
				Validacion val = new Validacion(TipoValidacion.LOCACION_PUERTO_DESEMBARQUE_OBLIGATORIO, false, new Date());
				val.setTexto(TipoValidacion.LOCACION_PUERTO_DESEMBARQUE_OBLIGATORIO.getTexto().replace("[1?]",docResponse.getGtime().getTipo()));
				docResponse.getListValidaciones().add(val);
			}
		}
		
		exchange.getIn().setHeader("docResponse", docResponse);
	}
}
