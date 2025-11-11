package cl.gob.sna.gtime.orchestrator.vo.jaxb;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@XmlRootElement(name = "cargo")
public class CargoJaxb implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8797420774437072294L;
	@XmlElement(name = "monto")
	private String monto;
	@XmlElement(name = "descripcion")
	private String descripcion;
	@XmlElement(name = "tipo-cargo")
	private String tipoCargo;
	@XmlElement(name = "moneda")
	private String moneda;
	@XmlElement(name = "cond-pago")
	private String condPago;
	@XmlElement(name = "observaciones")
	private String observaciones;
	@XmlElement(name = "tarifa")
	private String tarifa;

}
