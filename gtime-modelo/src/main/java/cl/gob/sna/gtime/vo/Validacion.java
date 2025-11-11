package cl.gob.sna.gtime.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Validacion implements Serializable {

	private static final long serialVersionUID = 1132566384025923226L;

	private TipoValidacion tipo;

	private boolean valido;
	private Date fechaValidacion;

	private String texto;
	
	@SuppressWarnings("unused")
	private Validacion() {
		
	}

	public Validacion(TipoValidacion tipo) {
		this.tipo = tipo;
		this.valido = false;
		this.texto = null;
	}

	public Validacion(TipoValidacion tipo, boolean valido) {
		this.tipo = tipo;
		this.valido = valido;
		this.texto = tipo.getTexto();
	}
	
	public Validacion(TipoValidacion tipo, boolean valido, Date fechaValidacion) {
		this.tipo = tipo;
		this.valido = valido;
		this.fechaValidacion = fechaValidacion;
		this.texto = tipo.getTexto();
	}
}
