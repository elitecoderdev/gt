package cl.gob.sna.gtime.processor.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Participacion {
    private Long idDocBase;
    private int idPersonaParticipante;
    private String nacionId;
    private String valorId;
    private String codigoPais;
    private String nombres;
    private String direccion;
    private String comuna;
    private String nombre;
    private String tipoId;

    public Participacion(Long idDocBase, int idPersonaParticipante, String nacionId,
                         String valorId, String codigoPais, String nombres, String direccion,
                         String comuna, String nombre, String tipoId) {
        this.idDocBase = idDocBase;
        this.idPersonaParticipante = idPersonaParticipante;
        this.nacionId = nacionId;
        this.valorId = valorId;
        this.codigoPais = codigoPais;
        this.nombres = nombres;
        this.direccion = direccion;
        this.comuna = comuna;
        this.nombre = nombre;
        this.tipoId = tipoId;
    }
}
