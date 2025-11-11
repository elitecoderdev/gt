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
@XmlRootElement(name = "vb")
public class VistoBuenoJaxb implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8797420774437072294L;
	@XmlElement(name = "tipo-vb")
	private String tipoVb;

}
