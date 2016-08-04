package es.caib.gusite.micromodel;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CPI_CMPCOD", insertable=false, updatable=false)
	private Componente componente;
	
	
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

	public void setComponente(Componente componente) {
		this.componente = componente;
	}

	public Componente getComponente() {
		return componente;
	}

}