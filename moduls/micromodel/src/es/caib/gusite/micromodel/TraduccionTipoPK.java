package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * The primary key class for the GUS_TPNIDI database table.
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@Embeddable
public class TraduccionTipoPK implements Serializable {

	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@XmlAttribute
	@Column(name = "TPI_CODIDI")
	private String codigoIdioma;

	@XmlAttribute
	@Column(name = "TPI_TIPCOD")
	private Long codigoTipo;

	public TraduccionTipoPK() {
	}

	public String getCodigoIdioma() {
		return this.codigoIdioma;
	}

	public void setCodigoIdioma(String codigoIdioma) {
		this.codigoIdioma = codigoIdioma;
	}

	public Long getCodigoTipo() {
		return this.codigoTipo;
	}

	public void setCodigoTipo(Long codigoTipo) {
		this.codigoTipo = codigoTipo;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TraduccionTipoPK)) {
			return false;
		}
		TraduccionTipoPK castOther = (TraduccionTipoPK) other;
		return this.codigoIdioma.equals(castOther.codigoIdioma)
				&& (this.codigoTipo == castOther.codigoTipo);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codigoIdioma.hashCode();
		hash = hash * prime
				+ ((int) (this.codigoTipo ^ (this.codigoTipo >>> 32)));

		return hash;
	}

}