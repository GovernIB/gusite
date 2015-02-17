package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * The primary key class for the GUS_PREIDI database table.
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@Embeddable
public class TraduccionPreguntaPK implements Serializable {

	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@XmlAttribute
	@Column(name = "PID_CODIDI")
	private String codigoIdioma;

	@XmlAttribute
	@Column(name = "PID_PRECOD")
	private Long codigoPregunta;

	public TraduccionPreguntaPK() {
	}

	public String getCodigoIdioma() {
		return this.codigoIdioma;
	}

	public void setCodigoIdioma(String codigoIdioma) {
		this.codigoIdioma = codigoIdioma;
	}

	public Long getCodigoPregunta() {
		return this.codigoPregunta;
	}

	public void setCodigoPregunta(Long codigoPregunta) {
		this.codigoPregunta = codigoPregunta;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TraduccionPreguntaPK)) {
			return false;
		}
		TraduccionPreguntaPK castOther = (TraduccionPreguntaPK) other;
		return this.codigoIdioma.equals(castOther.codigoIdioma)
				&& (this.codigoPregunta == castOther.codigoPregunta);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codigoIdioma.hashCode();
		hash = hash * prime
				+ ((int) (this.codigoPregunta ^ (this.codigoPregunta >>> 32)));

		return hash;
	}
}