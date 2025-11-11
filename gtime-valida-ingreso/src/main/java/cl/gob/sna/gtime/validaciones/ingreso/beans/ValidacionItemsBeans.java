package cl.gob.sna.gtime.validaciones.ingreso.beans;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.api.vo.Item;
import cl.gob.sna.gtime.api.vo.Proditem;
import cl.gob.sna.gtime.validaciones.genericas.service.ValidacionItemsService;
import cl.gob.sna.gtime.validaciones.genericas.service.ValidacionProductoService;
import cl.gob.sna.gtime.validaciones.utils.StringValidation;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;
public class ValidacionItemsBeans implements Processor {

	Logger LOG = Logger.getLogger(ValidacionItemsBeans.class);
	@Autowired
	private ValidacionItemsService validacionItemService;
	@Autowired
	private ValidacionProductoService validacionProductoService;
	
	public void process(Exchange exchange) throws Exception {
		//TODO: disminuir complejidad cognitiva
		DocumentoResponse docResponse = exchange.getIn().getHeader("docResponse",DocumentoResponse.class);
		Integer seqItem = 1;
		Map<String, Integer> tiposDeBulto = new HashMap<> ();
		BigDecimal sumaCantidad = BigDecimal.ZERO;
		BigDecimal sumaPeso = BigDecimal.ZERO;

		if(docResponse.getGtime().getItems() == null ||
				docResponse.getGtime().getItems().getItem() == null ||
					docResponse.getGtime().getItems().getItem().isEmpty()) {
   			docResponse.getListValidaciones().add(new Validacion(TipoValidacion.ITEMS_SIN_ITEMS, false, new Date()));
		} else {
			for (Item item : docResponse.getGtime().getItems().getItem()) {		
				if(tiposDeBulto.containsKey(item.getTipoBulto())) {
					tiposDeBulto.put(item.getTipoBulto(), tiposDeBulto.get(item.getTipoBulto())+1);
				}else {
					tiposDeBulto.put(item.getTipoBulto(), 0);
				}
				docResponse.getListValidaciones().addAll(validacionItemService.validarNumeroItems(seqItem, item));
				docResponse.getListValidaciones().addAll(validacionItemService.validarMarcas(item));
				docResponse.getListValidaciones().addAll(validacionItemService.validarTipoBulto(item));
				docResponse.getListValidaciones().addAll(validacionItemService.validarUnidadPeso(seqItem, docResponse.getGtime().getUnidadPeso(), item));

				try {
					BigDecimal pesoBruto = new BigDecimal(item.getPesoBruto());
					sumaPeso = pesoBruto.compareTo(BigDecimal.ZERO) > 0 ? sumaPeso.add(pesoBruto) : sumaPeso;
				}catch(NumberFormatException e) {
					LOG.info("En item secuencia "+seqItem+" no se obtiene un [peso-bruto] valido por lo que no es considerado en la totalizacion");
				}
				try {
					BigDecimal cantidad =new BigDecimal(item.getCantidad());
					sumaCantidad = cantidad.compareTo(BigDecimal.ZERO) > 0  ? sumaCantidad.add(cantidad) : sumaCantidad;
				}catch(NumberFormatException e) {
					LOG.info("En item secuencia "+seqItem+" no se obtiene un [cantidad] valida por lo que no es considerada en la totalizacion");
				}
				
				Integer seqProd = 1;
				if(item.getProdItem() != null && 
						item.getProdItem().getProditem() != null && 
							!item.getProdItem().getProditem().isEmpty()) {
					for (Proditem producto : item.getProdItem().getProditem()) {
						docResponse.getListValidaciones().addAll(validacionProductoService.validarDescripcion(seqItem, seqProd, producto));
						docResponse.getListValidaciones().addAll(validacionProductoService.validarUnidadMedida(seqItem, seqProd, producto));
						docResponse.getListValidaciones().addAll(validacionProductoService.validarCantidad(seqItem, seqProd, producto));
						docResponse.getListValidaciones().addAll(validacionProductoService.validarValorDeclarado(seqItem, seqProd, producto));
						docResponse.getListValidaciones().addAll(validacionProductoService.validarMoneda(seqItem, seqProd, producto));
						seqProd++;
					}
				}else {
					Validacion val = new Validacion(TipoValidacion.ITEMS_SIN_PRODUCTOS, false, new Date());
					val.setTexto(TipoValidacion.ITEMS_SIN_PRODUCTOS.getTexto().replace("[1?]",seqItem.toString()));
					docResponse.getListValidaciones().add(val);
				}
				
				seqItem++;
			}
			for (Iterator<Entry<String, Integer>> entries = tiposDeBulto.entrySet().iterator(); entries.hasNext(); ) {
				Entry<String, Integer> entry = entries.next();
				if(entry.getValue() > 1) {
					Validacion val = new Validacion(TipoValidacion.ITEMS_TIPO_BULTO_REPETIDO, false, new Date());
					val.setTexto(TipoValidacion.ITEMS_TIPO_BULTO_REPETIDO.getTexto().replace("[1?]",entry.getKey()));
					docResponse.getListValidaciones().add(val);
				}
			}
		}
		
 
		Integer totalItems = StringValidation.isNumeric(docResponse.getGtime().getTotalItem())? Integer.parseInt(docResponse.getGtime().getTotalItem()):0; 
		if(totalItems.compareTo(docResponse.getGtime().getItems().getItem().size()) != 0 ) {
			docResponse.getListValidaciones().add(new Validacion(TipoValidacion.ITEMS_TOTAL_ITEMS_DIFIERE_ENCABEZADO, false, new Date()));
		}
		
		BigDecimal totalPesoInformado = new BigDecimal(docResponse.getGtime().getTotalPeso());
		if(totalPesoInformado.setScale(2,RoundingMode.HALF_UP).compareTo(sumaPeso.setScale(2,RoundingMode.HALF_UP)) != 0) {
			docResponse.getListValidaciones().add(new Validacion(TipoValidacion.ITEMS_PESO_TOTAL_DIFIERE_ENCABEZADO, false, new Date()));
		}
		
		BigDecimal totalBultosInformados = new BigDecimal(docResponse.getGtime().getTotalBultos());
		
		if(totalBultosInformados.setScale(2,RoundingMode.HALF_UP).compareTo(sumaCantidad.setScale(2,RoundingMode.HALF_UP)) != 0) {
			docResponse.getListValidaciones().add(new Validacion(TipoValidacion.ITEMS_CANTIDAD_TOTAL_DIFIERE_ENCABEZADO, false, new Date()));
		}
		
 
		exchange.getIn().setHeader("docResponse", docResponse);
	}

}
