package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * The primary key class for the GUS_ database table.
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@Embeddable
public class TraduccionFContactoPK implements Serializable {

	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@XmlAttribute
	@Column(name = "FCI_CODIDI")
	private String codigoIdioma;

	@XmlAttribute
	@Column(name = "FCI_FCOCOD")
	private Long codigoFContacto;

	public TraduccionFContactoPK() {
	}

	public String getCodigoIdioma() {
		return this.codigoIdioma;
	}

	public void setCodigoIdioma(String codigoIdioma) {
		this.codigoIdioma = codigoIdioma;
	}

	public Long getCodigoFContacto() {
		return this.codigoFContacto;
	}

	public void setCodigoFContacto(Long codigoTipo) {
		this.codigoFContacto = codigoTipo;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TraduccionFContactoPK)) {
			return false;
		}
		TraduccionFContactoPK castOther = (TraduccionFContactoPK) other;
		return this.codigoIdioma.equals(castOther.codigoIdioma)
				&& (this.codigoFContacto == castOther.codigoFContacto);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codigoIdioma.hashCode();
		hash = hash * prime
				+ ((int) (this.codigoFContacto ^ (this.codigoFContacto >>> 32)));

		return hash;
	}

}