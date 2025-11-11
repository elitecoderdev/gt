package cl.gob.sna.gtime.orchestrator.vo.jaxb;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data @AllArgsConstructor @NoArgsConstructor @ToString
@XmlRootElement(name = "fecha")

public class FechaJaxb implements Serializable {

	private static final long serialVersionUID = -8291533176135747209L;
	@XmlElement(name = "valor")
	private String valor;
	
	@XmlElement(name = "nombre")
	private String nombre;

}
