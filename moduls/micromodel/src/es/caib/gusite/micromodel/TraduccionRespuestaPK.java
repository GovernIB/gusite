package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * The primary key class for the GUS_RESIDI database table.
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@Embeddable
public class TraduccionRespuestaPK implements Serializable {

	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

    @XmlAttribute
	@Column(name="REI_CODIDI")
	private String codigoIdioma;

    @XmlAttribute
	@Column(name="REI_RESCOD")
	private Long codigoRespuesta;

    public TraduccionRespuestaPK() {
    }

	public String getCodigoIdioma() {
		return codigoIdioma;
	}

	public void setCodigoIdioma(String codigoIdioma) {
		this.codigoIdioma = codigoIdioma;
	}

	public Long getCodigoRespuesta() {
		return codigoRespuesta;
	}

	public void setCodigoRespuesta(Long codigoRespuesta) {
		this.codigoRespuesta = codigoRespuesta;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TraduccionRespuestaPK)) {
			return false;
		}
		TraduccionRespuestaPK castOther = (TraduccionRespuestaPK)other;
		return 
			this.codigoRespuesta.equals(castOther.codigoRespuesta)
			&& (this.codigoRespuesta == castOther.codigoRespuesta);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codigoRespuesta.hashCode();
		hash = hash * prime + ((int) (this.codigoRespuesta ^ (this.codigoRespuesta >>> 32)));
		
		return hash;
    }
}