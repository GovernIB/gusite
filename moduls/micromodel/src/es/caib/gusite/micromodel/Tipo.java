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
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import es.caib.gusite.micromodel.adapter.TraduccionAdapter;

/**
 * Clase Tipo. Bean que define un Tipo. Modela la tabla de BBDD GUS_TPNOTI.
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_TPNOTI")
public class Tipo extends AuditableModel implements Traducible2 {

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
	public static String TIPO_MAPA = "5";

	@XmlAttribute
	@Id
	@SequenceGenerator(name = "GUS_TIPO_ID_GENERATOR", sequenceName = "GUS_SEQTPN", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_TIPO_ID_GENERATOR")
	@Column(name = "TPN_CODI")
	private Long id;

	@XmlAttribute
	@Column(name = "TPN_MICCOD")
	private Long idmicrosite;

	@XmlAttribute
	@Column(name = "TPN_TAMPAG")
	private Integer tampagina;

	@XmlAttribute
	@Column(name = "TPN_TIPPAG")
	private String tipopagina;

	@XmlAttribute
	@Column(name = "TPN_TPELEM")
	private String tipoelemento;

	@XmlAttribute
	@Column(name = "TPN_BUSCAR")
	private String buscador;

	@XmlAttribute
	@Column(name = "TPN_ORDEN")
	private String orden;

	@XmlAttribute
	@Column(name = "TPN_CLASIF")
	private String clasificacion;

	// nuevos atributos para conectar el tipo
	@XmlAttribute
	@Column(name = "TPN_XURL")
	private String xurl;

	@XmlAttribute
	@Column(name = "TPN_XJNDI")
	private String xjndi;

	@XmlAttribute
	@Column(name = "TPN_XLOCAL")
	private String xlocalizacion;

	@XmlAttribute
	@Column(name = "TPN_XAUTH")
	private String xauth;

	@XmlAttribute
	@Column(name = "TPN_XUSR")
	private String xusr;

	@XmlAttribute
	@Column(name = "TPN_XPWD")
	private String xpwd;

	@XmlAttribute
	@Column(name = "TPN_XID")
	private String xid;

	@XmlAttribute
	@Column(name = "TPN_FOTOSPORFILA")
	private Integer fotosporfila;

	@XmlElement
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TPN_PPLCOD")
	private PersonalizacionPlantilla personalizacionPlantilla;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JoinColumn(name = "TPI_TIPCOD")
	@MapKey(name = "id.codigoIdioma")
	@Fetch(FetchMode.SUBSELECT)
	private Map<String, TraduccionTipo> traducciones = new HashMap<String, TraduccionTipo>();

	@Transient
	private String idi = Idioma.getIdiomaPorDefecto();

	@XmlElement(name = "traducciones")
	@XmlJavaTypeAdapter(TraduccionAdapter.class)
	public Map<String, TraduccionTipo> getTranslates() {
		return this.traducciones;
	}

	public void setTranslates(Map<String, TraduccionTipo> traducciones) {
		this.traducciones = traducciones;
	}

	@Override
	public Long getIdmicrosite() {
		return this.idmicrosite;
	}

	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void addTraduccionMap(String lang, TraduccionTipo traduccion) {
		this.setTraduccion(lang, traduccion);
	}

	public Integer getTampagina() {
		return this.tampagina;
	}

	public void setTampagina(Integer tampagina) {
		this.tampagina = tampagina;
	}

	public String getTipoelemento() {
		return this.tipoelemento;
	}

	public void setTipoelemento(String tipoelemento) {
		this.tipoelemento = tipoelemento;
	}

	public String getTipopagina() {
		return this.tipopagina;
	}

	public void setTipopagina(String tipopagina) {
		this.tipopagina = tipopagina;
	}

	public String getBuscador() {
		return this.buscador;
	}

	public void setBuscador(String buscador) {
		this.buscador = buscador;
	}

	public String getOrden() {
		return this.orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public String getXurl() {
		return this.xurl;
	}

	public void setXurl(String xurl) {
		this.xurl = xurl;
	}

	public String getXjndi() {
		return this.xjndi;
	}

	public void setXjndi(String xjndi) {
		this.xjndi = xjndi;
	}

	public String getXlocalizacion() {
		return this.xlocalizacion;
	}

	public void setXlocalizacion(String xlocalizacion) {
		this.xlocalizacion = xlocalizacion;
	}

	public String getXauth() {
		return this.xauth;
	}

	public void setXauth(String xauth) {
		this.xauth = xauth;
	}

	public String getXusr() {
		return this.xusr;
	}

	public void setXusr(String xusr) {
		this.xusr = xusr;
	}

	public String getXpwd() {
		return this.xpwd;
	}

	public void setXpwd(String xpwd) {
		this.xpwd = xpwd;
	}

	public String getXid() {
		return this.xid;
	}

	public void setXid(String xid) {
		this.xid = xid;
	}

	public String getClasificacion() {
		return this.clasificacion;
	}

	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}

	public Integer getFotosporfila() {
		return this.fotosporfila;
	}

	public void setFotosporfila(Integer fotosporfila) {
		this.fotosporfila = fotosporfila;
	}

	public PersonalizacionPlantilla getPersonalizacionPlantilla() {
		return this.personalizacionPlantilla;
	}

	public void setPersonalizacionPlantilla(
			PersonalizacionPlantilla personalizacionPlantilla) {
		this.personalizacionPlantilla = personalizacionPlantilla;
	}

	@Override
	public Map<String, TraduccionTipo> getTraducciones() {
		return this.traducciones;
	}

	@Override
	public void setTraducciones(Map traducciones) {
		this.traducciones = traducciones;
	}

	@Override
	public Traduccion getTraduccion() {
		return this.traducciones.get(Idioma.getIdiomaPorDefecto());
	}

	@Override
	public Traduccion getTraduccion(String idioma) {
		return this.traducciones.get(idioma);
	}

	@Override
	public void setTraduccion(String idioma, Traduccion traduccion) {
		if (traduccion == null) {
			this.traducciones.remove(idioma);
		} else {
			this.traducciones.put(idioma, (TraduccionTipo) traduccion);
		}
	}

	@Override
	public Traduccion getTraduce() {
		return this.traducciones.get(this.idi);
	}

	@Override
	public Map getTraduccionMap() {
		return this.traducciones;
	}

	@Override
	public void setTraduccionMap(Map traduccionMap) {
		this.traducciones = new HashMap(traduccionMap);
	}

	public String getIdi() {
		return this.idi;
	}

	@Override
	public void setIdi(String idi) {
		this.idi = idi;
	}
}