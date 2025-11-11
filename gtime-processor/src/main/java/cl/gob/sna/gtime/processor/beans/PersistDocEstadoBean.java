package cl.gob.sna.gtime.processor.beans;

import cl.gob.sna.gtime.processor.repo.DocNotificacionBatchRepository;
import cl.gob.sna.gtime.processor.repo.DocPropagacionBatchRepository;
import cl.gob.sna.gtime.processor.util.DocEstadoUtil;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.processor.repo.DocEstadoRepository;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.Persistencia;
import cl.gob.sna.gtime.vo.TipoPersistencia;

import java.util.ArrayList;
import java.util.List;

public class PersistDocEstadoBean {
	private static final String DOC_ESTADOS_REC = "REC";

	private static final String DOC_ESTADOS_ACP = "ACP";

	private static final String DOC_ESTADOS_CMP = "CMP";

	private static final String DOC_ESTADOS_FPL = "FPLAZO";

	private static final String DOC_ESTADOS_SOB = "SOB";

	private static final Logger log = LoggerFactory.getLogger(PersistDocEstadoBean.class);

	@Autowired
	private DocEstadoRepository repoDocEstado;

	@Autowired
	private DocNotificacionBatchRepository repoDocNotiBatch;

	@Autowired
	private DocPropagacionBatchRepository repoDocProBatch;


	public void persistDocEstado(@Body DocumentoResponse doc, Exchange exchange) {
		//log.info(" [+] persistDocEstado ");
		List<Boolean> persistencias = new ArrayList<>();
		List<Boolean> persistenciaNotiBach = new ArrayList<>();
		List<Boolean> persistenciaPropaBach = new ArrayList<>();
		boolean inserted = false;

		try {
			Long idDocBase = (Long) exchange.getIn().getHeader("idDocBase");
			Gtime gtime = doc.getGtime();
			//log.info(" [+] Persistiendo DocEstado con idDocBase {}", idDocBase);

			//Primero, es generar el estado REC
			DocEstadoUtil docEstadoREC =
					new DocEstadoUtil(DOC_ESTADOS_REC , "  ", doc.getGtime().getLogin());
			//Segundo, es generar el estado ACP
			DocEstadoUtil docEstadoACP =
					new DocEstadoUtil(DOC_ESTADOS_ACP, "ACEPTACION GENERADA POR EL SISTEMA AUTOMATICO", "[automatico]");

			//REC
			persistencias.add(this.persistDocEstadoGenerico(idDocBase, docEstadoREC));
			persistenciaNotiBach.add(repoDocNotiBatch.persistNotificacionBach(idDocBase, docEstadoREC.getDocEstado()));
			persistenciaPropaBach.add(repoDocProBatch.persistPropagacionBach(idDocBase, docEstadoREC));

			//ACP
			persistencias.add(this.persistDocEstadoGenerico(idDocBase, docEstadoACP));
			//persistenciaNotiBach.add(repoDocNotiBatch.persistNotificacionBach(idDocBase, docEstadoACP.getDocEstado()));
			//persistenciaPropaBach.add(repoDocProBatch.persistPropagacionBach(idDocBase, docEstadoACP));

			String idMfto = gtime.getNroEncabezado();
			boolean isMftoConformado = repoDocEstado.esMftoConformado(idMfto);

			//CMP
			if (isMftoConformado){
				DocEstadoUtil docEstadoCMP =
						new DocEstadoUtil(DOC_ESTADOS_CMP , "CONFORMACION GENERADA POR EL SISTEMA AUTOMATICO", "[automatico]");
				persistencias.add(this.persistDocEstadoGenerico(idDocBase, docEstadoCMP));
				//persistenciaNotiBach.add(repoDocNotiBatch.persistNotificacionBach(idDocBase, docEstadoCMP.getDocEstado()));
				//persistenciaPropaBach.add(repoDocProBatch.persistPropagacionBach(idDocBase, docEstadoCMP));
			}

			boolean isMftoSob = repoDocEstado.esMftoSob(idMfto);

			//SOB
			if (isMftoSob){
				DocEstadoUtil docEstadoSob =
						new DocEstadoUtil(DOC_ESTADOS_SOB , "SOBRANTE GENERADA POR EL SISTEMA AUTOMATICO", "[automatico]");
				persistencias.add(this.persistDocEstadoGenerico(idDocBase, docEstadoSob));
				persistenciaNotiBach.add(repoDocNotiBatch.persistNotificacionBach(idDocBase, docEstadoSob.getDocEstado()));
				persistenciaPropaBach.add(repoDocProBatch.persistPropagacionBach(idDocBase, docEstadoSob));
			}
			//FPLAZO
			if (!isMftoSob && isMftoConformado){
				DocEstadoUtil docEstadoFPL =
						new DocEstadoUtil(DOC_ESTADOS_FPL , "FUERA DE PLAZO, MFTOC CONSOLIDADO EN OBSERVACION NO DECLARADO SOBRANTE", "[automatico]");
				persistencias.add(this.persistDocEstadoGenerico(idDocBase, docEstadoFPL));
				//persistenciaNotiBach.add(repoDocNotiBatch.persistNotificacionBach(idDocBase, docEstadoFPL.getDocEstado()));
				//persistenciaPropaBach.add(repoDocProBatch.persistPropagacionBach(idDocBase, docEstadoFPL));
			}
		} catch (Exception e) {
			log.error("ERROR persistDocEstado "+ e.getMessage());
			e.getStackTrace();
		} finally {
			inserted = (!persistencias.contains(false) && !persistencias.isEmpty());
			Persistencia estadoPersistencia = new Persistencia(TipoPersistencia.DOCESTADOS,
					inserted, "DOCUMENTOS", "DOCUMENTO");
			//exchange.getIn().setBody(estadoPersistencia);
			doc.getListEstadoPersistencia().add(estadoPersistencia);

			inserted = (!persistenciaNotiBach.contains(false) && !persistenciaNotiBach.isEmpty());
			Persistencia estadoPersistenciaBachNoti = new Persistencia(TipoPersistencia.DOCNOTIFICACIONBATCH,
					inserted, "DOCUMENTOS", "DOCUMENTO");

			//exchange.getIn().setBody(estadoPersistenciaBachNoti);
			doc.getListEstadoPersistencia().add(estadoPersistenciaBachNoti);

			inserted = (!persistenciaPropaBach.contains(false) && !persistenciaPropaBach.isEmpty());
			Persistencia estadoPersistenciaPropaBach = new Persistencia(TipoPersistencia.DOCPROPAGACIONESTADOBATCH,
					inserted, "DOCUMENTOS", "IDDOCUMENTO");

			doc.getListEstadoPersistencia().add(estadoPersistenciaPropaBach);

			exchange.getIn().setBody(doc.getListEstadoPersistencia());
			DocumentoResponse docResponse = new DocumentoResponse();
			docResponse.setNroDocumento(doc.getGtime().getNumeroReferencia());
			exchange.getIn().setHeader("docResponse", docResponse);
		}
	}

	/**
	 *
	 * @param idDocBase
	 * @param docEstadoGen
	 * @return
	 */
	public boolean persistDocEstadoGenerico(Long idDocBase, DocEstadoUtil docEstadoGen){
		return repoDocEstado.
				persisteDocEstado(idDocBase, docEstadoGen.getDocEstado(), docEstadoGen.getDocEstadoMSG(),
						docEstadoGen.getDocEstadoLogin());
	}
}
