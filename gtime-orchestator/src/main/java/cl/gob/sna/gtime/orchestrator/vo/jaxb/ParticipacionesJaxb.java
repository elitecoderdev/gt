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
public class ParticipacionesJaxb implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5718609625170577162L;
	@JacksonXmlElementWrapper(useWrapping = false)
	@XmlElement(name = "participacion")
	private List<ParticipacionJaxb> participacion;

}
