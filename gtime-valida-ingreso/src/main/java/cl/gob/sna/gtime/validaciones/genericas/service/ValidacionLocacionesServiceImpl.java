package cl.gob.sna.gtime.validaciones.genericas.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.gob.sna.gtime.api.vo.Locacion;
import cl.gob.sna.gtime.validaciones.repository.DocTipoDocLocacionRepository;
import cl.gob.sna.gtime.validaciones.repository.LocacionRepository;
import cl.gob.sna.gtime.validaciones.repository.dto.LocacionTo;
import cl.gob.sna.gtime.validaciones.utils.StringValidation;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;

@Service
public class ValidacionLocacionesServiceImpl implements ValidacionLocacionesService {
	private final Logger LOG = Logger.getLogger(ValidacionLocacionesServiceImpl.class);

	@Autowired
	private DocTipoDocLocacionRepository docTipoDocLocacionRepository;
	@Autowired
	private LocacionRepository locacionRepository;
	
	@Override
	public List<Validacion> validaNombreLocacion(Integer secuencia, String tipoDoc, Locacion locacion, Boolean existePe, Boolean existePd) {
		List<Validacion> validaciones = new ArrayList<>();
		if(StringValidation.isNull(locacion.getNombre()) ) {
			Validacion val = new Validacion(TipoValidacion.LOCACION_NOMBRE_NULL, false, new Date());
			val.setTexto(TipoValidacion.LOCACION_NOMBRE_NULL.getTexto().replace("[1?]",secuencia.toString()));
			validaciones.add(val);
		}else {
			if (StringValidation.largoMaximo(locacion.getNombre(), 30) ) {
				existePe = existePe || "PE".equals(locacion.getNombre());
				existePd = existePd || "PD".equals(locacion.getNombre());
				if(!docTipoDocLocacionRepository.isDocTipoDocLocacionExistente(tipoDoc, locacion.getNombre())) {
					Validacion val = new Validacion(TipoValidacion.LOCACION_NOMBRE_INVALIDO_TIPO_DOC, false, new Date());
					val.setTexto(TipoValidacion.LOCACION_NOMBRE_INVALIDO_TIPO_DOC.getTexto().replace("[1?]",secuencia.toString())
																							.replace("[2?]",locacion.getNombre()));
					validaciones.add(val);
				}
			} else { 
				Validacion val = new Validacion(TipoValidacion.LOCACION_NOMBRE_LARGO, false, new Date());
				val.setTexto(TipoValidacion.LOCACION_NOMBRE_LARGO.getTexto().replace("[1?]",secuencia.toString()));
				validaciones.add(val);
			}
		}		
		return validaciones;
	}

	@Override
	public List<Validacion> validaCodigoLocacion(Integer secuencia, Locacion locacion) {
		List<Validacion> validaciones = new ArrayList<>();
		if(StringValidation.isNull(locacion.getCodigo())) {
			Validacion val = new Validacion(TipoValidacion.LOCACION_COD_NULL, false, new Date());
			val.setTexto(TipoValidacion.LOCACION_COD_NULL.getTexto().replace("[1?]",secuencia.toString()));
			validaciones.add(val);
		}else {
			if (!StringValidation.largoMaximo(locacion.getCodigo(), 5) ) {
				Validacion val = new Validacion(TipoValidacion.LOCACION_COD_LARGO, false, new Date());
				val.setTexto(TipoValidacion.LOCACION_COD_LARGO.getTexto().replace("[1?]",secuencia.toString()));
				validaciones.add(val);
			}
		}
		return validaciones;
	}

	@Override
	public List<Validacion> validaDescripcionLocacion_VersionNot2_0(Integer secuencia, Locacion locacion) {
		List<Validacion> validaciones = new ArrayList<>();
		if(StringValidation.isNull(locacion.getDescripcion())) {
			Validacion val = new Validacion(TipoValidacion.LOCACION_DESCRIPCION_NULL, false, new Date());
			val.setTexto(TipoValidacion.LOCACION_DESCRIPCION_NULL.getTexto().replace("[1?]",secuencia.toString()));
			validaciones.add(val);
		}else {
			if (!StringValidation.largoMaximo(locacion.getDescripcion(), 255) ) {
				Validacion val = new Validacion(TipoValidacion.LOCACION_DESCRIPCION_LARGO, false, new Date());
				val.setTexto(TipoValidacion.LOCACION_DESCRIPCION_LARGO.getTexto().replace("[1?]",secuencia.toString()));
				validaciones.add(val);
			}
		}
		return validaciones;
	}

