package cl.gob.sna.gtime.cron.reencolador.jpa.service.impl;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import cl.gob.sna.gtime.cron.reencolador.jpa.model.entity.Queue;
import cl.gob.sna.gtime.cron.reencolador.jpa.model.repository.custom.ColaAQCITORepositoryCustom;
import cl.gob.sna.gtime.cron.reencolador.jpa.service.ReencoladorService;
import cl.gob.sna.gtime.cron.reencolador.rabbitmq.RabbitClient;

@Service
public class ReencoladorServiceImpl implements ReencoladorService {

	@Autowired
	private ColaAQCITORepositoryCustom colaAQCITORepository;	
	
	@Override
	public Boolean reencolarMensajes() {
		RabbitClient clienteRabbit = new RabbitClient();
		
		while(colaAQCITORepository.existenEncolados()) {
			Queue mensaje = colaAQCITORepository.findById(colaAQCITORepository.desencolarMensaje());
			try {
				clienteRabbit.sendToRabbit(mensaje.getMsg());
			} catch (IOException | TimeoutException e) {
				e.printStackTrace();
			}
			mensaje.setStatus(-8);
			colaAQCITORepository.actualiza(mensaje);
		}
		
		return true;
	}

}
