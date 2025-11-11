package cl.gob.sna.gtime.validaciones.ingreso.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.api.vo.Participacion;
import cl.gob.sna.gtime.validaciones.genericas.service.ValidacionDocRolTipoDocService;
import cl.gob.sna.gtime.validaciones.genericas.service.ValidacionOperadorService;
import cl.gob.sna.gtime.validaciones.genericas.service.ValidacionPaisService;
import cl.gob.sna.gtime.validaciones.repository.DocRolTipoDocRepository;
import cl.gob.sna.gtime.validaciones.repository.dto.EmisorTo;
import cl.gob.sna.gtime.validaciones.utils.RutValidation;
import cl.gob.sna.gtime.validaciones.utils.StringValidation;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;

public class ValidacionParticipacionesBeans implements Processor {

	private final Logger LOG = Logger.getLogger(ValidacionParticipacionesBeans.class);

	@Autowired
	private ValidacionPaisService validacionPaisService;
	@Autowired
	private ValidacionDocRolTipoDocService validacionDocRolTipoDocService;
	@Autowired
	private ValidacionOperadorService validacionOperadorService;
	
	public void process(Exchange exchange) throws Exception {
		DocumentoResponse docResponse =  exchange.getIn().getHeader("docResponse",DocumentoResponse.class);
		docResponse.getGtime().getParticipaciones();
		Integer ordinality = 1;
		List<String> participacionesObligatorias = new ArrayList<>();
		EmisorTo emisor = new EmisorTo();
		for (Participacion participacion : docResponse.getGtime().getParticipaciones().getParticipacion()) {
 			this.validarNombre(participacion.getNombre(), docResponse, ordinality);
 			this.validarTipoId(participacion.getTipoId(), docResponse, ordinality);
 			this.validarNacionId(participacion.getNacionId(), docResponse, ordinality);
 			this.validarValorId(participacion.getNacionId(), docResponse, ordinality);
  			this.validarNombres(participacion.getNombres(), docResponse, ordinality);
  			this.validarDireccion(participacion.getDireccion(), docResponse, ordinality);
  			this.validarCodPais(participacion.getCodigoPais(), docResponse, ordinality);
  			this.validarByTipoParticipante(participacion, docResponse, ordinality,participacionesObligatorias, emisor);
 			
			ordinality++;
		}

		exchange.getIn().setHeader("emisor", emisor);

		if(!participacionesObligatorias.contains(DocRolTipoDocRepository.TRANSPORTISTA)) {
			Validacion val = new Validacion(TipoValidacion.PARTICIPACIONES_SIN_TRANSPORTISTA, false, new Date());
			val.setTexto(TipoValidacion.PARTICIPACIONES_SIN_TRANSPORTISTA.getTexto().replace("[1?]",docResponse.getGtime().getTipo()));
			docResponse.getListValidaciones().add(val);
		}
		if(!participacionesObligatorias.contains(DocRolTipoDocRepository.EMISOR)) {
			Validacion val = new Validacion(TipoValidacion.PARTICIPACIONES_SIN_EMISOR, false, new Date());
			val.setTexto(TipoValidacion.PARTICIPACIONES_SIN_EMISOR.getTexto().replace("[1?]",docResponse.getGtime().getTipo()));
			docResponse.getListValidaciones().add(val);					
		}
		if(!participacionesObligatorias.contains(DocRolTipoDocRepository.CONSIGNANTE)) {
			Validacion val = new Validacion(TipoValidacion.PARTICIPACIONES_SIN_CONSIGNANTE, false, new Date());
			val.setTexto(TipoValidacion.PARTICIPACIONES_SIN_CONSIGNANTE.getTexto().replace("[1?]",docResponse.getGtime().getTipo()));
			docResponse.getListValidaciones().add(val);			
		}
		if(!participacionesObligatorias.contains(DocRolTipoDocRepository.CONSIGNATARIO)) {
			Validacion val = new Validacion(TipoValidacion.PARTICIPACIONES_SIN_CONSIGNATARIO, false, new Date());
			val.setTexto(TipoValidacion.PARTICIPACIONES_SIN_CONSIGNATARIO.getTexto().replace("[1?]",docResponse.getGtime().getTipo()));
			docResponse.getListValidaciones().add(val);			
		}
		
	}
	private void  validarNombre	(String nombre, DocumentoResponse docResponse, Integer ordinality) {
		
		
		if (StringValidation.isNull(nombre)) {
			Validacion val = new Validacion(TipoValidacion.PARTICIPACIONES_SIN_NOMBRE, false, new Date());
			val.setTexto(TipoValidacion.PARTICIPACIONES_SIN_NOMBRE.getTexto().replace("[1?]",ordinality.toString()));
			docResponse.getListValidaciones().add(val);
		} else if(!StringValidation.largoMaximo(nombre, 5)){
			Validacion val = new Validacion(TipoValidacion.PARTICIPACIONES_LARGO_NOMBRE, false, new Date());
			val.setTexto(TipoValidacion.PARTICIPACIONES_LARGO_NOMBRE.getTexto().replace("[1?]",ordinality.toString()));
			docResponse.getListValidaciones().add(val);
		}
		
		
	}
	private void  validarNombres(String nombre, DocumentoResponse docResponse, Integer ordinality) {
		
		
		if (StringValidation.isNull(nombre)) {
			Validacion val = new Validacion(TipoValidacion.PARTICIPACIONES_SIN_NOMBRES, false, new Date());
			val.setTexto(TipoValidacion.PARTICIPACIONES_SIN_NOMBRE.getTexto().replace("[1?]",ordinality.toString()));
			docResponse.getListValidaciones().add(val);
		} else if(!StringValidation.largoMaximo(nombre, 255)){
			Validacion val = new Validacion(TipoValidacion.PARTICIPACIONES_LARGO_NOMBRES, false, new Date());
			val.setTexto(TipoValidacion.PARTICIPACIONES_LARGO_NOMBRE.getTexto().replace("[1?]",ordinality.toString()));
			docResponse.getListValidaciones().add(val);
		}
		
		
	}
	private void  validarTipoId(String tipoId, DocumentoResponse docResponse, Integer ordinality) {
		
		if (!StringValidation.isNull(tipoId) && !StringValidation.largoMaximo(tipoId,30)) {
			Validacion val = new Validacion(TipoValidacion.PARTICIPACIONES_TIPO_ID, false, new Date());
			val.setTexto(TipoValidacion.PARTICIPACIONES_TIPO_ID.getTexto().replace("[1?]",ordinality.toString()));
			docResponse.getListValidaciones().add(val);
		}
	}
	private void  validarNacionId(String nacionId, DocumentoResponse docResponse, Integer ordinality) {
		if (!StringValidation.isNull(nacionId) && !StringValidation.largoMaximo(nacionId,5)) {
			Validacion val = new Validacion(TipoValidacion.PARTICIPACIONES_NACION_ID, false, new Date());
			val.setTexto(TipoValidacion.PARTICIPACIONES_NACION_ID.getTexto().replace("[1?]",ordinality.toString()));
			docResponse.getListValidaciones().add(val);
		}
	}
	private void  validarValorId(String valorId, DocumentoResponse docResponse, Integer ordinality) {
		if (!StringValidation.isNull(valorId) && !StringValidation.largoMaximo(valorId,255)) {
			Validacion val = new Validacion(TipoValidacion.PARTICIPACIONES_VALOR_ID, false, new Date());
			val.setTexto(TipoValidacion.PARTICIPACIONES_VALOR_ID.getTexto().replace("[1?]",ordinality.toString()));
			docResponse.getListValidaciones().add(val);
		}
	}
	private void  validarDireccion(String direccion, DocumentoResponse docResponse, Integer ordinality) {
		if (!StringValidation.isNull(direccion) && !StringValidation.largoMaximo(direccion,255)) {
			Validacion val = new Validacion(TipoValidacion.PARTICIPACIONES_DIRECCION, false, new Date());
			val.setTexto(TipoValidacion.PARTICIPACIONES_DIRECCION.getTexto().replace("[1?]",ordinality.toString()));
			docResponse.getListValidaciones().add(val);
		}
	}
	private void  validarCodPais(String codPais, DocumentoResponse docResponse, Integer ordinality) {
		
		if(!StringValidation.isNull(codPais) && !validacionPaisService.isPaisExistente(codPais)) {
				Validacion val = new Validacion(TipoValidacion.PARTICIPACIONES_PAIS_NO_EXISTE, false, new Date());
				val.setTexto(TipoValidacion.PARTICIPACIONES_PAIS_NO_EXISTE.getTexto().replace("[1?]",ordinality.toString()));
				docResponse.getListValidaciones().add(val);
		}
		 
	}
	private void  validarByTipoParticipante(Participacion participacion , DocumentoResponse docResponse, Integer ordinality,List<String> participacionesObligatorias, EmisorTo emisor) {
		if(validacionDocRolTipoDocService.isDocRolTipoDocExistente(participacion.getNombre(), docResponse.getGtime().getTipo())) {
			participacionesObligatorias.add(participacion.getNombre());
			
			switch (participacion.getNombre().trim()) {
				case DocRolTipoDocRepository.ALMACENISTA:
					this.validarAlmacenista(participacion, docResponse);
					break;
				case DocRolTipoDocRepository.CONSIGNATARIO:
					this.validarConsignatario(participacion, docResponse);			
					break;
				case DocRolTipoDocRepository.CONSIGNANTE:
					this.validarConsignante(participacion, docResponse);
					break;
				case DocRolTipoDocRepository.EMISOR:
					this.validarEmisor(participacion, docResponse,emisor);
					break;
				case DocRolTipoDocRepository.TRANSPORTISTA:
					this.validarTransportista(participacion, docResponse);
					break;
				default:
					break;
			}
		}else {
			Validacion val = new Validacion(TipoValidacion.PARTICIPACIONES_DOCROLTIPODOC_NO_EXISTE, false, new Date());
			val.setTexto(TipoValidacion.PARTICIPACIONES_DOCROLTIPODOC_NO_EXISTE.getTexto().replace("[1?]",ordinality.toString()).replace("[2?]",participacion.getNombre()));
			docResponse.getListValidaciones().add(val);
		}
	}
	
