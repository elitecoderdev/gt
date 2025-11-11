package cl.gob.sna.gtime.trazador.bean;


import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.modelo.vo.DetalleLog;
import cl.gob.sna.gtime.trazador.repo.DocDetalleLogRepository;

import java.util.List;

public class PersistDetalleLogBean {

	private static final Logger log = LoggerFactory.getLogger(PersistDetalleLogBean.class);

	@Autowired
	private DocDetalleLogRepository repoDetalleLog;

	public void persistDetalleLog(Exchange exchange) {
		DetalleLog traza = exchange.getIn().getBody(DetalleLog.class);
		try{
			if (traza != null) {
				Boolean idDetalleLog = repoDetalleLog.insertaDetalleLog(traza.getLogin(),
						traza.getNroDocumento(), traza.getEvento(), traza.getNroEncabezado(), traza.getVersion(), traza.getTipoDocumento());
				String msgLog = "[numReferencia : (" + traza.getNroDocumento() + ")]";
				exchange.getIn().setHeader("msgLog", msgLog);
			}
		}catch(Exception e){
			//log.error(" [+] No se pudo insertar la traza" + e.getMessage());
		}

	}

}
