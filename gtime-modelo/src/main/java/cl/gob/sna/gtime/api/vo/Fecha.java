
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
    "valor",
    "nombre"
})
public class Fecha implements Serializable{

    private static final long serialVersionUID = 3835105924388280295L;
	@JsonProperty("valor")
    private String valor;
    @JsonProperty("nombre")
    private String nombre;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Fecha() {
    }

    /**
     * 
     * @param valor
     * @param nombre
     */
    public Fecha(String valor, String nombre) {
        super();
        this.valor = valor;
        this.nombre = nombre;
    }

    @JsonProperty("valor")
    public String getValor() {
        return valor;
    }

    @JsonProperty("valor")
    public void setValor(String valor) {
        this.valor = valor;
    }

    public Fecha withValor(String valor) {
        this.valor = valor;
        return this;
    }

    @JsonProperty("nombre")
    public String getNombre() {
        return nombre;
    }

    @JsonProperty("nombre")
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Fecha withNombre(String nombre) {
        this.nombre = nombre;
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

    public Fecha withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Fecha.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("valor");
        sb.append('=');
        sb.append(((this.valor == null)?"<null>":this.valor));
        sb.append(',');
        sb.append("nombre");
        sb.append('=');
        sb.append(((this.nombre == null)?"<null>":this.nombre));
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
