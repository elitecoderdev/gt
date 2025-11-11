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
@XmlRootElement(name = "referencia")
public class ReferenciaJaxb implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5371001200740291783L;
	@XmlElement(name = "tipo-id-emisor")
	private String tipoIDEmisor;
	
	private String observaciones;
	
	@XmlElement(name = "valor-id-emisor")
	private String valorIDEmisor;
	
	@XmlElement(name = "nac-id-emisor")
	private String nacIDEmisor;
	
	@XmlElement(name = "tipo-documento")
	private String tipoDocumento;
	private String emisor;
	
	@XmlElement(name = "tipo-referencia")
	private String tipoReferencia;
	
	@XmlElement(name = "numero")
	private String numero;
	
	@XmlElement(name = "fecha")
	private String fecha;

}
