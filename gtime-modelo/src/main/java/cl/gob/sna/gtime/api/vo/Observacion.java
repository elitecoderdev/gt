
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
    "nombre",
    "contenido"
})
public class Observacion implements Serializable{

    private static final long serialVersionUID = -237478846289999906L;
	@JsonProperty("nombre")
    private String nombre;
    @JsonProperty("contenido")
    private String contenido;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Observacion() {
    }

    /**
     * 
     * @param contenido
     * @param nombre
     */
    public Observacion(String nombre, String contenido) {
        super();
        this.nombre = nombre;
        this.contenido = contenido;
    }

    @JsonProperty("nombre")
    public String getNombre() {
        return nombre;
    }

    @JsonProperty("nombre")
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Observacion withNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    @JsonProperty("contenido")
    public String getContenido() {
        return contenido;
    }

    @JsonProperty("contenido")
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Observacion withContenido(String contenido) {
        this.contenido = contenido;
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

    public Observacion withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Observacion.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("nombre");
        sb.append('=');
        sb.append(((this.nombre == null)?"<null>":this.nombre));
        sb.append(',');
        sb.append("contenido");
        sb.append('=');
        sb.append(((this.contenido == null)?"<null>":this.contenido));
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
