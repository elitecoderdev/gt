package cl.gob.sna.gtime.validaciones.repository.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class LocacionTo implements Serializable{
 
	private static final long serialVersionUID = -8388097717682941138L;
	
	public LocacionTo() {
 	}

 	private Integer locacion;
	private String descripcion;
	private String codigo;
}