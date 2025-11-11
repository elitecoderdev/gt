package cl.gob.sna.gtime.validaciones.genericas.service;

 
import java.util.List;

import cl.gob.sna.gtime.api.vo.Observacion;
import cl.gob.sna.gtime.vo.Validacion;

public interface ValidacionObservacionService {

	List<Validacion> validaNombre(Integer secuencia, String tipoDoc, Observacion observacion);

	List<Validacion> validaContenido(Integer secuencia, Observacion observacion);


	
}
