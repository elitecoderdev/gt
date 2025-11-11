package cl.gob.sna.gtime.processor.beans;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.api.vo.Participacion;
import cl.gob.sna.gtime.processor.repo.AdmPersonasRepository;
import cl.gob.sna.gtime.processor.repo.DocParticipacionRepository;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.Persistencia;
import cl.gob.sna.gtime.vo.TipoPersistencia;

public class PersistDocParticipacionesBean {

	private static final Logger log = LoggerFactory.getLogger(PersistDocParticipacionesBean.class);

	@Autowired
	private DocParticipacionRepository repoDocParticipacion;
	@Autowired
	private AdmPersonasRepository repoAdmPersonas;

	public void persistParticipacion(@Body DocumentoResponse doc, Exchange exchange) {
		//log.info(" [+] persistParticipacion ");
		List<Boolean> persistencias = new ArrayList<>();
		boolean inserted = false;
		try {
			if (doc != null && doc.getGtime() != null) {
				Long idDocBase = (Long) exchange.getIn().getHeader("idDocBase");
				List< cl.gob.sna.gtime.processor.vo.Participacion> listPartTo = new ArrayList<>();

				List<Participacion> listaParticipaciones = doc.getGtime().getParticipaciones().getParticipacion();

				listaParticipaciones.stream().forEach((participacion -> {
					if (participacion != null) {
						if (participacion.getNombre() == null) participacion.setNombre(" ");
						if (participacion.getDireccion() == null) participacion.setDireccion(" ");
						String idPersonaParticipante = repoAdmPersonas.obtieneIdPersonaParticipante(participacion);
						persistencias.add(repoDocParticipacion.persisteParticipacion(idDocBase, participacion, idPersonaParticipante));
					}
				}));
			}
		} catch (Exception e) {
			log.error("ERROR persistParticipacion "+ e.getMessage());
			e.getStackTrace();
		} finally {
			inserted = !persistencias.contains(false);
			exchange.getIn().setHeader("documento", doc);
			Persistencia val = new Persistencia(TipoPersistencia.DOCPARTICIPACION,
					inserted, "DOCUMENTOS", "DOCUMENTO");
			exchange.getIn().setBody(val);
			DocumentoResponse docResponse = new DocumentoResponse();
			docResponse.setNroDocumento(doc.getGtime().getNumeroReferencia());
			exchange.getIn().setHeader("docResponse", docResponse);
		}
	}
}
