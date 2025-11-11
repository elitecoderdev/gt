package cl.gob.sna.gtime.processor.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class InfoPersona implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = -8388097717682941138L;
	public InfoPersona() {
		//
	}

	private Long idPersona;
	private String emisor;
	private Long idDocBase;

}