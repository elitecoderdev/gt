package cl.gob.sna.gtime.processor.beans;

import java.util.ArrayList;
import java.util.List;

import cl.gob.sna.gtime.api.vo.Item;
import cl.gob.sna.gtime.processor.repo.DocTranItemProductoRepository;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.api.vo.Gtime;
import cl.gob.sna.gtime.processor.repo.DocTranItemTransporteRepository;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.Persistencia;
import cl.gob.sna.gtime.vo.TipoPersistencia;

public class PersistDocItemsBean {

	private static final Logger log = LoggerFactory.getLogger(PersistDocItemsBean.class);

	@Autowired
	private DocTranItemTransporteRepository repoItem;
	@Autowired
	private DocTranItemProductoRepository repoItemProd;

	public void persistItems(@Body DocumentoResponse doc, Exchange exchange) {
		//log.info(" [+] persistItems ");
		boolean inserted = false;
		List<Boolean> persistencias = new ArrayList<>();

		try{
			Long idDocBase = (Long) exchange.getIn().getHeader("idDocBase");
			if (doc != null && doc.getGtime() != null) {
				Gtime gtime = doc.getGtime();
				if (gtime != null) {
					List<Item> listaItems = gtime.getItems().getItem();

					int posItem = 1;

					for (Item item : listaItems){
						int nroEncabezado = Integer.valueOf(gtime.getNroEncabezado());

						persistencias.add(repoItem.persisteItem(idDocBase, nroEncabezado, item, posItem));
						persistencias.add(repoItemProd.persisteItemProducto(idDocBase, item));

						posItem++;
					}
				}
			}
		}catch (Exception e){
			log.error("ERROR persistItems "+ e.getMessage());
			e.getStackTrace();
		}finally {
			inserted = (!persistencias.contains(false) && !persistencias.isEmpty());
			exchange.getIn().setHeader("documento", doc);

			Persistencia persistenciaItems = new Persistencia(TipoPersistencia.DOCTRANITEMTRANSPORTE,
					inserted, "DOCTRANSPORTE", "DOCUMENTOTRANSPORTE");

			doc.getListEstadoPersistencia().add(persistenciaItems);

			Persistencia persistenciaItemProducto = new Persistencia(TipoPersistencia.DOCTRANITEMPRODUCTO,
					inserted, "DOCTRANSPORTE", "DOCUMENTOTRANSPORTE");

			doc.getListEstadoPersistencia().add(persistenciaItemProducto);

			exchange.getIn().setBody(doc.getListEstadoPersistencia());
			DocumentoResponse docResponse = new DocumentoResponse();
			docResponse.setNroDocumento(doc.getGtime().getNumeroReferencia());
			exchange.getIn().setHeader("docResponse", docResponse);
		}
	}
}
