package cl.gob.sna.gtime.validaciones.repository.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class DocumentoBaseTo implements Serializable{
 
 
	private static final long serialVersionUID = 8124464050858755784L;
	private Integer id;
	private String tipoId;
	private String numeroId;
	private String docEstados;
	private String viaTransporte;
	private String tipoManifiesto;
	private String numeroExterno;	
 
}