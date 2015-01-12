package es.caib.gusite.micromodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import es.caib.gusite.micromodel.adapter.TraduccionAdapter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Clase Menu. Bean que define un Menu. 
 * Modela la tabla de BBDD GUS_MENU
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({Contenido.class})
@Entity
@Table(name="GUS_MENU")
public class Menu implements Traducible2 {

	private static final long serialVersionUID = 1505040618105464154L;

    @XmlAttribute
	@Id
	@SequenceGenerator(name="GUS_MENU_ID_GENERATOR", sequenceName="GUS_SEQMEN", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GUS_MENU_ID_GENERATOR")
	@Column(name="MNU_CODI")
	private Long id;

    @XmlAttribute
	@Column(name="MNU_ORDEN")
    private int orden;

    @XmlAttribute
	@Column(name="MNU_PADRE")
    private Long padre;

    @XmlAttribute
	@Column(name="MNU_VISIB")
	private String visible;

    @XmlAttribute
	@Column(name="MNU_MODO")
	private String modo;

    @XmlElement
    @ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="MNU_IMGMEN")
    private Archivo imagenmenu;

    @XmlElement
	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="CON_MNUCOD")
	@MapKey(name="id")
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy("orden ASC")
    private List<Contenido> contenidos = new ArrayList<Contenido>();

  	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
  	@JoinColumn(name="MDI_MNUCOD")
  	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	@Fetch(FetchMode.SUBSELECT)
    @MapKey(name="id.codigoIdioma")
  	private Map<String, TraduccionMenu> traducciones = new HashMap<String, TraduccionMenu>();

//	@XmlElement
    @ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="MNU_MICCOD")
	private Microsite microsite;

  	@Transient
  	private String idi = Idioma.getIdiomaPorDefecto();

    @XmlElement(name = "traducciones")
    @XmlJavaTypeAdapter(TraduccionAdapter.class)
    public Map<String, TraduccionMenu> getTranslates() {

        return traducciones;
    }

    public void setTranslates(Map<String, TraduccionMenu> traducciones) {
        this.traducciones = traducciones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOrden()
    {
        return orden;
    }

    public void setOrden(int orden)
    {
        this.orden = orden;
    }

    public Microsite getMicrosite() {
		return microsite;
	}

	public void setMicrosite(Microsite microsite) {
		this.microsite = microsite;
	}

	public Archivo getImagenmenu() {
		return imagenmenu;
	}

	public void setImagenmenu(Archivo imagenmenu) {
		this.imagenmenu = imagenmenu;
	}

	public List<Contenido> getContenidos() {
		return contenidos;
	}

	public void setContenidos(List<Contenido> contenidos) {
		this.contenidos = contenidos;
	}

	public Long getPadre() {
		return padre;
	}

	public void setPadre(Long padre) {
		this.padre = padre;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getModo() {
		return modo;
	}

	public void setModo(String modo) {
		this.modo = modo;
	}
	
	// Metodo para poder leer la coleccion del XML
	
	public void addContenidos (Contenido con) {
		contenidos.add(con);
	}

	public void addTraduccionMap(String lang, TraduccionMenu traduccion) {
        setTraduccion(lang, traduccion);
    }

	public Map<String, TraduccionMenu> getTraducciones() {

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
            traducciones.put(idioma, (TraduccionMenu)traduccion);
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