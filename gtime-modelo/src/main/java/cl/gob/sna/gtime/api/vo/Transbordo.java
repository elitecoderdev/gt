
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
    "cod-lugar",
    "descripcion-lugar",
    "fecha-arribo"
})
public class Transbordo implements Serializable{

    private static final long serialVersionUID = 4338270228114802685L;
	@JsonProperty("cod-lugar")
    private String codLugar;
    @JsonProperty("descripcion-lugar")
    private String descripcionLugar;
    @JsonProperty("fecha-arribo")
    private String fechaArribo;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Transbordo() {
    }

    /**
     * 
     * @param descripcionLugar
     * @param codLugar
     * @param fechaArribo
     */
    public Transbordo(String codLugar, String descripcionLugar, String fechaArribo) {
        super();
        this.codLugar = codLugar;
        this.descripcionLugar = descripcionLugar;
        this.fechaArribo = fechaArribo;
    }

    @JsonProperty("cod-lugar")
    public String getCodLugar() {
        return codLugar;
    }

    @JsonProperty("cod-lugar")
    public void setCodLugar(String codLugar) {
        this.codLugar = codLugar;
    }

    public Transbordo withCodLugar(String codLugar) {
        this.codLugar = codLugar;
        return this;
    }

    @JsonProperty("descripcion-lugar")
    public String getDescripcionLugar() {
        return descripcionLugar;
    }

    @JsonProperty("descripcion-lugar")
    public void setDescripcionLugar(String descripcionLugar) {
        this.descripcionLugar = descripcionLugar;
    }

    public Transbordo withDescripcionLugar(String descripcionLugar) {
        this.descripcionLugar = descripcionLugar;
        return this;
    }

    @JsonProperty("fecha-arribo")
    public String getFechaArribo() {
        return fechaArribo;
    }

    @JsonProperty("fecha-arribo")
    public void setFechaArribo(String fechaArribo) {
        this.fechaArribo = fechaArribo;
    }

    public Transbordo withFechaArribo(String fechaArribo) {
        this.fechaArribo = fechaArribo;
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

    public Transbordo withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Transbordo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("codLugar");
        sb.append('=');
        sb.append(((this.codLugar == null)?"<null>":this.codLugar));
        sb.append(',');
        sb.append("descripcionLugar");
        sb.append('=');
        sb.append(((this.descripcionLugar == null)?"<null>":this.descripcionLugar));
        sb.append(',');
        sb.append("fechaArribo");
        sb.append('=');
        sb.append(((this.fechaArribo == null)?"<null>":this.fechaArribo));
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
