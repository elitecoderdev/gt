package cl.gob.sna.gtime.processor.beans;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.processor.repo.DocCargosRepository;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.Persistencia;
import cl.gob.sna.gtime.vo.TipoPersistencia;

public class PersistDocCargosBean {

	private static final Logger log = LoggerFactory.getLogger(PersistDocCargosBean.class);

	private static final Boolean VALIDO_DOCCARGOS_OPCIONAL = true;
	@Autowired
	private DocCargosRepository cargoRepo;

	public void persistDocCargos(@Body DocumentoResponse doc, Exchange exchange) {
		//log.info(" [+] persistDocCargos ");
		boolean inserted = false;
		List<Boolean> persistencias = new ArrayList<>();

		try{
			Long idDocBase = (Long) exchange.getIn().getHeader("idDocBase");
			if (doc != null && doc.getGtime() != null) {
				Gtime gtime = doc.getGtime();
				if (gtime.getCargos() != null) {
					persistencias.addAll(cargoRepo.persisteDocTransCargo(idDocBase, gtime));
					//log.info(" [+] Persistiendo DocCargos con idDocBase {}", idDocBase);
				}
			}
		}catch (Exception e){
			log.error("ERROR persistDocCargos "+ e.getMessage());
			e.getStackTrace();
		}finally{
			inserted = (!persistencias.contains(false) && !persistencias.isEmpty());
			exchange.getIn().setHeader("documento", doc);

			Persistencia estadoPersistencia = new Persistencia(TipoPersistencia.DOCTRANCARGO,
					inserted, "DOCTRANSPORTE", "DOCUMENTOTRANSPORTE");
			exchange.getIn().setBody(estadoPersistencia);

			DocumentoResponse docResponse = new DocumentoResponse();
			docResponse.setNroDocumento(doc.getGtime().getNumeroReferencia());
			exchange.getIn().setHeader("docResponse", docResponse);
		}
	}
}
