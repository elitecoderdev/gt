package cl.gob.sna.gtime.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cl.gob.sna.gtime.api.vo.Gtime;
import lombok.Data;
import lombok.ToString;
@ToString
@Data
public class DocumentoResponse implements Serializable {

 
	private static final long serialVersionUID = -766205181319265733L;
	private String nroDocumento;
	private List<Validacion> listValidaciones;	
	private Gtime gtime;	
	private String user;
	private String xml;
	private List<Persistencia> listEstadoPersistencia;
	private String estadoFinal;
	private Date fem;
	private String tipoManifiesto;
	private String viaTransporte;
	private boolean contieneFEM;
}
