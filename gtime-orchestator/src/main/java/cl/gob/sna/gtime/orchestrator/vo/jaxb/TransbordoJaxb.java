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
@XmlRootElement(name = "transbordo")
public class TransbordoJaxb implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8797420774437072294L;
	@XmlElement(name = "cod-lugar")
	private String codLugar;
	@XmlElement(name = "descripcion-lugar")
	private String descripcion;
	@XmlElement(name = "fecha-arribo")
	private String fechaArribo;

}
