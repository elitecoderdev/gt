package cl.gob.sna.gtime.processor.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString

public class Imagen implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = -6136171082777435988L;

	public Imagen(Long idDocBase, long id, String xml) {
		this.setIdDocBase(idDocBase);
		this.setId(id);
		this.setXml(xml);
		this.setTipo("XML");
	}

	private Long idDocBase;
	private Long id;
	private String xml;
	private String tipo;

}
