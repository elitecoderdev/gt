package cl.gob.sna.gtime.validaciones.repository.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class UnidadMedidaTo implements Serializable{
 
	private static final long serialVersionUID = -8388097717682941138L;
	public UnidadMedidaTo() {
 	}

 	private String codigo;
	private String descripcion;

}