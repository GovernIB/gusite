package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The primary key class for the GUS_CONIDI database table.
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@Embeddable
public class TraduccionContenidoPK implements Serializable {

	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@XmlAttribute
	@Column(name = "CID_CODIDI")
	private String codigoIdioma;

	@XmlAttribute
	@Column(name = "CID_CONCOD")
	private Long codigoContenido;

	public TraduccionContenidoPK() {
	}

	public String getCodigoIdioma() {
		return this.codigoIdioma;
	}

	public void setCodigoIdioma(String codigoIdioma) {
		this.codigoIdioma = codigoIdioma;
	}

	public Long getCodigoContenido() {
		return this.codigoContenido;
	}

	public void setCodigoContenido(Long codigoContenido) {
		this.codigoContenido = codigoContenido;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TraduccionContenidoPK)) {
			return false;
		}
		TraduccionContenidoPK castOther = (TraduccionContenidoPK) other;
		return this.codigoIdioma.equals(castOther.codigoIdioma)
				&& (this.codigoContenido == castOther.codigoContenido);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codigoIdioma.hashCode();
		hash = hash
				* prime
				+ ((int) (this.codigoContenido ^ (this.codigoContenido >>> 32)));

		return hash;
	}
}