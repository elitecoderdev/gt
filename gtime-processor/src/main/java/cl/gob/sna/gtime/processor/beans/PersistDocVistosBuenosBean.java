package cl.gob.sna.gtime.processor.beans;

import java.util.ArrayList;
import java.util.List;

import cl.gob.sna.gtime.api.vo.Vb;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.processor.repo.DocVistosBuenosRepository;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.Persistencia;
import cl.gob.sna.gtime.vo.TipoPersistencia;

public class PersistDocVistosBuenosBean {

	private static final Logger log = LoggerFactory.getLogger(PersistDocVistosBuenosBean.class);

	private static final Boolean VALIDO_VISTOBUENO_OPCIONAL = true;
	@Autowired
	private DocVistosBuenosRepository repoVistosBuenos;

	public void persistDocVistosBuenos(@Body DocumentoResponse doc, Exchange exchange) {
		//log.info(" [+] persistDocVistosBuenos ");
		boolean inserted = false;
		Long idDocBase = (Long) exchange.getIn().getHeader("idDocBase");
		List<Boolean> persistencias = new ArrayList<>();

		try{
			if (doc != null && doc.getGtime() != null) {
				Gtime gtime = doc.getGtime();
				if (gtime.getVistosBuenos() != null && gtime.getVistosBuenos().getVb() != null
						&& !gtime.getVistosBuenos().getVb().isEmpty()) {
					List<Vb> vistoBList = gtime.getVistosBuenos().getVb();
					for (Vb visto : vistoBList) {
						persistencias.add(repoVistosBuenos.persisteDocTransVistoBuenos(idDocBase,
								visto));
					}
				} else {
					persistencias.add(VALIDO_VISTOBUENO_OPCIONAL);
				}
			}
		}catch (Exception e){
			log.error("ERROR persistDocVistosBuenos "+ e.getMessage());
			e.getStackTrace();
		}finally {
			inserted = (!persistencias.contains(false) && !persistencias.isEmpty());
			exchange.getIn().setHeader("documento", doc);

			Persistencia estadoPersistencia = new Persistencia(TipoPersistencia.DOCTRANVISTOSBUENOS,
					inserted, "DOCTRANSPORTE", "DOCUMENTOTRANSPORTE");
			exchange.getIn().setBody(estadoPersistencia);
			DocumentoResponse docResponse = new DocumentoResponse();
			docResponse.setNroDocumento(doc.getGtime().getNumeroReferencia());
			exchange.getIn().setHeader("docResponse", docResponse);
		}
	}
}
