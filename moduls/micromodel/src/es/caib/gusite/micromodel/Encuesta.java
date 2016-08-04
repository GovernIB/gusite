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
 * Clase Encuesta. Bean que define una Encuesta. Modela la tabla de BBDD
 * GUS_ENCUST
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_ENCUST")
public class Encuesta extends AuditableModel implements Traducible2 {

	private static final long serialVersionUID = 2356576663603622282L;

	@XmlAttribute
	@Id
	@SequenceGenerator(name = "GUS_ENCUESTA_ID_GENERATOR", sequenceName = "GUS_SEQENC", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_ENCUESTA_ID_GENERATOR")
	@Column(name = "ENC_CODI")
	private Long id;

	@XmlAttribute
	@Column(name = "ENC_MICCOD")
	private Long idmicrosite;

	@XmlAttribute
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENC_CADUCA")
	private Date fcaducidad;

	@XmlAttribute
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENC_PUBLIC")
	private Date fpublicacion;

	@XmlAttribute
	@Column(name = "ENC_VISIB")
	private String visible;

	@XmlAttribute
	@Column(name = "ENC_INDIV")
	private String indivisible;

	@XmlAttribute
	@Column(name = "ENC_PAGINA")
	private Integer paginacion;

	@XmlAttribute
	@Column(name = "ENC_MUESTR")
	private String mostrar;

	@XmlAttribute
	@Column(name = "ENC_IDENTIF")
	private String identificacion;

	@XmlElement
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "PRE_ENCCOD")
	@MapKey(name = "id")
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy("orden ASC")
	private List<Pregunta> preguntas = new ArrayList();

	@Transient
	private String votoDuplicado;

	// @Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "EID_ENCCOD")
	@MapKey(name = "id.codigoIdioma")
	@Fetch(FetchMode.SUBSELECT)
	private Map<String, TraduccionEncuesta> traducciones = new HashMap<String, TraduccionEncuesta>();

	@Transient
	private String idi = Idioma.getIdiomaPorDefecto();

	@XmlElement(name = "traducciones")
	@XmlJavaTypeAdapter(TraduccionAdapter.class)
	public Map<String, TraduccionEncuesta> getTranslates() {
		return this.traducciones;
	}

	public void setTranslates(Map<String, TraduccionEncuesta> traducciones) {
		this.traducciones = traducciones;
	}

	@Override
	public Map<String, TraduccionEncuesta> getTraducciones() {
		return this.traducciones;
	}

	@Override
	public void setTraducciones(Map traducciones) {
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

	public String getIndivisible() {
		return this.indivisible;
	}

	public void setIndivisible(String indivisible) {
		this.indivisible = indivisible;
	}

	public Integer getPaginacion() {
		return this.paginacion;
	}

	public void setPaginacion(Integer paginacion) {
		this.paginacion = paginacion;
	}

	public String getVisible() {
		return this.visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public List<Pregunta> getPreguntas() {
		return this.preguntas;
	}

	public void setPreguntas(List<Pregunta> preguntas) {
		this.preguntas = preguntas;
	}

	// Metodos para poder leer las colecciones del XML
	public void addPreguntas(Pregunta pre) {
		this.preguntas.add(pre);
	}

	public void addTraduccionMap(String lang, TraduccionEncuesta traduccion) {
		this.setTraduccion(lang, traduccion);
	}

	public String getMostrar() {
		return this.mostrar;
	}

	public void setMostrar(String mostrar) {
		this.mostrar = mostrar;
	}

	public String getVotoDuplicado() {
		return this.votoDuplicado;
	}

	public void setVotoDuplicado(String votoDuplicado) {
		this.votoDuplicado = votoDuplicado;
	}

	public String getIdentificacion() {
		return this.identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getIdi() {
		return this.idi;
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
			this.traducciones.put(idioma, (TraduccionEncuesta) traduccion);
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
		for (Pregunta pregunta : this.preguntas) {
			pregunta.setIdi(idi);
		}
	}
}