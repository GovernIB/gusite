package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * The primary key class for the GUS_NOTIDI database table.
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@Embeddable
public class TraduccionNoticiaPK implements Serializable {

	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@XmlAttribute
	@Column(name = "NID_CODIDI")
	private String codigoIdioma;

	@XmlAttribute
	@Column(name = "NID_NOTCOD")
	private Long codigoNoticia;

	public TraduccionNoticiaPK() {
	}

	public String getCodigoIdioma() {
		return this.codigoIdioma;
	}

	public void setCodigoIdioma(String codigoIdioma) {
		this.codigoIdioma = codigoIdioma;
	}

	public Long getCodigoNoticia() {
		return this.codigoNoticia;
	}

	public void setCodigoNoticia(Long codigoNoticia) {
		this.codigoNoticia = codigoNoticia;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TraduccionNoticiaPK)) {
			return false;
		}
		TraduccionNoticiaPK castOther = (TraduccionNoticiaPK) other;
		return this.codigoIdioma.equals(castOther.codigoIdioma)
				&& (this.codigoNoticia == castOther.codigoNoticia);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codigoIdioma.hashCode();
		hash = hash * prime
				+ ((int) (this.codigoNoticia ^ (this.codigoNoticia >>> 32)));

		return hash;
	}
}