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
import oracle.jdbc.driver.OracleDriver;
import oracle.jdbc.OracleTypes;

public class HiloProcesamiento extends Thread {
	private int        instancia;
	private String     archivoLog;
	private String     archivoMonitor;
	private Connection conn;
	private int        sleep;
	private String     activo;
	private int        ciclos;
	private int        sleepCiclosMS;
	
	public HiloProcesamiento(int instancia) {
		super();		
		
		this.instancia = instancia;
		
		Properties props = new Properties();
		try {
			FileInputStream fis = new FileInputStream("conf/configuracion.properties");
			props.load(fis);
			fis.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}			

		String bdDriver      = props.getProperty("driver", "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=pilo-scan.aduana.cl)(PORT=1521))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=aries)))");
		String bdUsuario     = props.getProperty("usuario","DOCUMENTOS");
		String bdPassword    = props.getProperty("password", "********");
		String cfLog         = props.getProperty("log", "log/procesa_gtime.log");
		String cfMonitor     = props.getProperty("monitor", "log/monitor.txt");
		String cfSleep       = props.getProperty("sleep", "30");
		String cfSleepCiclos = props.getProperty("sleepCiclosMS", "10000");
		String cfCiclos      = props.getProperty("ciclosMonitor", "5");
		String cfActivo      = props.getProperty("activo", "N");
		
		String strArchivoLog = cfLog;
		if (cfLog == null || (cfLog != null && cfLog.trim().equals(""))) {
			cfLog = "log/procesa_gtime.log";
		}
		archivoLog = cfLog.replaceAll(".log", "_[" + this.instancia + "].log");
		
		if (cfMonitor == null || cfMonitor != null && cfMonitor.trim().equals("")) {
			cfMonitor = "log/monitor.txt";
		}
		archivoMonitor = cfMonitor;
		
		if(cfActivo != null && cfActivo.equals("S")) {
			escribeLog("Instanciando hilo...", "INFO");
			try {
				escribeLog("Cargando tiempo de sleep al no tener mensajes que procesar...", "INFO");
				this.sleep = Integer.parseInt(cfSleep);
				escribeLog("Tiempo sleep: " + this.sleep + " segundo(s).", "INFO");
			} catch(Exception e) {
				this.sleep = 30;
				escribeLog("Error al cargar tiempo de sleep. Se establece por defecto " + this.sleep + " segundos.", "WARNING");
			}

			try {
				escribeLog("Cargando ciclos para archivo de Monitor...", "INFO");
				this.ciclos = Integer.parseInt(cfCiclos);
				escribeLog("Ciclos de monitor: " + this.ciclos + " ciclo(s).", "INFO");
			} catch(Exception e) {
				this.ciclos = 5;
				escribeLog("Error al cargar ciclos del Monitor. Se establece por defecto " + this.ciclos + " ciclos.", "WARNING");
			}

			try {
				escribeLog("Cargando tiempo de sleep por cada ciclo...", "INFO");
				this.sleepCiclosMS = Integer.parseInt(cfSleepCiclos);
				escribeLog("Tiempo de sleep: " + this.sleepCiclosMS + " milisegundos.", "INFO");
			} catch(Exception e) {
				this.sleepCiclosMS = 100000;
				escribeLog("Error al cargar tiempo de sleep. Se establece por defecto " + this.sleepCiclosMS + " milisegundos.", "WARNING");
			}
			
			try {
				escribeLog("Creando conexion a la BD...", "INFO");
				DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
				conn = DriverManager.getConnection (bdDriver, bdUsuario, bdPassword);
				escribeLog("Conexion a la BD creada.", "INFO");
			} catch(Exception e) {
				escribeLog("Excepcion al crear conexion a la BD: " + e.getMessage(), "ERROR");
			}
		}
	}
	
	public void run() {
		procesa();
	}

	public void finalizar() {
		escribeLog("Destruyendo instancia...", "INFO");
		if(conn != null) {
			try {
				escribeLog("Desconectando de la BD...", "INFO");
				conn.close();
				escribeLog("Desconectado de la BD.", "INFO");
			} catch(Exception e) {
				escribeLog("Excepcion al desconectar de la BD:" + e.getMessage(), "ERROR");
			}
		}
		//currentThread().destroy();			
	}
	
