package cl.gob.sna.gtime.processor.aggregate;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.jboss.logging.Logger;

import cl.gob.sna.gtime.vo.Persistencia;


public class AggregationStrategyPersistencias implements AggregationStrategy {
	private static final String PERSISTENCIAS = "persistencias";
	Logger log = Logger.getLogger(AggregationStrategyPersistencias.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		//log.info("inicio aggregatePersistencias");
		
		
		if(oldExchange == null ) {
			oldExchange = newExchange;
		}
		
		Persistencia val = newExchange.getIn().getBody(Persistencia.class);
		List<Persistencia> listaEstadosPersistencias = (List<Persistencia>) oldExchange.getIn().getHeader(PERSISTENCIAS);
		
		listaEstadosPersistencias.add(val);
		oldExchange.getIn().setHeader(PERSISTENCIAS, listaEstadosPersistencias);
		oldExchange.getIn().setBody(listaEstadosPersistencias);

		return oldExchange;
	 
	}

}
