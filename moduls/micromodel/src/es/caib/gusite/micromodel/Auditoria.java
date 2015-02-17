package es.caib.gusite.micromodel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Clase Auditoria. Bean que define una Auditoria. Modela la tabla de BBDD
 * GUS_MENU
 * 
 * @author Brujula
 */
@Entity
@Table(name = "GUS_AUDITORIA")
public class Auditoria {

	public static int CREAR = 0;
	public static int MODIFICAR = 1;
	public static int ELIMINAR = 2;

	@Id
	@SequenceGenerator(name = "GUS_AUDT_ID_GENERATOR", sequenceName = "GUS_SEQMEN", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_AUDT_ID_GENERATOR")
	@Column(name = "AUD_CODI")
	private Long id;

	@Column(name = "AUD_MICCOD")
	private Long idmicrosite;

	@Column(name = "AUD_USUARI")
	private String usuario;

	@Column(name = "AUD_FECHA")
	private Date fecha;

	@Column(name = "AUD_OPERACION")
	private int operacion;

	@Column(name = "AUD_ENTIDAD")
	private String entidad;

	@Column(name = "AUD_INFORMACION")
	private byte[] informacion;

	@Column(name = "AUD_ID_ENTIDAD")
	private String idEntidad;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdmicrosite() {
		return this.idmicrosite;
	}

	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getOperacion() {
		return this.operacion;
	}

	public void setOperacion(int operacion) {
		this.operacion = operacion;
	}

	public String getEntidad() {
		return this.entidad;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public byte[] getInformacion() {
		return this.informacion;
	}

	public void setInformacion(byte[] informacion) {
		this.informacion = informacion;
	}

	public String getIdEntidad() {
		return this.idEntidad;
	}

	public void setIdEntidad(String idEntidad) {
		this.idEntidad = idEntidad;
	}

	public String estado(int estado) {
		if (estado == CREAR) {
			return "CREAR";
		} else if (estado == MODIFICAR) {
			return "MODIFICAR";
		} else if (estado == ELIMINAR) {
			return "ELIMINAR";
		} else {
			return "";
		}
	}

}
