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

import es.caib.gusite.micromodel.adapter.TraduccionAdapter;

/**
 * Clase Faq. Bean que define una Faq. Modela la tabla de BBDD GUS_FAQ
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_FAQ")
public class Faq extends AuditableModel implements Traducible2 {

	private static final long serialVersionUID = 2723640089721073919L;

	@XmlAttribute
	@Id
	@SequenceGenerator(name = "GUS_FAQ_ID_GENERATOR", sequenceName = "GUS_SEQFAQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_FAQ_ID_GENERATOR")
	@Column(name = "FAQ_CODI")
	private Long id;

	@XmlAttribute
	@Column(name = "FAQ_MICCOD")
	private Long idmicrosite;

	@XmlAttribute
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FAQ_FECHA")
	private Date fecha;

	@XmlAttribute
	@Column(name = "FAQ_VISIB")
	private String visible;

	@XmlElement
	@ManyToOne
	@JoinColumn(name = "FAQ_CODTEM")
	private Temafaq tema;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "FID_FAQCOD")
	@MapKey(name = "id.codigoIdioma")
	private Map<String, TraduccionFaq> traducciones = new HashMap<String, TraduccionFaq>();

	@Transient
	private String idi = Idioma.getIdiomaPorDefecto();

	@XmlElement(name = "traducciones")
	@XmlJavaTypeAdapter(TraduccionAdapter.class)
	public Map<String, TraduccionFaq> getTranslates() {
		return this.traducciones;
	}

	public void setTranslates(Map<String, TraduccionFaq> traducciones) {
		this.traducciones = traducciones;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
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

	public Temafaq getTema() {
		return this.tema;
	}

	public void setTema(Temafaq tema) {
		this.tema = tema;
	}

	public String getVisible() {
		return this.visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public void addTraduccionMap(String lang, TraduccionFaq traduccion) {
		this.setTraduccion(lang, traduccion);
	}

	@Override
	public Map<String, TraduccionFaq> getTraducciones() {
		return this.traducciones;
	}

	@Override
	public void setTraducciones(Map traducciones) {
		this.traducciones = traducciones;
	}

	public String getIdi() {
		return this.idi;
	}

	@Override
	public void setIdi(String idi) {
		this.idi = idi;
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
			this.traducciones.put(idioma, (TraduccionFaq) traduccion);
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

}