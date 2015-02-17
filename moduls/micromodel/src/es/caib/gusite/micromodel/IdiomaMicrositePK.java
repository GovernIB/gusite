package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * The primary key class for the GUS_IDIMIC database table.
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@Embeddable
public class IdiomaMicrositePK implements Serializable {

	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@XmlAttribute
	@Column(name = "IMI_CODIDI")
	private String codigoIdioma;

	@XmlAttribute
	@Column(name = "IMI_MICCOD")
	private Long codigoMicrosite;

	public IdiomaMicrositePK() {
	}

	public String getCodigoIdioma() {
		return this.codigoIdioma;
	}

	public void setCodigoIdioma(String codigoIdioma) {
		this.codigoIdioma = codigoIdioma;
	}

	public long getCodigoMicrosite() {
		return this.codigoMicrosite;
	}

	public void setCodigoMicrosite(Long codigoMicrosite) {
		this.codigoMicrosite = codigoMicrosite;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof IdiomaMicrositePK)) {
			return false;
		}
		IdiomaMicrositePK castOther = (IdiomaMicrositePK) other;
		return this.codigoIdioma.equals(castOther.codigoIdioma)
				&& (this.codigoMicrosite == castOther.codigoMicrosite);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codigoIdioma.hashCode();
		hash = hash
				* prime
				+ ((int) (this.codigoMicrosite ^ (this.codigoMicrosite >>> 32)));

		return hash;
	}
}