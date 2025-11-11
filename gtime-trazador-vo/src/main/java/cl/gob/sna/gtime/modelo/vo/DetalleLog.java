package cl.gob.sna.gtime.modelo.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DetalleLog implements Serializable {

	private static final long serialVersionUID = -766205181319265733L;
	private String login;
	private String tipoDocumento;			
	private String nroEncabezado;
	private String version;
	private String nroDocumento;
	private String evento;	
	
}
