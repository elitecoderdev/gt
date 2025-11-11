package cl.gob.sna.gtime.validaciones.genericas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.gob.sna.gtime.validaciones.repository.PaisRepository;

@Service
public class ValidacionPaisServiceImpl implements ValidacionPaisService {

	@Autowired
	private PaisRepository paisRepository;
 
	@Override
	public boolean isPaisExistente(String codPais) {
		return paisRepository.isPaisExistente(codPais);
	}
 
}
