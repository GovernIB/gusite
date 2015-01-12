package es.caib.gusite.micromodel;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import es.caib.gusite.micromodel.adapter.TraduccionAdapter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Clase Frqssi. Bean que define un Formulario QSSI. 
 * Modela la tabla de BBDD GUS_FRQSSI
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name="GUS_FRQSSI")
public class Frqssi implements Traducible2 {

	private static final long serialVersionUID = 6491465580006672873L;

    @XmlAttribute
	@Id
	@SequenceGenerator(name="GUS_FRQSSI_ID_GENERATOR", sequenceName="GUS_SEQFRQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GUS_FRQSSI_ID_GENERATOR")
	@Column(name="FRQ_CODI")
	private Long id;

    @XmlAttribute
	@Column(name="FRQ_MICCOD")
    private Long idmicrosite;

    @XmlAttribute
	@Column(name="FRQ_CENTRE")
    private String centro;

    @XmlAttribute
	@Column(name="FRQ_TPESCR")
    private String tipoescrito;

	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
  	@JoinColumn(name="FQI_FRQCOD")
	@MapKey(name="id.codigoIdioma")
	@Fetch(FetchMode.SUBSELECT)
	private Map<String, TraduccionFrqssi> traducciones = new HashMap<String, TraduccionFrqssi>();

	@Transient
	private String idi = Idioma.getIdiomaPorDefecto();;

    @XmlElement(name = "traducciones")
    @XmlJavaTypeAdapter(TraduccionAdapter.class)
    public Map<String, TraduccionFrqssi> getTranslates() {
        return traducciones;
    }

    public void setTranslates(Map<String, TraduccionFrqssi> traducciones) {
        this.traducciones = traducciones;
    }

 	public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

	public Long getIdmicrosite() {
		return idmicrosite;
	}

	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

	public String getCentro() {
		return centro;
	}

	public void setCentro(String centro) {
		this.centro = centro;
	}	

	public String getTipoescrito() {
		return tipoescrito;
	}

	public void setTipoescrito(String tipoescrito) {
		this.tipoescrito = tipoescrito;
	}	
	
	public void addTraduccionMap(String lang, TraduccionFrqssi traduccion) {
        setTraduccion(lang, traduccion);
    }

	public Map<String, TraduccionFrqssi> getTraducciones() {
		return traducciones;
	}

	public void setTraducciones(Map traducciones) {
		this.traducciones = traducciones;
	}

	public String getIdi() {
		return idi;
	}

	public void setIdi(String idi) {
		this.idi = idi;
	}

	public Traduccion getTraduccion() {
		return (Traduccion) traducciones.get(Idioma.getIdiomaPorDefecto());
	}

	public Traduccion getTraduccion(String idioma) {
		return (Traduccion) traducciones.get(idioma);
	}

	public void setTraduccion(String idioma, Traduccion traduccion) {
        if (traduccion == null) {
            traducciones.remove(idioma);
        } else {
            traducciones.put(idioma, (TraduccionFrqssi)traduccion);
        }
	}

	public Traduccion getTraduce() {
		return (Traduccion) traducciones.get(idi);
	}

	public Map getTraduccionMap() {
		return traducciones;
	}

	public void setTraduccionMap(Map traduccionMap) {
		this.traducciones = new HashMap(traduccionMap);
	}

}