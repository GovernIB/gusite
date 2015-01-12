package es.caib.gusite.micromodel;

import es.caib.gusite.micromodel.adapter.TraduccionAdapter;

import java.util.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Clase Faq. Bean que define una Faq. 
 * Modela la tabla de BBDD GUS_FAQ
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name="GUS_FAQ")
public class Faq implements Traducible2 {

	private static final long serialVersionUID = 2723640089721073919L;

    @XmlAttribute
	@Id
	@SequenceGenerator(name="GUS_FAQ_ID_GENERATOR", sequenceName="GUS_SEQFAQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GUS_FAQ_ID_GENERATOR")
	@Column(name="FAQ_CODI")
	private Long id;

    @XmlAttribute
	@Column(name="FAQ_MICCOD")
	private Long idmicrosite;

    @XmlAttribute
	@Temporal(TemporalType.DATE)
	@Column(name="FAQ_FECHA")
    private Date fecha;

    @XmlAttribute
	@Column(name="FAQ_VISIB")
    private String visible;

    @XmlElement
    @ManyToOne
	@JoinColumn(name="FAQ_CODTEM")
    private Temafaq tema;

	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
  	@JoinColumn(name="FID_FAQCOD")
	@MapKey(name="id.codigoIdioma")
	private Map<String, TraduccionFaq> traducciones = new HashMap<String, TraduccionFaq>();

	@Transient
	private String idi = Idioma.getIdiomaPorDefecto();

    @XmlElement(name = "traducciones")
    @XmlJavaTypeAdapter(TraduccionAdapter.class)
    public Map<String, TraduccionFaq> getTranslates() {
        return traducciones;
    }

    public void setTranslates(Map<String, TraduccionFaq> traducciones) {
        this.traducciones = traducciones;
    }

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdmicrosite() {
		return idmicrosite;
	}

	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

	public Temafaq getTema() {
		return tema;
	}

	public void setTema(Temafaq tema) {
		this.tema = tema;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public void addTraduccionMap(String lang, TraduccionFaq traduccion) {
        setTraduccion(lang, traduccion);
    }

	public Map<String, TraduccionFaq> getTraducciones() {
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
            traducciones.put(idioma, (TraduccionFaq)traduccion);
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