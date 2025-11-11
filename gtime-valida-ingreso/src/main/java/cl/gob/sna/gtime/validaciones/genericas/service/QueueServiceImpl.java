package cl.gob.sna.gtime.validaciones.genericas.service;

import cl.gob.sna.gtime.validaciones.repository.QueueRepository;
import cl.gob.sna.gtime.validaciones.repository.dto.QueueTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueueServiceImpl implements QueueService{
    @Autowired
    private QueueRepository queueRepository;
    @Override
    public QueueTo obtenerColaPorIdPayload(long idCola) {
        return queueRepository.obtenerQueuPorIDPayload(idCola);
    }
}
