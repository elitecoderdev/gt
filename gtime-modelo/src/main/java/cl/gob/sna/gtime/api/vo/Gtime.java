
package cl.gob.sna.gtime.api.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.*;

import lombok.ToString;


@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "fechaEncolamiento","nroEncabezado","pwd","xml","user", "parcial", "tipo", "version", "valor-declarado", "moneda-valor", "user-host-origen", "id-sender",
		"numero-referencia", "unidad-peso", "tipo-operacion", "total-peso", "tipo-accion", "total-item", "total-bultos",
		"total-volumen", "unidad-volumen", "login", "Observaciones", "Fechas", "Locaciones", "Participaciones",
		"Referencias", "Items", "Cargos", "Transbordos", "VistosBuenos" })
public class Gtime  implements Serializable{
	private static final long serialVersionUID = -565110020746720241L;

	@JsonProperty("loginDigitador")
	private String loginDigitador;
	@JsonProperty("idPersistencia")
	private String idPersistencia;
	@JsonProperty("tipoDocumento")
	private String tipoDocumento;
	@JsonProperty("estadoGtime")
	private String estadoGtime;
	@JsonProperty("idPayload")
	private long idPayload;
	@JsonProperty("startTime")
	private long startTime;
	@JsonProperty("fechaDesencolamiento")
	private String fechaDesencolamiento;	
	@JsonProperty("fechaEncolamiento")
	private String fechaEncolamiento;		
	@JsonProperty("nroEncabezado")
	private String nroEncabezado;	
	@JsonProperty("pwd")
	private String pwd;	
	@JsonProperty("xml")
	private String xml;
	@JsonProperty("user")
	private String user;
	@JsonProperty("parcial")
	private String parcial;
	@JsonProperty("tipo")
	private String tipo;
	@JsonProperty("version")
	private String version;
	@JsonProperty("valor-declarado")
	private String valorDeclarado;
	@JsonProperty("moneda-valor")
	private String monedaValor;
	@JsonProperty("user-host-origen")
	private String userHostOrigen;
	@JsonProperty("id-sender")
	private String idSender;
	@JsonProperty("numero-referencia")
	private String numeroReferencia;
	@JsonProperty("unidad-peso")
	private String unidadPeso;
	@JsonProperty("tipo-operacion")
	@JsonAnySetter
	private String tipoOperacion;
	@JsonProperty("total-peso")
	private String totalPeso;
	@JsonProperty("tipo-accion")
	private String tipoAccion;
	@JsonProperty("total-item")
	private String totalItem;
	@JsonProperty("total-bultos")
	private String totalBultos;
	@JsonProperty("total-volumen")
	private String totalVolumen;
	@JsonProperty("unidad-volumen")
	private String unidadVolumen;
	@JsonProperty("login")
	private String login;

	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	@JsonProperty("Observaciones")
	private Observaciones observaciones;

	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	@JsonProperty("Fechas")
	private Fechas fechas;
	
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	@JsonProperty("Locaciones")
	private Locaciones locaciones;
	
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	@JsonProperty("Participaciones")
	private Participaciones participaciones;
	
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	@JsonProperty("Referencias")
	private Referencias referencias;
	
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	@JsonProperty("Items")
	private Items items;
	
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	@JsonProperty("Cargos")
	private Cargos cargos;
	
	@JsonProperty("Transbordos")
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	private Transbordos transbordos;
	
	@JsonProperty("VistosBuenos")
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	private Vbs vistosBuenos;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public Gtime() {
	}

