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
 
public class ValidacionUnidadPesoBeans implements Processor {

	Logger LOG = Logger.getLogger(ValidacionUnidadPesoBeans.class);
	@Autowired
	private UnidadMedidaRepository unidadMedidaRepository;
	public void process(Exchange exchange) throws Exception {	
		DocumentoResponse docResponse = (DocumentoResponse) exchange.getIn().getHeader("docResponse");

		if (StringValidation.isNull(docResponse.getGtime().getUnidadPeso())) {
			Validacion val = new Validacion(TipoValidacion.UNIDAD_PESO_NULL, false, new Date());
			docResponse.getListValidaciones().add(val);
		} else {
 			UnidadMedidaTo unidadVolumen = unidadMedidaRepository.
					obtenerUnidadMedidaByCodigo(docResponse.getGtime().getUnidadPeso());
 			if (unidadVolumen == null){
				Validacion val = new Validacion(TipoValidacion.UNIDAD_PESO_UNEXIST, false, new Date());
				val.setTexto(TipoValidacion.UNIDAD_PESO_UNEXIST.
						getTexto().replace("[1?]",docResponse.getGtime().getUnidadPeso() ));
				docResponse.getListValidaciones().add(val);
			}
 		}

		exchange.getIn().setHeader("docResponse", docResponse);
	}

}
