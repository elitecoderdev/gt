package cl.gob.sna.gtime.validaciones.genericas.beans;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.validaciones.repository.UnidadMedidaRepository;
import cl.gob.sna.gtime.validaciones.repository.dto.UnidadMedidaTo;
import cl.gob.sna.gtime.validaciones.utils.StringValidation;
import cl.gob.sna.gtime.vo.DocumentoResponse;
import cl.gob.sna.gtime.vo.TipoValidacion;
import cl.gob.sna.gtime.vo.Validacion;


public class ValidacionUnidadVolumenBeans implements Processor {

	Logger LOG = Logger.getLogger(ValidacionUnidadVolumenBeans.class);
	@Autowired
	private UnidadMedidaRepository unidadMedidaRepository;
	public void process(Exchange exchange) throws Exception {	
		DocumentoResponse docResponse = (DocumentoResponse) exchange.getIn().getHeader("docResponse");
		
		if (!StringValidation.isNull(docResponse.getGtime().getUnidadVolumen())) {
 			UnidadMedidaTo unidadVolumen = unidadMedidaRepository.obtenerUnidadMedidaByCodigo(docResponse.getGtime().getUnidadVolumen());
 			boolean unidadExist = unidadVolumen != null;
			Validacion val = new Validacion(TipoValidacion.UNIDAD_VOLUMEN_UNEXIST, unidadExist, new Date());;
   			val.setTexto(TipoValidacion.UNIDAD_VOLUMEN_UNEXIST.getTexto().replace("[1?]",docResponse.getGtime().getUnidadVolumen() ));
 			docResponse.getListValidaciones().add(val);
		}
		
		
		if(!StringValidation.isNull(docResponse.getGtime().getTotalVolumen())) {
			Validacion val = new Validacion(TipoValidacion.UNIDAD_VOLUMEN_NULL, StringValidation.isNull(docResponse.getGtime().getUnidadVolumen()), new Date());
			docResponse.getListValidaciones().add(val);
		}
		
		exchange.getIn().setHeader("docResponse", docResponse);
		
 		
	}

}
