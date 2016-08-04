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
 * Clase Contenido. Bean que define un Contenido. Modela la tabla de BBDD
 * GUS_CONTEN
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_CONTEN")
@SuppressWarnings({"rawtypes", "unchecked"})
public class Contenido extends AuditableModel implements Traducible2 {

	private static final long serialVersionUID = 4688044820582237768L;

	@XmlAttribute
	@Id
	@SequenceGenerator(name = "GUS_CONTENIDO_ID_GENERATOR", sequenceName = "GUS_SEQCON", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_CONTENIDO_ID_GENERATOR")
	@Column(name = "CON_CODI")
	private Long id;

	@XmlAttribute
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CON_CADUCA")
	private Date fcaducidad;

	@XmlAttribute
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CON_PUBLIC")
	private Date fpublicacion;

	@XmlAttribute
	@Column(name = "CON_ORDEN")
	private int orden;

	@XmlAttribute
	@Column(name = "CON_VISIB")
	private String visible;

	// bi-directional many-to-one association to GusDocus
	@XmlElement
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CON_IMGMEN")
	private Archivo imagenmenu; // TODO amartin: candidato a ser borrado del modelo. Parece que no se usa. Lo que sí se usa es la imagen de menú de la entidad Menu.

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "CID_CONCOD")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@MapKey(name = "id.codigoIdioma")
	@Fetch(FetchMode.SUBSELECT)
	private Map<String, TraduccionContenido> traducciones = new HashMap<String, TraduccionContenido>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CON_MNUCOD")
	private Menu menu;

	@Transient
	private String urlExterna = "";

	@Transient
	private String idi = Idioma.getIdiomaPorDefecto();

	@XmlElement
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CON_PPLCOD")
	private PersonalizacionPlantilla personalizacionPlantilla;

	@XmlElement(name = "traducciones")
	@XmlJavaTypeAdapter(TraduccionAdapter.class)
	public Map<String, TraduccionContenido> getTranslates() {
		return this.traducciones;
	}

	public void setTranslates(Map<String, TraduccionContenido> traducciones) {
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

	public Archivo getImagenmenu() {
		return this.imagenmenu;
	}

	public void setImagenmenu(Archivo imagenmenu) {
		this.imagenmenu = imagenmenu;
	}

	public int getOrden() {
		return this.orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public String getVisible() {
		return this.visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public void addTraduccionMap(String lang, TraduccionContenido traduccion) {
		this.setTraduccion(lang, traduccion);
	}

	public String getUrlExterna() {
		return this.urlExterna;
	}

	public void setUrlExterna(String urlExterna) {
		this.urlExterna = urlExterna;
	}

	public PersonalizacionPlantilla getPersonalizacionPlantilla() {
		return this.personalizacionPlantilla;
	}

	public void setPersonalizacionPlantilla(
			PersonalizacionPlantilla personalizacionPlantilla) {
		this.personalizacionPlantilla = personalizacionPlantilla;
	}

	@Override
	public Map<String, TraduccionContenido> getTraducciones() {
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
			this.traducciones.put(idioma, (TraduccionContenido) traduccion);
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

	public String getIdi() {
		return this.idi;
	}

	public Menu getMenu() {
		return this.menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Microsite getMicrosite() {
		if (this.getMenu() != null) {
			return this.getMenu().getMicrosite();
		} else {
			return null;
		}
	}

	@Override
	public Long getIdmicrosite() {
		if (this.getMenu() != null && this.getMenu().getMicrosite() != null) {
			return this.getMenu().getMicrosite().getId();
		} else {
			return null;
		}
	}

}
