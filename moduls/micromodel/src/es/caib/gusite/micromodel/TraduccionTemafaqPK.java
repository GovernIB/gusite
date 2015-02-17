package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * The primary key class for the GUS_TEMIDI database table.
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@Embeddable
public class TraduccionTemafaqPK implements Serializable {

	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@XmlAttribute
	@Column(name = "TID_CODIDI")
	private String codigoIdioma;

	@XmlAttribute
	@Column(name = "TID_TEMCOD")
	private Long codigoTema;

	public TraduccionTemafaqPK() {
	}

	public String getCodigoIdioma() {
		return this.codigoIdioma;
	}

	public void setCodigoIdioma(String codigoIdioma) {
		this.codigoIdioma = codigoIdioma;
	}

	public Long getCodigoTema() {
		return this.codigoTema;
	}

	public void setCodigoTema(Long codigoTema) {
		this.codigoTema = codigoTema;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TraduccionTemafaqPK)) {
			return false;
		}
		TraduccionTemafaqPK castOther = (TraduccionTemafaqPK) other;
		return this.codigoIdioma.equals(castOther.codigoIdioma)
				&& (this.codigoTema == castOther.codigoTema);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codigoIdioma.hashCode();
		hash = hash * prime
				+ ((int) (this.codigoTema ^ (this.codigoTema >>> 32)));

		return hash;
	}
}