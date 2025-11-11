package cl.gob.sna.gtime.orchestrator.vo.jaxb;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@XmlRootElement(name = "Transbordos")
public class TransbordosJaxb implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7533465548782480501L;
	@JacksonXmlElementWrapper(useWrapping = false)
	@XmlElement(name = "transbordo")
	private List<TransbordoJaxb> transbordo;

}
