package cl.gob.sna.gtime.processor.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class FechaUtil {
	public final static String FORMATO_FECHA = "dd/MM/yyyy";
	public final static String FORMATO_FECHAMESDIA = "MM/dd/yyyy";
	public final static String FORMATO_FECHA_PROCEDIMIENTO = "ddMMyyyy";
	public final static String FORMATO_FECHA_SQL_DATE_ORACLE = "yyyy-MM-dd";
	public final static String FORMATO_HORA_MINUTO = "HH:mm";
	public final static String FORMATO_FECHA_HORA_GESTION_PLANTA_CORP = "dd/MM/yyyy HH:mm";
	public final static String FORMATO_GTIME_FECHA = "dd-MM-yyyy";
	public final static String FORMATO_GTIME_FECHA_HORA = "dd-MM-yyyy HH:mm";

	public final static String FORMATO_GTIME_VALIDEZ = "yyyyMMddHHmmss";

	public final static String FORMATO_GTIME_FECHA_ALT= "dd-mm-yyyy";
	public final static String FORMATO_GTIME_FECHA_ALT_HORA= "dd-mm-yyyy HH:mm:ss";

	public static java.sql.Date toDateGtime(String valor) {
		try {
			return FechaUtil.parseFecha(valor, FechaUtil.FORMATO_GTIME_FECHA_HORA);
		} catch (ParseException e) {
			try {
				return FechaUtil.parseFecha(valor, FechaUtil.FORMATO_GTIME_FECHA);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * @return
	 */
	public static java.util.Date getFechaActual() {

		return new Date();

	}

	/**
	 * @return
	 */
	public static java.sql.Date getFechaActualBD() {
		return new java.sql.Date(getFechaActual().getTime());
	}

	/**
	 *
	 * @param fechaBD
	 * @return
	 */
	public static String formatFechaBD(java.sql.Date fechaBD) {
		SimpleDateFormat patronParaFecha = new SimpleDateFormat(FORMATO_FECHA);
		if (fechaBD == null) {
			return "";
		} else {
			return patronParaFecha.format(fechaBD);
		}
	}

	/**
	 *
	 * @param fechaBD
	 * @return
	 */
	public static String formatFechaToProc(java.sql.Date fechaBD) {
		SimpleDateFormat patronParaFecha = new SimpleDateFormat(FORMATO_FECHA_PROCEDIMIENTO);
		if (fechaBD == null) {
			return "";
		} else {
			return patronParaFecha.format(fechaBD);
		}
	}

	/**
	 *
	 * @param valor
	 * @return
	 */
	public static java.sql.Date parseToFechaBD(String valor)  {
		if ((valor == null) || (valor.length() == 0))
			return null;
		SimpleDateFormat patronParaFecha = new SimpleDateFormat(FORMATO_GTIME_FECHA);
		patronParaFecha.setLenient(false);
		java.sql.Date fechaSQL = null;
		try {
			fechaSQL = new java.sql.Date(patronParaFecha.parse(valor).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fechaSQL;
	}

	/**
	 * @param fechaBD
	 * @param formato
	 * @return
	 */
	public static String formatFecha(java.sql.Date fechaBD, String formato) {
		SimpleDateFormat patronParaFecha = new SimpleDateFormat(formato);
		if (fechaBD == null) {
			return "";
		} else {
			return patronParaFecha.format(fechaBD);
		}
	}

	/**
	 * @param valor
	 * @param formato
	 * @throws ParseException
	 */
	public static java.sql.Date parseFecha(String valor, String formato) throws ParseException {
		if ((valor == null) || (valor.length() == 0))
			return null;

		SimpleDateFormat patronParaFecha = new SimpleDateFormat(formato);
		patronParaFecha.setLenient(false);
		java.sql.Date fechaSQL = new java.sql.Date(patronParaFecha.parse(valor).getTime());
		return fechaSQL;

	}

	/**
	 *
	 * @param valor
	 * @return
	 * @throws ParseException
	 */
	public static String parseToFechaMESDIA(String valor) throws ParseException {
		if ((valor == null) || (valor.length() == 0))
			return null;
		SimpleDateFormat patronParaFecha = new SimpleDateFormat(FORMATO_FECHAMESDIA);
		patronParaFecha.setLenient(false);
		java.sql.Date fechaSQL = new java.sql.Date(patronParaFecha.parse(valor).getTime());
		return fechaSQL.toString();
	}

	public static String parseFechaToPattern(String valor, String pattern) throws ParseException {
		if ((valor == null) || (valor.length() == 0))
			return null;
		SimpleDateFormat patronParaFecha = new SimpleDateFormat(pattern);
		patronParaFecha.setLenient(false);
		java.sql.Date fechaSQL = new java.sql.Date(patronParaFecha.parse(valor).getTime());
		return fechaSQL.toString();
	}

	public static java.sql.Date getFechaHoy() {
		java.util.Date fechaA = new java.util.Date();
		java.sql.Date fechaActual = new java.sql.Date(fechaA.getTime());
		return fechaActual;
	}

	public static void main(String[] args) {

	}

	// DatatypeFactory creates new javax.xml.datatype Objects that map XML to/from
	// Java Objects.
	private static DatatypeFactory df = null;

	static {
		try {
			df = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new IllegalStateException("Error while trying to obtain a new instance of DatatypeFactory", e);
		}
	}

	// Converts a java.util.Date into an instance of XMLGregorianCalendar
	public static XMLGregorianCalendar asXMLGregorianCalendar(java.util.Date date) {
		if (date == null) {
			return null;
		} else {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTimeInMillis(date.getTime());
			return df.newXMLGregorianCalendar(gc);
		}
	}

	// Converts an XMLGregorianCalendar to an instance of java.util.Date
	public static java.util.Date asDate(XMLGregorianCalendar xmlGC) {
		if (xmlGC == null) {
			return null;
		} else {
			return xmlGC.toGregorianCalendar().getTime();
		}
	}

	public static java.util.Date stringDateTimeAsDate(String xmlStringDataTime) {
		Date date = null;
		try {
			Calendar mPublishDate;
			df = DatatypeFactory.newInstance();
			mPublishDate = df.newXMLGregorianCalendar(xmlStringDataTime).toGregorianCalendar();
			date = mPublishDate.getTime();
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			return null;
		}
		return date;
	}

	public static java.sql.Date getFechaFinVigencia() {
		try {
			return parseFecha("01-01-2100", FORMATO_GTIME_FECHA);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static java.sql.Date parseFechaDoc(String valor)  {
		if ((valor == null) || (valor.length() == 0))
			return null;
		try {
			SimpleDateFormat formato = new SimpleDateFormat(FORMATO_GTIME_FECHA_ALT);

			java.util.Date fechaD = formato.parse(valor);
			java.sql.Date fechaSinHora = new java.sql.Date(fechaD.getTime());

			return fechaSinHora;
		} catch (ParseException e) {
			return FechaUtil.parseFechaDocAlt(valor);
		}
	}


	public static java.sql.Date parseFechaDocAlt(String valor){
		if ((valor == null) || (valor.length() == 0))
			return null;
		try{
			SimpleDateFormat formatoHora = new SimpleDateFormat(FORMATO_GTIME_FECHA_ALT_HORA);
			java.util.Date fechaD2 = formatoHora.parse(valor);
			java.sql.Date fechaSinHora = new java.sql.Date(fechaD2.getTime());
			return fechaSinHora;
		}catch (ParseException pe){
			return null;
		}
	}


	public static Date parseFechaFinVigencia(){
		String fechaFinVigencia = "01-01-2100 01:01:01";
		try{
			SimpleDateFormat formatDateCL = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			Date dateFinVigencia = formatDateCL.parse(fechaFinVigencia);
			return dateFinVigencia;
		}catch (Exception e){
			return null;
		}

	}

	public static java.sql.Timestamp parseFechaFinVigenciaRS(){
		Calendar cal = new GregorianCalendar();

		cal.set(Calendar.YEAR, 2100);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 1);
		cal.set(Calendar.MINUTE, 1);
		cal.set(Calendar.SECOND, 1);

		return new java.sql.Timestamp(cal.getTimeInMillis());
	}

	public static java.sql.Timestamp parseFechaHoyRS(){
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());

		return new java.sql.Timestamp(cal.getTimeInMillis());
	}

	/**
	 *
	 * @param valorFecha
	 * @return
	 * @throws ParseException
	 */
	public static Date getDateWithFormat(String valorFecha)  {
		List<SimpleDateFormat> formatterList = new ArrayList<>();
		formatterList.add(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
		formatterList.add(new SimpleDateFormat("dd-MM-yyyy HH:mm"));
		formatterList.add(new SimpleDateFormat("dd-MM-yyyy"));

		for (SimpleDateFormat pattern : formatterList){
			try{
				Date nuevaFecha = new Date(pattern.parse(valorFecha).getTime());
				return nuevaFecha;
			}catch (ParseException e) {
			}
		}

		return null;
	}
}