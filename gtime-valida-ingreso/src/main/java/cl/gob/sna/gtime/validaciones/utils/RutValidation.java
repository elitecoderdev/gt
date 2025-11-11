package cl.gob.sna.gtime.validaciones.utils;

import java.util.HashMap;
import java.util.Map;

public class RutValidation {

	private RutValidation() {

	}

 
 	public static boolean isRutValido(String rut) {

		boolean validacion = false;
		try {
			rut = rut.toUpperCase();
			rut = rut.replace(".", "");
			rut = rut.replace("-", "");
			int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

			char dv = rut.charAt(rut.length() - 1);
 
			validacion = dv == dvrut(rutAux);
		} catch (java.lang.NumberFormatException e) {
		} catch (Exception e) {
		}
		return validacion;
	}
 	
	public static char dvrut(int numrut) {
		int M = 0, S = 1, T = numrut;
		for (; T != 0; T /= 10) {
			S = (S + T % 10 * (9 - M++ % 6)) % 11;
		}
		return (char) (S != 0 ? S + 47 : 75);
	}

	public static Map<String, String> getRutyDV(String rut){
		Map<String, String> detalleRut = new HashMap<>();
		try{
			rut = rut.toUpperCase();

			rut = rut.replace(".", "");
			rut = rut.replace("-", "");

			int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));
			char dv = rut.charAt(rut.length() - 1);

			detalleRut.put(String.valueOf(rutAux), String.valueOf(dv));
			return detalleRut;
		}catch (Exception e){
			return null;
		}
	}
}
