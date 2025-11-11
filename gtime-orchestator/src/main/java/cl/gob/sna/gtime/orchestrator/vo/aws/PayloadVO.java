package cl.gob.sna.gtime.orchestrator.vo.aws;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id","mensaje" })
public class PayloadVO implements Serializable {
    private static final long serialVersionUID = 184896024568786442L;

    @JsonProperty("id")
    String id;
    @JsonProperty("mensaje")
    String mensaje;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("mensaje")
    public String getMensaje() {
        return mensaje;
    }

    @JsonProperty("mensaje")
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public PayloadVO(String id, String mensaje) {
        super();
        this.id = id;
        this.mensaje = mensaje;
    }

    public PayloadVO() {
    }
}
