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
 * Clase Noticia. Bean que define una Noticia. En la GUI los elementos de un
 * listado son noticias. Modela la tabla de BBDD GUS_NOTICS.
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_NOTICS")
@SuppressWarnings({"rawtypes", "unchecked"})
public class Noticia extends AuditableModel implements Traducible2 {

	private static final long serialVersionUID = -3615339661028201007L;

	@XmlAttribute
	@Id
	@SequenceGenerator(name = "GUS_NOTICIA_ID_GENERATOR", sequenceName = "GUS_SEQNOT", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_NOTICIA_ID_GENERATOR")
	@Column(name = "NOT_CODI")
	private Long id;

	@XmlAttribute
	@Column(name = "NOT_MICCOD")
	private Long idmicrosite;

	// bi-directional many-to-one association to GusDocus
	@XmlElement
	@ManyToOne
	@JoinColumn(name = "NOT_IMAGEN")
	private Archivo imagen;

	@XmlAttribute
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "NOT_CADUCA")
	private Date fcaducidad;

	@XmlAttribute
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "NOT_PUBLIC")
	private Date fpublicacion;

	@XmlAttribute
	@Column(name = "NOT_VISIB")
	private String visible;

	@XmlAttribute
	@Column(name = "NOT_ORDEN")
	private Integer orden;

	@XmlAttribute
	@Column(name = "NOT_VISWEB")
	private String visibleweb;
	
	/////////////////
	// campos mapa
	@XmlAttribute
	@Column(name = "NOT_LATITUD")
	private String latitud;
	
	@XmlAttribute
	@Column(name = "NOT_LONGITUD")
	private String longitud;	
	
	@XmlAttribute
	@Column(name = "NOT_ICOCOLOR")
	private String colorIcono;	
	///////////////
	
	@XmlElement
	@ManyToOne
	@JoinColumn(name = "NOT_TIPO")
	private Tipo tipo;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "NID_NOTCOD")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@MapKey(name = "id.codigoIdioma")
	@Fetch(FetchMode.SELECT)
	private Map<String, TraduccionNoticia> traducciones = new HashMap<String, TraduccionNoticia>();

	@Transient
	private String idi = Idioma.getIdiomaPorDefecto();

	@XmlElement(name = "traducciones")
	@XmlJavaTypeAdapter(TraduccionAdapter.class)
	public Map<String, TraduccionNoticia> getTranslates() {
		return this.traducciones;
	}

	public void setTranslates(Map<String, TraduccionNoticia> traducciones) {
		this.traducciones = traducciones;
	}

	public Date getFcaducidad() {
		return this.fcaducidad;
	}

	public void setFcaducidad(Date fcaducidad) {
		this.fcaducidad = fcaducidad;
	}

	public Date getFpublicacion() {
		return this.fpublicacion;
	}

	public void setFpublicacion(Date fpublicacion) {
		this.fpublicacion = fpublicacion;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getIdmicrosite() {
		return this.idmicrosite;
	}

	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

	public String getVisible() {
		return this.visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getVisibleweb() {
		return this.visibleweb;
	}

	public void setVisibleweb(String visibleweb) {
		this.visibleweb = visibleweb;
	}

	public Archivo getImagen() {
		return this.imagen;
	}

	public void setImagen(Archivo imagen) {
		this.imagen = imagen;
	}

	public Tipo getTipo() {
		return this.tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public void addTraduccionMap(String lang, TraduccionNoticia traduccion) {
		this.setTraduccion(lang, traduccion);
	}

	public Integer getOrden() {
		return this.orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}
	
	///////
	// campos mapa
	public String getLatitud() {
		return latitud;
	}

	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}

	public String getLongitud() {
		return longitud;
	}

	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}

	public String getColorIcono() {
		return colorIcono;
	}

	public void setColorIcono(String colorIcono) {
		this.colorIcono = colorIcono;
	}
	////////

	@Override
	public Map<String, TraduccionNoticia> getTraducciones() {
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
			this.traducciones.put(idioma, (TraduccionNoticia) traduccion);
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

	@Override
	public void setIdi(String idi) {
		this.idi = idi;
	}
	

}