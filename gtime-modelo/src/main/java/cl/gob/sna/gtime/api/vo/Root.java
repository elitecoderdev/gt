package cl.gob.sna.gtime.api.vo;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Root {
    @JsonProperty("Documento")
    public Gtime gtime;

    public Gtime getDocumento() {
        return gtime;
    }
    @JsonAnySetter
    public void setDocumento(Gtime documento) {
        this.gtime = documento;
    }
}
