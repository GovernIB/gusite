package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * The primary key class for the GUS_AGEIDI database table.
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@Embeddable
public class TraduccionAgendaPK implements Serializable {

	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

    @XmlAttribute
	@Column(name="AID_CODIDI")
	private String codigoIdioma;

    @XmlAttribute
	@Column(name="AID_AGECOD")
	private Long codigoAgenda;

    public TraduccionAgendaPK() {
    }
	
	public String getCodigoIdioma() {
		return codigoIdioma;
	}

	public void setCodigoIdioma(String codigoIdioma) {
		this.codigoIdioma = codigoIdioma;
	}

	public Long getCodigoAgenda() {
		return codigoAgenda;
	}

	public void setCodigoAgenda(Long codigoAgenda) {
		this.codigoAgenda = codigoAgenda;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TraduccionAgendaPK)) {
			return false;
		}
		TraduccionAgendaPK castOther = (TraduccionAgendaPK)other;
		return 
			this.codigoIdioma.equals(castOther.codigoIdioma)
			&& (this.codigoAgenda == castOther.codigoAgenda);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codigoIdioma.hashCode();
		hash = hash * prime + ((int) (this.codigoAgenda ^ (this.codigoAgenda >>> 32)));
		
		return hash;
    }

}