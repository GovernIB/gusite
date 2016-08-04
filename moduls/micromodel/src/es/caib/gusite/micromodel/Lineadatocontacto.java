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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import es.caib.gusite.micromodel.adapter.TraduccionAdapter;

/**
 * Clase Lineadatocontacto. Bean que define una Línea de Contacto. Modela la
 * tabla de BBDD GUS_FRMLIN
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_FRMLIN")
public class Lineadatocontacto extends AuditableModel implements Traducible2 {

	private static final long serialVersionUID = 8743296706196032258L;

	public static final int NUMERO_MAXIMO_SELECTOR = 30;

	@XmlAttribute
	@Id
	@SequenceGenerator(name = "GUS_LINEADATOCONTACTO_ID_GENERATOR", sequenceName = "GUS_SEQFLI", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_LINEADATOCONTACTO_ID_GENERATOR")
	@Column(name = "FLI_CODI")
	private Long id;

	@XmlAttribute
	@Column(name = "FLI_VISIB")
	private String visible;

	@XmlAttribute
	@Column(name = "FLI_TAMANO")
	private int tamano;

	@XmlAttribute
	@Column(name = "FLI_LINEAS")
	private int lineas;

	@XmlAttribute
	@Column(name = "FLI_OBLIGA")
	private int obligatorio;

	@XmlAttribute
	@Column(name = "FLI_ORDEN")
	private int orden;

	@XmlAttribute
	@Column(name = "FLI_TIPO")
	private String tipo;

	@XmlAttribute
	@Column(name = "FLI_FRMCOD")
	private Long idcontacto;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JoinColumn(name = "RID_FLICOD")
	@MapKey(name = "id.codigoIdioma")
	private Map<String, TraduccionLineadatocontacto> traducciones = new HashMap<String, TraduccionLineadatocontacto>();

	@XmlAttribute
	@Transient
	private String idi = Idioma.getIdiomaPorDefecto();

	@XmlElement(name = "traducciones")
	@XmlJavaTypeAdapter(TraduccionAdapter.class)
	public Map<String, TraduccionLineadatocontacto> getTranslates() {
		return this.traducciones;
	}

	public void setTranslates(
			Map<String, TraduccionLineadatocontacto> traducciones) {
		this.traducciones = traducciones;
	}

	@Override
	public Map<String, TraduccionLineadatocontacto> getTraducciones() {
		return this.traducciones;
	}

	@Override
	public void setTraducciones(Map traducciones) {
		this.traducciones = traducciones;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getLineas() {
		return this.lineas;
	}

	public void setLineas(int lineas) {
		this.lineas = lineas;
	}

	public int getObligatorio() {
		return this.obligatorio;
	}

	public void setObligatorio(int obligatorio) {
		this.obligatorio = obligatorio;
	}

	public int getOrden() {
		return this.orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public int getTamano() {
		return this.tamano;
	}

	public void setTamano(int tamano) {
		this.tamano = tamano;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getVisible() {
		return this.visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public Long getIdcontacto() {
		return this.idcontacto;
	}

	public void setIdcontacto(Long idcontacto) {
		this.idcontacto = idcontacto;
	}

	public void addTraduccionMap(String lang,
			TraduccionLineadatocontacto traduccion) {
		this.setTraduccion(lang, traduccion);
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
			this.traducciones.put(idioma,
					(TraduccionLineadatocontacto) traduccion);
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