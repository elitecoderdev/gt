package cl.gob.sna.gtime.processor.vo;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Locacion {
    private Long idDocBase;
    private int idLocacion;
    private String codigo;
    private String nombre;
    private String descripcion;

    public Locacion(Long idDocBase, int idLocacion, String codigo, String nombre, String descripcion) {
        this.idDocBase = idDocBase;
        this.idLocacion = idLocacion;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
}
