package cl.gob.sna.gtime.orchestrator.vo.jaxb;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data @AllArgsConstructor @NoArgsConstructor @ToString
@XmlRootElement(name = "locacion")
public class LocacionJaxb implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2309426116551590131L;
	
	@XmlElement(name = "descripcion")
	private String descripcion;
	
	@XmlElement(name = "nombre")
	private String nombre;
	
	@XmlElement(name = "codigo")
	private String codigo;

}
