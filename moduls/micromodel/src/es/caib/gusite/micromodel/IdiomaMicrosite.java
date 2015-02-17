package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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

	public IdiomaMicrosite() {
	}

	public IdiomaMicrositePK getId() {
		return this.id;
	}

	public void setId(IdiomaMicrositePK id) {
		this.id = id;
	}

}