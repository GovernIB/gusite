package es.caib.gusite.micromodel;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Clase Archivo. Bean que define una Archivo. Modela la tabla de BBDD GUS_DOCUS
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_DOCUS")
@SuppressWarnings({"rawtypes", "unchecked"}) // Para los Map sin parametrizar.
public class Archivo extends AuditableModel implements Indexable {
	
	public static final String ESTADO_ENFILESYSTEM_SI = "S";
	public static final String ESTADO_ENFILESYSTEM_NO = "N";
	public static final String ESTADO_ENFILESYSTEM_ERROR_EXPORTACION = "E";
	public static final String ESTADO_ENFILESYSTEM_PENDIENTE_ANALIZAR = "X";

	@SuppressWarnings("unused")
	private static final long serialVersionUID = -3122017714028641802L;

	@XmlAttribute
	@Id
	@SequenceGenerator(name = "GUS_DOCUS_ID_GENERATOR", sequenceName = "GUS_SEQDOC", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_DOCUS_ID_GENERATOR")
	@Column(name = "DCM_CODI")
	private Long id;

	@XmlAttribute
	@Column(name = "DCM_TIPO")
	private String mime;

	@XmlAttribute
	@Column(name = "DCM_NOMBRE")
	private String nombre;

	@XmlAttribute
	@Column(name = "DCM_TAMANO")
	private long peso;

	/* IMPORTANTE: 
	 * 		Comentado para que funcione con postgres bytea. 
	 * 		Parece ser que no afecta a oracle
	@Lob
	*/
	//@Column(name = "DCM_DATOS")
	@Transient
	private byte[] datos;

	@XmlAttribute
	@Column(name = "DCM_PAGINA")
	private Long pagina;

	@XmlAttribute
	@Column(name = "DCM_MICCOD")
	private Long idmicrosite;
	
	// Indica si el fichero ha sido exportado a File system (S) no ha sido exportado (N), está pendiente de analizar (X) o ocurrio un error al exportar (E)
	@XmlAttribute
	@Column(name = "DCM_INFSYS")
	private String exportadoAFileSystem;

	@Transient
	private Map traducciones = new HashMap();

	@Transient
	private String idi = Idioma.getIdiomaPorDefecto();

	protected Map getTraducciones() {
		return this.traducciones;
	}

	protected void setTraducciones(Map traducciones) {
		this.traducciones = traducciones;
	}

	/**
	 * Obtiene la traduccion por defecto.
	 * 
	 * @return La traduccion en el idioma per defecto.
	 */
	public Traduccion getTraduccion() {
		return (Traduccion) this.traducciones.get(Idioma.getIdiomaPorDefecto());
	}

	/**
	 * Obtiene la traduccion en un idioma determinado o <code>null</code>.
	 * 
	 * @param idioma
	 *            Idioma de la traducción.
	 * @return Traduccion en el idioma indicado o <code>null</code> si no
	 *         existe.
	 */
	public Traduccion getTraduccion(String idioma) {
		return (Traduccion) this.traducciones.get(idioma);
	}

	/**
	 * Fija una traducción en un idioma determinado, o la borra si es
	 * <code>null</code>.
	 * 
	 * @param idioma
	 *            Idioma de la traducción,
	 * @param traduccion
	 *            La traducción a fijar.
	 */
	public void setTraduccion(String idioma, Traduccion traduccion) {
		if (traduccion == null) {
			this.traducciones.remove(idioma);
		} else {
			this.traducciones.put(idioma, traduccion);
		}
	}

	/**
	 * Obtiene la traduccion por defecto.
	 * 
	 * @return La traduccion en el idioma per defecto.
	 */
	public Traduccion getTraduce() {
		return (Traduccion) this.traducciones.get(this.idi);
	}

	public Map getTraduccionMap() {
		return this.traducciones;
	}

	public void setTraduccionMap(Map traduccionMap) {
		this.traducciones = new HashMap(traduccionMap);
	}

	public void setIdi(String idi) {
		this.idi = idi;
	}

	public Archivo() {
	}

	public Long getPagina() {
		return this.pagina;
	}

	public void setPagina(Long pagina) {
		this.pagina = pagina;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMime() {
		return this.mime;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public long getPeso() {
		return this.peso;
	}

	public void setPeso(long peso) {
		this.peso = peso;
	}

	public byte[] getDatos() {
		return this.datos;
	}

	public void setDatos(byte[] datos) {
		this.datos = datos;
	}

	@Override
	public Long getIdmicrosite() {
		return this.idmicrosite;
	}

	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

	public String getIdi() {
		return this.idi;
	}

	/**
	 * @return the exportadoAFileSystem
	 */
	public String getExportadoAFileSystem() {
		return exportadoAFileSystem;
	}

	/**
	 * @param exportadoAFileSystem the exportadoAFileSystem to set
	 */
	public void setExportadoAFileSystem(String exportadoAFileSystem) {
		this.exportadoAFileSystem = exportadoAFileSystem;
	}
}
