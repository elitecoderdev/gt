
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
    "observaciones",
    "emisor",
    "tipo-id-emisor",
    "valor-id-emisor",
    "nac-id-emisor",
    "tipo-documento",
    "tipo-referencia",
    "numero",
    "fecha"
})
public class Referencia implements Serializable{

    private static final long serialVersionUID = 5318615112708014504L;
	@JsonProperty("observaciones")
    private String observaciones;
    @JsonProperty("emisor")
    private String emisor;
    @JsonProperty("tipo-id-emisor")
    private String tipoIdEmisor;
    @JsonProperty("valor-id-emisor")
    private String valorIdEmisor;
    @JsonProperty("nac-id-emisor")
    private String nacIdEmisor;
    @JsonProperty("tipo-documento")
    private String tipoDocumento;
    @JsonProperty("tipo-referencia")
    private String tipoReferencia;
    @JsonProperty("numero")
    private String numero;
    @JsonProperty("fecha")
    private String fecha;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Referencia() {
    }

    /**
     * 
     * @param tipoDocumento
     * @param fecha
     * @param valorIdEmisor
     * @param nacIdEmisor
     * @param numero
     * @param observaciones
     * @param tipoIdEmisor
     * @param tipoReferencia
     * @param emisor
     */
    public Referencia(String observaciones, String emisor, String tipoIdEmisor, String valorIdEmisor, String nacIdEmisor, String tipoDocumento, String tipoReferencia, String numero, String fecha) {
        super();
        this.observaciones = observaciones;
        this.emisor = emisor;
        this.tipoIdEmisor = tipoIdEmisor;
        this.valorIdEmisor = valorIdEmisor;
        this.nacIdEmisor = nacIdEmisor;
        this.tipoDocumento = tipoDocumento;
        this.tipoReferencia = tipoReferencia;
        this.numero = numero;
        this.fecha = fecha;
    }

    @JsonProperty("observaciones")
    public String getObservaciones() {
        return observaciones;
    }

    @JsonProperty("observaciones")
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Referencia withObservaciones(String observaciones) {
        this.observaciones = observaciones;
        return this;
    }

    @JsonProperty("emisor")
    public String getEmisor() {
        return emisor;
    }

    @JsonProperty("emisor")
    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public Referencia withEmisor(String emisor) {
        this.emisor = emisor;
        return this;
    }

    @JsonProperty("tipo-id-emisor")
    public String getTipoIdEmisor() {
        return tipoIdEmisor;
    }

    @JsonProperty("tipo-id-emisor")
    public void setTipoIdEmisor(String tipoIdEmisor) {
        this.tipoIdEmisor = tipoIdEmisor;
    }

    public Referencia withTipoIdEmisor(String tipoIdEmisor) {
        this.tipoIdEmisor = tipoIdEmisor;
        return this;
    }

    @JsonProperty("valor-id-emisor")
    public String getValorIdEmisor() {
        return valorIdEmisor;
    }

    @JsonProperty("valor-id-emisor")
    public void setValorIdEmisor(String valorIdEmisor) {
        this.valorIdEmisor = valorIdEmisor;
    }

    public Referencia withValorIdEmisor(String valorIdEmisor) {
        this.valorIdEmisor = valorIdEmisor;
        return this;
    }

    @JsonProperty("nac-id-emisor")
    public String getNacIdEmisor() {
        return nacIdEmisor;
    }

    @JsonProperty("nac-id-emisor")
    public void setNacIdEmisor(String nacIdEmisor) {
        this.nacIdEmisor = nacIdEmisor;
    }

    public Referencia withNacIdEmisor(String nacIdEmisor) {
        this.nacIdEmisor = nacIdEmisor;
        return this;
    }

    @JsonProperty("tipo-documento")
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    @JsonProperty("tipo-documento")
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Referencia withTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
        return this;
    }

    @JsonProperty("tipo-referencia")
    public String getTipoReferencia() {
        return tipoReferencia;
    }

    @JsonProperty("tipo-referencia")
    public void setTipoReferencia(String tipoReferencia) {
        this.tipoReferencia = tipoReferencia;
    }

    public Referencia withTipoReferencia(String tipoReferencia) {
        this.tipoReferencia = tipoReferencia;
        return this;
    }

    @JsonProperty("numero")
    public String getNumero() {
        return numero;
    }

    @JsonProperty("numero")
    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Referencia withNumero(String numero) {
        this.numero = numero;
        return this;
    }

    @JsonProperty("fecha")
    public String getFecha() {
        return fecha;
    }

    @JsonProperty("fecha")
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Referencia withFecha(String fecha) {
        this.fecha = fecha;
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

    public Referencia withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Referencia.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("observaciones");
        sb.append('=');
        sb.append(((this.observaciones == null)?"<null>":this.observaciones));
        sb.append(',');
        sb.append("emisor");
        sb.append('=');
        sb.append(((this.emisor == null)?"<null>":this.emisor));
        sb.append(',');
        sb.append("tipoIdEmisor");
        sb.append('=');
        sb.append(((this.tipoIdEmisor == null)?"<null>":this.tipoIdEmisor));
        sb.append(',');
        sb.append("valorIdEmisor");
        sb.append('=');
        sb.append(((this.valorIdEmisor == null)?"<null>":this.valorIdEmisor));
        sb.append(',');
        sb.append("nacIdEmisor");
        sb.append('=');
        sb.append(((this.nacIdEmisor == null)?"<null>":this.nacIdEmisor));
        sb.append(',');
        sb.append("tipoDocumento");
        sb.append('=');
        sb.append(((this.tipoDocumento == null)?"<null>":this.tipoDocumento));
        sb.append(',');
        sb.append("tipoReferencia");
        sb.append('=');
        sb.append(((this.tipoReferencia == null)?"<null>":this.tipoReferencia));
        sb.append(',');
        sb.append("numero");
        sb.append('=');
        sb.append(((this.numero == null)?"<null>":this.numero));
        sb.append(',');
        sb.append("fecha");
        sb.append('=');
        sb.append(((this.fecha == null)?"<null>":this.fecha));
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
