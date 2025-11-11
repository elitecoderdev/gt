package cl.gob.sna.gtime.validaciones.ingreso.aggregate;

import java.util.Date;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.jboss.logging.Logger;

import cl.gob.sna.gtime.validaciones.repository.dto.EmisorTo;
import cl.gob.sna.gtime.vo.Validacion;


public class AggregationStrategyValidaciones implements AggregationStrategy {
	private Logger LOG = Logger.getLogger(AggregationStrategyValidaciones.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		if(oldExchange == null ) {
			return newExchange;
		}

		EmisorTo emisor =  newExchange.getIn().getHeader("emisor", EmisorTo.class);	
		
		if (emisor != null) {
			oldExchange.getIn().setHeader("emisor", emisor);
 		}
		Date fem = newExchange.getIn().getHeader("fem",Date.class);
		if (fem != null) {
			oldExchange.getIn().setHeader("fem", fem);
 		}
		Validacion val = newExchange.getIn().getBody(Validacion.class);
		List<Validacion> listValidacion = (List<Validacion>) oldExchange.getIn().getHeader("validaciones");
		
		
		listValidacion.add(val);
		oldExchange.getIn().setHeader("validaciones", listValidacion);
		oldExchange.getIn().setBody(listValidacion);
 		return oldExchange;
	 
	}

}
