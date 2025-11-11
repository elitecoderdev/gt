package cl.gob.sna.gtime.processor.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class DocEstadoUtil {
    String docEstado;
    String docEstadoMSG;
    String docEstadoLogin;

    public DocEstadoUtil(String docEstado, String docEstadoMSG, String docEstadoLogin) {
        this.docEstado = docEstado;
        this.docEstadoMSG = docEstadoMSG;
        this.docEstadoLogin = docEstadoLogin;
    }
}
