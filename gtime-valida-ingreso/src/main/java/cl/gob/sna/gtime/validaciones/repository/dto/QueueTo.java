package cl.gob.sna.gtime.validaciones.repository.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class QueueTo implements Serializable {

    private static final long serialVersionUID = -4156778472854906013L;
    private String XML;
    private String login;
    private String tipoDocumento;
    private String tipoAccion;
    private String nombreUsuario;
    private String numeroReferencia;

}
