package cl.gob.sna.gtime.processor.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.api.vo.Referencia;
import cl.gob.sna.gtime.processor.repo.AdmPersonasRepository;
import cl.gob.sna.gtime.processor.repo.DocRelacionRepository;
import cl.gob.sna.gtime.processor.vo.InfoPersona;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.Persistencia;
import cl.gob.sna.gtime.vo.TipoPersistencia;

public class PersistDocRelacionesBean {

	private static final Logger log = LoggerFactory.getLogger(PersistDocRelacionesBean.class);

	private static final String TIPO_REFERENCIA_REF = "REF";

	@Autowired
	private DocRelacionRepository repoDocRelacion;
	@Autowired
	private AdmPersonasRepository repoAdmPersonas;

	public void persistRelaciones(@Body DocumentoResponse doc, Exchange exchange) {
		//log.info(" [+] persistRelaciones " );
		Long idDocBase = (Long) exchange.getIn().getHeader("idDocBase");
		Date fechaEmision = (Date) exchange.getIn().getHeader("fechaEmision");
		String login = (String) exchange.getIn().getHeader("user");
		List<Boolean> persistencias = new ArrayList<>();

		try{
			if (doc != null && doc.getGtime() != null && doc.getGtime().getReferencias() != null
					&& doc.getGtime().getReferencias().getReferencia() != null) {

				List<Referencia> listaReferencias = doc.getGtime().getReferencias().getReferencia();

				for (Referencia referencia : listaReferencias) {
					if (referencia != null && referencia.getTipoReferencia() != null
							&& referencia.getTipoReferencia().equals(TIPO_REFERENCIA_REF)) {

						InfoPersona info = repoAdmPersonas.obtieneInfoPersona(referencia);

						if (info != null || info.getIdPersona() != null){
							persistencias.add(repoDocRelacion.persisteRelacion(idDocBase, referencia, login,
									info.getIdPersona(), info.getIdDocBase(), info.getEmisor(), fechaEmision));
						}else{
							persistencias.add(false);
						}
					}
				}
			}
		}catch (Exception e){
			log.error("ERROR persistRelaciones "+ e.getMessage());
			e.getStackTrace();
		}finally {
			exchange.getIn().setHeader("documento", doc);

			Persistencia estadoPersistencia = new Persistencia(TipoPersistencia.DOCRELACIONDOCUMENTO,
					!persistencias.contains(false), "DOCUMENTOS", "DOCORIGEN");
			exchange.getIn().setBody(estadoPersistencia);

			DocumentoResponse docResponse = new DocumentoResponse();
			docResponse.setNroDocumento(doc.getGtime().getNumeroReferencia());
			exchange.getIn().setHeader("docResponse", docResponse);
		}
	}

}
