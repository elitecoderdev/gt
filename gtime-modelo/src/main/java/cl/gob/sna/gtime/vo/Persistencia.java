package cl.gob.sna.gtime.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Persistencia implements Serializable {


	/**
	 *
	 */
	private static final long serialVersionUID = -250877073268420695L;

	private TipoPersistencia tipo;

	private boolean valido;

	private String schemaTable;

	private String primariKey;

	@SuppressWarnings("unused")
	private Persistencia() {

	}

	public Persistencia(TipoPersistencia tipo) {
		this.tipo = tipo;
		this.valido = false;
	}

	public Persistencia(TipoPersistencia tipo, boolean valido) {
		this.tipo = tipo;
		this.valido = valido;
	}

	public Persistencia(TipoPersistencia tipo, boolean valido, String schema, String key){
		this.tipo = tipo;
		this.valido = valido;
		this.schemaTable = schema;
		this.primariKey = key;
	}

}
