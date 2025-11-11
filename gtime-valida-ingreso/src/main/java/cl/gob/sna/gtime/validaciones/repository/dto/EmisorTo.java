package cl.gob.sna.gtime.validaciones.repository.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class EmisorTo implements Serializable{
 
	private static final long serialVersionUID = -8388097717682941138L;
	public EmisorTo() {
 	}

	private String tipoId;
	private String valorId;
	private String nacion;
	private String nombre;
 
}