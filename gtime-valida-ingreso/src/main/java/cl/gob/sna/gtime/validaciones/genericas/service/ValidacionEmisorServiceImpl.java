package cl.gob.sna.gtime.validaciones.genericas.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.gob.sna.gtime.validaciones.repository.DocDocumentosBaseRepository;
import cl.gob.sna.gtime.validaciones.repository.PerPersonaRepository;

@Service
public class ValidacionEmisorServiceImpl implements ValidacionEmisorService {
	private final Logger LOG = Logger.getLogger(ValidacionEmisorServiceImpl.class);

	@Autowired
	private PerPersonaRepository perPersonaRepository;
	@Autowired
	private DocDocumentosBaseRepository docDocumentosBaseRepository;

	@Override
	public long obtenerIdPersonaEmisor(String tipoId, String valorId, String nacionId) {

		return perPersonaRepository.obtenerIdPersona(tipoId,valorId,nacionId);
				
	}
	
	@Override
	public boolean existDocBaseByEmisor(String tipoDocumento,String nroReferencia, Long idPersonaEmisor, String fem) {
		SimpleDateFormat formatterSinHora = new SimpleDateFormat("dd-MM-yyyy");
 		try {
			return existDocBaseByEmisor(tipoDocumento,nroReferencia,idPersonaEmisor, formatterSinHora.parse(fem));
		} catch (ParseException e) {
			LOG.error("No se logro pasar a tipo date la fecha obtenida como string fem -> {}",fem, e);
			return false;
		}
	}
	@Override
	public boolean existDocBaseByEmisor(String tipoDocumento,String nroReferencia, Long idPersonaEmisor, Date fem) {
		return docDocumentosBaseRepository.existDocBaseByEmisor(tipoDocumento, nroReferencia, idPersonaEmisor, fem);
	}

}
