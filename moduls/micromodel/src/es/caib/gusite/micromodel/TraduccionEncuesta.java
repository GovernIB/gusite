package es.caib.gusite.micromodel;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.*;

/**
 * Clase TraduccionEncuesta. Encapsula los datos  que pueden tener valor en diferentes idiomas del objeto Encuesta.
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name="GUS_ENCIDI")
public class TraduccionEncuesta implements Traduccion {

	private static final long serialVersionUID = 8202221844570423619L;

    @XmlElement
	@EmbeddedId
	private TraduccionEncuestaPK id;

    @XmlAttribute
	@Column(name="EID_TITULO")
	private String titulo;
    
    @XmlAttribute
	@Column(name="EID_URI")
	private String uri;
    
  	public String getUri() {
  		return uri;
  	}

  	public void setUri(String uri) {
  		this.uri = uri;
  	}

    public String getTitulo() {
    	return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

	public TraduccionEncuestaPK getId() {
		return id;
	}

	public void setId(TraduccionEncuestaPK id) {
		this.id = id;
	}

}