package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * The primary key class for the GUS_CMPIDI database table.
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@Embeddable
public class TraduccionComponentePK implements Serializable {

	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

    @XmlAttribute
	@Column(name="CPI_CODIDI")
	private String codigoIdioma;

    @XmlAttribute
	@Column(name="CPI_CMPCOD")
	private long codigoComponente;

    public TraduccionComponentePK() {
    }

	public String getCodigoIdioma() {
		return codigoIdioma;
	}

	public void setCodigoIdioma(String codigoIdioma) {
		this.codigoIdioma = codigoIdioma;
	}

	public long getCodigoComponente() {
		return codigoComponente;
	}

	public void setCodigoComponente(long codigoComponente) {
		this.codigoComponente = codigoComponente;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TraduccionComponentePK)) {
			return false;
		}
		TraduccionComponentePK castOther = (TraduccionComponentePK)other;
		return 
			this.codigoIdioma.equals(castOther.codigoIdioma)
			&& (this.codigoComponente == castOther.codigoComponente);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codigoIdioma.hashCode();
		hash = hash * prime + ((int) (this.codigoComponente ^ (this.codigoComponente >>> 32)));
		
		return hash;
    }
}