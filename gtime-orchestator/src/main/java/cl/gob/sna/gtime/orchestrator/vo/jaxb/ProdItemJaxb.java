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
@XmlRootElement(name = "proditem")
public class ProdItemJaxb implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2489099098576709277L;
	
	@XmlElement(name = "descripcion")
	private String descripcion;
	
	@XmlElement(name = "unidad-medida")
    private String unidadMedida;
	
	@XmlElement(name = "cantidad")
    private String cantidad;
    
	@XmlElement(name = "valor-declarado")
    private String valorDeclarado;
	
	@XmlElement(name = "moneda")
    private String moneda;

   
}
