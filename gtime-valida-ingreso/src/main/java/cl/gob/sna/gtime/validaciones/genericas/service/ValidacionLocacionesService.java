package cl.gob.sna.gtime.validaciones.genericas.service;

 
import java.util.List;

import cl.gob.sna.gtime.api.vo.Locacion;
import cl.gob.sna.gtime.vo.Validacion;

public interface ValidacionLocacionesService {

	 
 
 	
	List<Validacion> validaCodigoLocacion(Integer secuencia, Locacion locacion);

	List<Validacion> validaDescripcionLocacion_VersionNot2_0(Integer secuencia, Locacion locacion); 
	
	List<Validacion> validaExistenciaLocacionByViaTransporte(Integer secuencia, String viaTransporte, String tipoManifiesto,String tipoOperacion, Locacion locacion );

	List<Validacion> validaNombreLocacion(Integer secuencia,String tipoDoc, Locacion locacion,  Boolean existePe, Boolean existePd);
	
}
