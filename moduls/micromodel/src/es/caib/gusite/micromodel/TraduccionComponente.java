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
 * Clase TraduccionComponente. Encapsula los datos que pueden tener valor en
 * diferentes idiomas del objeto Componente.
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_CMPIDI")
public class TraduccionComponente implements Traduccion {

	private static final long serialVersionUID = -3454622370301464308L;

	@XmlElement
	@EmbeddedId
	private TraduccionComponentePK id;

	@XmlAttribute
	@Column(name = "CPI_TITULO")
	private String titulo;

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public TraduccionComponentePK getId() {
		return this.id;
	}

	public void setId(TraduccionComponentePK id) {
		this.id = id;
	}

}