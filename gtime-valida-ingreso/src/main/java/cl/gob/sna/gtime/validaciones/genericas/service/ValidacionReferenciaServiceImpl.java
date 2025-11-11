package cl.gob.sna.gtime.validaciones.genericas.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.gob.sna.gtime.validaciones.repository.DocDocumentosBaseRepository;
import cl.gob.sna.gtime.validaciones.repository.DocRelacionPermitidaRepository;
import cl.gob.sna.gtime.validaciones.repository.DocTipoDocumentoRepository;
import cl.gob.sna.gtime.validaciones.repository.PerPersonaRepository;
import cl.gob.sna.gtime.validaciones.repository.dto.DocumentoBaseTo;

@Service
public class ValidacionReferenciaServiceImpl implements ValidacionReferenciaService {
	private final Logger LOG = Logger.getLogger(ValidacionReferenciaServiceImpl.class);

	@Autowired
	private DocTipoDocumentoRepository docTipoDocumentoRepository;
	@Autowired
	private DocRelacionPermitidaRepository docRelacionPermitidaRepository;
	@Autowired
	private DocDocumentosBaseRepository docDocumentosBaseRepository;
	@Autowired
	private PerPersonaRepository perPersonaRepository;
	
	@Override
	public boolean isDocTipoDocumentoExistente(String tipoDocumento) {
		return docTipoDocumentoRepository.isDocTipoDocumentoExistente(tipoDocumento);
	}
	
	@Override
	public boolean existdocRelacionPermitida(String tipoDocumentoOrigen,String tipoReferencia, String tipoDocumentoDestino) {
		return docRelacionPermitidaRepository.existdocRelacionPermitida(tipoDocumentoOrigen, tipoReferencia, tipoDocumentoDestino);
	}
	
	@Override
	public DocumentoBaseTo obtenerByTipoDocNumExternoFechaEmision(String tipoDocumento, String numeroExterno, String fechaEmision) {
		SimpleDateFormat formatterSinHora = new SimpleDateFormat("dd-MM-yyyy");
		try {
			return obtenerByTipoDocNumExternoFechaEmision(tipoDocumento,numeroExterno, formatterSinHora.parse(fechaEmision));
		} catch (ParseException e) {
			LOG.error("No se logro pasar a tipo date la fecha obtenida como string fem -> {}",fechaEmision, e);
			return null;
		}
	}
	
	@Override
	public DocumentoBaseTo obtenerByTipoDocNumExternoFechaEmision(String tipoDocumento, String numeroExterno, Date fechaEmision) {
		return docDocumentosBaseRepository.obtenerByTipoDocNumExternoFechaEmision(tipoDocumento, numeroExterno, fechaEmision);
	}
 
	
	@Override
	public boolean existePersona(String tipoId, String valorId, String nacionId) {
		return perPersonaRepository.existePersona( tipoId,  valorId,  nacionId);
	}
	
	
	
}
