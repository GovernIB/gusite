package es.caib.gusite.micromodel;

import java.io.Serializable;
import java.util.*;

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
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import es.caib.gusite.micromodel.adapter.TraduccionAdapter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Clase MicrositeCompleto. Bean que define un Microsite completo. 
 * Modela la tabla de BBDD GUS_MICROS y todas las tablas 
 * de componentes que tiene relacionado un Microsite (agendas, documentos, etc.)
 * @author Indra
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({IdiomaMicrosite.class, Menu.class, Temafaq.class, Faq.class, Actividadagenda.class, Agenda.class, Tipo.class, Noticia.class, Frqssi.class, Contacto.class, Componente.class, Encuesta.class, TraduccionMicrosite.class})
@Entity
@Table(name="GUS_MICROS")
public class MicrositeCompleto implements Traducible2, Serializable {

	private static final long serialVersionUID = 5465456305944839473L;

    @XmlAttribute
	@Id
	@SequenceGenerator(name="GUS_MICROSITECOMPLETO_ID_GENERATOR", sequenceName="GUS_SEQMIC", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GUS_MICROSITECOMPLETO_ID_GENERATOR")
	@Column(name="MIC_CODI")
	private Long Id;

    @XmlAttribute
	@Column(name="MIC_CODUNI")
    private int idUA;
    
  //TEST
  	@XmlAttribute
  	@Column(name="MIC_ANALYTICS")
  	private String analytics;
    
  	@XmlAttribute
  	@Column(name="MIC_URI")
  	private String uri;
      //TEST

    @XmlAttribute
	@Temporal(TemporalType.DATE)
	@Column(name="MIC_FECHA")
    private Date fecha;

    @XmlAttribute
	@Column(name="MIC_VISIB")
    private String visible;

    @XmlElement
    @ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="MIC_IMAGEN")
    private Archivo imagenPrincipal;

    @XmlAttribute
    @Column(name="MIC_MENU")
    private String tipomenu;

    @XmlAttribute
    @Column(name="MIC_PLANTI")
    private String plantilla;

    @XmlAttribute
    @Column(name="MIC_OPFAQ")
    private String optFaq;

    @XmlAttribute
    @Column(name="MIC_OPMAPA")
    private String optMapa;

    @XmlAttribute
    @Column(name="MIC_OPCONT")
    private String optContacto;

    @XmlAttribute
    @Column(name="MIC_OPT1")
    private String opt1;

    @XmlAttribute
    @Column(name="MIC_OPT2")
    private String opt2;

    @XmlAttribute
    @Column(name="MIC_OPT3")
    private String opt3;

    @XmlAttribute
    @Column(name="MIC_OPT4")
    private String opt4;

    @XmlAttribute
    @Column(name="MIC_OPT5")
    private String opt5;

    @XmlAttribute
    @Column(name="MIC_OPT6")
    private String opt6;

    @XmlAttribute
    @Column(name="MIC_OPT7")
    private String opt7;

    @XmlAttribute
    @Column(name="MIC_URLHOM")
    private String urlhome;

