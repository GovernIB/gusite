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
 * Clase Temafaq. Bean que define un Tema. 
 * Modela la tabla de BBDD GUS_TEMAS.
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name="GUS_TEMAS")
public class Temafaq implements Traducible2 {

	private static final long serialVersionUID = -1590941371205365963L;

    @XmlAttribute
	@Id
	@SequenceGenerator(name="GUS_TEMAS_ID_GENERATOR", sequenceName="GUS_SEQTEM", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GUS_TEMAS_ID_GENERATOR")
	@Column(name="TEM_CODI")
	private Long id;

    @XmlAttribute
	@Column(name="TEM_MICCOD")
	private Long idmicrosite;

  	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
  	@JoinColumn(name="TID_TEMCOD")
  	@MapKey(name="id.codigoIdioma")
	@Fetch(FetchMode.SUBSELECT)
  	private Map<String, TraduccionTemafaq> traducciones = new HashMap<String, TraduccionTemafaq>();

  	@Transient
  	private String idi = Idioma.getIdiomaPorDefecto();

    @XmlElement(name = "traducciones")
    @XmlJavaTypeAdapter(TraduccionAdapter.class)
    public Map<String, TraduccionTemafaq> getTranslates() {
        return traducciones;
    }

    public void setTranslates(Map<String, TraduccionTemafaq> traducciones) {
        this.traducciones = traducciones;
    }

	public Long getId() {
        return id;
    }

    public String getIdi() {
		return idi;
	}

	public Long getIdmicrosite() {
		return idmicrosite;
	}

	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

	public void setId(Long id) {
        this.id = id;
    }

	public void addTraduccionMap(String lang, TraduccionTemafaq traduccion) {
        setTraduccion(lang, traduccion);
    }

	public Map<String, TraduccionTemafaq> getTraducciones() {
		return traducciones;
	}

	public void setTraducciones(Map traducciones) {
		this.traducciones = traducciones;
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
            traducciones.put(idioma, (TraduccionTemafaq)traduccion);
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

	public void setIdi(String idi) {
		this.idi=idi;
	}
}