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
 * Clase Noticia. Bean que define una Noticia. En la GUI los elementos de un listado son noticias.
 * Modela la tabla de BBDD GUS_NOTICS.
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name="GUS_NOTICS")
public class Noticia implements Traducible2 {

	private static final long serialVersionUID = -3615339661028201007L;

    @XmlAttribute
	@Id
	@SequenceGenerator(name="GUS_NOTICIA_ID_GENERATOR", sequenceName="GUS_SEQNOT", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GUS_NOTICIA_ID_GENERATOR")
	@Column(name="NOT_CODI")
	private Long id;

    @XmlAttribute
	@Column(name="NOT_MICCOD")
    private Long idmicrosite;
	
	//bi-directional many-to-one association to GusDocus
    @XmlElement
    @ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="NOT_IMAGEN")
    private Archivo imagen;

    @XmlAttribute
	@Temporal(TemporalType.DATE)
	@Column(name="NOT_CADUCA")
    private Date fcaducidad;

    @XmlAttribute
	@Temporal(TemporalType.DATE)
	@Column(name="NOT_PUBLIC")
    private Date fpublicacion;

    @XmlAttribute
	@Column(name="NOT_VISIB")
    private String visible;

    @XmlAttribute
	@Column(name="NOT_ORDEN")
    private Integer orden;

    @XmlAttribute
	@Column(name="NOT_VISWEB")
    private String visibleweb;

    @XmlElement
    @ManyToOne
	@JoinColumn(name="NOT_TIPO")
    private Tipo tipo;

  	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
  	@JoinColumn(name="NID_NOTCOD")
  	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
  	@MapKey(name="id.codigoIdioma")
	@Fetch(FetchMode.SUBSELECT)
  	private Map<String, TraduccionNoticia> traducciones = new HashMap<String, TraduccionNoticia>();

  	@Transient
  	private String idi = Idioma.getIdiomaPorDefecto();

    @XmlElement(name = "traducciones")
    @XmlJavaTypeAdapter(TraduccionAdapter.class)
    public Map<String, TraduccionNoticia> getTranslates() {
        return traducciones;
    }

    public void setTranslates(Map<String, TraduccionNoticia> traducciones) {
        this.traducciones = traducciones;
    }

    public Date getFcaducidad()
    {
        return fcaducidad;
    }

    public void setFcaducidad(Date fcaducidad)
    {
        this.fcaducidad = fcaducidad;
    }

    public Date getFpublicacion()
    {
        return fpublicacion;
    }

    public void setFpublicacion(Date fpublicacion)
    {
        this.fpublicacion = fpublicacion;
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

	public String getVisible()
    {
    	return visible;
    }

    public void setVisible(String visible)
    {
   		this.visible=visible;
    }

    public String getVisibleweb()
    {
        return visibleweb;
    }

    public void setVisibleweb(String visibleweb)
    {
        this.visibleweb = visibleweb;
    }

    public Archivo getImagen()
    {
        return imagen;
    }

    public void setImagen(Archivo imagen)
    {
        this.imagen = imagen;
    }

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
   
	public void addTraduccionMap(String lang, TraduccionNoticia traduccion) {
        setTraduccion(lang, traduccion);
    }

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public Map<String, TraduccionNoticia> getTraducciones() {
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
            traducciones.put(idioma, (TraduccionNoticia)traduccion);
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