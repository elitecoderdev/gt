package cl.gob.sna.gtime.cron.reencolador.beans;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.cron.reencolador.jpa.service.ReencoladorService;


public class ReencoladorBean implements Processor {

	@Autowired
	private ReencoladorService reencoladorService;	
	
	@Override
	public void process(Exchange exchange) throws Exception {
		reencoladorService.reencolarMensajes();
	}

}
