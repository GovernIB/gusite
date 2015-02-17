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
 * Clase TraduccionTemafaq. Encapsula los datos que pueden tener valor en
 * diferentes idiomas del objeto Tema.
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_TEMIDI")
public class TraduccionTemafaq implements Traduccion {

	private static final long serialVersionUID = -1492821805120870638L;

	public TraduccionTemafaq() {
	}

	@XmlElement
	@EmbeddedId
	private TraduccionTemafaqPK id;

	@XmlAttribute
	@Column(name = "TID_NOMBRE")
	private String nombre;

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public TraduccionTemafaqPK getId() {
		return this.id;
	}

	public void setId(TraduccionTemafaqPK id) {
		this.id = id;
	}

}