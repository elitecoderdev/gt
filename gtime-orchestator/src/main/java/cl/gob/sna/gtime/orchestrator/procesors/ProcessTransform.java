package cl.gob.sna.gtime.orchestrator.procesors;

import cl.gob.sna.gtime.api.vo.Gtime;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.logging.Logger;

import cl.gob.sna.gtime.orchestrator.vo.queue.QueueGtime;

public class ProcessTransform implements Processor {

	Logger log = Logger.getLogger(ProcessTransform.class);

	public void process(Exchange exchange) throws Exception {
		Gtime gtimeQueued = exchange.getIn().getBody(Gtime.class);
		if (gtimeQueued != null){
			log.info(" gtime {}" + gtimeQueued.getTipoOperacion());
		}
		/*QueueGtime gtimeQueued = exchange.getIn().getBody(QueueGtime.class);
		log.info("ProcessTransform: "+gtimeQueued.getIdQueue());

		if (gtimeQueued != null) {
			exchange.getIn().setHeader("idQueued", gtimeQueued.getIdQueue());
			exchange.getIn().setBody(gtimeQueued.getXml());
		}*/

	}

}
