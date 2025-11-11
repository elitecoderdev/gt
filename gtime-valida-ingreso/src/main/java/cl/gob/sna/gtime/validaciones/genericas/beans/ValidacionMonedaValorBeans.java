package cl.gob.sna.gtime.validaciones.genericas.beans;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.validaciones.repository.MonedaValorRepository;
import cl.gob.sna.gtime.validaciones.repository.dto.MonedaValorTo;
import cl.gob.sna.gtime.validaciones.utils.StringValidation;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;


public class ValidacionMonedaValorBeans implements Processor {

	Logger LOG = Logger.getLogger(ValidacionMonedaValorBeans.class);
	@Autowired
	private MonedaValorRepository monedaValorRepository;
	public void process(Exchange exchange) throws Exception {	
		DocumentoResponse docResponse = (DocumentoResponse) exchange.getIn().getHeader("docResponse");
		Validacion val = null;
		if (StringValidation.isNull(docResponse.getGtime().getMonedaValor())) {
			val = new Validacion(TipoValidacion.MONEDA_VALOR_NULL, false, new Date());
		} else {
			MonedaValorTo monedaValor =  monedaValorRepository.obtenerMonedaValorByCodigo(docResponse.getGtime().getMonedaValor());
			boolean unidadExist = monedaValor != null;
			val = new Validacion(TipoValidacion.MONEDA_VALOR_UNEXIST, unidadExist, new Date());
			val.setTexto(TipoValidacion.MONEDA_VALOR_UNEXIST.getTexto().replace("[1?]",docResponse.getGtime().getMonedaValor() ));
 		}
		docResponse.getListValidaciones().add(val);
		exchange.getIn().setHeader("docResponse", docResponse);
	}

}
