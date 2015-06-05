package es.caib.gusite.micromodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
//No hay equivalente en JPA para DELETE_ORPHAN (Sí en JPA2). Ver http://stackoverflow.com/questions/2011519/jpa-onetomany-not-deleting-child
import org.hibernate.annotations.Cascade;

/**
 * Clase Microsite. Bean que define un Microsite. Modela la tabla de BBDD
 * GUS_MENU
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_MICROS")
@SuppressWarnings({"unchecked", "rawtypes"})
public class Microsite extends AuditableModel implements Traducible2 {

	private static final long serialVersionUID = -608935483446670811L;

	@XmlAttribute
	@Id
	@SequenceGenerator(name = "GUS_MICROSITE_ID_GENERATOR", sequenceName = "GUS_SEQMIC", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_MICROSITE_ID_GENERATOR")
	@Column(name = "MIC_CODI")
	private Long id;

	@Column(name = "MIC_CODUNI")
	private int unidadAdministrativa;

	// TEST
	@XmlAttribute
	@Column(name = "MIC_ANALYTICS")
	private String analytics;

	@XmlAttribute
	@Column(name = "MIC_URI")
	private String uri;
	// TEST

	@Transient
	private int idUA;

	@Transient
	private String nombreUA;

	@Transient
	private ArrayList<?> listaUAs;

	@Transient
	private int nivelAccesibilidad;

	@Temporal(TemporalType.DATE)
	@Column(name = "MIC_FECHA")
	private Date fecha;

	@Column(name = "MIC_VISIB")
	private String visible;

	@Column(name = "MIC_MENU")
	private String tipomenu;

	@Column(name = "MIC_PLANTI")
	private String plantilla;

	@Column(name = "MIC_OPFAQ")
	private String optFaq;

	@Column(name = "MIC_OPMAPA")
	private String optMapa;

	@Column(name = "MIC_OPCONT")
	private String optContacto;

	@Column(name = "MIC_OPT1")
	private String opt1;

	@Column(name = "MIC_OPT2")
	private String opt2;

	@Column(name = "MIC_OPT3")
	private String opt3;

	@Column(name = "MIC_OPT4")
	private String opt4;

	@Column(name = "MIC_OPT5")
	private String opt5;

	@Column(name = "MIC_OPT6")
	private String opt6;

	@Column(name = "MIC_OPT7")
	private String opt7;

	@ManyToOne
	@JoinColumn(name = "MIC_IMAGEN")
	private Archivo imagenPrincipal;

	@ManyToOne
	@JoinColumn(name = "MIC_IMGCAM")
	private Archivo imagenCampanya;

	@ManyToOne
	@JoinColumn(name = "MIC_CSS")
	private Archivo estiloCSS;

	@Column(name = "MIC_CSSPTR")
	private String estiloCSSPatron;

	@Column(name = "MIC_URLHOM")
	private String urlhome;

	@Column(name = "MIC_URLCAM")
	private String urlcampanya;

	@Column(name = "MIC_RESTRI")
	private String restringido;

	@Column(name = "MIC_ROL")
	private String rol;

	@Column(name = "MIC_DOMINI")
	private String domini;

	@Column(name = "MIC_NUMNOT")
	private int numeronoticias;

	@Column(name = "MIC_SERSEL")
	private String serviciosSeleccionados;

	@Column(name = "MIC_SEROFR")
	private String serviciosOfrecidos;

	@Column(name = "MIC_CAB")
	private String tipocabecera;

	@Column(name = "MIC_PIE")
	private String tipopie;

	@Column(name = "MIC_BUSCA")
	private String buscador;

	@Column(name = "MIC_CLAVE")
	private String claveunica;

	@Column(name = "MIC_MNUCRP")
	private String menucorporativo;

	@XmlAttribute
	@Column(name = "MIC_VERSION")
	private String versio;

	@XmlAttribute
	@Column(name = "MIC_TIPO_ACCESO")
	private String acceso;

	/**
	 * Pone el sitio web en modo desarrollo, deshabilitando caché de plantillas.
	 */
	@XmlAttribute
	@Column(name = "MIC_DESARROLLO", nullable = false, length = 1)
	private String desarrollo = "N";

	@Transient
	private List<?> tiposServicios;

	@Transient
	private String mvsUrlMigapan;

	@Transient
	private String mensajeInfo;

	@Transient
	private String mensajeError;

	@Transient
	private boolean funcionalidadTraduccion;

	@Transient
	private String mvsCssTiny;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "MID_MICCOD")
	@MapKey(name = "id.codigoIdioma")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@Fetch(FetchMode.SUBSELECT)
	private Map<String, TraduccionMicrosite> traducciones = new HashMap<String, TraduccionMicrosite>();

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy="microsite")
	@Fetch(FetchMode.SUBSELECT)
	@Cascade (org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Set<IdiomaMicrosite> idiomas = new HashSet<IdiomaMicrosite>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "microsite")
	private Set<PersonalizacionPlantilla> personalizacionesPlantilla = new HashSet<PersonalizacionPlantilla>(0);

	/**
	 * Tema a aplicar
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MIC_FTECOD")
	private TemaFront tema;

	@Transient
	private String idi = Idioma.getIdiomaPorDefecto();

	public Microsite() {
		this.setOptMapa("N");
		this.setOptFaq("N");
		this.setOptContacto("N");
		this.setOpt1("N");
		this.setOpt2("N");
		this.setOpt3("N");
		this.setOpt4("N");
		this.setOpt5("N");
		this.setOpt6("N");
		this.setOpt7("N");
		this.setFecha(new Date());
		this.setPlantilla("1");
	}

	public String getIdi() {
		return this.idi;
	}

	public Archivo getImagenCampanya() {
		return this.imagenCampanya;
	}

	public void setImagenCampanya(Archivo imagenCampanya) {
		this.imagenCampanya = imagenCampanya;
	}

	public String getRestringido() {
		return this.restringido;
	}

	public void setRestringido(String restringido) {
		this.restringido = restringido;
	}

	public String getRol() {
		return this.rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getDomini() {
		return this.domini;
	}

	// TEST
	public String getAnalytics() {
		return this.analytics;
	}

	public void setAnalytics(String analytics) {
		this.analytics = analytics;
	}

	public String getUri() {
		return this.uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	// TEST

	public void setDomini(String domini) {
		this.domini = domini;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Archivo getImagenPrincipal() {
		return this.imagenPrincipal;
	}

	public void setImagenPrincipal(Archivo imagenPrincipal) {
		this.imagenPrincipal = imagenPrincipal;
	}

	public String getOpt1() {
		return this.opt1;
	}

	public void setOpt1(String opt1) {
		this.opt1 = opt1;
	}

	public String getOpt2() {
		return this.opt2;
	}

	public void setOpt2(String opt2) {
		this.opt2 = opt2;
	}

	public String getOpt3() {
		return this.opt3;
	}

	public void setOpt3(String opt3) {
		this.opt3 = opt3;
	}

	public String getOpt4() {
		return this.opt4;
	}

	public void setOpt4(String opt4) {
		this.opt4 = opt4;
	}

	public String getOpt5() {
		return this.opt5;
	}

	public void setOpt5(String opt5) {
		this.opt5 = opt5;
	}

	public String getOpt6() {
		return this.opt6;
	}

	public void setOpt6(String opt6) {
		this.opt6 = opt6;
	}

	public String getOpt7() {
		return this.opt7;
	}

	public void setOpt7(String opt7) {
		this.opt7 = opt7;
	}

	public String getOptContacto() {
		return this.optContacto;
	}

	public void setOptContacto(String optContacto) {
		this.optContacto = optContacto;
	}

	public String getOptFaq() {
		return this.optFaq;
	}

	public void setOptFaq(String optFaq) {
		this.optFaq = optFaq;
	}

	public String getOptMapa() {
		return this.optMapa;
	}

	public void setOptMapa(String optMapa) {
		this.optMapa = optMapa;
	}

	public String getPlantilla() {
		return this.plantilla;
	}

	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}

	public String getTipomenu() {
		return this.tipomenu;
	}

	public void setTipomenu(String tipomenu) {
		this.tipomenu = tipomenu;
	}

	public String getEstiloCSSPatron() {
		return this.estiloCSSPatron;
	}

	public void setEstiloCSSPatron(String estiloCSSPatron) {
		this.estiloCSSPatron = estiloCSSPatron;
	}

	public int getUnidadAdministrativa() {
		return this.unidadAdministrativa;
	}

	public void setUnidadAdministrativa(int unidadAdministrativa) {
		this.unidadAdministrativa = unidadAdministrativa;
	}

	public String getVisible() {
		return this.visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public Set<IdiomaMicrosite> getIdiomas() {
		return this.idiomas;
	}

	public String[] getIdiomas(Set<?> idiomas) {
		Object[] array = idiomas.toArray();
		String[] idiomasArray = new String[idiomas.size()];
		int n = 0;
		for (Object obj : array) {
			idiomasArray[n] = ((IdiomaMicrosite) obj).getId().getCodigoIdioma();
			n++;
		}
		return idiomasArray;
	}

	public void setIdiomas(Set<IdiomaMicrosite> idiomas) {
		this.idiomas = idiomas;
	}

	public void setIdiomas(String[] idiomas) {
		Set<IdiomaMicrosite> borrarIdiomas = new HashSet<IdiomaMicrosite>();

		//Eliminamos los inexistentes
		List<String> idiomasList = Arrays.asList(idiomas);
		for (IdiomaMicrosite idiMicro : this.idiomas) {
			if (!idiomasList.contains(idiMicro.getId().getCodigoIdioma())) {
				borrarIdiomas.add(idiMicro);
			}
		}
		for (IdiomaMicrosite idiMicro : borrarIdiomas ) {
			this.idiomas.remove(idiMicro);			
		}

		//Añadimos los nuevos
		for (String lang : idiomas) {
			boolean existe = false;
			for (IdiomaMicrosite idiMicro : this.idiomas) {
				if (idiMicro.getId().getCodigoIdioma().compareTo(lang) == 0) {
					existe = true;
					break;
				}
			}
			if (!existe) {
				IdiomaMicrosite idioma = new IdiomaMicrosite();
				idioma.setId(new IdiomaMicrositePK());
				idioma.getId().setCodigoIdioma(lang);
				if (this.id != null) {
					idioma.getId().setCodigoMicrosite(this.id);
				}
				this.idiomas.add(idioma);
			}
		}
		//this.getIdiomas().clear();
		//this.setIdiomas(nuevosIdiomas);
		
	}

	public String getUrlhome() {
		return this.urlhome;
	}

	public void setUrlhome(String urlhome) {
		this.urlhome = urlhome;
	}

	public String getServiciosSeleccionados() {
		return this.serviciosSeleccionados;
	}

	public String[] getServiciosSeleccionados(String serviciosSeleccionados) {

		String serviciosSeleccionadosTabla[] = null;
		if (serviciosSeleccionados != null) {
			StringTokenizer st = new StringTokenizer(serviciosSeleccionados,
					";");
			int n = st.countTokens();
			serviciosSeleccionadosTabla = new String[n];
			for (int i = 0; i < n; i++) {
				serviciosSeleccionadosTabla[i] = st.nextToken();
			}
		}

		return serviciosSeleccionadosTabla;
	}

	public void setServiciosSeleccionados(String serviciosSeleccionados) {
		this.serviciosSeleccionados = serviciosSeleccionados;
	}

	public void setServiciosSeleccionados(String[] serviciosSeleccionadosTabla) {

		String serviciosSeleccionados = "";

		if (serviciosSeleccionadosTabla.length > 0) {
			for (String element : serviciosSeleccionadosTabla) {
				serviciosSeleccionados += element + ";";
			}
			serviciosSeleccionados = serviciosSeleccionados.substring(0,
					serviciosSeleccionados.length() - 1);
		}

		this.serviciosSeleccionados = serviciosSeleccionados;
	}

	public String getUrlcampanya() {
		return this.urlcampanya;
	}

	public void setUrlcampanya(String urlcampanya) {
		this.urlcampanya = urlcampanya;
	}

	public String getServiciosOfrecidos() {
		return this.serviciosOfrecidos;
	}

	public String[] getServiciosOfrecidos(String serviciosOfrecidos) {

		String serviciosOfrecidosTabla[] = null;
		if (serviciosOfrecidos != null) {
			StringTokenizer st = new StringTokenizer(serviciosOfrecidos, ";");
			int n = st.countTokens();
			serviciosOfrecidosTabla = new String[n];
			for (int i = 0; i < n; i++) {
				serviciosOfrecidosTabla[i] = st.nextToken();
			}
		}

		return serviciosOfrecidosTabla;
	}

	public void setServiciosOfrecidos(String serviciosOfrecidos) {
		this.serviciosOfrecidos = serviciosOfrecidos;
	}

	public void setServiciosOfrecidos(String[] serviciosOfrecidosTabla) {

		String serviciosOfrecidos = "";

		for (String element : serviciosOfrecidosTabla) {
			serviciosOfrecidos += element + ";";
		}
		if (serviciosOfrecidos.length() > 0) {
			serviciosOfrecidos = serviciosOfrecidos.substring(0,
					serviciosOfrecidos.length() - 1);
		}

		this.serviciosOfrecidos = serviciosOfrecidos;
	}

	public int getIdUA() {
		return this.idUA;
	}

	public void setIdUA(int idUA) {
		this.idUA = idUA;
	}

	public String getNombreUA() {
		return this.nombreUA;
	}

	public void setNombreUA(String nombreUA) {
		this.nombreUA = nombreUA;
	}

	public void setNivelAccesibilidad(int nivelAccesibilidad) {
		this.nivelAccesibilidad = nivelAccesibilidad;
	}

	public int getNivelAccesibilidad() {
		return this.nivelAccesibilidad;
	}

	public Archivo getEstiloCSS() {
		return this.estiloCSS;
	}

	public void setEstiloCSS(Archivo estiloCSS) {
		this.estiloCSS = estiloCSS;
	}

	public int getNumeronoticias() {
		return this.numeronoticias;
	}

	public void setNumeronoticias(int numeronoticias) {
		this.numeronoticias = numeronoticias;
	}

	public String getTipocabecera() {
		return this.tipocabecera;
	}

	public void setTipocabecera(String tipocabecera) {
		this.tipocabecera = tipocabecera;
	}

	public String getTipopie() {
		return this.tipopie;
	}

	public void setTipopie(String tipopie) {
		this.tipopie = tipopie;
	}

	public String getBuscador() {
		return this.buscador;
	}

	public void setBuscador(String buscador) {
		this.buscador = buscador;
	}

	public String getClaveunica() {
		return this.claveunica;
	}

	public void setClaveunica(String claveunica) {
		this.claveunica = claveunica;
	}

	public String getMenucorporativo() {
		return this.menucorporativo;
	}

	public void setMenucorporativo(String menucorporativo) {
		this.menucorporativo = menucorporativo;
	}

	public List<?> getTiposServicios() {
		return this.tiposServicios;
	}

	public void setTiposServicios(List<?> tiposServicios) {
		this.tiposServicios = tiposServicios;
	}

	public String getMvsUrlMigapan() {
		return this.mvsUrlMigapan;
	}

	public void setMvsUrlMigapan(String mvsUrlMigapan) {
		this.mvsUrlMigapan = mvsUrlMigapan;
	}

	/**
	 * * Pone el sitio web en modo desarrollo, deshabilitando caché de
	 * plantillas.
	 */
	public String getDesarrollo() {
		return this.desarrollo;
	}

	public void setDesarrollo(String desarrollo) {
		this.desarrollo = desarrollo;
	}

	public Set<PersonalizacionPlantilla> getPersonalizacionesPlantilla() {
		return this.personalizacionesPlantilla;
	}

	public void setPersonalizacionesPlantilla(
			Set<PersonalizacionPlantilla> personalizacionesPlantilla) {
		this.personalizacionesPlantilla = personalizacionesPlantilla;
	}

	/**
	 * * Tema a aplicar
	 */
	public TemaFront getTema() {
		return this.tema;
	}

	public void setTema(TemaFront tema) {
		this.tema = tema;
	}

	public String getMensajeInfo() {
		return this.mensajeInfo;
	}

	public void setMensajeInfo(String mensajeInfo) {
		this.mensajeInfo = mensajeInfo;
	}

	public String getMensajeError() {
		return this.mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public boolean getFuncionalidadTraduccion() {
		return this.funcionalidadTraduccion;
	}

	public void setFuncionalidadTraduccion() {

		this.funcionalidadTraduccion = false;
		if (!traductorHabilitat()) {
			return;
		}
		if (this.idiomas.size() > 1) {
			this.funcionalidadTraduccion = true;
		}
	}

	public static boolean traductorHabilitat() {
		return System.getProperty("es.caib.gusite.integracion.traductor").equals("S");
	}

	public String getMvsCssTiny() {
		return this.mvsCssTiny;
	}

	public void setMvsCssTiny(String mvsCssTiny) {
		this.mvsCssTiny = mvsCssTiny;
	}

	public ArrayList<?> getListaUAs() {
		return this.listaUAs;
	}

	public void setListaUAs(ArrayList<?> listaUAs) {
		this.listaUAs = listaUAs;
	}

	@Override
	public Map<String, TraduccionMicrosite> getTraducciones() {
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
			this.traducciones.put(idioma, (TraduccionMicrosite) traduccion);
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

	public String getVersio() {
		return this.versio;
	}

	public void setVersio(String versio) {
		this.versio = versio;
	}

	public String getAcceso() {
		return this.acceso;
	}

	public void setAcceso(String acceso) {
		this.acceso = acceso;
	}
	
	
	/**
	 * toString
	 * 
	 * @return String
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(getClass().getName()).append("@")
				.append(Integer.toHexString(hashCode())).append(" [");
		buffer.append("id").append("='").append(getId()).append("' ");
		buffer.append("]");

		return buffer.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof Plantilla))
			return false;
		Plantilla castOther = (Plantilla) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());

		return result;
	}	
}