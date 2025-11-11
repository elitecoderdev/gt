
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
    "nacion-id",
    "valor-id",
    "codigo-pais",
    "nombres",
    "direccion",
    "comuna",
    "nombre",
    "tipo-id"
})
public class Participacion implements Serializable{

    private static final long serialVersionUID = -2091430993547258342L;
	@JsonProperty("nacion-id")
    private String nacionId;
    @JsonProperty("valor-id")
    private String valorId;
    @JsonProperty("codigo-pais")
    private String codigoPais;
    @JsonProperty("nombres")
    private String nombres;
    @JsonProperty("direccion")
    private String direccion;
    @JsonProperty("comuna")
    private String comuna;
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("tipo-id")
    private String tipoId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Participacion() {
    }

    /**
     * 
     * @param tipoId
     * @param valorId
     * @param direccion
     * @param comuna
     * @param nacionId
     * @param nombre
     * @param codigoPais
     * @param nombres
     */
    public Participacion(String nacionId, String valorId, String codigoPais, String nombres, String direccion, String comuna, String nombre, String tipoId) {
        super();
        this.nacionId = nacionId;
        this.valorId = valorId;
        this.codigoPais = codigoPais;
        this.nombres = nombres;
        this.direccion = direccion;
        this.comuna = comuna;
        this.nombre = nombre;
        this.tipoId = tipoId;
    }

    @JsonProperty("nacion-id")
    public String getNacionId() {
        return nacionId;
    }

    @JsonProperty("nacion-id")
    public void setNacionId(String nacionId) {
        this.nacionId = nacionId;
    }

    public Participacion withNacionId(String nacionId) {
        this.nacionId = nacionId;
        return this;
    }

    @JsonProperty("valor-id")
    public String getValorId() {
        return valorId;
    }

    @JsonProperty("valor-id")
    public void setValorId(String valorId) {
        this.valorId = valorId;
    }

    public Participacion withValorId(String valorId) {
        this.valorId = valorId;
        return this;
    }

    @JsonProperty("codigo-pais")
    public String getCodigoPais() {
        return codigoPais;
    }

    @JsonProperty("codigo-pais")
    public void setCodigoPais(String codigoPais) {
        this.codigoPais = codigoPais;
    }

    public Participacion withCodigoPais(String codigoPais) {
        this.codigoPais = codigoPais;
        return this;
    }

    @JsonProperty("nombres")
    public String getNombres() {
        return nombres;
    }

    @JsonProperty("nombres")
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Participacion withNombres(String nombres) {
        this.nombres = nombres;
        return this;
    }

    @JsonProperty("direccion")
    public String getDireccion() {
        return direccion;
    }

    @JsonProperty("direccion")
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Participacion withDireccion(String direccion) {
        this.direccion = direccion;
        return this;
    }

    @JsonProperty("comuna")
    public String getComuna() {
        return comuna;
    }

    @JsonProperty("comuna")
    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public Participacion withComuna(String comuna) {
        this.comuna = comuna;
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

    public Participacion withNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    @JsonProperty("tipo-id")
    public String getTipoId() {
        return tipoId;
    }

    @JsonProperty("tipo-id")
    public void setTipoId(String tipoId) {
        this.tipoId = tipoId;
    }

    public Participacion withTipoId(String tipoId) {
        this.tipoId = tipoId;
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

    public Participacion withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Participacion.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("nacionId");
        sb.append('=');
        sb.append(((this.nacionId == null)?"<null>":this.nacionId));
        sb.append(',');
        sb.append("valorId");
        sb.append('=');
        sb.append(((this.valorId == null)?"<null>":this.valorId));
        sb.append(',');
        sb.append("codigoPais");
        sb.append('=');
        sb.append(((this.codigoPais == null)?"<null>":this.codigoPais));
        sb.append(',');
        sb.append("nombres");
        sb.append('=');
        sb.append(((this.nombres == null)?"<null>":this.nombres));
        sb.append(',');
        sb.append("direccion");
        sb.append('=');
        sb.append(((this.direccion == null)?"<null>":this.direccion));
        sb.append(',');
        sb.append("comuna");
        sb.append('=');
        sb.append(((this.comuna == null)?"<null>":this.comuna));
        sb.append(',');
        sb.append("nombre");
        sb.append('=');
        sb.append(((this.nombre == null)?"<null>":this.nombre));
        sb.append(',');
        sb.append("tipoId");
        sb.append('=');
        sb.append(((this.tipoId == null)?"<null>":this.tipoId));
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
