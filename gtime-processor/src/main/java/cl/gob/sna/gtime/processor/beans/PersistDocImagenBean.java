package cl.gob.sna.gtime.processor.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cl.gob.sna.gtime.processor.util.HelperUtil;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.processor.repo.DocImagenRepository;
import cl.gob.sna.gtime.processor.vo.Imagen;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.Persistencia;
import cl.gob.sna.gtime.vo.TipoPersistencia;

public class PersistDocImagenBean {

	private static final Logger log = LoggerFactory.getLogger(PersistDocImagenBean.class);

	@Autowired
	private DocImagenRepository repoImagen;

	public void persistDocImagen(@Body DocumentoResponse doc, Exchange exchange) {
		boolean inserted = true;
		Long idDocBase = (Long) exchange.getIn().getHeader("idDocBase");
		List<String> tagsToRemove = Arrays.asList("user-host-origen", "id-sender");
		//log.info(" persistDocImagen ");
		try{
			if (doc != null && doc.getGtime() != null) {
				if (doc.getGtime().getXml() != null) {
					Imagen imagen = new Imagen(idDocBase, repoImagen.obtieneSecuenciaDocImagen(idDocBase),
							HelperUtil.removeElementXML(doc.getGtime().getXml(), tagsToRemove));

					List<Imagen> listaImagen = new ArrayList<>();
					listaImagen.add(imagen);

					repoImagen.persisteDocImagen(listaImagen);
					//log.info(" [+] Persistiendo DocImagen con idDocBase {}", idDocBase);
				}
			}
		}catch (Exception e){
			log.info("ERROR persistDocImagen "+ e.getMessage());
			e.getStackTrace();
			inserted = false;
		}finally{
			exchange.getIn().setHeader("documento", doc);

			Persistencia estadoPersistencia = new Persistencia(TipoPersistencia.DOCIMAGEN,
					inserted, "DOCUMENTOS", "DOCUMENTO");
			exchange.getIn().setBody(estadoPersistencia);

			DocumentoResponse docResponse = new DocumentoResponse();
			docResponse.setNroDocumento(doc.getGtime().getNumeroReferencia());
			exchange.getIn().setHeader("docResponse", docResponse);
		}
	}

}
