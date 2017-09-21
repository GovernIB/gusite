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
 * Clase TraduccionFContacto. Encapsula los datos que pueden tener valor en
 * diferentes idiomas del objeto Contacto.
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_FCOIDI")
public class TraduccionFContacto implements Traduccion {

	private static final long serialVersionUID = 1L;

	public TraduccionFContacto() {
	}

	@XmlElement
	@EmbeddedId
	private TraduccionFContactoPK id;

	@XmlAttribute
	@Column(name = "FCI_NOMBRE")
	private String nombre;

	@XmlAttribute
	@Column(name = "FCI_URI")
	private String uri;

	public String getUri() {
		return this.uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public TraduccionFContactoPK getId() {
		return this.id;
	}

	public void setId(TraduccionFContactoPK id) {
		this.id = id;
	}

}