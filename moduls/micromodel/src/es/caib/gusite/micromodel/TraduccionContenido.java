package es.caib.gusite.micromodel;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.xml.bind.annotation.*;

/**
 * Clase TraduccionContenido. Encapsula los datos  que pueden tener valor en diferentes idiomas del objeto Contenido.
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name="GUS_CONIDI")
public class TraduccionContenido  implements Traduccion {

	private static final long serialVersionUID = -3199527315588330827L;

    @XmlElement
	@EmbeddedId
	private TraduccionContenidoPK id;

    @XmlAttribute
	@Column(name="CID_TITULO")
	private String titulo;
    
    @XmlAttribute
	@Column(name="CID_URI")
    private String uri;
    
    @XmlAttribute
	@Column(name="CID_URL")
    private String url;

    @XmlAttribute
	@Lob
	@Column(name="CID_TEXTO")
    private String texto;

    @XmlAttribute
	@Lob
	@Column(name="CID_TXBETA")
    private String txbeta;

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public String getTxbeta() {
		return txbeta;
	}

	public void setTxbeta(String txbeta) {
		this.txbeta = txbeta;
	}

	public TraduccionContenidoPK getId() {
		return id;
	}

	public void setId(TraduccionContenidoPK id) {
		this.id = id;
	}
}