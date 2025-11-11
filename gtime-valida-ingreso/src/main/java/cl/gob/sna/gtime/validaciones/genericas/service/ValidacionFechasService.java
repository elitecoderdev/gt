package cl.gob.sna.gtime.validaciones.genericas.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cl.gob.sna.gtime.api.vo.Fecha;

public interface ValidacionFechasService {
	Map<String, Integer> countFechasRepetidas(List<Fecha> fechas);

	boolean isFechaValidaGtime(String tipoFecha);

	boolean isFechaCreacion(String tipoFecha);

	boolean isFormatoCorrectoFecha(String tipoFecha, String valorFecha);

	boolean isFechaFemMenorHoy(String tipoFecha, String valorFecha) throws ParseException;

	boolean isFem(String tipoFecha);

	Date stringToDate(String tipoFecha, String valorFecha);

 
}
