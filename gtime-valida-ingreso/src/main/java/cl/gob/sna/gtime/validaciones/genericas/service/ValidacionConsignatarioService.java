package cl.gob.sna.gtime.validaciones.genericas.service;

import java.util.Date;
import java.util.Map;

public interface ValidacionConsignatarioService {
    Map<String, String> getValidaRUTDocumento(String numReferencia, String tipoDoc, String tipoAccion,
                                              String rutCons, String dvRutCons, Date fechaEmision);
}
