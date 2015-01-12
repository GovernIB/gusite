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
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import es.caib.gusite.micromodel.adapter.TraduccionAdapter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Clase Lineadatocontacto. Bean que define una Línea  de Contacto. 
 * Modela la tabla de BBDD GUS_FRMLIN
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name="GUS_FRMLIN")
public class Lineadatocontacto implements Traducible2 {

	private static final long serialVersionUID = 8743296706196032258L;
	
	public static final int NUMERO_MAXIMO_SELECTOR = 30;

    @XmlAttribute
	@Id
	@SequenceGenerator(name="GUS_LINEADATOCONTACTO_ID_GENERATOR", sequenceName="GUS_SEQFLI", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GUS_LINEADATOCONTACTO_ID_GENERATOR")
	@Column(name="FLI_CODI")
	private Long id;

    @XmlAttribute
	@Column(name="FLI_VISIB")
    private String visible;

    @XmlAttribute
	@Column(name="FLI_TAMANO")
    private int tamano;

    @XmlAttribute
	@Column(name="FLI_LINEAS")
    private int lineas;

    @XmlAttribute
	@Column(name="FLI_OBLIGA")
    private int obligatorio;

    @XmlAttribute
	@Column(name="FLI_ORDEN")
    private int orden;

    @XmlAttribute
	@Column(name="FLI_TIPO")
    private String tipo;

    @XmlAttribute
	@Column(name="FLI_FRMCOD")
    private Long idcontacto;

  	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
  	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
  	@JoinColumn(name="RID_FLICOD")
  	@MapKey(name="id.codigoIdioma")
  	private Map<String, TraduccionLineadatocontacto> traducciones = new HashMap<String, TraduccionLineadatocontacto>();

    @XmlAttribute
  	@Transient
  	private String idi = Idioma.getIdiomaPorDefecto();

    @XmlElement(name = "traducciones")
    @XmlJavaTypeAdapter(TraduccionAdapter.class)
    public Map<String, TraduccionLineadatocontacto> getTranslates() {
        return traducciones;
    }

    public void setTranslates(Map<String, TraduccionLineadatocontacto> traducciones) {
        this.traducciones = traducciones;
    }

    public Map<String, TraduccionLineadatocontacto> getTraducciones() {
        return traducciones;
    }

    public void setTraducciones(Map traducciones) {
        this.traducciones = traducciones;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getLineas() {
		return lineas;
	}

	public void setLineas(int lineas) {
		this.lineas = lineas;
	}

	public int getObligatorio() {
		return obligatorio;
	}

	public void setObligatorio(int obligatorio) {
		this.obligatorio = obligatorio;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public int getTamano() {
		return tamano;
	}

	public void setTamano(int tamano) {
		this.tamano = tamano;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public Long getIdcontacto() {
		return idcontacto;
	}

	public void setIdcontacto(Long idcontacto) {
		this.idcontacto = idcontacto;
	}

	public void addTraduccionMap(String lang, TraduccionLineadatocontacto traduccion) {
        setTraduccion(lang, traduccion);
    }

	public String getIdi() {
		return idi;
	}

	public void setIdi(String idi) {
		this.idi = idi;
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
            traducciones.put(idioma, (TraduccionLineadatocontacto)traduccion);
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
	
}