package cl.gob.sna.gtime.validaciones.genericas.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.gob.sna.gtime.api.vo.Observacion;
import cl.gob.sna.gtime.validaciones.repository.DocTipoObservacionRepository;
import cl.gob.sna.gtime.validaciones.utils.StringValidation;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;

@Service
public class ValidacionObservacionServiceImpl implements ValidacionObservacionService {
	private final Logger LOG = Logger.getLogger(ValidacionObservacionServiceImpl.class);

 
	@Autowired
	private DocTipoObservacionRepository docTipoObservacionRepository;
	
	@Override
	public List<Validacion> validaContenido(Integer secuencia,  Observacion observacion) {
		List<Validacion> validaciones = new ArrayList<>();
		if(StringValidation.isNull(observacion.getContenido())) {
			Validacion val = new Validacion(TipoValidacion.OBSERVACION_CONTENIDO_NULL, false, new Date());
			val.setTexto(TipoValidacion.OBSERVACION_CONTENIDO_NULL.getTexto().replace("[1?]",secuencia.toString()));
			validaciones.add(val);
		}
		return validaciones;
		
	}
	
	@Override
	public List<Validacion> validaNombre(Integer secuencia, String tipoDoc, Observacion observacion) {
		List<Validacion> validaciones = new ArrayList<>();
		if(!StringValidation.isNull(observacion.getNombre()) ) {
			boolean existeTipoDoc = docTipoObservacionRepository.isDocTipoDocObservacionExistente(tipoDoc, observacion.getNombre());
			if(!existeTipoDoc) {
				Validacion val = new Validacion(TipoValidacion.OBSERVACION_TIPO_DOC_NO_EXISTE, false, new Date());
				val.setTexto(TipoValidacion.OBSERVACION_TIPO_DOC_NO_EXISTE.getTexto().replace("[1?]",secuencia.toString())
																					 .replace("[2?]",observacion.getNombre())
																					 .replace("[3?]",tipoDoc));
				validaciones.add(val);
			}
			if(!"GRAL".equals(observacion.getNombre()) && 
					!"SOB".equals(observacion.getNombre())) {
				Validacion val = new Validacion(TipoValidacion.OBSERVACION_NOMBRE_NO_GRAL, false, new Date());
				val.setTexto(TipoValidacion.OBSERVACION_NOMBRE_NO_GRAL.getTexto().replace("[1?]",secuencia.toString()));
				validaciones.add(val);
			}
		}
		
		return validaciones;
	}
	

}
