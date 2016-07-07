package es.caib.gusite.micromodel;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Clase Indexación por solr pendiente. Bean que define indexación por solr pendiente. Modela la tabla de BBDD
 * GUS_SOLRPD
 * 
 * @author Indra
 */

@Entity
@Table(name = "GUS_SOLRPD")
public class SolrPendiente implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	
	@XmlAttribute
	@Id
	@SequenceGenerator(name = "GUS_TEMAS_ID_GENERATOR", sequenceName = "GUS_SEQSOLR", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_TEMAS_ID_GENERATOR")
	@Column(name = "SLP_ID")
	private Long id;

	/** tipo field */
	@Column(name = "SLP_TIPO")
	private String tipo;

	/** elemento field */
	@Column(name = "SLP_IDELEM")
	private Long idElem;

	/** accion field */
	@Column(name = "SLP_ACCION")
	private Integer accion;

	/** fecha creación field */
	@Column(name = "SLP_FECCRE")
	private Date fechaCreacion;

	/** fecha indexación field */
	@Column(name = "SLP_FECIDX")
	private Date fechaIndexacion;

	/** resultado field */
	@Column(name = "SLP_RESULT")
	private Integer resultado;
	
	/** error field */
	@Column(name = "SLP_TXTERR")
	private String mensajeError;
	
	/** archivo field */
	@Column(name = "SLP_IDARCHIVO")
	private Long idArchivo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Long getIdElem() {
		return idElem;
	}

	public void setIdElem(Long idElem) {
		this.idElem = idElem;
	}

	public Integer getAccion() {
		return accion;
	}

	public void setAccion(Integer accion) {
		this.accion = accion;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaIndexacion() {
		return fechaIndexacion;
	}

	public void setFechaIndexacion(Date fechaIndexacion) {
		this.fechaIndexacion = fechaIndexacion;
	}

	public Integer getResultado() {
		return resultado;
	}

	public void setResultado(Integer resultado) {
		this.resultado = resultado;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public Long getIdArchivo() {
		return idArchivo;
	}

	public void setIdArchivo(Long idArchivo) {
		this.idArchivo = idArchivo;
	}

	
}