	/**
	 * 
	 * @param tipo
	 * @param vistosBuenos
	 * @param parcial
	 * @param tipoAccion
	 * @param referencias
	 * @param cargos
	 * @param login
	 * @param totalBultos
	 * @param numeroReferencia
	 * @param valorDeclarado
	 * @param fechas
	 * @param monedaValor
	 * @param idSender
	 * @param totalPeso
	 * @param tipoOperacion
	 * @param totalItem
	 * @param userHostOrigen
	 * @param version
	 * @param locaciones
	 * @param participaciones
	 * @param transbordos
	 * @param totalVolumen
	 * @param unidadPeso
	 * @param observaciones
	 * @param items
	 * @param unidadVolumen
	 */
	public Gtime(String parcial, String tipo, String version, String valorDeclarado, String monedaValor,
			String userHostOrigen, String idSender, String numeroReferencia, String unidadPeso, String tipoOperacion,
			String totalPeso, String tipoAccion, String totalItem, String totalBultos, String totalVolumen,
			String unidadVolumen, String login, Observaciones observaciones, Fechas fechas,
			Locaciones locaciones, Participaciones participaciones, Referencias referencias,
			Items items, Cargos cargos, Transbordos transbordos, Vbs vistosBuenos) {
		super();
		this.parcial = parcial;
		this.tipo = tipo;
		this.version = version;
		this.valorDeclarado = valorDeclarado;
		this.monedaValor = monedaValor;
		this.userHostOrigen = userHostOrigen;
		this.idSender = idSender;
		this.numeroReferencia = numeroReferencia;
		this.unidadPeso = unidadPeso;
		this.tipoOperacion = tipoOperacion;
		this.totalPeso = totalPeso;
		this.tipoAccion = tipoAccion;
		this.totalItem = totalItem;
		this.totalBultos = totalBultos;
		this.totalVolumen = totalVolumen;
		this.unidadVolumen = unidadVolumen;
		this.login = login;
		this.observaciones = observaciones;
		this.fechas = fechas;
		this.locaciones = locaciones;
		this.participaciones = participaciones;
		this.referencias = referencias;
		this.items = items;
		this.cargos = cargos;
		this.transbordos = transbordos;
		this.vistosBuenos = vistosBuenos;
	}

	@JsonProperty("parcial")
	public String getParcial() {
		return parcial;
	}

	@JsonProperty("parcial")
	public void setParcial(String parcial) {
		this.parcial = parcial;
	}

	public Gtime withParcial(String parcial) {
		this.parcial = parcial;
		return this;
	}

	@JsonProperty("tipo")
	public String getTipo() {
		return tipo;
	}

	@JsonProperty("tipo")
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Gtime withTipo(String tipo) {
		this.tipo = tipo;
		return this;
	}

	@JsonProperty("version")
	public String getVersion() {
		return version;
	}

	@JsonProperty("version")
	public void setVersion(String version) {
		this.version = version;
	}

	public Gtime withVersion(String version) {
		this.version = version;
		return this;
	}

	@JsonProperty("valor-declarado")
	public String getValorDeclarado() {
		return valorDeclarado;
	}

	@JsonProperty("valor-declarado")
	public void setValorDeclarado(String valorDeclarado) {
		this.valorDeclarado = valorDeclarado;
	}

	public Gtime withValorDeclarado(String valorDeclarado) {
		this.valorDeclarado = valorDeclarado;
		return this;
	}

	@JsonProperty("moneda-valor")
	public String getMonedaValor() {
		return monedaValor;
	}

	@JsonProperty("moneda-valor")
	public void setMonedaValor(String monedaValor) {
		this.monedaValor = monedaValor;
	}

	public Gtime withMonedaValor(String monedaValor) {
		this.monedaValor = monedaValor;
		return this;
	}

	@JsonProperty("user-host-origen")
	public String getUserHostOrigen() {
		return userHostOrigen;
	}

	@JsonProperty("user-host-origen")
	public void setUserHostOrigen(String userHostOrigen) {
		this.userHostOrigen = userHostOrigen;
	}

	public Gtime withUserHostOrigen(String userHostOrigen) {
		this.userHostOrigen = userHostOrigen;
		return this;
	}

	@JsonProperty("id-sender")
	public String getIdSender() {
		return idSender;
	}

	@JsonProperty("id-sender")
	public void setIdSender(String idSender) {
		this.idSender = idSender;
	}

	public Gtime withIdSender(String idSender) {
		this.idSender = idSender;
		return this;
	}

	@JsonProperty("numero-referencia")
	public String getNumeroReferencia() {
		return numeroReferencia;
	}

	@JsonProperty("numero-referencia")
	public void setNumeroReferencia(String numeroReferencia) {
		this.numeroReferencia = numeroReferencia;
	}

	public Gtime withNumeroReferencia(String numeroReferencia) {
		this.numeroReferencia = numeroReferencia;
		return this;
	}

