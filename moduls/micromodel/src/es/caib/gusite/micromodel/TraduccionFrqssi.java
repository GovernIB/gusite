package es.caib.gusite.micromodel;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.*;

/**
 * Clase TraduccionFrqssi. Encapsula los datos  que pueden tener valor en diferentes idiomas del objeto Frqssi.
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name="GUS_FRQIDI")
public class TraduccionFrqssi  implements Traduccion{

	private static final long serialVersionUID = -633154459030254267L;

	public TraduccionFrqssi()   { }

    @XmlElement
	@EmbeddedId
	private TraduccionFrqssiPK id;

    @XmlAttribute
	@Column(name="FQI_NOMBRE")
	private String nombre;

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

	public TraduccionFrqssiPK getId() {
		return id;
	}

	public void setId(TraduccionFrqssiPK id) {
		this.id = id;
	}

}