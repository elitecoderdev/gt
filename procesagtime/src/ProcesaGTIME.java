import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

public class ProcesaGTIME {
	
	public static void main(String[] args) {
		Properties props = new Properties();
		try {
			FileInputStream fis = new FileInputStream("conf/configuracion.properties");
			props.load(fis);
			fis.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		String cfThreads  = props.getProperty("threads", "1");
		String cfActivo   = props.getProperty("activo", "N");
		String cfLog      = props.getProperty("log", "log/procesa_gtime.log");
		if (cfLog == null || (cfLog != null && cfLog.trim().equals(""))) {
			cfLog = "log/procesa_gtime.log";
		}
		String archivoLog = cfLog.replaceAll(".log", "_[principal].log");
		
		escribeLog("Inicio de ejecucion del proceso principal", "INFO", archivoLog);
		escribeLog("Para detener, ir el proceso se debe ir al archivo de configuracion y cambiar valor del atributo 'activo' distinto a 'S'.", "INFO", archivoLog);
		
		int threads = 0;
		if (cfThreads != null) {
			try {
				threads = Integer.parseInt(cfThreads);
			} catch(Exception e) {
			}
		}
		
		if (cfActivo != null && cfActivo.equals("S") && threads > 0) {
			escribeLog("Se crean " + threads + " hilo(s).", "INFO", archivoLog);
			for(int x = 0; x < threads; x++) {
				Thread th = new Thread(new HiloProcesamiento(x + 1), "th" + (x + 1));
				th.start();
			}
		} else {
			if (cfActivo != null && !cfActivo.equals("S")) {
				escribeLog("El proceso se no se encuentra activado en el archivo de configuracion.", "INFO", archivoLog);
			}
			if (threads == 0) {
				escribeLog("No hay threads configuradas para iniciar.", "INFO", archivoLog);
			}
		}
		escribeLog("Fin de ejecucion del proceso principal.", "INFO", archivoLog);
	}

	private static void escribeLog(String mensaje, String nivel, String archivoLog) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfl = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("America/Santiago"));
		
		String _archivoLog = archivoLog.replaceAll(".log", "_" + sdf.format(calendar.getTime()) + ".log");
		
		try {
			File file = new File(_archivoLog);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			
			String datos = sdfl.format(calendar.getTime()) + "[principal]" + "[" + nivel + "]: " + mensaje + "\n";
			bw.write(datos);
			System.out.print(datos);
		} catch (IOException e) {
			System.out.println("Error al escribir log: " + e.getMessage());
		} finally {
			try {
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			} catch (IOException ex) {
				System.out.println("Error al cerrar archivo de log: " + ex.getMessage());
			}
		}
	}
}