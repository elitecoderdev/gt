
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
    "marcas",
    "descripcion",
    "numero-item",
    "peso-bruto",
    "cantidad",
    "unidad-peso",
    "tipo-bulto",
    "ProdItem"
})
public class Item implements Serializable{

    private static final long serialVersionUID = -8261517098156437353L;
	@JsonProperty("marcas")
    private String marcas;
    @JsonProperty("descripcion")
    private String descripcion;
    @JsonProperty("numero-item")
    private String numeroItem;
    @JsonProperty("peso-bruto")
    private String pesoBruto;
    @JsonProperty("cantidad")
    private String cantidad;
    @JsonProperty("unidad-peso")
    private String unidadPeso;
    @JsonProperty("tipo-bulto")
    private String tipoBulto;
    @JsonProperty("ProdItem")
    private ProdItems prodItem;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Item() {
    }

    /**
     * 
     * @param descripcion
     * @param prodItem
     * @param unidadPeso
     * @param pesoBruto
     * @param tipoBulto
     * @param marcas
     * @param cantidad
     * @param numeroItem
     */
    public Item(String marcas, String descripcion, String numeroItem, String pesoBruto, String cantidad, String unidadPeso, String tipoBulto, ProdItems prodItem) {
        super();
        this.marcas = marcas;
        this.descripcion = descripcion;
        this.numeroItem = numeroItem;
        this.pesoBruto = pesoBruto;
        this.cantidad = cantidad;
        this.unidadPeso = unidadPeso;
        this.tipoBulto = tipoBulto;
        this.prodItem = prodItem;
    }

    @JsonProperty("marcas")
    public String getMarcas() {
        return marcas;
    }

    @JsonProperty("marcas")
    public void setMarcas(String marcas) {
        this.marcas = marcas;
    }

    public Item withMarcas(String marcas) {
        this.marcas = marcas;
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

    public Item withDescripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    @JsonProperty("numero-item")
    public String getNumeroItem() {
        return numeroItem;
    }

    @JsonProperty("numero-item")
    public void setNumeroItem(String numeroItem) {
        this.numeroItem = numeroItem;
    }

    public Item withNumeroItem(String numeroItem) {
        this.numeroItem = numeroItem;
        return this;
    }

    @JsonProperty("peso-bruto")
    public String getPesoBruto() {
        return pesoBruto;
    }

    @JsonProperty("peso-bruto")
    public void setPesoBruto(String pesoBruto) {
        this.pesoBruto = pesoBruto;
    }

    public Item withPesoBruto(String pesoBruto) {
        this.pesoBruto = pesoBruto;
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

    public Item withCantidad(String cantidad) {
        this.cantidad = cantidad;
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

    public Item withUnidadPeso(String unidadPeso) {
        this.unidadPeso = unidadPeso;
        return this;
    }

    @JsonProperty("tipo-bulto")
    public String getTipoBulto() {
        return tipoBulto;
    }

    @JsonProperty("tipo-bulto")
    public void setTipoBulto(String tipoBulto) {
        this.tipoBulto = tipoBulto;
    }

    public Item withTipoBulto(String tipoBulto) {
        this.tipoBulto = tipoBulto;
        return this;
    }

    @JsonProperty("ProdItem")
    public ProdItems getProdItem() {
        return prodItem;
    }

    @JsonProperty("ProdItem")
    public void setProdItem(ProdItems prodItem) {
        this.prodItem = prodItem;
    }


    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Item withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Item.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("marcas");
        sb.append('=');
        sb.append(((this.marcas == null)?"<null>":this.marcas));
        sb.append(',');
        sb.append("descripcion");
        sb.append('=');
        sb.append(((this.descripcion == null)?"<null>":this.descripcion));
        sb.append(',');
        sb.append("numeroItem");
        sb.append('=');
        sb.append(((this.numeroItem == null)?"<null>":this.numeroItem));
        sb.append(',');
        sb.append("pesoBruto");
        sb.append('=');
        sb.append(((this.pesoBruto == null)?"<null>":this.pesoBruto));
        sb.append(',');
        sb.append("cantidad");
        sb.append('=');
        sb.append(((this.cantidad == null)?"<null>":this.cantidad));
        sb.append(',');
        sb.append("unidadPeso");
        sb.append('=');
        sb.append(((this.unidadPeso == null)?"<null>":this.unidadPeso));
        sb.append(',');
        sb.append("tipoBulto");
        sb.append('=');
        sb.append(((this.tipoBulto == null)?"<null>":this.tipoBulto));
        sb.append(',');
        sb.append("prodItem");
        sb.append('=');
        sb.append(((this.prodItem == null)?"<null>":this.prodItem));
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
