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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import es.caib.gusite.micromodel.adapter.TraduccionAdapter;

/**
 * Clase Temafaq. Bean que define un Tema. Modela la tabla de BBDD GUS_TEMAS.
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_TEMAS")
public class Temafaq extends AuditableModel implements Traducible2 {

	private static final long serialVersionUID = -1590941371205365963L;

	@XmlAttribute
	@Id
	@SequenceGenerator(name = "GUS_TEMAS_ID_GENERATOR", sequenceName = "GUS_SEQTEM", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_TEMAS_ID_GENERATOR")
	@Column(name = "TEM_CODI")
	private Long id;

	@XmlAttribute
	@Column(name = "TEM_MICCOD")
	private Long idmicrosite;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "TID_TEMCOD")
	@MapKey(name = "id.codigoIdioma")
	@Fetch(FetchMode.SUBSELECT)
	private Map<String, TraduccionTemafaq> traducciones = new HashMap<String, TraduccionTemafaq>();

	@Transient
	private String idi = Idioma.getIdiomaPorDefecto();

	@XmlElement(name = "traducciones")
	@XmlJavaTypeAdapter(TraduccionAdapter.class)
	public Map<String, TraduccionTemafaq> getTranslates() {
		return this.traducciones;
	}

	public void setTranslates(Map<String, TraduccionTemafaq> traducciones) {
		this.traducciones = traducciones;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public String getIdi() {
		return this.idi;
	}

	@Override
	public Long getIdmicrosite() {
		return this.idmicrosite;
	}

	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void addTraduccionMap(String lang, TraduccionTemafaq traduccion) {
		this.setTraduccion(lang, traduccion);
	}

	@Override
	public Map<String, TraduccionTemafaq> getTraducciones() {
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
			this.traducciones.put(idioma, (TraduccionTemafaq) traduccion);
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