	@Override
	public List<Validacion> validaExistenciaLocacionByViaTransporte(Integer secuencia, String viaTransporte, String tipoManifiesto,String tipoOperacion, Locacion locacion) {
		//TODO: DT disminuir complejidad cognitiva
		List<Validacion> validaciones = new ArrayList<>();
		LocacionTo locacionTo = null;

		if (viaTransporte == null){
			return new ArrayList<>();
		}
		switch (viaTransporte) {
			case "7":
			case "07":
				locacionTo = locacionRepository.obtenerLocacionUnByCodigo(locacion.getCodigo());
				break;
			default:
				locacionTo = locacionRepository.obtenerLocacionIataByCodigo(locacion.getCodigo());
				break;
		}
 		if(locacionTo == null) {
 			Validacion val = new Validacion(TipoValidacion.LOCACION_COD_NO_ENCONTRADO, false, new Date());
			val.setTexto(TipoValidacion.LOCACION_COD_NO_ENCONTRADO.getTexto().replace("[1?]",secuencia.toString())
																			 .replace("[2?]",locacion.getNombre())
																			 .replace("[3?]",locacion.getCodigo())
					);
			validaciones.add(val);
 		}else {
 			if(!locacionTo.getDescripcion().toUpperCase().equals(locacion.getDescripcion())) {
 				Validacion val = new Validacion(TipoValidacion.LOCACION_DESCRIPCION_NO_COINCIDE, false, new Date());
 				val.setTexto(TipoValidacion.LOCACION_DESCRIPCION_NO_COINCIDE.getTexto().replace("[1?]",secuencia.toString()));
 				validaciones.add(val);
 			}
 			if("I".equals(tipoManifiesto) && "I".equals(tipoOperacion)) {
 				//TODO: DT verificar control para locacion.getNombre() no nulo
 				//TODO: DT verificar solo una vez if(locacionTo.getCodigo() == null )
 				//TODO: DT optimizar switch no hay mayores diferencias entre los case mas que una variable dentro del mensaje
 				//TODO: DT muchos mensajes son muy similares evaluar si se pueden estandarizar esto permitiria a su vez optimizar las condiciones existentes para mostrar mensajes de validacion ademas de aportar en disminuir dificultad cognitiva
 				switch (locacion.getNombre()) {
					case "PE":
					case "LRM":
						if(locacionTo.getCodigo() == null ) {
							Validacion val = new Validacion(TipoValidacion.LOCACION_SIN_PAIS, false, new Date());
 	 		 				validaciones.add(val);
						} else if("CL".equals(locacionTo.getCodigo())){
							if("PE".equals(locacionTo.getCodigo())) {
								Validacion val = new Validacion(TipoValidacion.LOCACION_PUERTO_EMBARQUE_NO_CL, false, new Date());
								val.setTexto(TipoValidacion.LOCACION_PUERTO_EMBARQUE_NO_CL.getTexto().replace("[1?]",locacionTo.getCodigo()));
		 		 				validaciones.add(val);
							} else {
								Validacion val = new Validacion(TipoValidacion.LOCACION_LUGAR_RECEPCION_NO_CL, false, new Date());
								val.setTexto(TipoValidacion.LOCACION_LUGAR_RECEPCION_NO_CL.getTexto().replace("[1?]",locacionTo.getCodigo()));
		 		 				validaciones.add(val);
							}
						}
						break;
					case "PD":
					case "LD":
					case "LEM":
						if(locacionTo.getCodigo() == null ) {
							Validacion val = new Validacion(TipoValidacion.LOCACION_SIN_PAIS, false, new Date());
 	 		 				validaciones.add(val);
						} else if("CL".equals(locacionTo.getCodigo())){
							if("PD".equals(locacion.getNombre())) {
								Validacion val = new Validacion(TipoValidacion.LOCACION_PUERTO_DESEMBARQUE_NO_CL, false, new Date());
								val.setTexto(TipoValidacion.LOCACION_PUERTO_DESEMBARQUE_NO_CL.getTexto().replace("[1?]",locacionTo.getCodigo()));
		 		 				validaciones.add(val);
							} else if("LD".equals(locacion.getNombre())) {
								Validacion val = new Validacion(TipoValidacion.LOCACION_LUGAR_DESTINO_NO_CL, false, new Date());
								val.setTexto(TipoValidacion.LOCACION_LUGAR_DESTINO_NO_CL.getTexto().replace("[1?]",locacionTo.getCodigo()));
		 		 				validaciones.add(val);
							}else {
								Validacion val = new Validacion(TipoValidacion.LOCACION_LUGAR_ENTREGA_NO_CL, false, new Date());
								val.setTexto(TipoValidacion.LOCACION_LUGAR_ENTREGA_NO_CL.getTexto().replace("[1?]",locacionTo.getCodigo()));
		 		 				validaciones.add(val);
							}
						}
						break;
					default:
						break;
					
				}
 			}else if("I".equals(tipoManifiesto) && "TR".equals(tipoOperacion)) {
 				switch (locacion.getNombre()) {
				case "PE":
				case "LRM":	
				case "PD":
					if("PE".equals(locacion.getNombre()) && (locacionTo.getCodigo() == null || "CL".equals(locacionTo.getCodigo()))) {
						Validacion val = new Validacion(TipoValidacion.LOCACION_PUERTO_EMBARQUE_ES_CL, false, new Date());
						val.setTexto(TipoValidacion.LOCACION_PUERTO_EMBARQUE_ES_CL.getTexto().replace("[1?]",locacionTo.getCodigo()));
 		 				validaciones.add(val);
					}else if("LRM".equals(locacion.getNombre()) && (locacionTo.getCodigo() == null || "CL".equals(locacionTo.getCodigo()))){
						Validacion val = new Validacion(TipoValidacion.LOCACION_LUGAR_RECEPCION_NO_CL, false, new Date());
						val.setTexto(TipoValidacion.LOCACION_LUGAR_RECEPCION_NO_CL.getTexto().replace("[1?]",locacionTo.getCodigo()));
 		 				validaciones.add(val);
					}else if("PD".equals(locacion.getNombre()) && (locacionTo.getCodigo() == null || "CL".equals(locacionTo.getCodigo()))) {
						Validacion val = new Validacion(TipoValidacion.LOCACION_DESTINO_NO_CL, false, new Date());
						val.setTexto(TipoValidacion.LOCACION_DESTINO_NO_CL.getTexto().replace("[1?]",locacionTo.getCodigo()));
 		 				validaciones.add(val);
					}
					
					break;

				default:
					if("LD".equals(locacion.getNombre()) &&  (locacionTo.getCodigo() == null || !"CL".equals(locacionTo.getCodigo()))) {
						Validacion val = new Validacion(TipoValidacion.LOCACION_LUGAR_DESTINO_NO_CL, false, new Date());
						val.setTexto(TipoValidacion.LOCACION_LUGAR_DESTINO_NO_CL.getTexto().replace("[1?]",locacionTo.getCodigo()));
 		 				validaciones.add(val);
					}
					break;
				}
 			}else if("S".equals(tipoManifiesto) && "S".equals(tipoOperacion)) {
 					if("PE".equals(locacion.getNombre()) && (locacionTo.getCodigo() == null || "CL".equals(locacionTo.getCodigo()))) { 
 						Validacion val = new Validacion(TipoValidacion.LOCACION_PUERTO_EMBARQUE_NO_CL, false, new Date());
						val.setTexto(TipoValidacion.LOCACION_PUERTO_EMBARQUE_NO_CL.getTexto().replace("[1?]",locacionTo.getCodigo()));
 		 				validaciones.add(val);
 					}else if("LRM".equals(locacion.getNombre()) && (locacionTo.getCodigo() == null || "CL".equals(locacionTo.getCodigo())) ) {
 						Validacion val = new Validacion(TipoValidacion.LOCACION_RECEPCION_NO_CL, false, new Date());
						val.setTexto(TipoValidacion.LOCACION_RECEPCION_NO_CL.getTexto().replace("[1?]",locacionTo.getCodigo()));
 		 				validaciones.add(val);
 					}else if("PD".equals(locacion.getNombre()) && (locacionTo.getCodigo() == null || "CL".equals(locacionTo.getCodigo()))) {
 						Validacion val = new Validacion(TipoValidacion.LOCACION_PUERTO_DESEMBARQUE_NO_CL_2, false, new Date());
						val.setTexto(TipoValidacion.LOCACION_PUERTO_DESEMBARQUE_NO_CL_2.getTexto().replace("[1?]",locacionTo.getCodigo()));
 		 				validaciones.add(val);
 					}else if("LD".equals(locacion.getNombre()) && (locacionTo.getCodigo() == null || "CL".equals(locacionTo.getCodigo()))) {
 						Validacion val = new Validacion(TipoValidacion.LOCACION_LUGAR_DESTINO_NO_CL_2, false, new Date());
						val.setTexto(TipoValidacion.LOCACION_LUGAR_DESTINO_NO_CL_2.getTexto().replace("[1?]",locacionTo.getCodigo()));
 		 				validaciones.add(val);
 					}else if("LEM".equals(locacion.getNombre()) && (locacionTo.getCodigo() == null || "CL".equals(locacionTo.getCodigo()))) {
 						Validacion val = new Validacion(TipoValidacion.LOCACION_LUGAR_ENTREGA_NO_CL_2, false, new Date());
						val.setTexto(TipoValidacion.LOCACION_LUGAR_ENTREGA_NO_CL_2.getTexto().replace("[1?]",locacionTo.getCodigo()));
 		 				validaciones.add(val);
 					} 
 			}else if("S".equals(tipoManifiesto) && "TR".equals(tipoOperacion)) {
 				if("PE".equals(locacion.getNombre()) && (locacionTo.getCodigo() == null || !"CL".equals(locacionTo.getCodigo()))) { 
 					Validacion val = new Validacion(TipoValidacion.LOCACION_PUERTO_EMBARQUE_NO_CL_2, false, new Date());
					val.setTexto(TipoValidacion.LOCACION_PUERTO_EMBARQUE_NO_CL_2.getTexto().replace("[1?]",locacionTo.getCodigo()));
		 			validaciones.add(val);
 				}else if("PD".equals(locacion.getNombre())&& (locacionTo.getCodigo() == null || "CL".equals(locacionTo.getCodigo()))) {
 					Validacion val = new Validacion(TipoValidacion.LOCACION_LUGAR_RECEPCION_NO_CL, false, new Date());
					val.setTexto(TipoValidacion.LOCACION_LUGAR_RECEPCION_NO_CL.getTexto().replace("[1?]",locacionTo.getCodigo()));
		 			validaciones.add(val);
 				}else if("LD".equals(locacion.getNombre()) && (locacionTo.getCodigo() == null || "CL".equals(locacionTo.getCodigo()))) {
 					Validacion val = new Validacion(TipoValidacion.LOCACION_PUERTO_DESEMBARQUE_ES_CL, false, new Date());
					val.setTexto(TipoValidacion.LOCACION_PUERTO_DESEMBARQUE_ES_CL.getTexto().replace("[1?]",locacionTo.getCodigo()));
		 			validaciones.add(val);
 				}else if("LEM".equals(locacion.getNombre()) && (locacionTo.getCodigo() == null || "CL".equals(locacionTo.getCodigo()))) {
 					Validacion val = new Validacion(TipoValidacion.LOCACION_LUGAR_DESTINO_ES_CL, false, new Date());
					val.setTexto(TipoValidacion.LOCACION_LUGAR_DESTINO_ES_CL.getTexto().replace("[1?]",locacionTo.getCodigo()));
		 			validaciones.add(val);
 				}
 			}
 		}
		return validaciones;
	}

	 
	
}
