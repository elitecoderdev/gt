package cl.gob.sna.gtime.processor.beans;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.processor.repo.DocTransporteRepository;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.Persistencia;
import cl.gob.sna.gtime.vo.TipoPersistencia;

public class PersistDocTransporteBean {

	private static final Logger log = LoggerFactory.getLogger(PersistDocTransporteBean.class);

	@Autowired
	private DocTransporteRepository repoDocTransporte;

	public void persistDocTransporte(@Body DocumentoResponse doc, Exchange exchange) {
		//log.info(" [+] persistDocTransporte ");
		boolean inserted = false;

		try{
			Long idDocBase = (Long) exchange.getIn().getHeader("idDocBase");

			if (doc != null && doc.getGtime() != null) {
				Gtime gtime = doc.getGtime();
				if (gtime != null) {
					inserted = repoDocTransporte.persisteDocTransporte(idDocBase, gtime);
				}
			}
		}catch(Exception e){
			log.error("ERROR persistDocTransporte "+ e.getMessage());
			e.getStackTrace();
		}finally {
			exchange.getIn().setHeader("documento", doc);
			Persistencia estadoPersistencia = new Persistencia(TipoPersistencia.DOCTRANDOCTRANSPORTE,
					inserted, "DOCTRANSPORTE", "ID");
			exchange.getIn().setBody(estadoPersistencia);
			DocumentoResponse docResponse = new DocumentoResponse();
			docResponse.setNroDocumento(doc.getGtime().getNumeroReferencia());
			exchange.getIn().setHeader("docResponse", docResponse);
		}
	}

}