	private void procesa() {
		int ciclo = 0;
		do {
			if(conn != null) {
				CallableStatement cs = null;
				boolean excepcionSQL = false;
				boolean colaVacia = false;
				String idCola = null;
				try {
					cs = conn.prepareCall("{call DOCUMENTOS.NEW_VALIDA_GTIME.DESENCOLAR_MENSAJE(?)}");
					cs.registerOutParameter(1, OracleTypes.INTEGER);
					cs.execute();
					idCola = cs.getString(1);
				} catch(Exception e) {
					excepcionSQL = true;
					escribeLog("Excepcion al llamar Procedure DESENCOLAR_MENSAJE:" + e.getMessage(), "ERROR");
				} finally {
					if (cs != null) try { cs.close(); } catch(Exception _e) {}
				}
				if (idCola == null) {
					colaVacia = true;
				} else {
					escribeLog("Desencolado mensaje ID: " + idCola, "INFO");
					String procesado = null;
					String estado = null;
					String mensaje = null;
					String numeroExterno = null;
					String idDocumento = null;
					escribeLog("Inicia validacion ID " + idCola + "...", "INFO");
					try {
						cs = conn.prepareCall("{call DOCUMENTOS.NEW_VALIDA_GTIME.VALIDAR_MENSAJE_QUEUE(?, ?, ?, ?, ?, ?)}");
						cs.setLong(1, Long.parseLong(idCola));
						cs.registerOutParameter(2, OracleTypes.INTEGER);
						cs.registerOutParameter(3, OracleTypes.VARCHAR);
						cs.registerOutParameter(4, OracleTypes.VARCHAR);
						cs.registerOutParameter(5, OracleTypes.VARCHAR);
						cs.registerOutParameter(6, OracleTypes.INTEGER);
						cs.execute();
						procesado = cs.getString(2);
						estado = cs.getString(3);
						mensaje = cs.getString(4);
						numeroExterno = cs.getString(5);
						idDocumento = cs.getString(6);
					} catch(Exception e) {
						excepcionSQL = true;
						escribeLog("Excepcion al llamar Procedure VALIDAR_MENSAJE_QUEUE:" + e.getMessage(), "ERROR");
					} finally {
						if (cs != null) try { cs.close(); } catch(Exception _e) {}
					}
					escribeLog("Termino validacion ID " + idCola + ": Numero Externo {" + numeroExterno + "}, Procesado {" + procesado + "}, Estado {" + estado + "}, idDocumento {" + idDocumento + "}, Mensaje {" + mensaje + "}", "INFO");
				}
				if (colaVacia) {
					escribeLog("Sin mensajes para desencolar. Durmiendo " + this.sleep + " segundo(s).", "INFO");
					try {
						Thread.sleep(sleep * 1000);
					} catch(Exception e) {
					}
					escribeLog("Despertando...", "INFO");
				}
				if (!excepcionSQL) {
					actualizaActivo();
				} else {
					this.activo = "N";
				}
			} else {
				this.activo = "N";
			}
			ciclo++;
			if(ciclo == ciclos) {
				escribeMonitor("Instancia " + this.instancia + " activa luego de " + ciclos + " ciclos");
				if(this.sleepCiclosMS > 0) {
					escribeLog("Durmiendo " + this.sleepCiclosMS + " milisegundo(s) luego de " + ciclos + " ciclos...", "INFO");
					try {
						Thread.sleep(this.sleepCiclosMS);
					} catch(Exception e) {
					}
					escribeLog("Despertando...", "INFO");
				}
				ciclo = 0;
			}
		} while (this.activo.equals("S"));
		finalizar();
	}
	
	private void actualizaActivo() {
		Properties props = new Properties();
		try {
			FileInputStream fis = new FileInputStream("conf/configuracion.properties");
			props.load(fis);
			fis.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}			

		this.activo = props.getProperty("activo", "N");
		
		if (this.activo == null || (this.activo != null && this.activo.trim().equals(""))) {
			this.activo = "N";
		}
	}
	
	private void escribeLog(String mensaje, String nivel) {
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
			
			String datos = sdfl.format(calendar.getTime()) + "[hilo:" + this.instancia + "]" + "[" + nivel + "]: " + mensaje + "\n";
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
	
	private void escribeMonitor(String mensaje) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		try {
			File file = new File(archivoMonitor);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			
			String datos = mensaje + "\n";
			bw.write(datos);
		} catch (IOException e) {
			System.out.println("Error al escribir monitor: " + e.getMessage());
		} finally {
			try {
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			} catch (IOException ex) {
				System.out.println("Error al cerrar archivo de monitor: " + ex.getMessage());
			}
		}
	}
}