	@JsonProperty("unidad-peso")
	public String getUnidadPeso() {
		return unidadPeso;
	}

	@JsonProperty("unidad-peso")
	public void setUnidadPeso(String unidadPeso) {
		this.unidadPeso = unidadPeso;
	}

	public Gtime withUnidadPeso(String unidadPeso) {
		this.unidadPeso = unidadPeso;
		return this;
	}

	@JsonProperty("tipo-operacion")
	public String getTipoOperacion() {
		return tipoOperacion;
	}

	@JsonProperty("tipo-operacion")
	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public Gtime withTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
		return this;
	}

	@JsonProperty("total-peso")
	public String getTotalPeso() {
		return totalPeso;
	}

	@JsonProperty("total-peso")
	public void setTotalPeso(String totalPeso) {
		this.totalPeso = totalPeso;
	}

	public Gtime withTotalPeso(String totalPeso) {
		this.totalPeso = totalPeso;
		return this;
	}

	@JsonProperty("tipo-accion")
	public String getTipoAccion() {
		return tipoAccion;
	}

	@JsonProperty("tipo-accion")
	public void setTipoAccion(String tipoAccion) {
		this.tipoAccion = tipoAccion;
	}

	public Gtime withTipoAccion(String tipoAccion) {
		this.tipoAccion = tipoAccion;
		return this;
	}

	@JsonProperty("total-item")
	public String getTotalItem() {
		return totalItem;
	}

	@JsonProperty("total-item")
	public void setTotalItem(String totalItem) {
		this.totalItem = totalItem;
	}

	public Gtime withTotalItem(String totalItem) {
		this.totalItem = totalItem;
		return this;
	}

	@JsonProperty("total-bultos")
	public String getTotalBultos() {
		return totalBultos;
	}

	@JsonProperty("total-bultos")
	public void setTotalBultos(String totalBultos) {
		this.totalBultos = totalBultos;
	}

	public Gtime withTotalBultos(String totalBultos) {
		this.totalBultos = totalBultos;
		return this;
	}

	@JsonProperty("total-volumen")
	public String getTotalVolumen() {
		return totalVolumen;
	}

	@JsonProperty("total-volumen")
	public void setTotalVolumen(String totalVolumen) {
		this.totalVolumen = totalVolumen;
	}

	public Gtime withTotalVolumen(String totalVolumen) {
		this.totalVolumen = totalVolumen;
		return this;
	}

	@JsonProperty("unidad-volumen")
	public String getUnidadVolumen() {
		return unidadVolumen;
	}

	@JsonProperty("unidad-volumen")
	public void setUnidadVolumen(String unidadVolumen) {
		this.unidadVolumen = unidadVolumen;
	}

	public Gtime withUnidadVolumen(String unidadVolumen) {
		this.unidadVolumen = unidadVolumen;
		return this;
	}

	@JsonProperty("login")
	public String getLogin() {
		return login;
	}

	@JsonProperty("login")
	public void setLogin(String login) {
		this.login = login;
	}

	public Gtime withLogin(String login) {
		this.login = login;
		return this;
	}

	@JsonProperty("Observaciones")
	public Observaciones getObservaciones() {
		return observaciones;
	}

	@JsonProperty("Observaciones")
	public void setObservaciones(Observaciones observaciones) {
		this.observaciones = observaciones;
	}

	public Gtime withObservaciones(Observaciones observaciones) {
		this.observaciones = observaciones;
		return this;
	}

	@JsonProperty("Fechas")
	public Fechas getFechas() {
		return fechas;
	}
	
	@JsonProperty("Fechas")
	public void setFechas(Fechas fechas) {
		this.fechas = fechas;
	}


	@JsonProperty("Locaciones")
	public Locaciones getLocaciones() {
		return locaciones;
	}

	@JsonProperty("Locaciones")
	public void setLocaciones(Locaciones locaciones) {
		this.locaciones = locaciones;
	}

	

	@JsonProperty("Participaciones")
	public Participaciones getParticipaciones() {
		return participaciones;
	}

	@JsonProperty("Participaciones")
	public void setParticipaciones(Participaciones participaciones) {
		this.participaciones = participaciones;
	}
	
	@JsonProperty("Referencias")
	public Referencias getReferencias() {
		return referencias;
	}

	@JsonProperty("Referencias")
	public void setReferencias(Referencias referencias) {
		this.referencias = referencias;
	}

	
	@JsonProperty("Items")
	public Items getItems() {
		return items;
	}

	@JsonProperty("Items")
	public void setItems(Items items) {
		this.items = items;
	}


	@JsonProperty("Cargos")
	public Cargos getCargos() {
		return cargos;
	}

	@JsonProperty("Cargos")
	public void setCargos(Cargos cargos) {
		this.cargos = cargos;
	}


	@JsonProperty("Transbordos")
	public Transbordos getTransbordos() {
		return transbordos;
	}

	@JsonProperty("Transbordos")
	public void setTransbordos(Transbordos transbordos) {
		this.transbordos = transbordos;
	}	

	@JsonProperty("VistosBuenos")
	public Vbs getVistosBuenos() {
		return vistosBuenos;
	}

	@JsonProperty("VistosBuenos")
	public void setVistosBuenos(Vbs vistosBuenos) {
		this.vistosBuenos = vistosBuenos;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public Gtime withAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
		return this;
	}


	
	@JsonProperty("user")
	public String getUser() {
		return user;
	}
	
	@JsonProperty("user")
	public void setUser(String user) {
		this.user = user;
	}
	
	@JsonProperty("xml")
	public String getXml() {
		return xml;
	}
	
	@JsonProperty("xml")
	public void setXml(String xml) {
		this.xml = xml;
	}

	@JsonProperty("fechaEncolamiento")
	public String getFechaEncolamiento() {
		return fechaEncolamiento;
	}

	@JsonProperty("fechaEncolamiento")
	public void setFechaEncolamiento(String fechaEncolamiento) {
		this.fechaEncolamiento = fechaEncolamiento;
	}

	@JsonProperty("nroEncabezado")
	public String getNroEncabezado() {
		return nroEncabezado;
	}

	@JsonProperty("nroEncabezado")
	public void setNroEncabezado(String nroEncabezado) {
		this.nroEncabezado = nroEncabezado;
	}

	@JsonProperty("pwd")
	public String getPwd() {
		return pwd;
	}

	@JsonProperty("pwd")
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@JsonProperty("fechaDesencolamiento")
	public String getFechaDesencolamiento() {
		return fechaDesencolamiento;
	}

	@JsonProperty("fechaDesencolamiento")
	public void setFechaDesencolamiento(String fechaDesencolamiento) {
		this.fechaDesencolamiento = fechaDesencolamiento;
	}

	@JsonProperty("startTime")
	public long getStartTime() {
		return startTime;
	}
	@JsonProperty("startTime")
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	@JsonProperty("idPayload")
	public long getIdPayload() {
		return idPayload;
	}
	@JsonProperty("idPayload")
	public void setIdPayload(long idPayload) {
		this.idPayload = idPayload;
	}

	/**
	 * Execution time (ms)
	 * @return
	 */
	public long executionTime(){
		long endTime = System.currentTimeMillis();
		return (endTime - this.getStartTime()) ;
	}


	public String executionTimeMsg(){
		String msg = "M:Tiempo ejecución " + executionTime() + " [ms]";
		return msg;
	}

	@JsonProperty("estadoGtime")
	public String getEstadoGtime() {
		return estadoGtime;
	}

	@JsonProperty("estadoGtime")
	public void setEstadoGtime(String estadoGtime) {
		this.estadoGtime = estadoGtime;
	}
	@JsonProperty("tipoDocumento")
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	@JsonProperty("tipoDocumento")
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	@JsonProperty("idPersistencia")
	public String getIdPersistencia() {
		return idPersistencia;
	}
	@JsonProperty("idPersistencia")
	public void setIdPersistencia(String idPersistencia) {
		this.idPersistencia = idPersistencia;
	}

	@JsonProperty("loginDigitador")
	public String getLoginDigitador() {
		return loginDigitador;
	}
	@JsonProperty("loginDigitador")
	public void setLoginDigitador(String loginDigitador) {
		this.loginDigitador = loginDigitador;
	}
}
