package es.caib.gusite.micromodel;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Clase TraduccionFaq. Encapsula los datos que pueden tener valor en diferentes
 * idiomas del objeto Faq.
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_FAQIDI")
public class TraduccionFaq implements Traduccion {

	private static final long serialVersionUID = -5527976114771535502L;

	@XmlElement
	@EmbeddedId
	private TraduccionFaqPK id;

	@XmlAttribute
	@Column(name = "FID_PREGUN")
	private String pregunta;

	@XmlAttribute
	@Column(name = "FID_RESPU")
	private String respuesta;

	@XmlAttribute
	@Column(name = "FID_URL")
	private String url;

	@XmlAttribute
	@Column(name = "FID_URLNOM")
	private String urlnom;

	public String getPregunta() {
		return this.pregunta;
	}

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}

	public String getRespuesta() {
		return this.respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
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

	public TraduccionFaqPK getId() {
		return this.id;
	}

	public void setId(TraduccionFaqPK id) {
		this.id = id;
	}

}