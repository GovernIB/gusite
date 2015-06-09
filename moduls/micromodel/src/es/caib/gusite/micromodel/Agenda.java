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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import es.caib.gusite.micromodel.adapter.TraduccionAdapter;

/**
 * Clase Agenda. Bean que define una Agenda. Modela la tabla de BBDD GUS_AGENDA
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_AGENDA")
@SuppressWarnings({"unchecked", "rawtypes"})
public class Agenda extends AuditableModel implements Traducible2 {

	private static final long serialVersionUID = 7222009737907543946L;

	@XmlAttribute
	@Id
	@SequenceGenerator(name = "GUS_AGENDA_ID_GENERATOR", sequenceName = "GUS_SEQAGE", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_AGENDA_ID_GENERATOR")
	@Column(name = "AGE_CODI")
	private Long id;

	@XmlAttribute
	@Column(name = "AGE_ORGANI")
	private String organizador;

	@XmlAttribute
	@Temporal(TemporalType.DATE)
	@Column(name = "AGE_INICIO")
	private Date finicio;

	@XmlAttribute
	@Temporal(TemporalType.DATE)
	@Column(name = "AGE_FIN")
	private Date ffin;

	@XmlAttribute
	@Column(name = "AGE_VISIB")
	private String visible;

	@XmlAttribute
	@Column(name = "AGE_MICCOD")
	private Long idmicrosite;

	@Transient
	private String idi = Idioma.getIdiomaPorDefecto();

	// bi-directional many-to-one association to GusActivi
	@XmlElement
	@ManyToOne()
	@JoinColumn(name = "AGE_ACTIVI")
	private Actividadagenda actividad;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "AID_AGECOD")
	@MapKey(name = "id.codigoIdioma")
	@Fetch(FetchMode.SUBSELECT)
	private Map<String, TraduccionAgenda> traducciones = new HashMap<String, TraduccionAgenda>();

	public String getIdi() {
		return this.idi;
	}

	@XmlElement(name = "traducciones")
	@XmlJavaTypeAdapter(TraduccionAdapter.class)
	public Map<String, TraduccionAgenda> getTranslates() {
		return this.traducciones;
	}

	public void setTranslates(Map<String, TraduccionAgenda> traducciones) {
		this.traducciones = traducciones;
	}

	@Override
	public Map<String, TraduccionAgenda> getTraducciones() {
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
			this.traducciones.put(idioma, (TraduccionAgenda) traduccion);
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

	public Actividadagenda getActividad() {
		return this.actividad;
	}

	public void setActividad(Actividadagenda actividad) {
		this.actividad = actividad;
	}

	public Date getFfin() {
		return this.ffin;
	}

	public void setFfin(Date fecha) {
		this.ffin = fecha;
	}

	public Date getFinicio() {
		return this.finicio;
	}

	public void setFinicio(Date finicio) {
		this.finicio = finicio;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrganizador() {
		return this.organizador;
	}

	@Override
	public Long getIdmicrosite() {
		return this.idmicrosite;
	}

	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

	public void setOrganizador(String organizador) {
		this.organizador = organizador;
	}

	public String getVisible() {
		return this.visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public void addTraduccionMap(String lang, TraduccionAgenda traduccion) {
		this.setTraduccion(lang, traduccion);
	}

}