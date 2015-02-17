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
 * Clase TraduccionPregunta. Encapsula los datos que pueden tener valor en
 * diferentes idiomas del objeto Pregunta.
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_PREIDI")
public class TraduccionPregunta implements Traduccion {

	private static final long serialVersionUID = 4201974686622631379L;

	@XmlElement
	@EmbeddedId
	private TraduccionPreguntaPK id;

	@XmlAttribute
	@Column(name = "PID_TITULO")
	private String titulo;

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public TraduccionPreguntaPK getId() {
		return this.id;
	}

	public void setId(TraduccionPreguntaPK id) {
		this.id = id;
	}

}