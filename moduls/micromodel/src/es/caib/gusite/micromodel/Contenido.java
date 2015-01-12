package es.caib.gusite.micromodel;

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

import es.caib.gusite.micromodel.adapter.TraduccionAdapter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Clase Contenido. Bean que define un Contenido. 
 * Modela la tabla de BBDD GUS_CONTEN
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name="GUS_CONTEN")
public class Contenido implements Traducible2 {
	
	private static final long serialVersionUID = 4688044820582237768L;

    @XmlAttribute
	@Id
	@SequenceGenerator(name="GUS_CONTENIDO_ID_GENERATOR", sequenceName="GUS_SEQCON", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GUS_CONTENIDO_ID_GENERATOR")
	@Column(name="CON_CODI")
	private Long id;

    @XmlAttribute
	@Temporal(TemporalType.DATE)
	@Column(name="CON_CADUCA")
	private Date fcaducidad;

    @XmlAttribute
	@Temporal(TemporalType.DATE)
	@Column(name="CON_PUBLIC")
	private Date fpublicacion;

    @XmlAttribute
	@Column(name="CON_ORDEN")
	private int orden;

    @XmlAttribute
	@Column(name="CON_VISIB")
	private String visible;
	
	//bi-directional many-to-one association to GusDocus
    @XmlElement
    @ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="CON_IMGMEN")
	private Archivo imagenmenu;

	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="CID_CONCOD")
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	@MapKey(name="id.codigoIdioma")
	@Fetch(FetchMode.SUBSELECT)
	private Map<String, TraduccionContenido> traducciones = new HashMap<String, TraduccionContenido>();

    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="CON_MNUCOD")
	private Menu menu;

    @Transient
	private String urlExterna="";

    @Transient
    private String idi = Idioma.getIdiomaPorDefecto();

    @XmlElement(name = "traducciones")
    @XmlJavaTypeAdapter(TraduccionAdapter.class)
    public Map<String, TraduccionContenido> getTranslates() {
        return traducciones;
    }

    public void setTranslates(Map<String, TraduccionContenido> traducciones) {
        this.traducciones = traducciones;
    }

	public Date getFcaducidad() {
		return fcaducidad;
	}

	public void setFcaducidad(Date fcaducidad) {
		this.fcaducidad = fcaducidad;
	}

	public Date getFpublicacion() {
		return fpublicacion;
	}

	public void setFpublicacion(Date fpublicacion) {
		this.fpublicacion = fpublicacion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Archivo getImagenmenu() {
		return imagenmenu;
	}

	public void setImagenmenu(Archivo imagenmenu) {
		this.imagenmenu = imagenmenu;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}  

	public void addTraduccionMap(String lang, TraduccionContenido traduccion) {
        setTraduccion(lang, traduccion);
    }

	public String getUrlExterna(){
		return urlExterna;
	}

	public void setUrlExterna(String urlExterna){
		this.urlExterna=urlExterna;
	}

	public Map<String, TraduccionContenido> getTraducciones() {
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
            traducciones.put(idioma, (TraduccionContenido)traduccion);
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

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

}