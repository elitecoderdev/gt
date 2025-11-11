package cl.gob.sna.gtime.queue.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QueueRecord implements Serializable {

	public static final String RECHAZADO_EI = "Rechazado EI";
	public static final String RECHAZADO = "Rechazado";
	public static final String ACEPTADO = "Aceptado";

	private static final long serialVersionUID = 6502392601310555692L;

	private String estado;
	private Date fechaEncolado;
	private Date fechaDeseconlado;
	private Date fechaFinProcesamiento;
	private String xml;
	private String versionTipodoc;
	private String tipoDocumento;
	private String numeroReferencia;
	private String numeroEncabezado;
	private String nombreUsuario;
	private String idSender;
	private String identificacion;
	private String clave;
	private long idPayload;
	private String idPersistencia;
	private List<Propiedad> listaPropiedades = new ArrayList<Propiedad>();
}
