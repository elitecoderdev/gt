package cl.gob.sna.gtime.validaciones.genericas.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import cl.gob.sna.gtime.validaciones.utils.FechaFormat;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.gob.sna.gtime.api.vo.Fecha;
import cl.gob.sna.gtime.validaciones.repository.DocTipoDocFechaRepository;
import cl.gob.sna.gtime.validaciones.utils.StringValidation;

@Service
public class ValidacionFechasServiceImpl implements ValidacionFechasService {
	private final Logger LOG = Logger.getLogger(ValidacionFechasServiceImpl.class);

	@Autowired
	private DocTipoDocFechaRepository docTipoDocFechaRepository;

	@Override
	public Map<String, Integer> countFechasRepetidas(List<Fecha> fechas) {

		Collection<String> comparadorFecha = new ArrayList<>();
		HashMap<String, Integer> fechasRepetidas = null;
		if (fechas == null) {
			return null;
		}
		for (Fecha fecha : fechas) {
			if (comparadorFecha.contains(fecha.getNombre() == null ? "NULO" : fecha.getNombre().toUpperCase().trim())) {
				fechasRepetidas = fechasRepetidas == null ? new HashMap<String, Integer>() : fechasRepetidas;
				String key = fecha.getNombre() == null ? "null" : fecha.getNombre();

				Integer cantidad = fechasRepetidas.containsKey(key) ? fechasRepetidas.get(key) : 0;
				fechasRepetidas.put(key, ++cantidad);
			}
			comparadorFecha.add(fecha.getNombre() == null ? "NULO" : fecha.getNombre().toUpperCase().trim());

		}

		return fechasRepetidas;
	}

	@Override
	public boolean isFechaValidaGtime(String tipoFecha) {
		return docTipoDocFechaRepository.isFechaValidaTipoDocumento(DocTipoDocFechaRepository.TIPO_DOC_GTIME, tipoFecha);
	}

	@Override
	public boolean isFechaCreacion(String tipoFecha) {
		return DocTipoDocFechaRepository.FECHA_CREACION.equals(tipoFecha);
	}

	@Override
	public boolean isFormatoCorrectoFecha(String tipoFecha, String valorFecha) {
		String pattern = "dd-MM-yyyy HH:mm";
		if (DocTipoDocFechaRepository.FECHA_FEM.equals(tipoFecha)) {
			pattern = "dd-MM-yyyy";
		}

		return StringValidation.isDate(valorFecha, pattern);
	}

	@Override
	public boolean isFechaFemMenorHoy(String tipoFecha, String valorFecha) throws ParseException {
		boolean isFechaFemMenorHoy = true;

		try{
			if (DocTipoDocFechaRepository.FECHA_FEM.equals(tipoFecha)) {
				Date fechaHoy = FechaFormat.setDateNoTime(new Date());
				Date fechaFEM = FechaFormat.getDateNoTime(valorFecha);

				if (fechaFEM.equals(fechaHoy)) return true;

				isFechaFemMenorHoy = fechaFEM.before(fechaHoy);
				return isFechaFemMenorHoy;
			}
		}catch (Exception e){
			return false;
		}

		return isFechaFemMenorHoy;
	}

	@Override
	public boolean isFem(String tipoFecha) {
		return DocTipoDocFechaRepository.FECHA_FEM.equals(tipoFecha);
	}
	
	@Override
	public Date stringToDate(String tipoFecha, String valorFecha) {
		if(isFormatoCorrectoFecha(tipoFecha,valorFecha)) {
 			String pattern = "dd-MM-yyyy HH:mm";
			if(isFem(tipoFecha)) {
				pattern = "dd-MM-yyyy";
			} 
			
			if(!StringValidation.isDate(valorFecha, pattern)) {
				return null;
			}
			
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			try {
				return formatter.parse(valorFecha);
			} catch (ParseException e) {
				LOG.error("Error al intentar convertir String en Fecha valorFecha: {}",valorFecha, e);
				return null;
			}
 		}else {
			return null;
		}
		
	}
	
}
