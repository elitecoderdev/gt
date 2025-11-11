package cl.gob.sna.gtime.processor.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cl.gob.sna.gtime.processor.util.FechaUtil;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.api.vo.Fecha;
import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.processor.repo.DocFechaDocumentoRepository;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.Persistencia;
import cl.gob.sna.gtime.vo.TipoPersistencia;

public class PersistDocFechasBean {

	private static final Logger log = LoggerFactory.getLogger(PersistDocFechasBean.class);

	@Autowired
	private DocFechaDocumentoRepository repoDocFecha;

	public void persistFechas(@Body DocumentoResponse doc, Exchange exchange) {
		//log.info(" [+] persistFechas");

		boolean inserted = false;
		List<Boolean> persistencias = new ArrayList<>();

		try{
			Long idDocBase = (Long) exchange.getIn().getHeader("idDocBase");
			if (doc != null && doc.getGtime() != null) {
				Gtime gtime = doc.getGtime();
				if (gtime != null) {
					List<Fecha> listaFechas = doc.getGtime().getFechas().getFecha();
					for (Fecha fecha : listaFechas){
						Date dateWFormat = FechaUtil.getDateWithFormat(fecha.getValor());
						if (fecha.getNombre().equals("FEM")){
							dateWFormat = FechaUtil.toDateGtime(fecha.getValor());
						}
						if (dateWFormat != null && !fecha.getNombre().equalsIgnoreCase("FECREACION")){
							persistencias.add(repoDocFecha.persisteFechas(idDocBase, dateWFormat, fecha.getNombre()));
						}
					}
				}
			}
			repoDocFecha.persisteFechas(idDocBase, new Date(), "FECREACION");

		}catch(Exception e){
			log.error("ERROR persistFechas "+ e.getMessage());
			e.getStackTrace();
			persistencias.add(false);
		}finally{
			inserted = !persistencias.contains(false);
			exchange.getIn().setHeader("documento", doc);

			Persistencia estadoPersistencia = new Persistencia(TipoPersistencia.DOCFECHADOCUMENTO,
					inserted, "DOCUMENTOS", "DOCUMENTO");
			exchange.getIn().setBody(estadoPersistencia);

			DocumentoResponse docResponse = new DocumentoResponse();
			docResponse.setNroDocumento(doc.getGtime().getNumeroReferencia());
			exchange.getIn().setHeader("docResponse", docResponse);
		}
	}
	
	
	public void generaFechaCreacion(Exchange exchange) {
		Long idDocBase = (Long) exchange.getIn().getHeader("idDocBase");

		repoDocFecha.generaFechaCreacion(idDocBase);
	}
}
