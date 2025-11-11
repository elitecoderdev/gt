package cl.gob.sna.gtime.processor.beans;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.processor.repo.DocEstadoRepository;
import cl.gob.sna.gtime.processor.repo.VerificacionSobRepository;
import cl.gob.sna.gtime.vo.DocumentoResponse;

public class VerificacionSobBean {

	private static final Logger log = LoggerFactory.getLogger(VerificacionSobBean.class);

	@Autowired
	private VerificacionSobRepository verificaSobRepo;
	@Autowired
	private	DocEstadoRepository docEstadoRepository;
	public void valida(@Body DocumentoResponse doc, Exchange exchange) {
		Long idDocBase = (Long) exchange.getIn().getHeader("idDocBase");
		boolean isMftoConformado = (boolean) exchange.getIn().getHeader("isMftoConformado");

		if (doc != null && doc.getGtime() != null) {
			boolean isSobrante = verificaSobRepo.isSobrante(Long.valueOf(doc.getGtime().getNroEncabezado()));
			exchange.getIn().setHeader("isSobrante", isSobrante);

			if (!isSobrante && isMftoConformado) {
				log.info("isSobrante: "+isSobrante);
				docEstadoRepository.persisteDocEstado(idDocBase, "FPLAZO", "FUERA DE PLAZO, MFTOC CONSOLIDADO EN OBSERVACION NO DECLARADO SOBRANTE", "[automatico]");
			}

			if (isSobrante) {
				docEstadoRepository.persisteDocEstado(idDocBase, "SOB", "SOBRANTE GENERADA POR EL SISTEMA AUTOMATICO", "[automatico]");
			}
	
			
//			  GRABAR_DETALLE_LOG(p_TIPO_DOCUMENTO, p_LOGIN, v_NUMERO_REFERENCIA, 'M:[' || to_char(v_TIMESTAMP_TERMINO, 'dd/mm/yyyy hh24:mi:ss') || '] Termino procesado en servidor.', p_SECUENCIA_LOG);
//		      GRABAR_DETALLE_LOG(p_TIPO_DOCUMENTO, p_LOGIN, v_NUMERO_REFERENCIA, 'M:Tiempo de procesado: ' || v_TIMEDIFF || '[ms]', p_SECUENCIA_LOG);
		    
		      
		      
			DocumentoResponse docResponse = new DocumentoResponse();
			docResponse.setNroDocumento(doc.getGtime().getNumeroReferencia());
			exchange.getIn().setHeader("docResponse", docResponse);
		}

	}

}
