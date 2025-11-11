package cl.gob.sna.gtime.validaciones.genericas.service;

import cl.gob.sna.gtime.validaciones.repository.DetalleLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetalleLogServiceImpl implements DetalleLogService{
    @Autowired
    private DetalleLogRepository detalleLogRepository;
    @Override
    public void deleteFromDetalleLogByNumRef(String tipoDoc, String numRef, String login) {
        detalleLogRepository.deleteFromDetalleLogByNumRef(tipoDoc, numRef, login);
    }

    @Override
    public void deleteFromDocLogRecepcion(String tipoDoc, String numRef, String login) {
        detalleLogRepository.deleteFromDocLogRecepcion(tipoDoc, numRef, login);
    }
}
