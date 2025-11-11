package cl.gob.sna.gtime.cron.reencolador.jpa.model.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name="QUEUE", schema = "ADMMENSAJES")
@Data
@AllArgsConstructor
public class Queue implements Serializable {
	@Id
	@Column(name="ID")
	private BigInteger id;
	
	@Column(name="ENQ")
	private Date enq;
	
	@Column(name="DEQ")
	private Date deq;
	
	@Column(name="PROC")
	private Date proc;

	@Column(name="DEQTIME")
	private Date deqtime;

	@Column(name="DEQLIMIT")
	private Date deqlimit;

	@Column(name="STATUS")
	private Integer status;

	@Column(name="RETRY")
	private Integer retry;

	@Column(name="TYPE")
	private String type;

	@Column(name="MSG")
	private String msg;

	@Column(name="TIPO_ACCION")
	private String tipoAccion;

	@Column(name="ESTADO")
	private String estado;

	@Column(name="TIPO_DOCUMENTO")
	private String tipoDocumento;
}