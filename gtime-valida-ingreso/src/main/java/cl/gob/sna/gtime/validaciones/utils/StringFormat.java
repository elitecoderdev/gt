package cl.gob.sna.gtime.validaciones.utils;

public class StringFormat {
	
	public static String formatToRut(String rut) {
		if(rut == null) {
			return null;
		}
			
        int cont = 0;
        String format;
        rut = rut.replace(".", "");
        rut = rut.replace("-", "");
       	format = "-" + rut.substring(rut.length() - 1);
        
        for (int i = rut.length() - 2; i >= 0; i--) {
            format = rut.substring(i, i + 1) + format;
            cont++;
            if (cont == 3 && i != 0) {
                format = "." + format;
                cont = 0;
            }
        }
        return format;
	}
	
	public static Integer convertToIntegerIfExceptionToZero(String value) {	
		try {
			return Integer.parseInt(value);
		}catch (NumberFormatException  e) {
			return 0;
		}
		
	}
	
}
