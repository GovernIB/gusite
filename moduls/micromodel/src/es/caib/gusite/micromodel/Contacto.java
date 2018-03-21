package es.caib.gusite.micromodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
 * Clase Contacto. Bean que define un Contacto. Modela la tabla de BBDD
 * GUS_FRMCON
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_FRMCON")
public class Contacto extends AuditableModel implements Traducible2 {

	private static final long serialVersionUID = 465778084775544846L;

	public static final String RTYPE_TITULO = "1";
	public static final String RTYPE_TEXTO = "2";
	public static final String RTYPE_TEXTAREA = "3";
	public static final String RTYPE_SELECTOR = "4";
	public static final String RTYPE_SELECTORMULTIPLE = "5";
	public static final String RTYPE_TEXTODESCRIPTIVO = "6";

	@XmlAttribute
	@Id
	@SequenceGenerator(name = "GUS_COMPONENTE_ID_GENERATOR", sequenceName = "GUS_SEQFRM", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_COMPONENTE_ID_GENERATOR")
	@Column(name = "FRM_CODI")
	private Long id;

	@XmlAttribute
	@Column(name = "FRM_EMAIL")
	private String email;

	@XmlAttribute
	@Column(name = "FRM_VISIB")
	private String visible;

	@XmlAttribute
	@Column(name = "FRM_ANEXARCH")
	private String anexarch;

	@XmlAttribute
	@Column(name = "FRM_MICCOD")
	private Long idmicrosite;

	@XmlElement
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "FLI_FRMCOD")
	@MapKey(name = "id")
	@OrderBy("orden ASC")
	@Fetch(FetchMode.SUBSELECT)
	private List<Lineadatocontacto> lineasdatocontacto = new ArrayList();
	
	
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL },mappedBy="id.codigoFContacto")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@MapKey(name = "id.codigoIdioma")
	@Fetch(FetchMode.SUBSELECT)
	private Map<String, TraduccionFContacto> traducciones = new HashMap<String, TraduccionFContacto>();

	@Transient
	private String idi = Idioma.getIdiomaPorDefecto();
	
	
	@XmlElement(name = "traducciones")
	@XmlJavaTypeAdapter(TraduccionAdapter.class)
	public Map<String, TraduccionFContacto> getTranslates() {
		return this.traducciones;
	}

	public void setTranslates(Map<String, TraduccionFContacto> traducciones) {
		this.traducciones = traducciones;
	}
	
	
	public Contacto() {
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public Long getIdmicrosite() {
		return this.idmicrosite;
	}

	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

	public List<Lineadatocontacto> getLineasdatocontacto() {
		return this.lineasdatocontacto;
	}

	public void setLineasdatocontacto(List<Lineadatocontacto> lineasdatocontacto) {
		this.lineasdatocontacto = lineasdatocontacto;
	}

	// Metodo para poder leer la coleccion del XML
	public void addLineasdatocontacto(Lineadatocontacto lineas) {
		this.lineasdatocontacto.add(lineas);
	}

	public String getVisible() {
		return this.visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getAnexarch() {
		return this.anexarch;
	}

	public void setAnexarch(String anexarch) {
		this.anexarch = anexarch;
	}

	public String getTitulocontacto(String idi) {		
		return getTitulocontacto(idi, false);
	}
	
	public String getTitulocontacto(String idi, boolean nuevo) {
		
		String retorno = "";
		
		if(nuevo) {
			//extraido de traducciones
			try {
				retorno = ((TraduccionFContacto) this.getTraduccion(idi)).getNombre(); 
			} catch (Exception e) {
				retorno = "[sense titol]";
			}								
		}else {
			//extraido de titulo almacenado en la linea (obsoleto)
			try {
				Iterator<?> iter = this.lineasdatocontacto.iterator();
				Lineadatocontacto lineadatocontacto;
				while (iter.hasNext()) {
					lineadatocontacto = (Lineadatocontacto) iter.next();
					if (lineadatocontacto.getTipo().equals(RTYPE_TITULO)) {
						retorno = ((TraduccionLineadatocontacto) lineadatocontacto
								.getTraduccion(idi)).getTexto();
						break;
					}
				}
			} catch (Exception e) {
				retorno = "[sense titol]";
			}			
		}
		return retorno;
	}
	
	@Override
	public Map<String, TraduccionFContacto> getTraducciones() {
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
			this.traducciones.put(idioma, (TraduccionFContacto) traduccion);
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

	public String getIdi() {
		return this.idi;
	}

	@Override
	public void setIdi(String idi) {
		this.idi = idi;
	}

}