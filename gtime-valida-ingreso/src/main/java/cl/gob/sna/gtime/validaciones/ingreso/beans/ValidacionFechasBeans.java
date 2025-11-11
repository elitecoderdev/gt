package cl.gob.sna.gtime.validaciones.ingreso.beans;

import java.util.Date;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.api.vo.Fecha;
import cl.gob.sna.gtime.validaciones.genericas.service.ValidacionFechasService;
import cl.gob.sna.gtime.validaciones.utils.StringValidation;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;

public class ValidacionFechasBeans implements Processor {

	final Logger LOG = Logger.getLogger(ValidacionFechasBeans.class);

	@Autowired
	private ValidacionFechasService validacionFechasService;

	private DocumentoResponse validaNombreFecha(Fecha fecha, DocumentoResponse docResponse, Integer ordinality) {
		try {
			if (StringValidation.isNull(fecha.getNombre())) {
				Validacion val = new Validacion(TipoValidacion.FECHAS_SIN_NOMBRE, false, new Date());
				val.setTexto(TipoValidacion.FECHAS_SIN_NOMBRE.getTexto().replace("[1?]", ordinality.toString()));
				docResponse.getListValidaciones().add(val);
			} else if (!StringValidation.largoMaximo(fecha.getNombre(), 10)) {
				Validacion val = new Validacion(TipoValidacion.FECHAS_LARGO_NOMBRE, false, new Date());
				val.setTexto(TipoValidacion.FECHAS_LARGO_NOMBRE.getTexto().replace("[1?]", ordinality.toString()));
				docResponse.getListValidaciones().add(val);
			} else if (!validacionFechasService.isFechaValidaGtime(fecha.getNombre())) {
				Validacion val = new Validacion(TipoValidacion.FECHAS_TIPO_FECHA_NO_DEFINIDO, false, new Date());
				val.setTexto(TipoValidacion.FECHAS_TIPO_FECHA_NO_DEFINIDO.getTexto()
						.replace("[1?]", ordinality.toString()).replace("[2?]", ordinality.toString()));
				docResponse.getListValidaciones().add(val);
			} else if (validacionFechasService.isFechaCreacion(fecha.getNombre())) {
				Validacion val = new Validacion(TipoValidacion.FECHAS_FECHA_CREACION_NO_PERMITIDA, false, new Date());
				val.setTexto(TipoValidacion.FECHAS_FECHA_CREACION_NO_PERMITIDA.getTexto().replace("[1?]",
						ordinality.toString()));
				docResponse.getListValidaciones().add(val);
			}
		} catch (Exception e) {
			LOG.error(TipoValidacion.FECHAS_ERROR_PROCESAR_NOMBRE.getTexto().replace("[1?]", ordinality.toString()), e);
			Validacion val = new Validacion(TipoValidacion.FECHAS_ERROR_PROCESAR_NOMBRE, false, new Date());
			val.setTexto(TipoValidacion.FECHAS_ERROR_PROCESAR_NOMBRE.getTexto().replace("[1?]", ordinality.toString()));
			docResponse.getListValidaciones().add(val);
		}

		return docResponse;
	}

	private DocumentoResponse validaValorFecha(Fecha fecha, DocumentoResponse docResponse, Integer ordinality) {
		
		try {
 			if (StringValidation.isNull(fecha.getValor())) {
				Validacion val = new Validacion(TipoValidacion.FECHAS_SIN_VALOR, false, new Date());
				val.setTexto(TipoValidacion.FECHAS_SIN_VALOR.getTexto().replace("[1?]", fecha.getNombre()));
				docResponse.getListValidaciones().add(val);
			}else if(!validacionFechasService.isFormatoCorrectoFecha(fecha.getNombre(), fecha.getValor())){
				throw new Exception("formato de fecha incorrecto nombre: "+ fecha.getNombre() + " valor: "+fecha.getValor());
			}else if(!validacionFechasService.isFechaFemMenorHoy(fecha.getNombre(), fecha.getValor())){
				Validacion val = new Validacion(TipoValidacion.FECHAS_EMISION_NO_VALIDA, false, new Date());
 				docResponse.getListValidaciones().add(val);
			}
  			
		} catch (Exception e) {
			LOG.error(TipoValidacion.FECHAS_ERROR_PROCESAR_VALOR.getTexto().replace("[1?]", ordinality.toString()), e);
 			Validacion val = new Validacion(TipoValidacion.FECHAS_ERROR_PROCESAR_VALOR, false, new Date());
			val.setTexto(TipoValidacion.FECHAS_ERROR_PROCESAR_VALOR.getTexto().replace("[1?]", ordinality.toString()));
			docResponse.getListValidaciones().add(val);
		}
		return docResponse;
	}

	public void process(Exchange exchange) throws Exception {
		DocumentoResponse docResponse = (DocumentoResponse) exchange.getIn().getHeader("docResponse");
		Map<String, Integer> fechasRepetidas = validacionFechasService
				.countFechasRepetidas(docResponse.getGtime().getFechas().getFecha());
		Date fem = null;
		if (fechasRepetidas == null) {
			Integer ordinality = 1;
//			boolean contieneFEM = false;
			for (Fecha fecha : docResponse.getGtime().getFechas().getFecha()) {
				docResponse = validaNombreFecha(fecha, docResponse, ordinality);
				docResponse = validaValorFecha(fecha, docResponse, ordinality);
				
//				if(!contieneFEM) {
//					contieneFEM = validacionFechasService.isFem(fecha.getNombre());
//				}	
				if(fem == null) {
					fem = validacionFechasService.isFem(fecha.getNombre())? validacionFechasService.stringToDate(fecha.getNombre(), fecha.getValor()):null;
				}
				ordinality++;
			}
			if(fem == null) {
				Validacion val = new Validacion(TipoValidacion.FECHAS_FEM_NO_VALIDA, false, new Date());
 				docResponse.getListValidaciones().add(val);
			}
//			docResponse.setContieneFEM(fem != null);
			docResponse.setFem(fem);
			
		} else {
			for (Map.Entry<String, Integer> fecha : fechasRepetidas.entrySet()) {
				Validacion val = new Validacion(TipoValidacion.FECHAS_REPETIDAS, false, new Date());
				val.setTexto(TipoValidacion.FECHAS_REPETIDAS.getTexto()
						.replace("[1?]", fecha.getKey().equals("NULO") ? "" : fecha.getKey())
						.replace("[2?]", fecha.getValue().toString()));
				docResponse.getListValidaciones().add(val);
			}
		}
		exchange.getIn().setHeader("fem",fem);
		exchange.getIn().getHeader("docResponse",docResponse);

	}

}
