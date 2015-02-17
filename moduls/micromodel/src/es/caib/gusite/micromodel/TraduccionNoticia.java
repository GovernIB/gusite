package es.caib.gusite.micromodel;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Clase TraduccionNoticia. Encapsula los datos que pueden tener valor en
 * diferentes idiomas del objeto Noticia.
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_NOTIDI")
public class TraduccionNoticia implements Traduccion {

	private static final long serialVersionUID = -6878989762827827892L;

	@XmlElement
	@EmbeddedId
	private TraduccionNoticiaPK id;

	@XmlAttribute
	@Column(name = "NID_TITULO")
	private String titulo;

	@XmlAttribute
	@Column(name = "NID_URI")
	private String uri;

	@XmlAttribute
	@Column(name = "NID_SUBTIT")
	private String subtitulo;

	@XmlAttribute
	@Column(name = "NID_FUENTE")
	private String fuente;

	@XmlAttribute
	@Column(name = "NID_URL")
	private String laurl;

	@XmlAttribute
	@Column(name = "NID_URLNOM")
	private String urlnom;

	@XmlElement
	@ManyToOne
	@JoinColumn(name = "NID_DOCU")
	private Archivo docu;

	@XmlAttribute
	@Lob
	@Column(name = "NID_TEXTO")
	private String texto;

	public TraduccionNoticia() {
	}

	public Archivo getDocu() {
		return this.docu;
	}

	public void setDocu(Archivo docu) {
		this.docu = docu;
	}

	public String getFuente() {
		return this.fuente;
	}

	public void setFuente(String fuente) {
		this.fuente = fuente;
	}

	public String getLaurl() {
		return this.laurl;
	}

	public void setLaurl(String laurl) {
		this.laurl = laurl;
	}

	public String getUri() {
		return this.uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUrlnom() {
		return this.urlnom;
	}

	public void setUrlnom(String urlnom) {
		this.urlnom = urlnom;
	}

	public String getSubtitulo() {
		return this.subtitulo;
	}

	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return this.texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public TraduccionNoticiaPK getId() {
		return this.id;
	}

	public void setId(TraduccionNoticiaPK id) {
		this.id = id;
	}

}