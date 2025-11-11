package cl.gob.sna.gtime.queue.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Propiedad implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8877919341186620063L;
	private String nombre;
	private String valor;
}
