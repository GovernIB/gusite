package es.caib.gusite.micromodel;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.*;

/**
 * Clase TraduccionTipo. Encapsula los datos  que pueden tener valor en diferentes idiomas del objeto Tipo.
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name="GUS_TPNIDI")
public class TraduccionTipo  implements Traduccion{

	private static final long serialVersionUID = 3384380781846326766L;

	public TraduccionTipo()   { }

    @XmlElement
	@EmbeddedId
	private TraduccionTipoPK id;

    @XmlAttribute
	@Column(name="TPI_NOMBRE")
	private String nombre;
    
    @XmlAttribute
	@Column(name="TPI_URI")
	private String uri;
    
    public String getUri() {
  		return uri;
  	}

  	public void setUri(String uri) {
  		this.uri = uri;
  	}

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

	public TraduccionTipoPK getId() {
		return id;
	}

	public void setId(TraduccionTipoPK id) {
		this.id = id;
	}

}