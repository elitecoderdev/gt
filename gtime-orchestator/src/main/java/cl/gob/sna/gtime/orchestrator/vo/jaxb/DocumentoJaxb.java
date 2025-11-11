package cl.gob.sna.gtime.orchestrator.vo.jaxb;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement(name = "Documento")
@XmlAccessorType(XmlAccessType.PROPERTY)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DocumentoJaxb implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1063695835668830421L;
    
	
	@XmlElement(name = "fechaEncolamiento")
	private String fechaEncolamiento;	
	
	@XmlElement(name = "nroEncabezado")
	private String nroEncabezado;
	
	@XmlElement(name = "pwd")
	private String pwd;
	
	@XmlElement(name = "user")
	private String user;
	
	@XmlElement(name = "xml")
	private String xml;
	
	@XmlElement(name = "valor-declarado")
	private String valorDeclarado;
	
	@XmlElement(name = "moneda-valor")
	private String monedaValor;

	private String parcial;
	
	@XmlElement(name = "user-host-origen")
	private String userHostOrigen;
	
	@XmlElement(name = "id-sender")
	private String idSender;
	
	@XmlElement(name = "numero-referencia")
	private String numeroReferencia;
	
	@XmlElement(name = "unidad-peso")
	private String unidadPeso;
	
	@XmlElement(name = "tipo-operacion")
	private String tipoOperacion;

	@XmlElement(name = "total-peso")
	private String totalPeso;
	
	@XmlElement(name = "tipo-accion")
	private String tipoAccion;

	@XmlElement(name = "total-item")
	private String totalItem;

	@XmlElement(name = "total-bultos")
	private String totalBultos;
	
	@XmlElement(name = "total-volumen")
	private String totalVolumen;
	
	@XmlElement(name = "unidad-volumen")
	private String unidadVolumen;
	
	@XmlElement(name = "login")
	private String login;
	
	private String tipo;
	private String version;

	@XmlElement(name = "Observaciones")
	private ObservacionesJaxb observaciones;
		
	@XmlElement(name = "Fechas")
	private FechasJaxb fechas;

	@XmlElement(name = "Locaciones")
	private LocacionesJaxb locaciones;

	@XmlElement(name = "Participaciones")
	private ParticipacionesJaxb participaciones;

	@XmlElement(name = "Referencias")
	private ReferenciasJaxb referencias;
	
	@XmlElement(name = "Items")
	private ItemsJaxb items;

	@XmlElement(name = "Cargos")
	private CargosJaxb cargos;

	@XmlElement(name = "Transbordos")
	private TransbordosJaxb transbordos;
	
	@XmlElement(name = "VistosBuenos")
	private VistosBuenosJaxb vistosBuenos;
}
