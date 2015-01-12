package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * The primary key class for the GUS_FRQIDI database table.
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@Embeddable
public class TraduccionFrqssiPK implements Serializable {

	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

    @XmlAttribute
	@Column(name="FQI_CODIDI")
	private String codigoIdioma;

    @XmlAttribute
	@Column(name="FQI_FRQCOD")
	private Long codigoFrqssi;

    public TraduccionFrqssiPK() {
    }
	
    public String getCodigoIdioma() {
		return codigoIdioma;
	}

	public void setCodigoIdioma(String codigoIdioma) {
		this.codigoIdioma = codigoIdioma;
	}

	public Long getCodigoFrqssi() {
		return codigoFrqssi;
	}

	public void setCodigoFrqssi(Long codigoFrqssi) {
		this.codigoFrqssi = codigoFrqssi;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TraduccionFrqssiPK)) {
			return false;
		}
		TraduccionFrqssiPK castOther = (TraduccionFrqssiPK)other;
		return 
			this.codigoIdioma.equals(castOther.codigoIdioma)
			&& (this.codigoFrqssi == castOther.codigoFrqssi);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codigoIdioma.hashCode();
		hash = hash * prime + ((int) (this.codigoFrqssi ^ (this.codigoFrqssi >>> 32)));
		
		return hash;
    }
}