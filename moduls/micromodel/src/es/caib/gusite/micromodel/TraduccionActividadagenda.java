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
 * Clase TraduccionActividadagenda. Encapsula los datos que pueden tener valor
 * en diferentes idiomas del objeto Actividadagenda.
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_ACTIDI")
public class TraduccionActividadagenda implements Traduccion {

	private static final long serialVersionUID = -7351780786615474210L;

	public TraduccionActividadagenda() {
	}

	@XmlElement
	@EmbeddedId
	private TraduccionActividadagendaPK id;

	@XmlAttribute
	@Column(name = "ATI_NOMBRE")
	private String nombre;

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public TraduccionActividadagendaPK getId() {
		return this.id;
	}

	public void setId(TraduccionActividadagendaPK id) {
		this.id = id;
	}

}