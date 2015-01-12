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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Clase Tipo. Bean que define un Tipo. 
 * Modela la tabla de BBDD GUS_TPNOTI.
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name="GUS_TPNOTI")
public class Tipo implements Traducible2 {
	
	private static final long serialVersionUID = -4636608744934761537L;
	public static String AUTENTICACION_EXPLICITA_CAIB = "C";
	public static String AUTENTICACION_NO = "N";
	public static String AUTENTICACION_STANDARD = "S";

	public static String EJB_LOCAL = "L";
	public static String EJB_REMOTO = "R";

	public static String TIPO_FICHA = "0";
	public static String TIPO_LINK = "1";
	public static String TIPO_DOCUMENTOS = "2";
	public static String TIPO_CONEXIO_EXTERNA = "3";
	public static String TIPO_FOTO = "4";

    @XmlAttribute
	@Id
	@SequenceGenerator(name="GUS_TIPO_ID_GENERATOR", sequenceName="GUS_SEQTPN", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GUS_TIPO_ID_GENERATOR")
	@Column(name="TPN_CODI")
	private Long id;

    @XmlAttribute
	@Column(name="TPN_MICCOD")
    private Long idmicrosite;

    @XmlAttribute
	@Column(name="TPN_TAMPAG")
    private Integer tampagina;

    @XmlAttribute
	@Column(name="TPN_TIPPAG")
    private String tipopagina;

    @XmlAttribute
	@Column(name="TPN_TPELEM")
    private String tipoelemento;

    @XmlAttribute
	@Column(name="TPN_BUSCAR")
    private String buscador;

    @XmlAttribute
	@Column(name="TPN_ORDEN")
    private String orden;

    @XmlAttribute
	@Column(name="TPN_CLASIF")
    private String clasificacion;
    
    //nuevos atributos para conectar el tipo
    @XmlAttribute
	@Column(name="TPN_XURL")
    private String xurl;

    @XmlAttribute
	@Column(name="TPN_XJNDI")
    private String xjndi;

    @XmlAttribute
	@Column(name="TPN_XLOCAL")
    private String xlocalizacion;

    @XmlAttribute
	@Column(name="TPN_XAUTH")
    private String xauth;

    @XmlAttribute
	@Column(name="TPN_XUSR")
    private String xusr;

    @XmlAttribute
	@Column(name="TPN_XPWD")
    private String xpwd;

    @XmlAttribute
	@Column(name="TPN_XID")
    private String xid;

    @XmlAttribute
	@Column(name="TPN_FOTOSPORFILA")
	private Integer fotosporfila;

  	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
  	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
  	@JoinColumn(name="TPI_TIPCOD")
  	@MapKey(name="id.codigoIdioma")
	@Fetch(FetchMode.SUBSELECT)
  	private Map<String, TraduccionTipo> traducciones = new HashMap<String, TraduccionTipo>();

  	@Transient
  	private String idi = Idioma.getIdiomaPorDefecto();

    @XmlElement(name = "traducciones")
    @XmlJavaTypeAdapter(TraduccionAdapter.class)

    public Map<String, TraduccionTipo> getTranslates() {
        return traducciones;
    }

    public void setTranslates(Map<String, TraduccionTipo> traducciones) {
        this.traducciones = traducciones;
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

	public void addTraduccionMap(String lang, TraduccionTipo traduccion) {
        setTraduccion(lang, traduccion);
    }

	public Integer getTampagina() {
		return tampagina;
	}

	public void setTampagina(Integer tampagina) {
		this.tampagina = tampagina;
	}

	public String getTipoelemento() {
		return tipoelemento;
	}

	public void setTipoelemento(String tipoelemento) {
		this.tipoelemento = tipoelemento;
	}

	public String getTipopagina() {
		return tipopagina;
	}

	public void setTipopagina(String tipopagina) {
		this.tipopagina = tipopagina;
	}

	public String getBuscador() {
		return buscador;
	}

	public void setBuscador(String buscador) {
		this.buscador = buscador;
	}

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public String getXurl() {
		return xurl;
	}

	public void setXurl(String xurl) {
		this.xurl = xurl;
	}

	public String getXjndi() {
		return xjndi;
	}

	public void setXjndi(String xjndi) {
		this.xjndi = xjndi;
	}

	public String getXlocalizacion() {
		return xlocalizacion;
	}

	public void setXlocalizacion(String xlocalizacion) {
		this.xlocalizacion = xlocalizacion;
	}

	public String getXauth() {
		return xauth;
	}

	public void setXauth(String xauth) {
		this.xauth = xauth;
	}

	public String getXusr() {
		return xusr;
	}

	public void setXusr(String xusr) {
		this.xusr = xusr;
	}

	public String getXpwd() {
		return xpwd;
	}

	public void setXpwd(String xpwd) {
		this.xpwd = xpwd;
	}

	public String getXid() {
		return xid;
	}

	public void setXid(String xid) {
		this.xid = xid;
	}

	public String getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	} 	

	public Integer getFotosporfila() {
		return fotosporfila;
	}

	public void setFotosporfila(Integer fotosporfila) {
		this.fotosporfila = fotosporfila;
	}

	public Map<String, TraduccionTipo> getTraducciones() {
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
            traducciones.put(idioma, (TraduccionTipo)traduccion);
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

	public String getIdi() {
		return idi;
	}

	public void setIdi(String idi) {
		this.idi = idi;
	}
}