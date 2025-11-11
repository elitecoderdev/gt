package cl.gob.sna.gtime.cron.reencolador.jpa.model.repository.custom;

import java.math.BigInteger;

import cl.gob.sna.gtime.cron.reencolador.jpa.model.entity.Queue;

public interface ColaAQCITORepositoryCustom {
	public BigInteger desencolarMensaje();
	public Boolean existenEncolados();
	public Queue findById(BigInteger id);
	public Queue actualiza(Queue queue);
}
