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
 * Clase Actividadagenda. Bean que define la actividad de una agenda. En la GUI una actividad se considera un "Esdeveniment" 
 * Modela la tabla de BBDD GUS_ACTIVI
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name="GUS_ACTIVI")
public class Actividadagenda implements Traducible2 {

	private static final long serialVersionUID = -2270566256357933414L;

    @XmlAttribute
	@Id
	@SequenceGenerator(name="GUS_ACCTIVIDAD_AGENDA_ID_GENERATOR", sequenceName="GUS_SEQACT", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GUS_ACCTIVIDAD_AGENDA_ID_GENERATOR")
	@Column(name="ACT_CODI")
	private Long id;

    @XmlAttribute
	@Column(name="ACT_MICCOD")
    private Long idmicrosite;

	@Transient 
	private String idi = Idioma.getIdiomaPorDefecto();

	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="ATI_ACTCOD")
	@MapKey(name="id.codigoIdioma")
	@Fetch(FetchMode.SUBSELECT)
	private Map<String, TraduccionActividadagenda> traducciones = new HashMap<String, TraduccionActividadagenda>();

    @XmlElement(name = "traducciones")
    @XmlJavaTypeAdapter(TraduccionAdapter.class)
    public Map<String, TraduccionActividadagenda> getTranslates() {
        return traducciones;
    }

    public void setTranslates(Map<String, TraduccionActividadagenda> traducciones) {
        this.traducciones = traducciones;
    }

	public Map<String, TraduccionActividadagenda> getTraducciones() {
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
            traducciones.put(idioma, (TraduccionActividadagenda)traduccion);
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
		this.idi = idi;
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

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
}