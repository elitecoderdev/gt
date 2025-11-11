package cl.gob.sna.gtime.validaciones.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FechaFormat {

    /**
     *
     * @param valorFecha
     * @return
     * @throws ParseException
     */
    public static Date getDateNoTime(String valorFecha) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat formatterSinHora = new SimpleDateFormat("dd-MM-yyyy");

        try{
            Date valorFEM = formatter.parse(valorFecha);
            return FechaFormat.setDateNoTime(valorFEM);
        }catch (ParseException e){
            Date valorFEMSinHora = formatterSinHora.parse(valorFecha);
            return FechaFormat.setDateNoTime(valorFEMSinHora);
        }
    }

    /**
     *
     * @param valor
     * @return
     */
    public static Date setDateNoTime(Date valor){
        Calendar cal = new GregorianCalendar();

        cal.setTime(valor);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }
}
