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

@XmlRootElement(name = "participacion")
public class ParticipacionJaxb implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7977560583827055018L;
	
	@XmlElement(name = "nacion-id")
	private String nacionID;
	
	@XmlElement(name = "valor-id")	
	private String valorID;
	
	@XmlElement(name = "codigo-pais")
	private String codigoPais;
	
	@XmlElement(name = "nombres")
	private String nombres;
	
	@XmlElement(name = "direccion")
	private String direccion;
	
	@XmlElement(name = "comuna")
	private String comuna;
	
	@XmlElement(name = "nombre")
	private String nombre;
	
	@XmlElement(name = "tipo-id")
	private String tipoID;

}
