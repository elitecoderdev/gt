
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
    "descripcion",
    "unidad-medida",
    "cantidad",
    "valor-declarado",
    "moneda"
})
public class Proditem implements Serializable{

    private static final long serialVersionUID = -1389693997460169331L;
	@JsonProperty("descripcion")
    private String descripcion;
    @JsonProperty("unidad-medida")
    private String unidadMedida;
    @JsonProperty("cantidad")
    private String cantidad;
    @JsonProperty("valor-declarado")
    private String valorDeclarado;
    @JsonProperty("moneda")
    private String moneda;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Proditem() {
    }

    /**
     * 
     * @param descripcion
     * @param unidadMedida
     * @param valorDeclarado
     * @param moneda
     * @param cantidad
     */
    public Proditem(String descripcion, String unidadMedida, String cantidad, String valorDeclarado, String moneda) {
        super();
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
        this.cantidad = cantidad;
        this.valorDeclarado = valorDeclarado;
        this.moneda = moneda;
    }

    @JsonProperty("descripcion")
    public String getDescripcion() {
        return descripcion;
    }

    @JsonProperty("descripcion")
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Proditem withDescripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    @JsonProperty("unidad-medida")
    public String getUnidadMedida() {
        return unidadMedida;
    }

    @JsonProperty("unidad-medida")
    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Proditem withUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
        return this;
    }

    @JsonProperty("cantidad")
    public String getCantidad() {
        return cantidad;
    }

    @JsonProperty("cantidad")
    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public Proditem withCantidad(String cantidad) {
        this.cantidad = cantidad;
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

    public Proditem withValorDeclarado(String valorDeclarado) {
        this.valorDeclarado = valorDeclarado;
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

    public Proditem withMoneda(String moneda) {
        this.moneda = moneda;
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

    public Proditem withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Proditem.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("descripcion");
        sb.append('=');
        sb.append(((this.descripcion == null)?"<null>":this.descripcion));
        sb.append(',');
        sb.append("unidadMedida");
        sb.append('=');
        sb.append(((this.unidadMedida == null)?"<null>":this.unidadMedida));
        sb.append(',');
        sb.append("cantidad");
        sb.append('=');
        sb.append(((this.cantidad == null)?"<null>":this.cantidad));
        sb.append(',');
        sb.append("valorDeclarado");
        sb.append('=');
        sb.append(((this.valorDeclarado == null)?"<null>":this.valorDeclarado));
        sb.append(',');
        sb.append("moneda");
        sb.append('=');
        sb.append(((this.moneda == null)?"<null>":this.moneda));
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
