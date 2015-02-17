package es.caib.gusite.micromodel;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Clase TraduccionContenido. Encapsula los datos que pueden tener valor en
 * diferentes idiomas del objeto Contenido.
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_CONIDI")
public class TraduccionContenido implements Traduccion {

	private static final long serialVersionUID = -3199527315588330827L;

	@XmlElement
	@EmbeddedId
	private TraduccionContenidoPK id;

	@XmlAttribute
	@Column(name = "CID_TITULO")
	private String titulo;

	@XmlAttribute
	@Column(name = "CID_URI")
	private String uri;

	@XmlAttribute
	@Column(name = "CID_URL")
	private String url;

	@XmlAttribute
	@Lob
	@Column(name = "CID_TEXTO")
	private String texto;

	@XmlAttribute
	@Lob
	@Column(name = "CID_TXBETA")
	private String txbeta;

	public String getTexto() {
		return this.texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
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

	public String getUri() {
		return this.uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getTxbeta() {
		return this.txbeta;
	}

	public void setTxbeta(String txbeta) {
		this.txbeta = txbeta;
	}

	public TraduccionContenidoPK getId() {
		return this.id;
	}

	public void setId(TraduccionContenidoPK id) {
		this.id = id;
	}
}