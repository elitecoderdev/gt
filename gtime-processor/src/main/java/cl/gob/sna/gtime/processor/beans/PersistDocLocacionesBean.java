package cl.gob.sna.gtime.processor.beans;

import java.util.ArrayList;
import java.util.List;

import cl.gob.sna.gtime.api.vo.Referencia;
import cl.gob.sna.gtime.processor.repo.DocDocumentosRepository;
import cl.gob.sna.gtime.processor.util.HelperUtil;


import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.api.vo.Locacion;
import cl.gob.sna.gtime.processor.repo.DocLocacionRepository;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.Persistencia;
import cl.gob.sna.gtime.vo.TipoPersistencia;

public class PersistDocLocacionesBean {

	private static final Logger log = LoggerFactory.getLogger(PersistDocLocacionesBean.class);

	private static final String TIPO_REFERENCIA_REF = "REF";
	@Autowired
	private DocLocacionRepository repoDocLocacion;

	@Autowired
	private DocDocumentosRepository repoDocBase;

	public void persistLocacion(@Body DocumentoResponse doc, Exchange exchange) {
		//log.info(" [+] persistLocacion " );
		List<Boolean> persistencias = new ArrayList<>();
		boolean inserted = true;
		try{
			if (doc != null && doc.getGtime() != null) {
				Long idDocBase = (Long) exchange.getIn().getHeader("idDocBase");

				Gtime gtime = doc.getGtime();
				if (gtime != null && gtime.getLocaciones() != null && gtime.getLocaciones().getLocacion() != null) {
					List<Locacion> listaLocaciones = gtime.getLocaciones().getLocacion();
					String viaTransporte = null;

					Referencia referencia = HelperUtil.obtenerReferencia(TIPO_REFERENCIA_REF, gtime.getReferencias().
							getReferencia());

					viaTransporte = repoDocBase.obtieneViaTransporte(referencia.getTipoDocumento(),
							referencia.getNumero(), referencia.getFecha());

					if (viaTransporte == null) viaTransporte = "04";

					/*for (Locacion locacion : listaLocaciones) {
						int idLocacion = repoDocLocacion.obtieneInfoLocacion(locacion.getCodigo(), viaTransporte);
						persistencias.add(repoDocLocacion.persisteLocacion(idDocBase, locacion, idLocacion));
						//log.info(" persistida Locacion");
					}*/

					List<cl.gob.sna.gtime.processor.vo.Locacion> locacionToList =  new ArrayList<>();

					int idLocacion = 0;

					for (Locacion locacion : listaLocaciones) {
						idLocacion = repoDocLocacion.obtieneInfoLocacion(locacion.getCodigo(), viaTransporte);

						if (idLocacion > 0){
							cl.gob.sna.gtime.processor.vo.Locacion loc =
									new cl.gob.sna.gtime.processor.vo.Locacion(idDocBase,
											idLocacion, locacion.getCodigo(), locacion.getNombre(), locacion.getDescripcion());
							locacionToList.add(loc);
						}
					}
					if (!locacionToList.isEmpty() && locacionToList.size() > 0){
						repoDocLocacion.persisteLocaciones(locacionToList);
					}
				}
			}
		} catch (Exception e){
			log.error("ERROR persistLocacion "+ e.getMessage());
			e.getStackTrace();
			inserted = false;
		}finally {
			exchange.getIn().setHeader("documento", doc);

			Persistencia persistencia = new Persistencia(TipoPersistencia.DOCLOCACIONDOCUMENTO,
					inserted, "DOCUMENTOS", "DOCUMENTO");
			exchange.getIn().setBody(persistencia);

			DocumentoResponse docResponse = new DocumentoResponse();
			docResponse.setNroDocumento(doc.getGtime().getNumeroReferencia());
			exchange.getIn().setHeader("docResponse", docResponse);
		}
	}
}
