package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * The persistent class for the GUS_IDIMIC database table.
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_IDIMIC")
public class IdiomaMicrosite implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement
	@EmbeddedId
	private IdiomaMicrositePK id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMI_MICCOD", insertable=false, updatable=false)
	private Microsite microsite;

	public IdiomaMicrosite() {
	}

	public IdiomaMicrositePK getId() {
		return this.id;
	}

	public void setId(IdiomaMicrositePK id) {
		this.id = id;
	}

	public void setMicrosite(Microsite microsite) {
		this.microsite = microsite;
	}

	public Microsite getMicrosite() {
		return microsite;
	}

}