package cl.gob.sna.gtime.cron.reencolador.jpa.model.repository.custom;

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.gob.sna.gtime.cron.reencolador.jpa.model.entity.Queue;
import cl.gob.sna.gtime.cron.reencolador.jpa.model.repository.QueueRepository;

@Service
public class ColaAQCITORepositoryImpl implements ColaAQCITORepositoryCustom {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private QueueRepository queueRepository;
	
	@Override
	public BigInteger desencolarMensaje() {
		StoredProcedureQuery storedProcedureQuery = em.createStoredProcedureQuery("DOCUMENTOS.NEW_VALIDA_GTIME.DESENCOLAR_MENSAJE");
		storedProcedureQuery.registerStoredProcedureParameter("p_ID_COLA", BigInteger.class, ParameterMode.OUT);
		storedProcedureQuery.execute();
		return (BigInteger) storedProcedureQuery.getOutputParameterValue("p_ID_COLA");
	}

	@Override
	public Boolean existenEncolados() {
		StoredProcedureQuery storedProcedureQuery = em.createStoredProcedureQuery("DOCUMENTOS.NEW_VALIDA_GTIME.DESENCOLAR_MENSAJE");
		storedProcedureQuery.registerStoredProcedureParameter("p_ID_COLA", BigInteger.class, ParameterMode.OUT);
		storedProcedureQuery.execute();
		return (Boolean) storedProcedureQuery.getOutputParameterValue("p_ID_COLA");
	}
		
	@Override
	public Queue actualiza(Queue queue) {
		queueRepository.save(queue);
		return queue;
	}

	@Override
	public Queue findById(BigInteger id) {
		return queueRepository.findById(id);
	}
}
