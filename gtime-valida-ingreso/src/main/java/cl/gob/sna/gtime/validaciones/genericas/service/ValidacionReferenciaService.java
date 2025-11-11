package cl.gob.sna.gtime.validaciones.genericas.service;

import java.util.Date;

import cl.gob.sna.gtime.validaciones.repository.dto.DocumentoBaseTo;

public interface ValidacionReferenciaService {

	boolean isDocTipoDocumentoExistente(String tipoDocumento);

	boolean existdocRelacionPermitida(String tipoDocumentoOrigen, String tipoReferencia, String tipoDocumentoDestino);

	DocumentoBaseTo obtenerByTipoDocNumExternoFechaEmision(String tipoDocumento, String numeroExterno,
			Date fechaEmision);

	DocumentoBaseTo obtenerByTipoDocNumExternoFechaEmision(String tipoDocumento, String numeroExterno,			String fechaEmision);

	boolean existePersona(String tipoId, String valorId, String nacionId);

 
 
 
 
 

 
}
