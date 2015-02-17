package es.caib.gusite.micromodel;

import java.io.Serializable;
import java.util.Date;
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
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import es.caib.gusite.micromodel.adapter.TraduccionAdapter;

/**
 * Clase MicrositeCompleto. Bean que define un Microsite completo. Modela la
 * tabla de BBDD GUS_MICROS y todas las tablas de componentes que tiene
 * relacionado un Microsite (agendas, documentos, etc.)
 * 
 * @author Indra
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({ IdiomaMicrosite.class, Menu.class, Temafaq.class, Faq.class,
		Actividadagenda.class, Agenda.class, Tipo.class, Noticia.class,
		Frqssi.class, Contacto.class, Componente.class, Encuesta.class,
		TraduccionMicrosite.class })
@Entity
@Table(name = "GUS_MICROS")
public class MicrositeCompleto implements Traducible2, Serializable, Auditable {

	private static final long serialVersionUID = 5465456305944839473L;

	@XmlAttribute
	@Id
	@SequenceGenerator(name = "GUS_MICROSITECOMPLETO_ID_GENERATOR", sequenceName = "GUS_SEQMIC", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_MICROSITECOMPLETO_ID_GENERATOR")
	@Column(name = "MIC_CODI")
	private Long Id;

	@XmlAttribute
	@Column(name = "MIC_CODUNI")
	private int idUA;

	// TEST
	@XmlAttribute
	@Column(name = "MIC_ANALYTICS")
	private String analytics;

	@XmlAttribute
	@Column(name = "MIC_URI")
	private String uri;
	// TEST

	@XmlAttribute
	@Temporal(TemporalType.DATE)
	@Column(name = "MIC_FECHA")
	private Date fecha;

	@XmlAttribute
	@Column(name = "MIC_VISIB")
	private String visible;

	@XmlElement
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "MIC_IMAGEN")
	private Archivo imagenPrincipal;

	@XmlAttribute
	@Column(name = "MIC_MENU")
	private String tipomenu;

	@XmlAttribute
	@Column(name = "MIC_PLANTI")
	private String plantilla;

	@XmlAttribute
	@Column(name = "MIC_OPFAQ")
	private String optFaq;

	@XmlAttribute
	@Column(name = "MIC_OPMAPA")
	private String optMapa;

	@XmlAttribute
	@Column(name = "MIC_OPCONT")
	private String optContacto;

	@XmlAttribute
	@Column(name = "MIC_OPT1")
	private String opt1;

	@XmlAttribute
	@Column(name = "MIC_OPT2")
	private String opt2;

	@XmlAttribute
	@Column(name = "MIC_OPT3")
	private String opt3;

	@XmlAttribute
	@Column(name = "MIC_OPT4")
	private String opt4;

	@XmlAttribute
	@Column(name = "MIC_OPT5")
	private String opt5;

	@XmlAttribute
	@Column(name = "MIC_OPT6")
	private String opt6;

	@XmlAttribute
	@Column(name = "MIC_OPT7")
	private String opt7;

	@XmlAttribute
	@Column(name = "MIC_URLHOM")
	private String urlhome;

	@XmlElement
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "MIC_IMGCAM")
	private Archivo imagenCampanya;

	@XmlElement
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "MIC_CSS")
	private Archivo estiloCSS;

	@XmlAttribute
	@Column(name = "MIC_CSSPTR")
	private String estiloCSSPatron;

	@XmlAttribute
	@Column(name = "MIC_RESTRI")
	private String restringido;

	@XmlAttribute
	@Column(name = "MIC_ROL")
	private String rol;

	@XmlAttribute
	@Column(name = "MIC_NUMNOT")
	private int numeronoticias;

	@XmlAttribute
	@Column(name = "MIC_SERSEL")
	private String serviciosSeleccionados;

	@XmlAttribute
	@Column(name = "MIC_URLCAM")
	private String urlcampanya;

	@XmlAttribute
	@Column(name = "MIC_SEROFR")
	private String serviciosOfrecidos;

	@XmlAttribute
	@Column(name = "MIC_CAB")
	private String tipocabecera;

	@XmlAttribute
	@Column(name = "MIC_PIE")
	private String tipopie;

	@XmlAttribute
	@Column(name = "MIC_BUSCA")
	private String buscador;

	@XmlAttribute
	@Transient
	private String version;

	@XmlAttribute
	@Column(name = "MIC_CLAVE")
	private String claveunica;

	@XmlAttribute
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
	private String desarrollo;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "IMI_MICCOD")
	@Fetch(FetchMode.SUBSELECT)
	private Set<IdiomaMicrosite> idiomas = new HashSet<IdiomaMicrosite>();

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "DCM_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
	private Set<Archivo> docus = new HashSet();

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "MNU_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
	private Set<Menu> menus = new HashSet();

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "TEM_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
	private Set<Temafaq> temas = new HashSet();

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "FAQ_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
	private Set<Faq> faqs = new HashSet();

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "ACT_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
	private Set<Actividadagenda> actividades = new HashSet();

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "AGE_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
	private Set<Agenda> agendas = new HashSet();

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "TPN_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
	private Set<Tipo> tiponotis = new HashSet();

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "NOT_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
	private Set<Noticia> noticias = new HashSet();

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "FRQ_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
	private Set<Frqssi> frqssis = new HashSet();

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "FRM_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
	private Set<Contacto> formularioscontacto = new HashSet();

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "CMP_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
	private Set<Componente> componentes = new HashSet();

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "ENC_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
	private Set<Encuesta> encuestas = new HashSet();

	@XmlElement
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "microsite")
	@Fetch(FetchMode.SUBSELECT)
	private Set<PersonalizacionPlantilla> personalizacionesPlantilla = new HashSet<PersonalizacionPlantilla>(
			0);

	/**
	 * Tema a aplicar
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MIC_FTECOD")
	private TemaFront tema;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JoinColumn(name = "MID_MICCOD")
	@MapKey(name = "id.codigoIdioma")
	@Fetch(FetchMode.SUBSELECT)
	private Map<String, TraduccionMicrosite> traducciones = new HashMap<String, TraduccionMicrosite>();

	@Transient
	private String idi = Idioma.getIdiomaPorDefecto();

	@XmlElement(name = "idiomaMicrosite", type = IdiomaMicrosite.class)
	@XmlElementWrapper(name = "idiomas")
	public Set getIdiomas() {
		return this.idiomas;
	}

	public void setIdiomas(Set idiomas) {
		this.idiomas = idiomas;
	}

	@XmlElement(name = "docus", type = Archivo.class)
	@XmlElementWrapper(name = "documento")
	public Set getDocus() {
		return this.docus;
	}

	public void setDocus(Set docus) {
		this.docus = docus;
	}

	@XmlElement(name = "menu", type = Menu.class)
	@XmlElementWrapper(name = "menus")
	public Set getMenus() {
		return this.menus;
	}

	public void setMenus(Set menus) {
		this.menus = menus;
	}

	@XmlElement(name = "temafaq", type = Temafaq.class)
	@XmlElementWrapper(name = "temas")
	public Set getTemas() {
		return this.temas;
	}

	public void setTemas(Set temas) {
		this.temas = temas;
	}

	@XmlElement(name = "faq", type = Faq.class)
	@XmlElementWrapper(name = "faqs")
	public Set getFaqs() {
		return this.faqs;
	}

	public void setFaqs(Set faqs) {
		this.faqs = faqs;
	}

	@XmlElement(name = "actividadagenda", type = Actividadagenda.class)
	@XmlElementWrapper(name = "actividades")
	public Set getActividades() {
		return this.actividades;
	}

	public void setActividades(Set actividades) {
		this.actividades = actividades;
	}

	@XmlElement(name = "agenda", type = Agenda.class)
	@XmlElementWrapper(name = "agendas")
	public Set getAgendas() {
		return this.agendas;
	}

	public void setAgendas(Set agendas) {
		this.agendas = agendas;
	}

	@XmlElement(name = "tipo", type = Tipo.class)
	@XmlElementWrapper(name = "tiponotis")
	public Set getTiponotis() {
		return this.tiponotis;
	}

	public void setTiponotis(Set tiponotis) {
		this.tiponotis = tiponotis;
	}

	@XmlElement(name = "noticia", type = Noticia.class)
	@XmlElementWrapper(name = "noticias")
	public Set getNoticias() {
		return this.noticias;
	}

	public void setNoticias(Set noticias) {
		this.noticias = noticias;
	}

	@XmlElement(name = "frqssi", type = Frqssi.class)
	@XmlElementWrapper(name = "frqssis")
	public Set getFrqssis() {
		return this.frqssis;
	}

	public void setFrqssis(Set frqssis) {
		this.frqssis = frqssis;
	}

	@XmlElement(name = "contacto", type = Contacto.class)
	@XmlElementWrapper(name = "formularioscontacto")
	public Set getFormularioscontacto() {
		return this.formularioscontacto;
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

	public void setFormularioscontacto(Set formularioscontacto) {
		this.formularioscontacto = formularioscontacto;
	}

	@XmlElement(name = "componente", type = Componente.class)
	@XmlElementWrapper(name = "componentes")
	public Set getComponentes() {
		return this.componentes;
	}

	public void setComponentes(Set componentes) {
		this.componentes = componentes;
	}

	@XmlElement(name = "encuesta", type = Encuesta.class)
	@XmlElementWrapper(name = "encuestas")
	public Set getEncuestas() {
		return this.encuestas;
	}

	public void setEncuestas(Set encuestas) {
		this.encuestas = encuestas;
	}

	@XmlElement(name = "traducciones")
	@XmlJavaTypeAdapter(TraduccionAdapter.class)
	public Map<String, TraduccionMicrosite> getTranslates() {
		return this.traducciones;
	}

	public void setTranslates(Map<String, TraduccionMicrosite> traducciones) {
		this.traducciones = traducciones;
	}

	@Override
	public Map<String, TraduccionMicrosite> getTraducciones() {
		return this.traducciones;
	}

	@Override
	public void setTraducciones(Map traducciones) {
		this.traducciones = traducciones;
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

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Long getId() {
		return this.Id;
	}

	public void setId(Long id) {
		this.Id = id;
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

	public String getVisible() {
		return this.visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
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

	public void setServiciosSeleccionados(String serviciosSeleccionados) {
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

	public void setServiciosOfrecidos(String serviciosOfrecidos) {
		this.serviciosOfrecidos = serviciosOfrecidos;
	}

	public int getIdUA() {
		return this.idUA;
	}

	public void setIdUA(int idUA) {
		this.idUA = idUA;
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

	// Metodos para poder leer las colecciones del XML

	public void addIdioma(String idi) {
		IdiomaMicrosite idioma = new IdiomaMicrosite();
		idioma.setId(new IdiomaMicrositePK());
		idioma.getId().setCodigoIdioma(idi);
		idioma.getId().setCodigoMicrosite(this.Id);
		this.idiomas.add(idioma);
	}

	public void addDocus(Archivo fich) {
		this.docus.add(fich);
	}

	public void addMenus(Menu men) {
		this.menus.add(men);
	}

	public void addTemas(Temafaq tem) {
		this.temas.add(tem);
	}

	public void addFaqs(Faq faq) {
		this.faqs.add(faq);
	}

	public void addActividades(Actividadagenda act) {
		this.actividades.add(act);
	}

	public void addAgendas(Agenda age) {
		this.agendas.add(age);
	}

	public void addTiponotis(Tipo tp) {
		this.tiponotis.add(tp);
	}

	public void addNoticias(Noticia not) {
		this.noticias.add(not);
	}

	public void addFrqssis(Frqssi frq) {
		this.frqssis.add(frq);
	}

	public void addFormularioscontacto(Contacto contac) {
		this.formularioscontacto.add(contac);
	}

	public void addComponentes(Componente cmp) {
		this.componentes.add(cmp);
	}

	public void addEncuestas(Encuesta encu) {
		this.encuestas.add(encu);
	}

	public void addTraduccion(String lang, TraduccionMicrosite traduccion) {
		this.setTraduccion(lang, traduccion);
	}

	public String getBuscador() {
		return this.buscador;
	}

	public void setBuscador(String buscador) {
		this.buscador = buscador;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	@Override
	public String getAuditKey() {
		return String.valueOf(this.getId());
	}

	@Override
	public Long getIdmicrosite() {
		return this.getId();
	}
}