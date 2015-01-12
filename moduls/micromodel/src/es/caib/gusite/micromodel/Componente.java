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
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import es.caib.gusite.micromodel.adapter.TraduccionAdapter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Clase Componente. Bean que define un Componente. 
 * Modela la tabla de BBDD GUS_COMPOS
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name="GUS_COMPOS")
public class Componente implements Traducible2 {

	private static final long serialVersionUID = 5072614956105180822L;

    @XmlAttribute
	@Id
	@SequenceGenerator(name="GUS_COMPONENTE_ID_GENERATOR", sequenceName="GUS_SEQCMP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GUS_COMPONENTE_ID_GENERATOR")
	@Column(name="CMP_CODI")
	private Long id;

    @XmlAttribute
	@Column(name="CMP_MICCOD")
	private Long idmicrosite;
	
	//bi-directional many-to-one association to GusDocus
    @XmlElement
    @ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="CMP_IMGBUL")
	private Archivo imagenbul;

	//bi-directional many-to-one association to GusTpnoti
    @XmlElement
    @ManyToOne
	@JoinColumn(name="CMP_TIPO")
	private Tipo tipo;

    @XmlAttribute
    @Column(name="CMP_NOMBRE")
    private String nombre;

    @XmlAttribute
    @Column(name="CMP_SOLOIM")
	private String soloimagen;

    @XmlAttribute
    @Column(name="CMP_FILAS")
	private String filas;

    @XmlAttribute
    @Column(name="CMP_NUMELE")
	private Integer numelementos;

    @XmlAttribute
    @Column(name="CMP_ORDEN")
	private Integer ordenacion;

    @Transient
    private String idi = Idioma.getIdiomaPorDefecto();

  	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
  	@JoinColumn(name="CPI_CMPCOD")
  	@MapKey(name="id.codigoIdioma")
  	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	@Fetch(FetchMode.SUBSELECT)
  	private Map<String, TraduccionComponente> traducciones = new HashMap<String, TraduccionComponente>();

    @XmlElement(name = "traducciones")
    @XmlJavaTypeAdapter(TraduccionAdapter.class)
    public Map<String, TraduccionComponente> getTranslates() {
        return traducciones;
    }

    public void setTranslates(Map<String, TraduccionComponente> traducciones) {
        this.traducciones = traducciones;
    }

    public Map<String, TraduccionComponente> getTraducciones() {
        return traducciones;
    }

    public void setTraducciones(Map traducciones) {
        this.traducciones = traducciones;
    }

	public String getFilas() {
		return filas;
	}

	public void setFilas(String filas) {
		this.filas = filas;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdmicrosite() {
		return idmicrosite;
	}

	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

	public Archivo getImagenbul() {
		return imagenbul;
	}

	public void setImagenbul(Archivo imagenbul) {
		this.imagenbul = imagenbul;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getNumelementos() {
		return numelementos;
	}

	public void setNumelementos(Integer numelementos) {
		this.numelementos = numelementos;
	}

	public Integer getOrdenacion() {
		return ordenacion;
	}

	public void setOrdenacion(Integer ordenacion) {
		this.ordenacion = ordenacion;
	}

	public String getSoloimagen() {
		return soloimagen;
	}

	public void setSoloimagen(String soloimagen) {
		this.soloimagen = soloimagen;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public void addTraduccionMap(String lang, TraduccionComponente traduccion) {
        setTraduccion(lang, traduccion);
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
            traducciones.put(idioma, (TraduccionComponente)traduccion);
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

}