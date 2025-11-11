package cl.gob.sna.gtime.cron.reencolador.jpa.model.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import cl.gob.sna.gtime.cron.reencolador.jpa.model.entity.Queue;


public interface QueueRepository extends CrudRepository<Queue, BigInteger> {
	
	@Query("select q from Queue r WHERE id = :id")
	Queue findById(@Param("id") BigInteger id);
}
