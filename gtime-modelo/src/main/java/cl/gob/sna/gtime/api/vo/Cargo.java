
package cl.gob.sna.gtime.api.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "monto",
    "descripcion",
    "tipo-cargo",
    "moneda",
    "cond-pago",
    "observaciones",
    "tarifa"
})
public class Cargo implements Serializable{

    private static final long serialVersionUID = 8465842207529752887L;
	@JsonProperty("monto")
    private String monto;
    @JsonProperty("descripcion")
    private String descripcion;
    @JsonProperty("tipo-cargo")
    private String tipoCargo;
    @JsonProperty("moneda")
    private String moneda;
    @JsonProperty("cond-pago")
    private String condPago;
    @JsonProperty("observaciones")
    private String observaciones;
    @JsonProperty("tarifa")
    private String tarifa;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Cargo() {
    }

    /**
     * 
     * @param descripcion
     * @param tarifa
     * @param monto
     * @param tipoCargo
     * @param observaciones
     * @param moneda
     * @param condPago
     */
    public Cargo(String monto, String descripcion, String tipoCargo, String moneda, String condPago, String observaciones, String tarifa) {
        super();
        this.monto = monto;
        this.descripcion = descripcion;
        this.tipoCargo = tipoCargo;
        this.moneda = moneda;
        this.condPago = condPago;
        this.observaciones = observaciones;
        this.tarifa = tarifa;
    }

    @JsonProperty("monto")
    public String getMonto() {
        return monto;
    }

    @JsonProperty("monto")
    public void setMonto(String monto) {
        this.monto = monto;
    }

    public Cargo withMonto(String monto) {
        this.monto = monto;
        return this;
    }

    @JsonProperty("descripcion")
    public String getDescripcion() {
        return descripcion;
    }

    @JsonProperty("descripcion")
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Cargo withDescripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    @JsonProperty("tipo-cargo")
    public String getTipoCargo() {
        return tipoCargo;
    }

    @JsonProperty("tipo-cargo")
    public void setTipoCargo(String tipoCargo) {
        this.tipoCargo = tipoCargo;
    }

    public Cargo withTipoCargo(String tipoCargo) {
        this.tipoCargo = tipoCargo;
        return this;
    }

    @JsonProperty("moneda")
    public String getMoneda() {
        return moneda;
    }

    @JsonProperty("moneda")
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public Cargo withMoneda(String moneda) {
        this.moneda = moneda;
        return this;
    }

    @JsonProperty("cond-pago")
    public String getCondPago() {
        return condPago;
    }

    @JsonProperty("cond-pago")
    public void setCondPago(String condPago) {
        this.condPago = condPago;
    }

    public Cargo withCondPago(String condPago) {
        this.condPago = condPago;
        return this;
    }

    @JsonProperty("observaciones")
    public String getObservaciones() {
        return observaciones;
    }

    @JsonProperty("observaciones")
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Cargo withObservaciones(String observaciones) {
        this.observaciones = observaciones;
        return this;
    }

    @JsonProperty("tarifa")
    public String getTarifa() {
        return tarifa;
    }

    @JsonProperty("tarifa")
    public void setTarifa(String tarifa) {
        this.tarifa = tarifa;
    }

    public Cargo withTarifa(String tarifa) {
        this.tarifa = tarifa;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Cargo withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Cargo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("monto");
        sb.append('=');
        sb.append(((this.monto == null)?"<null>":this.monto));
        sb.append(',');
        sb.append("descripcion");
        sb.append('=');
        sb.append(((this.descripcion == null)?"<null>":this.descripcion));
        sb.append(',');
        sb.append("tipoCargo");
        sb.append('=');
        sb.append(((this.tipoCargo == null)?"<null>":this.tipoCargo));
        sb.append(',');
        sb.append("moneda");
        sb.append('=');
        sb.append(((this.moneda == null)?"<null>":this.moneda));
        sb.append(',');
        sb.append("condPago");
        sb.append('=');
        sb.append(((this.condPago == null)?"<null>":this.condPago));
        sb.append(',');
        sb.append("observaciones");
        sb.append('=');
        sb.append(((this.observaciones == null)?"<null>":this.observaciones));
        sb.append(',');
        sb.append("tarifa");
        sb.append('=');
        sb.append(((this.tarifa == null)?"<null>":this.tarifa));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
