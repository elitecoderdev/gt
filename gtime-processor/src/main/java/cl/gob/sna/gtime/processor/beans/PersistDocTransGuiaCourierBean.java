package cl.gob.sna.gtime.processor.beans;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.processor.repo.DocTransGuiaCourierRepository;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.Persistencia;
import cl.gob.sna.gtime.vo.TipoPersistencia;

public class PersistDocTransGuiaCourierBean {
	private static final Logger log = LoggerFactory.getLogger(PersistDocTransGuiaCourierBean.class);

	@Autowired
	private DocTransGuiaCourierRepository repoDocTransGuiaCourier;

	public void persistDocTransGuiaCourier(@Body DocumentoResponse doc, Exchange exchange) {
		//log.info(" [+] persistDocTransGuiaCourier ");
		boolean inserted = false;

		try{
			Long idDocBase = (Long) exchange.getIn().getHeader("idDocBase");
			if (doc != null && doc.getGtime() != null) {
				Gtime gtime = doc.getGtime();
				if (gtime != null) {
					inserted = repoDocTransGuiaCourier.persisteDocTransGuiaCourier(idDocBase, gtime);
				}
			}
		}catch(Exception e){
			log.error("ERROR persistDocTransGuiaCourier"+ e.getMessage());
			e.getStackTrace();
		}finally{
			exchange.getIn().setHeader("documento", doc);
			Persistencia estadoPersistencia = new Persistencia(TipoPersistencia.DOCTRANGUIACOURIER,
					inserted, "DOCTRANSPORTE", "DOCUMENTOTRANSPORTE");
			exchange.getIn().setBody(estadoPersistencia);
			DocumentoResponse docResponse = new DocumentoResponse();
			docResponse.setNroDocumento(doc.getGtime().getNumeroReferencia());
			exchange.getIn().setHeader("docResponse", docResponse);
		}




	}

}
