package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * The primary key class for the GUS_ACTIDI database table.
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@Embeddable
public class TraduccionActividadagendaPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@XmlAttribute
	@Column(name = "ATI_CODIDI")
	private String codigoIdioma;

	@XmlAttribute
	@Column(name = "ATI_ACTCOD")
	private long codigoActividadAgenda;

	public TraduccionActividadagendaPK() {
	}

	public String getCodigoIdioma() {
		return this.codigoIdioma;
	}

	public void setCodigoIdioma(String codigoIdioma) {
		this.codigoIdioma = codigoIdioma;
	}

	public long getCodigoActividadAgenda() {
		return this.codigoActividadAgenda;
	}

	public void setCodigoActividadAgenda(long codigoActividadAgenda) {
		this.codigoActividadAgenda = codigoActividadAgenda;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TraduccionActividadagendaPK)) {
			return false;
		}
		TraduccionActividadagendaPK castOther = (TraduccionActividadagendaPK) other;
		return this.codigoIdioma.equals(castOther.codigoIdioma)
				&& (this.codigoActividadAgenda == castOther.codigoActividadAgenda);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codigoIdioma.hashCode();
		hash = hash
				* prime
				+ ((int) (this.codigoActividadAgenda ^ (this.codigoActividadAgenda >>> 32)));

		return hash;
	}
}