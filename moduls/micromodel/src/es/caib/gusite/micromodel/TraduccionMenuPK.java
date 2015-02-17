package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * The primary key class for the GUS_MNUIDI database table.
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@Embeddable
public class TraduccionMenuPK implements Serializable {

	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@XmlAttribute
	@Column(name = "MDI_MNUCOD")
	private Long codigoMenu;

	@XmlAttribute
	@Column(name = "MDI_CODIDI")
	private String codigoIdioma;

	public TraduccionMenuPK() {
	}

	public Long getCodigoMenu() {
		return this.codigoMenu;
	}

	public void setCodigoMenu(Long codigoMenu) {
		this.codigoMenu = codigoMenu;
	}

	public String getCodigoIdioma() {
		return this.codigoIdioma;
	}

	public void setCodigoIdioma(String codigoIdioma) {
		this.codigoIdioma = codigoIdioma;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TraduccionMenuPK)) {
			return false;
		}
		TraduccionMenuPK castOther = (TraduccionMenuPK) other;
		return (this.codigoMenu == castOther.codigoMenu)
				&& this.codigoIdioma.equals(castOther.codigoIdioma);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime
				+ ((int) (this.codigoMenu ^ (this.codigoMenu >>> 32)));
		hash = hash * prime + this.codigoIdioma.hashCode();

		return hash;
	}
}