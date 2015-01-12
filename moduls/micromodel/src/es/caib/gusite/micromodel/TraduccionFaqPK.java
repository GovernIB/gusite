package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * The primary key class for the GUS_FAQIDI database table.
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@Embeddable
public class TraduccionFaqPK implements Serializable {

	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

    @XmlAttribute
	@Column(name="FID_CODIDI")
	private String codigoIdioma;

    @XmlAttribute
	@Column(name="FID_FAQCOD")
	private Long codigoFaq;

    public TraduccionFaqPK() {
    }

    public String getCodigoIdioma() {
		return codigoIdioma;
	}

	public void setCodigoIdioma(String codigoIdioma) {
		this.codigoIdioma = codigoIdioma;
	}

	public Long getCodigoFaq() {
		return codigoFaq;
	}

	public void setCodigoFaq(long codigoFaq) {
		this.codigoFaq = codigoFaq;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TraduccionFaqPK)) {
			return false;
		}
		TraduccionFaqPK castOther = (TraduccionFaqPK)other;
		return 
			this.codigoIdioma.equals(castOther.codigoIdioma)
			&& (this.codigoFaq == castOther.codigoFaq);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codigoIdioma.hashCode();
		hash = hash * prime + ((int) (this.codigoFaq ^ (this.codigoFaq >>> 32)));
		
		return hash;
    }

}