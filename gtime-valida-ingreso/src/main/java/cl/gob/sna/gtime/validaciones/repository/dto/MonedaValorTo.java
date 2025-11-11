package cl.gob.sna.gtime.validaciones.repository.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class MonedaValorTo implements Serializable{
 
	private static final long serialVersionUID = -8388097717682941138L;
	public MonedaValorTo() {
 	}

	private String codigo;
	private String nombre;
 
}