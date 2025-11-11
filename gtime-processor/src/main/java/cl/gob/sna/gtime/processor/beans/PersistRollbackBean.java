package cl.gob.sna.gtime.processor.beans;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.queue.vo.QueueRecord;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.Persistencia;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.processor.repo.DocDocumentosRepository;

import java.util.List;

public class PersistRollbackBean {
	@Autowired
	private DocDocumentosRepository repoDocBase;
	private static final String PERSISTENCIAS = "persistencias";

	private static final Logger log = LoggerFactory.getLogger(PersistRollbackBean.class);

	public void rollBackGtime(Exchange exchange) {
		Long idDocBase = (Long) exchange.getIn().getHeader("idDocBase");
		DocumentoResponse documento = (DocumentoResponse) exchange.getIn().getBody();

		if (documento != null){
			Gtime gtime = documento.getGtime();
			if (gtime != null){
				documento.getGtime().setEstadoGtime("Rechazado");
				exchange.getIn().setHeader("docResponse", documento);
			}
		}

		if (idDocBase == null) return;

		log.info(" [+] Ejecutando Rollback para : " + idDocBase);

		List<Persistencia> listaEstadosPersistencias = (List<Persistencia>) exchange.getIn().getHeader(PERSISTENCIAS);

		if (listaEstadosPersistencias != null) {
			for (Persistencia persistencia : listaEstadosPersistencias) {
				if (persistencia != null && !persistencia.isValido()){
					repoDocBase.
							deleteFromRollback(idDocBase, persistencia);
				}
			}
			repoDocBase.deleteFromIdDocBase(idDocBase);
		}
	}
}
