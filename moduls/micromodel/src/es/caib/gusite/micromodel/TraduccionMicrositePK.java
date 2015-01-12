package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * The primary key class for the GUS_MICIDI database table.
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@Embeddable
public class TraduccionMicrositePK implements Serializable {

	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

    @XmlAttribute
	@Column(name="MID_CODIDI")
	private String codigoIdioma;

    @XmlAttribute
	@Column(name="MID_MICCOD")
	private Long codigoMicrosite;

    public TraduccionMicrositePK() {
    }

	public String getCodigoIdioma() {
		return codigoIdioma;
	}

	public void setCodigoIdioma(String codigoIdioma) {
		this.codigoIdioma = codigoIdioma;
	}

	public long getCodigoMicrosite() {
		return codigoMicrosite;
	}

	public void setCodigoMicrosite(Long codigoMicrosite) {
		this.codigoMicrosite = codigoMicrosite;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TraduccionMicrositePK)) {
			return false;
		}
		TraduccionMicrositePK castOther = (TraduccionMicrositePK)other;
		return 
			this.codigoMicrosite.equals(castOther.codigoMicrosite)
			&& (this.codigoMicrosite == castOther.codigoMicrosite);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codigoMicrosite.hashCode();
		hash = hash * prime + ((int) (this.codigoMicrosite ^ (this.codigoMicrosite >>> 32)));
		
		return hash;
    }
}