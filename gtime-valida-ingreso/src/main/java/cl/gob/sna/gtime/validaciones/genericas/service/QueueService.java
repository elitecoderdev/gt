package cl.gob.sna.gtime.validaciones.genericas.service;

import cl.gob.sna.gtime.validaciones.repository.dto.QueueTo;

public interface QueueService {
    QueueTo obtenerColaPorIdPayload(long idCola);
}
