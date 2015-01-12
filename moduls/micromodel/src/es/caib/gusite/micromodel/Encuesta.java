package es.caib.gusite.micromodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import javax.persistence.OrderBy;
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
 * Clase Encuesta. Bean que define una Encuesta. 
 * Modela la tabla de BBDD GUS_ENCUST
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name="GUS_ENCUST")
public class Encuesta implements Traducible2 {

	private static final long serialVersionUID = 2356576663603622282L;

    @XmlAttribute
	@Id
	@SequenceGenerator(name="GUS_ENCUESTA_ID_GENERATOR", sequenceName="GUS_SEQENC", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GUS_ENCUESTA_ID_GENERATOR")
	@Column(name="ENC_CODI")
	private Long id;

    @XmlAttribute
	@Column(name="ENC_MICCOD")
    private Long idmicrosite;

    @XmlAttribute
	@Temporal(TemporalType.DATE)
	@Column(name="ENC_CADUCA")
    private Date fcaducidad;

    @XmlAttribute
	@Temporal(TemporalType.DATE)
	@Column(name="ENC_PUBLIC")
    private Date fpublicacion;

    @XmlAttribute
	@Column(name="ENC_VISIB")
    private String visible;

    @XmlAttribute
	@Column(name="ENC_INDIV")
    private String indivisible;

    @XmlAttribute
	@Column(name="ENC_PAGINA")
    private Integer paginacion;

    @XmlAttribute
	@Column(name="ENC_MUESTR")
    private String mostrar;

    @XmlAttribute
	@Column(name="ENC_IDENTIF")
    private String identificacion;

    @XmlElement
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="PRE_ENCCOD")
	@MapKey(name="id")
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy("orden ASC")
    private List<Pregunta> preguntas = new ArrayList();

	@Transient
    private String votoDuplicado;
	
	//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
  	@JoinColumn(name="EID_ENCCOD")
	@MapKey(name="id.codigoIdioma")
	@Fetch(FetchMode.SUBSELECT)
	private Map<String, TraduccionEncuesta> traducciones = new HashMap<String, TraduccionEncuesta>();

	@Transient
  	private String idi = Idioma.getIdiomaPorDefecto();

    @XmlElement(name = "traducciones")
    @XmlJavaTypeAdapter(TraduccionAdapter.class)
    public Map<String, TraduccionEncuesta> getTranslates() {
        return traducciones;
    }

    public void setTranslates(Map<String, TraduccionEncuesta> traducciones) {
        this.traducciones = traducciones;
    }

    public Map<String, TraduccionEncuesta> getTraducciones() {
        return traducciones;
    }

    public void setTraducciones(Map traducciones) {
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

	public Long getIdmicrosite() {
		return idmicrosite;
	}

	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

	public String getIndivisible() {
		return indivisible;
	}

	public void setIndivisible(String indivisible) {
		this.indivisible = indivisible;
	}

	public Integer getPaginacion() {
		return paginacion;
	}

	public void setPaginacion(Integer paginacion) {
		this.paginacion = paginacion;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public List<Pregunta> getPreguntas() {
		return preguntas;
	}

	public void setPreguntas(List<Pregunta> preguntas) {
		this.preguntas = preguntas;
	}

	// Metodos para poder leer las colecciones del XML	
	public void addPreguntas(Pregunta pre) {
		preguntas.add(pre);
	}
	
	public void addTraduccionMap(String lang, TraduccionEncuesta traduccion) {
        setTraduccion(lang, traduccion);
    }

	public String getMostrar() {
		return mostrar;
	}

	public void setMostrar(String mostrar) {
		this.mostrar = mostrar;
	}
	
	public String getVotoDuplicado() {
		return votoDuplicado;
	}

	public void setVotoDuplicado(String votoDuplicado) {
		this.votoDuplicado = votoDuplicado;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getIdi() {
		return idi;
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
            traducciones.put(idioma, (TraduccionEncuesta)traduccion);
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