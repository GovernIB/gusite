package es.caib.gusite.micromodel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import es.caib.gusite.micromodel.adapter.TraduccionAdapter;

/**
 * Clase Respuesta. Bean que define una Respuesta de la respuesta de una
 * encuesta. Modela la tabla de BBDD GUS_RESPUS.
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_RESPUS")
public class Respuesta extends AuditableModel implements Traducible2 {

	private static final long serialVersionUID = 2967918550911471091L;

	@XmlAttribute
	@Id
	@SequenceGenerator(name = "GUS_RESPUESTA_ID_GENERATOR", sequenceName = "GUS_SEQRES", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_RESPUESTA_ID_GENERATOR")
	@Column(name = "RES_CODI")
	private Long id;

	@XmlAttribute
	@Column(name = "RES_PRECOD")
	private Long idpregunta;

	@XmlAttribute
	@Column(name = "RES_ORDEN")
	private Integer orden;

	@XmlAttribute
	@Column(name = "RES_NRESP")
	private Integer nrespuestas;

	@XmlAttribute
	@Column(name = "RES_TIPO")
	private String tipo;

	@XmlElement
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "VOT_IDRESP")
	@MapKey
	private Set<RespuestaDato> respuestadato = new HashSet();

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JoinColumn(name = "REI_RESCOD")
	@MapKey(name = "id.codigoIdioma")
	@Fetch(FetchMode.SUBSELECT)
	private Map<String, TraduccionRespuesta> traducciones = new HashMap<String, TraduccionRespuesta>();

	@Transient
	private String idi = Idioma.getIdiomaPorDefecto();

	@XmlElement(name = "traducciones")
	@XmlJavaTypeAdapter(TraduccionAdapter.class)
	public Map<String, TraduccionRespuesta> getTranslates() {
		return this.traducciones;
	}

	public void setTranslates(Map<String, TraduccionRespuesta> traducciones) {
		this.traducciones = traducciones;
	}

	@Override
	public Map<String, TraduccionRespuesta> getTraducciones() {
		return this.traducciones;
	}

	@Override
	public void setTraducciones(Map traducciones) {
		this.traducciones = traducciones;
	}

	public Set<RespuestaDato> getRespuestadato() {
		return this.respuestadato;
	}

	public void setRespuestadato(Set<RespuestaDato> respuestadato) {
		this.respuestadato = respuestadato;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdpregunta() {
		return this.idpregunta;
	}

	public void setIdpregunta(Long idpregunta) {
		this.idpregunta = idpregunta;
	}

	public Integer getNrespuestas() {
		return this.nrespuestas;
	}

	public void setNrespuestas(Integer nrespuestas) {
		this.nrespuestas = nrespuestas;
	}

	public Integer getOrden() {
		return this.orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void addTraduccionMap(String lang, TraduccionRespuesta traduccion) {
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
			this.traducciones.put(idioma, (TraduccionRespuesta) traduccion);
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