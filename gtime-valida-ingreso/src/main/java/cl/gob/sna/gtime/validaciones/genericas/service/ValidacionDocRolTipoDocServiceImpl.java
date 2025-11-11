package cl.gob.sna.gtime.validaciones.genericas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cl.gob.sna.gtime.validaciones.repository.DocRolTipoDocRepository;

@Service
public class ValidacionDocRolTipoDocServiceImpl implements ValidacionDocRolTipoDocService {

	@Autowired
	private DocRolTipoDocRepository docRolTipoDocRepository;
 
	@Override
	public boolean isDocRolTipoDocExistente(String nombre, String tipoDocumento) {
		return docRolTipoDocRepository.isDocRolTipoDocExistente(nombre,tipoDocumento);
	}
 
}