    @XmlElement
    @ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="MIC_IMGCAM")
    private Archivo imagenCampanya;

    @XmlElement
    @ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="MIC_CSS")
    private Archivo estiloCSS;

    @XmlAttribute
    @Column(name="MIC_CSSPTR")
    private String estiloCSSPatron;

    @XmlAttribute
    @Column(name="MIC_RESTRI")
    private String restringido;

    @XmlAttribute
    @Column(name="MIC_ROL")
    private String rol;

    @XmlAttribute
    @Column(name="MIC_NUMNOT")
    private int numeronoticias;

    @XmlAttribute
    @Column(name="MIC_SERSEL")
    private String serviciosSeleccionados;

    @XmlAttribute
    @Column(name="MIC_URLCAM")
    private String urlcampanya;

    @XmlAttribute
    @Column(name="MIC_SEROFR")
    private String serviciosOfrecidos;

    @XmlAttribute
    @Column(name="MIC_CAB")
    private String tipocabecera;

    @XmlAttribute
    @Column(name="MIC_PIE")
    private String tipopie;

    @XmlAttribute
    @Column(name="MIC_BUSCA")
    private String buscador;

    @XmlAttribute
    @Transient
    private String version;

    @XmlAttribute
    @Column(name="MIC_CLAVE")
    private String claveunica;

    @XmlAttribute
    @Column(name="MIC_MNUCRP")
    private String menucorporativo;

    @XmlAttribute
    @Column(name="MIC_VERSION")
    private String versio;

    @XmlAttribute
    @Column(name="MIC_TIPO_ACCESO")
    private String acceso;

    @OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
    @JoinColumn(name="IMI_MICCOD")
	@Fetch(FetchMode.SUBSELECT)
    private Set<IdiomaMicrosite> idiomas = new HashSet<IdiomaMicrosite>();

	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="DCM_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
    private Set<Archivo> docus = new HashSet();

	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="MNU_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
    private Set<Menu> menus = new HashSet();

	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="TEM_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
    private Set<Temafaq> temas = new HashSet();

	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="FAQ_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
    private Set<Faq> faqs = new HashSet();

	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="ACT_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
    private Set<Actividadagenda> actividades = new HashSet();

	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="AGE_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
    private Set<Agenda> agendas = new HashSet();

	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="TPN_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
    private Set<Tipo> tiponotis = new HashSet();

	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="NOT_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
    private Set<Noticia> noticias = new HashSet();

	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="FRQ_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
    private Set<Frqssi> frqssis = new HashSet();

	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="FRM_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
    private Set<Contacto> formularioscontacto = new HashSet();

	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="CMP_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
    private Set<Componente> componentes = new HashSet();

	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="ENC_MICCOD")
	@MapKey
	@Fetch(FetchMode.SUBSELECT)
    private Set<Encuesta> encuestas = new HashSet();

    @OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
    @Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	@JoinColumn(name="MID_MICCOD")
    @MapKey(name="id.codigoIdioma")
	@Fetch(FetchMode.SUBSELECT)
    private Map<String, TraduccionMicrosite> traducciones = new HashMap<String, TraduccionMicrosite>();

    @Transient
    private String idi = Idioma.getIdiomaPorDefecto();

    @XmlElement(name = "idiomaMicrosite", type = IdiomaMicrosite.class)
    @XmlElementWrapper(name ="idiomas")
    public Set getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(Set idiomas) {
        this.idiomas = idiomas;
    }

    @XmlElement(name = "docus", type = Archivo.class)
    @XmlElementWrapper(name ="documento")
    public Set getDocus() {
        return docus;
    }

    public void setDocus(Set docus) {
        this.docus = docus;
    }

    @XmlElement(name = "menu", type = Menu.class)
    @XmlElementWrapper(name ="menus")
    public Set getMenus() {
        return menus;
    }

    public void setMenus(Set menus) {
        this.menus = menus;
    }

    @XmlElement(name = "temafaq", type = Temafaq.class)
    @XmlElementWrapper(name ="temas")
    public Set getTemas() {
        return temas;
    }

    public void setTemas(Set temas) {
        this.temas = temas;
    }

    @XmlElement(name = "faq", type = Faq.class)
    @XmlElementWrapper(name ="faqs")
    public Set getFaqs() {
        return faqs;
    }

    public void setFaqs(Set faqs) {
        this.faqs = faqs;
    }

    @XmlElement(name = "actividadagenda", type = Actividadagenda.class)
    @XmlElementWrapper(name ="actividades")
    public Set getActividades() {
        return actividades;
    }

    public void setActividades(Set actividades) {
        this.actividades = actividades;
    }

    @XmlElement(name = "agenda", type = Agenda.class)
    @XmlElementWrapper(name ="agendas")
    public Set getAgendas() {
        return agendas;
    }

    public void setAgendas(Set agendas) {
        this.agendas = agendas;
    }

    @XmlElement(name = "tipo", type = Tipo.class)
    @XmlElementWrapper(name ="tiponotis")
    public Set getTiponotis() {
        return tiponotis;
    }

    public void setTiponotis(Set tiponotis) {
        this.tiponotis = tiponotis;
    }

    @XmlElement(name = "noticia", type = Noticia.class)
    @XmlElementWrapper(name ="noticias")
    public Set getNoticias() {
        return noticias;
    }

    public void setNoticias(Set noticias) {
        this.noticias = noticias;
    }

    @XmlElement(name = "frqssi", type = Frqssi.class)
    @XmlElementWrapper(name ="frqssis")
    public Set getFrqssis() {
        return frqssis;
    }

    public void setFrqssis(Set frqssis) {
        this.frqssis = frqssis;
    }

    @XmlElement(name = "contacto", type = Contacto.class)
    @XmlElementWrapper(name ="formularioscontacto")
    public Set getFormularioscontacto() {
        return formularioscontacto;
    }
    
  //TEST
  	public String getAnalytics() {
  		return analytics;
  	}

  	public void setAnalytics(String analytics) {
  		this.analytics = analytics;
  	}
  	
  	public String getUri() {
  		return uri;
  	}
  	
  	public void setUri(String uri) {
  		this.uri = uri;
  	}
  	//TEST

    public void setFormularioscontacto(Set formularioscontacto) {
        this.formularioscontacto = formularioscontacto;
    }

    @XmlElement(name = "componente", type = Componente.class)
    @XmlElementWrapper(name ="componentes")
    public Set getComponentes() {
        return componentes;
    }

    public void setComponentes(Set componentes) {
        this.componentes = componentes;
    }

    @XmlElement(name = "encuesta", type = Encuesta.class)
    @XmlElementWrapper(name ="encuestas")
    public Set getEncuestas() {
        return encuestas;
    }

    public void setEncuestas(Set encuestas) {
        this.encuestas = encuestas;
    }

    @XmlElement(name = "traducciones")
    @XmlJavaTypeAdapter(TraduccionAdapter.class)
    public Map<String, TraduccionMicrosite> getTranslates() {
        return traducciones;
    }

    public void setTranslates(Map<String, TraduccionMicrosite> traducciones) {
        this.traducciones = traducciones;
    }

    public Map<String, TraduccionMicrosite> getTraducciones() {
        return traducciones;
    }

    public void setTraducciones(Map traducciones) {
        this.traducciones = traducciones;
    }

	public Archivo getImagenCampanya() {
		return imagenCampanya;
	}
	
	public void setImagenCampanya(Archivo imagenCampanya) {
		this.imagenCampanya = imagenCampanya;
	}

	public String getRestringido() {
		return restringido;
	}

	public void setRestringido(String restringido) {
		this.restringido = restringido;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

    public Date getFecha()
    {
        return fecha;
    }

    public void setFecha(Date fecha)
    {
        this.fecha = fecha;
    }

    public Long getId()
    {
        return Id;
    }

    public void setId(Long id)
    {
        Id = id;
    }

    public Archivo getImagenPrincipal()
    {
        return imagenPrincipal;
    }

    public void setImagenPrincipal(Archivo imagenPrincipal)
    {
        this.imagenPrincipal = imagenPrincipal;
    }

    public String getOpt1()
    {
        return opt1;
    }

    public void setOpt1(String opt1)
    {
        this.opt1 = opt1;
    }

    public String getOpt2()
    {
        return opt2;
    }

    public void setOpt2(String opt2)
    {
        this.opt2 = opt2;
    }

    public String getOpt3()
    {
        return opt3;
    }

    public void setOpt3(String opt3)
    {
        this.opt3 = opt3;
    }

    public String getOpt4()
    {
        return opt4;
    }

    public void setOpt4(String opt4)
    {
        this.opt4 = opt4;
    }

    public String getOpt5()
    {
        return opt5;
    }

    public void setOpt5(String opt5)
    {
        this.opt5 = opt5;
    }

    public String getOpt6()
    {
        return opt6;
    }

    public void setOpt6(String opt6)
    {
        this.opt6 = opt6;
    }

    public String getOpt7()
    {
        return opt7;
    }

    public void setOpt7(String opt7)
    {
        this.opt7 = opt7;
    }

    public String getOptContacto()
    {
        return optContacto;
    }

    public void setOptContacto(String optContacto)
    {
        this.optContacto = optContacto;
    }

    public String getOptFaq()
    {
        return optFaq;
    }

    public void setOptFaq(String optFaq)
    {
        this.optFaq = optFaq;
    }

    public String getOptMapa()
    {
        return optMapa;
    }

    public void setOptMapa(String optMapa)
    {
        this.optMapa = optMapa;
    }

    public String getPlantilla()
    {
        return plantilla;
    }

    public void setPlantilla(String plantilla)
    {
        this.plantilla = plantilla;
    }

    public String getTipomenu()
    {
        return tipomenu;
    }

    public void setTipomenu(String tipomenu)
    {
        this.tipomenu = tipomenu;
    }

    public String getEstiloCSSPatron()
    {
        return estiloCSSPatron;
    }

    public void setEstiloCSSPatron(String estiloCSSPatron)
    {
        this.estiloCSSPatron = estiloCSSPatron;
    }    
    
    public String getVisible()
    {
        return visible;
    }

    public void setVisible(String visible)
    {
        this.visible = visible;
    }

	public String getUrlhome() {
		return urlhome;
	}

	public void setUrlhome(String urlhome) {
		this.urlhome = urlhome;
	}

	public String getServiciosSeleccionados() {
		return serviciosSeleccionados;
	}

	public void setServiciosSeleccionados(String serviciosSeleccionados) {
		this.serviciosSeleccionados = serviciosSeleccionados;
	}

	public String getUrlcampanya() {
		return urlcampanya;
	}

	public void setUrlcampanya(String urlcampanya) {
		this.urlcampanya = urlcampanya;
	}

	public String getServiciosOfrecidos() {
		return serviciosOfrecidos;
	}

	public void setServiciosOfrecidos(String serviciosOfrecidos) {
		this.serviciosOfrecidos = serviciosOfrecidos;
	}

	public int getIdUA() {
		return idUA;
	}

	public void setIdUA(int idUA) {
		this.idUA = idUA;
	}

	public Archivo getEstiloCSS() {
		return estiloCSS;
	}

	public void setEstiloCSS(Archivo estiloCSS) {
		this.estiloCSS = estiloCSS;
	}

	public int getNumeronoticias() {
		return numeronoticias;
	}

	public void setNumeronoticias(int numeronoticias) {
		this.numeronoticias = numeronoticias;
	}

	public String getTipocabecera() {
		return tipocabecera;
	}

	public void setTipocabecera(String tipocabecera) {
		this.tipocabecera = tipocabecera;
	}

	public String getTipopie() {
		return tipopie;
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
        idiomas.add(idioma);
    }
    
	public void addDocus(Archivo fich) {
		docus.add(fich);
	}

	public void addMenus(Menu men) {
		menus.add(men);
	}

	public void addTemas(Temafaq tem) {
		temas.add(tem);
	}

	public void addFaqs(Faq faq) {
		faqs.add(faq);
	}

	public void addActividades(Actividadagenda act) {
		actividades.add(act);
	}

	public void addAgendas(Agenda age) {
		agendas.add(age);
	}
	
	public void addTiponotis(Tipo tp) {
		tiponotis.add(tp);
	}
	
	public void addNoticias(Noticia not) {
		noticias.add(not);
	}
	
	public void addFrqssis(Frqssi frq) {
		frqssis.add(frq);
	}	
	
	public void addFormularioscontacto(Contacto contac) {
		formularioscontacto.add(contac);
	}
	
	public void addComponentes(Componente cmp) {
		componentes.add(cmp);
	}

	public void addEncuestas(Encuesta encu) {
		encuestas.add(encu);
	}

	public void addTraduccion(String lang, TraduccionMicrosite traduccion) {
        setTraduccion(lang, traduccion);
    }

	public String getBuscador() {
		return buscador;
	}

	public void setBuscador(String buscador) {
		this.buscador = buscador;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getClaveunica() {
		return claveunica;
	}

	public void setClaveunica(String claveunica) {
		this.claveunica = claveunica;
	}

	public String getMenucorporativo() {
		return menucorporativo;
	}

	public void setMenucorporativo(String menucorporativo) {
		this.menucorporativo = menucorporativo;
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
            traducciones.put(idioma, (TraduccionMicrosite)traduccion);
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

    public String getVersio() {
        return versio;
    }

    public void setVersio(String versio) {
        this.versio = versio;
    }

    public String getAcceso() {
        return acceso;
    }

    public void setAcceso(String acceso) {
        this.acceso = acceso;
    }
}