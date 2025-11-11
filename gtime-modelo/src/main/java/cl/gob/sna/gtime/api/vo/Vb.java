
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
    "tipo-vb"
})
public class Vb implements Serializable{

    private static final long serialVersionUID = -2326989764994206586L;
	@JsonProperty("tipo-vb")
    private String tipoVb;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Vb() {
    }

    /**
     * 
     * @param tipoVb
     */
    public Vb(String tipoVb) {
        super();
        this.tipoVb = tipoVb;
    }

    @JsonProperty("tipo-vb")
    public String getTipoVb() {
        return tipoVb;
    }

    @JsonProperty("tipo-vb")
    public void setTipoVb(String tipoVb) {
        this.tipoVb = tipoVb;
    }

    public Vb withTipoVb(String tipoVb) {
        this.tipoVb = tipoVb;
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

    public Vb withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Vb.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("tipoVb");
        sb.append('=');
        sb.append(((this.tipoVb == null)?"<null>":this.tipoVb));
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
