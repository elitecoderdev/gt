package cl.gob.sna.gtime.processor.beans;

import java.util.ArrayList;
import java.util.List;

import cl.gob.sna.gtime.api.vo.Transbordo;
import cl.gob.sna.gtime.processor.repo.DocLocacionRepository;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.processor.repo.DocTransbordosRepository;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.Persistencia;
import cl.gob.sna.gtime.vo.TipoPersistencia;

public class PersistDocTransbordosBean {

	private static final Logger log = LoggerFactory.getLogger(PersistDocTransbordosBean.class);

	private static final Boolean VALIDO_TRANSBORDO_OPCIONAL = true;

	@Autowired
	private DocTransbordosRepository repoTransbordos;

	@Autowired
	private DocLocacionRepository repoDocLocacion;

	public void persistDocTransbordos(@Body DocumentoResponse doc, Exchange exchange) {
		//log.info(" [+] persistDocTransbordos");
		boolean inserted = false;

		List<Boolean> persistencias = new ArrayList<>();

		try {
			Long idDocBase = (Long) exchange.getIn().getHeader("idDocBase");
			if (doc != null && doc.getGtime() != null) {
				Gtime gtime = doc.getGtime();
				if (gtime.getTransbordos() != null
						&& gtime.getTransbordos().getTransbordo() != null
						&& !gtime.getTransbordos().getTransbordo().isEmpty()) {
					String viaTransporte = doc.getViaTransporte();

					List<Transbordo> transbordosList = gtime.getTransbordos().getTransbordo();
					for (Transbordo tra : transbordosList) {
						int codLocacion = repoDocLocacion.obtieneInfoLocacion(tra.getCodLugar(), viaTransporte);
						persistencias.add(repoTransbordos.persisteDocTranGcTransbordos(idDocBase, tra, codLocacion));
					}
				} else {
					persistencias.add(VALIDO_TRANSBORDO_OPCIONAL);
				}
			}
		} catch (Exception e) {
			log.error("ERROR persistDocTransbordos "+ e.getMessage());
			e.getStackTrace();
		}finally{
			inserted = (!persistencias.contains(false) && !persistencias.isEmpty());
			exchange.getIn().setHeader("documento", doc);

			Persistencia estadoPersistencia = new Persistencia(TipoPersistencia.DOCTRANGCTRANSBORDOS,
					inserted, "DOCTRANSPORTE", "DOCUMENTOTRANSPORTE");
			exchange.getIn().setBody(estadoPersistencia);

			DocumentoResponse docResponse = new DocumentoResponse();
			docResponse.setNroDocumento(doc.getGtime().getNumeroReferencia());
			exchange.getIn().setHeader("docResponse", docResponse);
		}
	}

}
