package cl.gob.sna.gtime.processor.beans;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.api.vo.Observacion;
import cl.gob.sna.gtime.processor.repo.DocObservacionRepository;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.Persistencia;
import cl.gob.sna.gtime.vo.TipoPersistencia;

public class PersistDocObservacionesBean {

	private static final Logger log = LoggerFactory.getLogger(PersistDocObservacionesBean.class);

	@Autowired
	private DocObservacionRepository repoDocObservacion;

	public void persistObservacion(@Body DocumentoResponse doc, Exchange exchange) {
		//log.info(" [+] persistObservacion " );

		List<Boolean> persistencias = new ArrayList<>();

		try{
			Long idDocBase = (Long) exchange.getIn().getHeader("idDocBase");
			if (doc != null && doc.getGtime() != null) {
				Gtime gtime = doc.getGtime();
				if (gtime != null && gtime.getObservaciones() != null
						&& gtime.getObservaciones().getObservacion() != null) {
					List<Observacion> listaObservaciones = gtime.getObservaciones().getObservacion();
					int pos = 1;
					for (Observacion obs : listaObservaciones) {
						persistencias.add(repoDocObservacion.persisteObservacion(idDocBase, obs, gtime.getLogin(),
								gtime.getTipo(), pos));
						//log.info(" [+] Persistiendo observacion {} con idDocBase {}", obs.getNombre(), idDocBase);
						pos++;
					}
				}
			}
		}catch(Exception e){
			log.error("ERROR persistObservacion "+ e.getMessage());
			e.getStackTrace();
			persistencias.add(false);
		}finally{
			exchange.getIn().setHeader("documento", doc);

			Persistencia val = new Persistencia(TipoPersistencia.DOCOBSERVACION,
					!persistencias.contains(false), "DOCUMENTOS", "DOCUMENTO");
			exchange.getIn().setBody(val);

			DocumentoResponse docResponse = new DocumentoResponse();
			docResponse.setNroDocumento(doc.getGtime().getNumeroReferencia());
			exchange.getIn().setHeader("docResponse", docResponse);
		}
	}
}
