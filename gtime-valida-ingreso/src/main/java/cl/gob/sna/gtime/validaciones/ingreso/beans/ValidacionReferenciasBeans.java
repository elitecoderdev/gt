package cl.gob.sna.gtime.validaciones.ingreso.beans;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.api.vo.Referencia;
import cl.gob.sna.gtime.validaciones.genericas.service.ValidacionReferenciaService;
import cl.gob.sna.gtime.validaciones.repository.dto.DocumentoBaseTo;
import cl.gob.sna.gtime.validaciones.repository.dto.EmisorTo;
import cl.gob.sna.gtime.validaciones.utils.StringValidation;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;

public class ValidacionReferenciasBeans implements Processor {

	private final Logger LOG = Logger.getLogger(ValidacionReferenciasBeans.class);

	@Autowired
	private ValidacionReferenciaService validacionReferenciaService;
	
	private static final String REFERENCIA_TIPO_REF = "REF";
	private static final String TIPO_INGRESO = "I";
	private static final String TIPO_SALIDA = "S";
	//TODO: bajar complejidad cognitiva
	public void process(Exchange exchange) throws Exception {
		EmisorTo emisor  =  exchange.getIn().getHeader("emisor", EmisorTo.class);
		DocumentoResponse docResponse = exchange.getIn().getHeader("docResponse",DocumentoResponse.class);
		int countReferenciasTipoRef = 0;
		
		if(docResponse.getGtime().getReferencias() == null 
				|| docResponse.getGtime().getReferencias().getReferencia() == null 
					|| docResponse.getGtime().getReferencias().getReferencia().isEmpty() ) {
			Validacion val = new Validacion(TipoValidacion.REFERENCIA_SIN_REFERENCIA_DOCUMENTAL, false, new Date());
 			docResponse.getListValidaciones().add(val);
		} else {
			Integer secuencia = 1;
			for (Referencia referencia : docResponse.getGtime().getReferencias().getReferencia()) {
				if(StringValidation.isNull(referencia.getTipoReferencia()) ) {
					Validacion val = new Validacion(TipoValidacion.REFERENCIA_TIPO_NULO, false, new Date());
					val.setTexto(TipoValidacion.REFERENCIA_TIPO_NULO.getTexto().replace("[1?]",secuencia.toString()));
		 			docResponse.getListValidaciones().add(val);
				}
				
				if(REFERENCIA_TIPO_REF.equals(referencia.getTipoReferencia())) {
					countReferenciasTipoRef++;
				}
				
				if(StringValidation.isNull(referencia.getTipoDocumento())) {
					Validacion val = new Validacion(TipoValidacion.REFERENCIA_TIPO_DOCUMENTO_NULO, false, new Date());
					val.setTexto(TipoValidacion.REFERENCIA_TIPO_DOCUMENTO_NULO.getTexto().replace("[1?]",secuencia.toString()));
		 			docResponse.getListValidaciones().add(val);
				}else if(!validacionReferenciaService.isDocTipoDocumentoExistente(referencia.getTipoDocumento().trim())){
					Validacion val = new Validacion(TipoValidacion.REFERENCIA_TIPO_DOCUMENTO_NO_REGISTRADO, false, new Date());
					val.setTexto(TipoValidacion.REFERENCIA_TIPO_DOCUMENTO_NO_REGISTRADO.getTexto()
																							.replace("[1?]",secuencia.toString())
																								.replace("[2?]",referencia.getTipoDocumento().trim()));
					
		 			docResponse.getListValidaciones().add(val);
				}
				
				if(StringValidation.isNull(referencia.getTipoReferencia()) 
						&& StringValidation.isNull(referencia.getTipoDocumento()) 
							&& !validacionReferenciaService.existdocRelacionPermitida(docResponse.getGtime().getTipo(), 
																					 	referencia.getTipoReferencia(), 
																					 		referencia.getTipoDocumento())) {
					Validacion val = new Validacion(TipoValidacion.REFERENCIA_TIPO_DOCUMENTO_NO_RELACIONADO, false, new Date());
					val.setTexto(TipoValidacion.REFERENCIA_TIPO_DOCUMENTO_NO_RELACIONADO.getTexto()
																							.replace("[1?]",secuencia.toString())
																								.replace("[2?]",referencia.getTipoReferencia().trim())
																									.replace("[3?]",docResponse.getGtime().getTipo().trim())
																										.replace("[4?]",referencia.getTipoDocumento().trim()));
 		 			docResponse.getListValidaciones().add(val);
 				}				
				
				if(StringValidation.isNull(referencia.getNumero())) {
					Validacion val = new Validacion(TipoValidacion.REFERENCIA_SIN_ATRIBUTO_NUMERO, false, new Date());
					val.setTexto(TipoValidacion.REFERENCIA_SIN_ATRIBUTO_NUMERO.getTexto().replace("[1?]",secuencia.toString()));
		 			docResponse.getListValidaciones().add(val);
 				}
 				if(StringValidation.isNull(referencia.getFecha())) {
					Validacion val = new Validacion(TipoValidacion.REFERENCIA_SIN_FECHA, false, new Date());
					val.setTexto(TipoValidacion.REFERENCIA_SIN_FECHA.getTexto().replace("[1?]",secuencia.toString()));
		 			docResponse.getListValidaciones().add(val);
 				}
				
				DocumentoBaseTo documentoBaseTo = null;
				if(!StringValidation.isNull(referencia.getTipoDocumento())  
						&& !StringValidation.isNull(referencia.getNumero())
							&& !StringValidation.isNull(referencia.getFecha())) {
					documentoBaseTo = validacionReferenciaService.obtenerByTipoDocNumExternoFechaEmision(referencia.getTipoDocumento(), referencia.getNumero(), referencia.getFecha());
					//HOT FIX
					if (documentoBaseTo != null && documentoBaseTo.getId() != null){
						docResponse.getGtime().setNroEncabezado("" + documentoBaseTo.getId());
					}
				}
				
				if(documentoBaseTo == null) {
					Validacion val = new Validacion(TipoValidacion.REFERENCIA_REF_EMI_FEM_NO_REGISTRADO, false, new Date());
					val.setTexto(TipoValidacion.REFERENCIA_REF_EMI_FEM_NO_REGISTRADO.getTexto()
																						.replace("[1?]",secuencia.toString())
																						.replace("[2?]",referencia.getTipoDocumento())
																						.replace("[3?]",referencia.getEmisor())
																						.replace("[4?]",referencia.getFecha())
																						.replace("[5?]",referencia.getNumero()));
		 			docResponse.getListValidaciones().add(val);
				}else {
					if(StringValidation.isNull(documentoBaseTo.getDocEstados())){
						Validacion val = new Validacion(TipoValidacion.REFERENCIA_MANIFIESTO_ANULADO, false, new Date());
						val.setTexto(TipoValidacion.REFERENCIA_MANIFIESTO_ANULADO.getTexto()
																							.replace("[1?]",referencia.getNumero())
																							.replace("[2?]",referencia.getFecha())
																							.replace("[3?]",referencia.getEmisor()));
			 			docResponse.getListValidaciones().add(val);
					}else {
						docResponse.setTipoManifiesto(documentoBaseTo.getTipoManifiesto()); 
						docResponse.setViaTransporte(documentoBaseTo.getViaTransporte()); 
						
						if(!documentoBaseTo.getTipoId().equals(emisor.getTipoId()) ){
							Validacion val = new Validacion(TipoValidacion.REFERENCIA_EMISOR_TIPO_NO_CORRESPONDE, false, new Date());
 				 			docResponse.getListValidaciones().add(val);
						}
						if(!documentoBaseTo.getNumeroId().equals(emisor.getValorId()) ){
							Validacion val = new Validacion(TipoValidacion.REFERENCIA_EMISOR_VALOR_NO_CORRESPONDE, false, new Date());
 				 			docResponse.getListValidaciones().add(val);
						}

						if(TIPO_INGRESO.equals(documentoBaseTo.getTipoManifiesto()) && !TIPO_INGRESO.equals(docResponse.getGtime().getTipoOperacion()) ){
							Validacion val = new Validacion(TipoValidacion.REFERENCIA_MANIFIESTO_REFERENCIADO_INGRESO, false, new Date());
 				 			docResponse.getListValidaciones().add(val);
						}
						if(TIPO_SALIDA.equals(documentoBaseTo.getTipoManifiesto()) && !TIPO_SALIDA.equals(docResponse.getGtime().getTipoOperacion()) ){
							Validacion val = new Validacion(TipoValidacion.REFERENCIA_MANIFIESTO_REFERENCIADO_SALIDA, false, new Date());
 				 			docResponse.getListValidaciones().add(val);
						}
					}
				}
				
				if(!StringValidation.isNull(referencia.getTipoIdEmisor())
						&& !StringValidation.isNull(referencia.getNacIdEmisor())
							&& !StringValidation.isNull(referencia.getValorIdEmisor())
								 ) {
					if(!validacionReferenciaService.existePersona(referencia.getTipoIdEmisor(),
																	referencia.getValorIdEmisor(), 
																	referencia.getNacIdEmisor())) {
						Validacion val = new Validacion(TipoValidacion.REFERENCIA_EMISOR_NO_REGISTRADO, false, new Date());
						val.setTexto(TipoValidacion.REFERENCIA_EMISOR_NO_REGISTRADO.getTexto().replace("[1?]",secuencia.toString()));
			 			docResponse.getListValidaciones().add(val);
					}
				} else {
					Validacion val = new Validacion(TipoValidacion.REFERENCIA_EMISOR_INCOMPLETO, false, new Date());
					val.setTexto(TipoValidacion.REFERENCIA_EMISOR_INCOMPLETO.getTexto().replace("[1?]",secuencia.toString()));
		 			docResponse.getListValidaciones().add(val);
					
				}
				
				if(StringValidation.isNull(referencia.getEmisor())){
					Validacion val = new Validacion(TipoValidacion.REFERENCIA_EMISOR_SIN_NOMBRE, false, new Date());
					val.setTexto(TipoValidacion.REFERENCIA_EMISOR_SIN_NOMBRE.getTexto().replace("[1?]",secuencia.toString()));
		 			docResponse.getListValidaciones().add(val);
				}
				secuencia++;
			}
			
			if(countReferenciasTipoRef == 0) {
				Validacion val = new Validacion(TipoValidacion.REFERENCIA_SIN_REFERENCIA, false, new Date());
 	 			docResponse.getListValidaciones().add(val);
			} else if(countReferenciasTipoRef > 1) {
				Validacion val = new Validacion(TipoValidacion.REFERENCIA_MAXIMO_REFERENCIA, false, new Date());
 	 			docResponse.getListValidaciones().add(val);
			}
		}
		
		
//	 if(docResponse.getGtime().getReferencias().getReferencia().size() > 1  ){
//			Validacion val = new Validacion(TipoValidacion.REFERENCIA_MAXIMO_REFERENCIA, false, new Date());
// 			docResponse.getListValidaciones().add(val);
//		} 
	}
}
