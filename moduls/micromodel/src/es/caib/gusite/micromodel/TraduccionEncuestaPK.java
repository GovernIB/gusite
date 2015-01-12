package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * The primary key class for the GUS_ENCIDI database table.
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@Embeddable
public class TraduccionEncuestaPK implements Serializable {

	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

    @XmlAttribute
	@Column(name="EID_CODIDI")
	private String codigoIdioma;

    @XmlAttribute
	@Column(name="EID_ENCCOD")
	private Long codigoEncuesta;

    public TraduccionEncuestaPK() {
    }

	public String getCodigoIdioma() {
		return codigoIdioma;
	}

	public void setCodigoIdioma(String codigoIdioma) {
		this.codigoIdioma = codigoIdioma;
	}

	public long getCodigoEncuesta() {
		return codigoEncuesta;
	}

	public void setCodigoEncuesta(Long codigoEncuesta) {
		this.codigoEncuesta = codigoEncuesta;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TraduccionEncuestaPK)) {
			return false;
		}
		TraduccionEncuestaPK castOther = (TraduccionEncuestaPK)other;
		return 
			this.codigoIdioma.equals(castOther.codigoIdioma)
			&& (this.codigoEncuesta == castOther.codigoEncuesta);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codigoIdioma.hashCode();
		hash = hash * prime + ((int) (this.codigoEncuesta ^ (this.codigoEncuesta >>> 32)));
		
		return hash;
    }
}