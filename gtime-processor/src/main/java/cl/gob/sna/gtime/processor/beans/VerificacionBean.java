package cl.gob.sna.gtime.processor.beans;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.processor.repo.DocEstadoRepository;
import cl.gob.sna.gtime.processor.repo.VerificacionRepository;
import cl.gob.sna.gtime.vo.DocumentoResponse;

public class VerificacionBean {

	private static final Logger log = LoggerFactory.getLogger(VerificacionBean.class);

	@Autowired
	private VerificacionRepository verificaRepo;
	
	@Autowired
	private	DocEstadoRepository docEstadoRepository;
	
	public void valida(@Body DocumentoResponse doc, Exchange exchange) {

		Long idDocBase = (Long) exchange.getIn().getHeader("idDocBase");

		if (doc != null && doc.getGtime() != null) {
			boolean isMftoConformado = verificaRepo.isMftoConformado(Long.valueOf(doc.getGtime().getNroEncabezado()));
			if (isMftoConformado) {
				log.info("Manifiesto conformado");
				docEstadoRepository.persisteDocEstado(idDocBase, "CMP", "CONFORMACION GENERADA POR EL SISTEMA AUTOMATICO", "[automatico]");
			}else {
				log.info("Manifiesto NO conformado");
			}
			exchange.getIn().setHeader("isMftoConformado", isMftoConformado);

			
//			if (gtime.getCargos() != null) {
//				try {
//					persistencias.addAll(cargoRepo.persisteDocTransCargo(idDocBase, gtime));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//			} else {
//				log.error("Cargos no encontrados");				
//			}
//
//			inserted = (!persistencias.contains(false) && !persistencias.isEmpty());
//			exchange.getIn().setHeader("documento", doc);
//
//			Persistencia estadoPersistencia = new Persistencia(TipoPersistencia.CARGOS, inserted);
//			exchange.getIn().setBody(estadoPersistencia);
//
//			DocumentoResponse docResponse = new DocumentoResponse();
//			docResponse.setNroDocumento(doc.getGtime().getNumeroReferencia());
//			exchange.getIn().setHeader("docResponse", docResponse);
		}

	}

}
