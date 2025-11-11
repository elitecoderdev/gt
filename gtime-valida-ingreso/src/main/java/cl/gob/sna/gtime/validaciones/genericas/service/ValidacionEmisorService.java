package cl.gob.sna.gtime.validaciones.genericas.service;

import java.util.Date;

public interface ValidacionEmisorService {

	long obtenerIdPersonaEmisor(String tipoId, String valorId, String nacionId);

 
	boolean existDocBaseByEmisor(String tipoDocumento, String nroReferencia, Long idPersonaEmisor, String fem);

	boolean existDocBaseByEmisor(String tipoDocumento, String nroReferencia, Long idPersonaEmisor, Date fem);

 
 
 
 
 

 
}
