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
@XmlRootElement(name = "observacion")

public class ObservacionJaxb implements Serializable {

	private static final long serialVersionUID = -2434280260574694760L;

	@XmlElement(name = "nombre")
	private String nombre;

	@XmlElement(name = "contenido")
	private String contenido;

}
