package cl.gob.sna.gtime.orchestrator.vo.jaxb;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data @AllArgsConstructor @NoArgsConstructor @ToString
@XmlRootElement(name = "item")
public class ItemJaxb implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7133746449545884049L;

	@XmlElement(name = "marcas")
	private String marcas;
	
	@XmlElement(name = "descripcion")
	private String descripcion;
	
	@XmlElement(name = "numero-item")
	private String numeroItem;

	@XmlElement(name = "peso-bruto")
	private String pesoBruto;
	
	@XmlElement(name = "cantidad")
	private String cantidad;
	
	@XmlElement(name = "unidad-peso")
	private String unidadPeso;
	
	@XmlElement(name = "tipo-bulto")
	private String tipoBulto;
	
	@XmlElement(name = "ProdItem")
	private ProdItemsJaxb prodItem;

}
