package cl.gob.sna.gtime.orchestrator.vo.jaxb;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LocacionesJaxb implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3152714379266592783L;
	@JacksonXmlElementWrapper(useWrapping = false)
	@XmlElement(name = "locacion")
	private List<LocacionJaxb> locacion;

}
