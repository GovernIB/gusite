package es.caib.gusite.micromodel;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Clase TraduccionLineadatocontacto. Encapsula los datos que pueden tener valor
 * en diferentes idiomas del objeto Lineadatocontacto.
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_FRMIDI")
public class TraduccionLineadatocontacto implements Traduccion {

	private static final long serialVersionUID = -2647818259513112558L;

	@XmlElement
	@EmbeddedId
	private TraduccionLineadatocontactoPK id;

	@XmlAttribute
	@Column(name = "RID_TEXTO")
	private String texto;

	public String getTexto() {
		return this.texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public TraduccionLineadatocontactoPK getId() {
		return this.id;
	}

	public void setId(TraduccionLineadatocontactoPK id) {
		this.id = id;
	}

}