package cl.gob.sna.gtime.validaciones.genericas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.gob.sna.gtime.validaciones.repository.OperadorRepository;
import cl.gob.sna.gtime.validaciones.utils.StringFormat;

@Service
public class ValidacionOperadorServiceImpl implements ValidacionOperadorService {

	@Autowired
	private OperadorRepository operadorRepository;

	@Override
	public boolean isOperadorCourierExistente(String rut) {

		return operadorRepository.isOperadorExistente(StringFormat.formatToRut(rut), OperadorRepository.TIPO_OPERADOR_COURIER);
	}

}
