package es.caib.gusite.micromodel;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.*;

/**
 * Clase TraduccionRespuesta. Encapsula los datos  que pueden tener valor en diferentes idiomas del objeto Respuesta.
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name="GUS_RESIDI")
public class TraduccionRespuesta implements Traduccion {

	private static final long serialVersionUID = 6426636976073934777L;

    @XmlElement
	@EmbeddedId
	private TraduccionRespuestaPK id;

    @XmlAttribute
	@Column(name="REI_TITULO")
	private String titulo;

    public String getTitulo() {
    	return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

	public TraduccionRespuestaPK getId() {
		return id;
	}

	public void setId(TraduccionRespuestaPK id) {
		this.id = id;
	}

}
