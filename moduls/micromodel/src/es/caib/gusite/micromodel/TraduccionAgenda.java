package es.caib.gusite.micromodel;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Clase TraduccionAgenda. Encapsula los datos que pueden tener valor en
 * diferentes idiomas del objeto Agenda.
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_AGEIDI")
public class TraduccionAgenda implements Traduccion {

	private static final long serialVersionUID = -8998196005824126269L;

	@XmlElement
	@EmbeddedId
	private TraduccionAgendaPK id;

	@XmlAttribute
	@Column(name = "AID_TITULO")
	private String titulo;

	@XmlAttribute
	@Column(name = "AID_DESCRI")
	private String descripcion;

	@XmlElement
	@ManyToOne
	@JoinColumn(name = "AID_IMAGEN")
	private Archivo documento;

	@XmlElement
	@ManyToOne
	@JoinColumn(name = "AID_DOCU")
	private Archivo imagen;

	@XmlAttribute
	@Column(name = "AID_URL")
	private String url;

	@XmlAttribute
	@Column(name = "AID_URLNOM")
	private String urlnom;

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Archivo getDocumento() {
		return this.documento;
	}

	public void setDocumento(Archivo documento) {
		this.documento = documento;
	}

	public Archivo getImagen() {
		return this.imagen;
	}

	public void setImagen(Archivo imagen) {
		this.imagen = imagen;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlnom() {
		return this.urlnom;
	}

	public void setUrlnom(String urlnom) {
		this.urlnom = urlnom;
	}

	public TraduccionAgendaPK getId() {
		return this.id;
	}

	public void setId(TraduccionAgendaPK id) {
		this.id = id;
	}

}