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
 * Clase Componente. Bean que define un Componente. Modela la tabla de BBDD
 * GUS_COMPOS
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_COMPOS")
public class Componente extends AuditableModel implements Traducible2 {

	private static final long serialVersionUID = 5072614956105180822L;

	@XmlAttribute
	@Id
	@SequenceGenerator(name = "GUS_COMPONENTE_ID_GENERATOR", sequenceName = "GUS_SEQCMP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_COMPONENTE_ID_GENERATOR")
	@Column(name = "CMP_CODI")
	private Long id;

	@XmlAttribute
	@Column(name = "CMP_MICCOD")
	private Long idmicrosite;

	// bi-directional many-to-one association to GusDocus
	@XmlElement
	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "CMP_IMGBUL")
	private Archivo imagenbul;

	// bi-directional many-to-one association to GusTpnoti
	@XmlElement
	@ManyToOne
	@JoinColumn(name = "CMP_TIPO")
	private Tipo tipo;

	@XmlAttribute
	@Column(name = "CMP_NOMBRE")
	private String nombre;

	@XmlAttribute
	@Column(name = "CMP_SOLOIM")
	private String soloimagen;

	@XmlAttribute
	@Column(name = "CMP_FILAS")
	private String filas;

	@XmlAttribute
	@Column(name = "CMP_NUMELE")
	private Integer numelementos;

	@XmlAttribute
	@Column(name = "CMP_ORDEN")
	private Integer ordenacion;

	@Transient
	private String idi = Idioma.getIdiomaPorDefecto();

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy="componente")
	@MapKey(name = "id.codigoIdioma")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@Fetch(FetchMode.SUBSELECT)
	private Map<String, TraduccionComponente> traducciones = new HashMap<String, TraduccionComponente>();

	@XmlElement
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CMP_PPLCOD")
	private PersonalizacionPlantilla personalizacionPlantilla;

	@XmlElement(name = "traducciones")
	@XmlJavaTypeAdapter(TraduccionAdapter.class)
	public Map<String, TraduccionComponente> getTranslates() {
		return this.traducciones;
	}

	public void setTranslates(Map<String, TraduccionComponente> traducciones) {
		this.traducciones = traducciones;
	}

	@Override
	public Map<String, TraduccionComponente> getTraducciones() {
		return this.traducciones;
	}

	@Override
	public void setTraducciones(Map traducciones) {
		this.traducciones = traducciones;
	}

	public String getFilas() {
		return this.filas;
	}

	public void setFilas(String filas) {
		this.filas = filas;
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

	public Archivo getImagenbul() {
		return this.imagenbul;
	}

	public void setImagenbul(Archivo imagenbul) {
		this.imagenbul = imagenbul;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getNumelementos() {
		return this.numelementos;
	}

	public void setNumelementos(Integer numelementos) {
		this.numelementos = numelementos;
	}

	public Integer getOrdenacion() {
		return this.ordenacion;
	}

	public void setOrdenacion(Integer ordenacion) {
		this.ordenacion = ordenacion;
	}

	public String getSoloimagen() {
		return this.soloimagen;
	}

	public void setSoloimagen(String soloimagen) {
		this.soloimagen = soloimagen;
	}

	public Tipo getTipo() {
		return this.tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public PersonalizacionPlantilla getPersonalizacionPlantilla() {
		return this.personalizacionPlantilla;
	}

	public void setPersonalizacionPlantilla(
			PersonalizacionPlantilla personalizacionPlantilla) {
		this.personalizacionPlantilla = personalizacionPlantilla;
	}

	public void addTraduccionMap(String lang, TraduccionComponente traduccion) {
		this.setTraduccion(lang, traduccion);
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
			this.traducciones.put(idioma, (TraduccionComponente) traduccion);
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

}