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
 * Clase TraduccionMenu. Encapsula los datos que pueden tener valor en
 * diferentes idiomas del objeto Menu.
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_MNUIDI")
public class TraduccionMenu implements Traduccion {

	private static final long serialVersionUID = 8119316534600598810L;

	@XmlElement
	@EmbeddedId
	private TraduccionMenuPK id;

	@XmlAttribute
	@Column(name = "MDI_NOMBRE")
	private String nombre;

	public TraduccionMenu() {
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public TraduccionMenuPK getId() {
		return this.id;
	}

	public void setId(TraduccionMenuPK id) {
		this.id = id;
	}

}