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
public class ReferenciasJaxb implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1664494411566054803L;
	@JacksonXmlElementWrapper(useWrapping = false)
	@XmlElement(name = "referencia")
	private List<ReferenciaJaxb> referencia;

}