	private void validarAlmacenista(Participacion participacion , DocumentoResponse docResponse) {
		if(StringValidation.isNull(participacion.getTipoId())) {
				docResponse.getListValidaciones().add(new Validacion(TipoValidacion.PARTICIPACIONES_TIPO_ALMACENISTA_TIPO_ID, false, new Date()));
		}else if("ADU".equals(participacion.getTipoId())){
				docResponse.getListValidaciones().add(new Validacion(TipoValidacion.PARTICIPACIONES_TIPO_ALMACENISTA_TIPO_ID_ADU, false, new Date()));
		}
		if(StringValidation.isNull(participacion.getNacionId())) {
				docResponse.getListValidaciones().add(new Validacion(TipoValidacion.PARTICIPACIONES_TIPO_ALMACENISTA_NACION_ID, false, new Date()));
		} 
		if(StringValidation.isNull(participacion.getValorId())) {
				docResponse.getListValidaciones().add(new Validacion(TipoValidacion.PARTICIPACIONES_TIPO_ALMACENISTA_VALOR_ID, false, new Date()));
		} 
		if(StringValidation.isNull(participacion.getCodigoPais())) {
				docResponse.getListValidaciones().add(new Validacion(TipoValidacion.PARTICIPACIONES_TIPO_ALMACENISTA_COD_PAIS, false, new Date()));
		}  	
	}
	private void validarTransportista(Participacion participacion , DocumentoResponse docResponse) {
		if(StringValidation.isNull(participacion.getTipoId())) {
				docResponse.getListValidaciones().add(new Validacion(TipoValidacion.PARTICIPACIONES_TIPO_ALMACENISTA_TIPO_ID, false, new Date()));
		}
		if(StringValidation.isNull(participacion.getNacionId())) {
				docResponse.getListValidaciones().add(new Validacion(TipoValidacion.PARTICIPACIONES_TIPO_ALMACENISTA_NACION_ID, false, new Date()));
		} 
		if(StringValidation.isNull(participacion.getValorId())) {
				docResponse.getListValidaciones().add(new Validacion(TipoValidacion.PARTICIPACIONES_TIPO_ALMACENISTA_VALOR_ID, false, new Date()));
		} 
		if(StringValidation.isNull(participacion.getCodigoPais())) {
				docResponse.getListValidaciones().add(new Validacion(TipoValidacion.PARTICIPACIONES_TIPO_ALMACENISTA_COD_PAIS, false, new Date()));
		}  	
	}
	private void validarEmisor(Participacion participacion , DocumentoResponse docResponse, EmisorTo emisor) {
		if(StringValidation.isNull(participacion.getTipoId())) {
				docResponse.getListValidaciones().add(new Validacion(TipoValidacion.PARTICIPACIONES_TIPO_EMISOR_TIPO_ID, false, new Date()));
		}
		if(StringValidation.isNull(participacion.getNacionId())) {
				docResponse.getListValidaciones().add(new Validacion(TipoValidacion.PARTICIPACIONES_TIPO_EMISOR_NACION_ID, false, new Date()));
		}
		if(StringValidation.isNull(participacion.getValorId())) {
				docResponse.getListValidaciones().add(new Validacion(TipoValidacion.PARTICIPACIONES_TIPO_EMISOR_VALOR_ID, false, new Date()));
		} else if(!validacionOperadorService.isOperadorCourierExistente(participacion.getValorId())) {
			Validacion val = new Validacion(TipoValidacion.PARTICIPACIONES_TIPO_EMISOR_OPERADOR_COURIER_NO_EXISTE, false, new Date());
			val.setTexto(TipoValidacion.PARTICIPACIONES_TIPO_EMISOR_OPERADOR_COURIER_NO_EXISTE.getTexto().replace("[1?]",participacion.getValorId()));
			docResponse.getListValidaciones().add(val);
		}
		
	
//TODO: 
//	     IF r_PARTICIPACIONES.TIPO_ID IS NOT NULL AND r_PARTICIPACIONES.NACION_ID IS NOT NULL AND r_PARTICIPACIONES.VALOR_ID IS NOT NULL THEN
//         p_TIPO_ID_EMISOR   := r_PARTICIPACIONES.TIPO_ID;
//         p_NACION_ID_EMISOR := r_PARTICIPACIONES.NACION_ID;
//         p_VALOR_ID_EMISOR  := r_PARTICIPACIONES.VALOR_ID;
//         p_NOMBRE_EMISOR    := r_PARTICIPACIONES.NOMBRES;
//       END IF;

		if(	!StringValidation.isNull(participacion.getTipoId()) 	&& 
			!StringValidation.isNull(participacion.getNacionId()) 	&& 
			!StringValidation.isNull(participacion.getValorId()) 	&& 
			!StringValidation.isNull(participacion.getNombres()) 
			){
			emisor.setTipoId(participacion.getTipoId());
			emisor.setNacion(participacion.getNacionId());
			emisor.setNombre(participacion.getNombres());
			emisor.setValorId(participacion.getValorId());
		 										 
		}

	}
	private void validarConsignante(Participacion participacion , DocumentoResponse docResponse) {
		
		if("PAS".equals(participacion.getTipoId()) && StringValidation.isNull(participacion.getNacionId())) {
			docResponse.getListValidaciones().add(new Validacion(TipoValidacion.PARTICIPACIONES_TIPO_CONSIGNANTE_SIN_NACION, false, new Date()));
 		}
		if(StringValidation.isNull(participacion.getCodigoPais())) {
			docResponse.getListValidaciones().add(new Validacion(TipoValidacion.PARTICIPACIONES_TIPO_CONSIGNANTE_SIN_PAIS, false, new Date()));
 		}
	}
	private void validarConsignatario(Participacion participacion , DocumentoResponse docResponse) {
		if(StringValidation.isNull(participacion.getTipoId())) {
			docResponse.getListValidaciones().add(new Validacion(TipoValidacion.PARTICIPACIONES_TIPO_EMISOR_TIPO_ID, false, new Date()));
		}
		if("PAS".equals(participacion.getTipoId()) && StringValidation.isNull(participacion.getNacionId())) {
			docResponse.getListValidaciones().add(new Validacion(TipoValidacion.PARTICIPACIONES_TIPO_CONSIGNATARIO_SIN_NACION, false, new Date()));
 		}
		if(StringValidation.isNull(participacion.getCodigoPais())) {
			docResponse.getListValidaciones().add(new Validacion(TipoValidacion.PARTICIPACIONES_TIPO_CONSIGNATARIO_SIN_PAIS, false, new Date()));
 		}
		if("RUT".equals(participacion.getTipoId()) && StringValidation.isNull(participacion.getValorId())) {
			docResponse.getListValidaciones().add(new Validacion(TipoValidacion.PARTICIPACIONES_TIPO_CONSIGNATARIO_VALOR_ID_TIPO_RUT, false, new Date()));
 		}
		if("RUT".equals(participacion.getTipoId()) &&
				(StringValidation.isNull(participacion.getValorId()) ||
					!RutValidation.isRutValido(participacion.getValorId()))) {
			Validacion val = new Validacion(TipoValidacion.PARTICIPACIONES_TIPO_CONSIGNATARIO_VALOR_ID_TIPO_RUT_ERRONEO, false, new Date());
			val.setTexto(TipoValidacion.PARTICIPACIONES_TIPO_CONSIGNATARIO_VALOR_ID_TIPO_RUT_ERRONEO.getTexto().replace("[1?]",participacion.getNombre()));
			docResponse.getListValidaciones().add(val); 		
		}
		
	}
}
