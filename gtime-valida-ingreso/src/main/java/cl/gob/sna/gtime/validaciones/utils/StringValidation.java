package cl.gob.sna.gtime.validaciones.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class StringValidation {

	private StringValidation() {

	}

	public static boolean isNull(String value) {
		return value == null || value.trim().isEmpty();
	}

	public static boolean largoMaximo(String value, long maxLengt) {

		return !(isNull(value) || value.trim().length() > maxLengt);
	}

	public static boolean isNumeric(String value) {

		return value != null && value.trim().matches("[+-]?\\d*(\\.\\d+)?");

	}

	public static boolean strInArray(String value, String[] array) {

		if (array == null || value == null) {
			return false;
		}
		List<String> list = Arrays.asList(array);

		return list.contains(value.trim());
	}
	
	public static boolean isDate(String value, String pattern) {
		boolean isDate = true;
		if(pattern != null) {
		    SimpleDateFormat formatter =new SimpleDateFormat(pattern);  
		    try {
				 formatter.parse(value);
			} catch (ParseException e) {
				isDate = false;
			}  
 		}else {
 			isDate = false;
 		}
		return isDate;
		
	}
	
}
