package cl.gob.sna.gtime.validaciones.genericas.service;

import cl.gob.sna.gtime.validaciones.repository.DINRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Qualifier
@Service
public class ValidacionConsignatarioServiceImpl implements ValidacionConsignatarioService{
    @Autowired
    private DINRepository dinRepository;
    @Override
    public Map<String, String> getValidaRUTDocumento(String numReferencia, String tipoDoc, String tipoAccion, String rutCons,
                                                     String dvRutCons, Date fechaEmision) {

        return dinRepository.getDinConsigatarioRes(numReferencia, tipoDoc, tipoAccion, rutCons, dvRutCons, fechaEmision);
    }
}
