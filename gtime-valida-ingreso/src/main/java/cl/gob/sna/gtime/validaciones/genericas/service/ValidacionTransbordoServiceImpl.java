package cl.gob.sna.gtime.validaciones.genericas.service;

import cl.gob.sna.gtime.validaciones.repository.LocacionRepository;
import cl.gob.sna.gtime.validaciones.repository.dto.LocacionTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidacionTransbordoServiceImpl implements ValidacionTransbordoService{

    @Autowired
    private LocacionRepository locacionRepository;

    @Override
    public LocacionTo buscaLocacionYDescPorCodigo7(String codigo) {
        LocacionTo locacion = locacionRepository.obtenerLocacionUnByCodigo(codigo);

        return locacion;
    }

    @Override
    public LocacionTo buscaLocacionYDescPorCodigoIATA(String codigo) {
        LocacionTo locacion = locacionRepository.obtenerLocacionIataByCodigo(codigo);

        return locacion;
    }
}
