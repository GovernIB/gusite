package es.caib.gusite.micromodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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

/**
 * Clase Microsite. Bean que define un Microsite. 
 * Modela la tabla de BBDD GUS_MENU
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity()
@Table(name="GUS_MICROS")
public class Microsite implements Traducible2 {	

	private static final long serialVersionUID = -608935483446670811L;

    @XmlAttribute
	@Id
	@SequenceGenerator(name="GUS_MICROSITE_ID_GENERATOR", sequenceName="GUS_SEQMIC", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GUS_MICROSITE_ID_GENERATOR")
	@Column(name="MIC_CODI")
	private Long id;
	
	@Column(name="MIC_CODUNI")
	private int unidadAdministrativa;

	//TEST
	@XmlAttribute
	@Column(name="MIC_ANALYTICS")
	private String analytics;
  
	@XmlAttribute
	@Column(name="MIC_URI")
	private String uri;
    //TEST
  
  
	@Transient
    private int idUA;
	
	@Transient
    private String nombreUA;

	@Transient
    private ArrayList<?> listaUAs;
       
	@Transient
    private int nivelAccesibilidad;
    
	@Temporal(TemporalType.DATE)
	@Column(name="MIC_FECHA")
    private Date fecha;
	
	@Column(name="MIC_VISIB")
    private String visible;
	
	@Column(name="MIC_MENU")
    private String tipomenu;
	
	@Column(name="MIC_PLANTI")
    private String plantilla;
	
	@Column(name="MIC_OPFAQ")
    private String optFaq;
	
	@Column(name="MIC_OPMAPA")
    private String optMapa;
	
	@Column(name="MIC_OPCONT")
    private String optContacto;
	
	@Column(name="MIC_OPT1")
    private String opt1;
	
	@Column(name="MIC_OPT2")
    private String opt2;
	
	@Column(name="MIC_OPT3")
    private String opt3;
	
	@Column(name="MIC_OPT4")
    private String opt4;
	
	@Column(name="MIC_OPT5")
    private String opt5;
	
	@Column(name="MIC_OPT6")
    private String opt6;
	
	@Column(name="MIC_OPT7")
    private String opt7;
	
    @ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="MIC_IMAGEN")
    private Archivo imagenPrincipal;
    
    @ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="MIC_IMGCAM")
    private Archivo imagenCampanya;
    
    @ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="MIC_CSS")
    private Archivo estiloCSS;
    
    @Column(name="MIC_CSSPTR")
    private String estiloCSSPatron;
    
    @Column(name="MIC_URLHOM")
    private String urlhome;
    
    @Column(name="MIC_URLCAM")
    private String urlcampanya;
    
    @Column(name="MIC_RESTRI")
    private String restringido;
    
    @Column(name="MIC_ROL")
    private String rol;
    
    @Column(name="MIC_DOMINI")
    private String domini;
    
    @Column(name="MIC_NUMNOT")
    private int numeronoticias;
    
    @Column(name="MIC_SERSEL")
    private String serviciosSeleccionados;
    
    @Column(name="MIC_SEROFR")
    private String serviciosOfrecidos;
    
    @Column(name="MIC_CAB")
    private String tipocabecera;
    
    @Column(name="MIC_PIE")
    private String tipopie;
    
    @Column(name="MIC_BUSCA")
    private String buscador;
    
    @Column(name="MIC_CLAVE")
    private String claveunica;
    
    @Column(name="MIC_MNUCRP")
    private String menucorporativo;

    @XmlAttribute
    @Column(name="MIC_VERSION")
    private String versio;

    @XmlAttribute
    @Column(name="MIC_TIPO_ACCESO")
    private String acceso;

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

    @OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
    @JoinColumn(name="MID_MICCOD")
    @MapKey(name="id.codigoIdioma")
  	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	@Fetch(FetchMode.SUBSELECT)
  	private Map<String, TraduccionMicrosite> traducciones = new HashMap<String, TraduccionMicrosite>();

    @OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
    @JoinColumn(name="IMI_MICCOD")
	@Fetch(FetchMode.SUBSELECT)
	private Set<IdiomaMicrosite> idiomas = new HashSet<IdiomaMicrosite>();
	
	@Transient 
	private String idi = Idioma.getIdiomaPorDefecto();
    
	public Microsite() {
		setOptMapa("N");
		setOptFaq("N");
		setOptContacto("N");
		setOpt1("N");
		setOpt2("N");
		setOpt3("N");
		setOpt4("N");
		setOpt5("N");
		setOpt6("N");
		setOpt7("N");
		setFecha(new Date());
		setPlantilla("1");
    }
	
	public String getIdi() {
		return idi;
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
	
	public String getDomini() {
		return domini;
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

	public void setDomini(String domini) {
		this.domini = domini;
	}
	   
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Archivo getImagenPrincipal() {
        return imagenPrincipal;
    }

    public void setImagenPrincipal(Archivo imagenPrincipal) {
        this.imagenPrincipal = imagenPrincipal;
    }

    public String getOpt1() {
        return opt1;
    }

    public void setOpt1(String opt1) {
        this.opt1 = opt1;
    }

    public String getOpt2() {
        return opt2;
    }

    public void setOpt2(String opt2) {
        this.opt2 = opt2;
    }

    public String getOpt3() {
        return opt3;
    }

    public void setOpt3(String opt3) {
        this.opt3 = opt3;
    }

    public String getOpt4() {
        return opt4;
    }

    public void setOpt4(String opt4) {
        this.opt4 = opt4;
    }

    public String getOpt5() {
        return opt5;
    }

    public void setOpt5(String opt5) {
        this.opt5 = opt5;
    }

    public String getOpt6() {
        return opt6;
    }

    public void setOpt6(String opt6) {
        this.opt6 = opt6;
    }

    public String getOpt7() {
        return opt7;
    }

    public void setOpt7(String opt7) {
        this.opt7 = opt7;
    }

    public String getOptContacto() {
        return optContacto;
    }

    public void setOptContacto(String optContacto) {
        this.optContacto = optContacto;
    }

    public String getOptFaq() {
        return optFaq;
    }

    public void setOptFaq(String optFaq) {
        this.optFaq = optFaq;
    }

    public String getOptMapa() {
        return optMapa;
    }

    public void setOptMapa(String optMapa) {
        this.optMapa = optMapa;
    }

    public String getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(String plantilla) {
        this.plantilla = plantilla;
    }

    public String getTipomenu() {
        return tipomenu;
    }

    public void setTipomenu(String tipomenu) {
        this.tipomenu = tipomenu;
    }

    public String getEstiloCSSPatron() {
        return estiloCSSPatron;
    }

    public void setEstiloCSSPatron(String estiloCSSPatron) {
        this.estiloCSSPatron = estiloCSSPatron;
    }
    
    public int getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(int unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }
    
	public Set<IdiomaMicrosite> getIdiomas() {
		return idiomas;
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

        for (int i = 0; i < idiomas.length; i++) {
            boolean existe = false;
            Iterator iter = this.idiomas.iterator();
            while (iter.hasNext()) {
                IdiomaMicrosite idiMicro = (IdiomaMicrosite) iter.next();
                if (idiMicro.getId().getCodigoIdioma().compareTo(idiomas[i]) == 0) {
                    existe = true;
                }
            }
            if (!existe) {
                IdiomaMicrosite idioma = new IdiomaMicrosite();
                idioma.setId(new IdiomaMicrositePK());
                idioma.getId().setCodigoIdioma(idiomas[i]);
                if (this.id != null) {
                    idioma.getId().setCodigoMicrosite(this.id);
                }
                this.idiomas.add(idioma);
            }
    	}
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
	
	public String[] getServiciosSeleccionados(String serviciosSeleccionados) {
		
    	String serviciosSeleccionadosTabla[]=null;
    	if (serviciosSeleccionados!=null) {
    		StringTokenizer st=new StringTokenizer(serviciosSeleccionados,";");
    		int n=st.countTokens();
    		serviciosSeleccionadosTabla= new String[n];
    		for (int i=0;i<n;i++) {
    			serviciosSeleccionadosTabla[i]=st.nextToken();
    		}
    	}
    	
		return serviciosSeleccionadosTabla;
	}

	public void setServiciosSeleccionados(String serviciosSeleccionados) {
		this.serviciosSeleccionados = serviciosSeleccionados;
	}

	public void setServiciosSeleccionados(String[] serviciosSeleccionadosTabla) {
		
        String serviciosSeleccionados="";
        
        if (serviciosSeleccionadosTabla.length>0) {
        	for (int i=0;i<serviciosSeleccionadosTabla.length;i++)
        		serviciosSeleccionados+=serviciosSeleccionadosTabla[i]+";";
        	serviciosSeleccionados=serviciosSeleccionados.substring(0,serviciosSeleccionados.length()-1);
        }
		
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
	
	public String[] getServiciosOfrecidos(String serviciosOfrecidos) {
		
    	String serviciosOfrecidosTabla[]=null;
    	if (serviciosOfrecidos!=null) {
    		StringTokenizer st=new StringTokenizer(serviciosOfrecidos,";");
    		int n=st.countTokens();
    		serviciosOfrecidosTabla= new String[n];
    		for (int i=0;i<n;i++) {
    			serviciosOfrecidosTabla[i]=st.nextToken();
    		}
    	}
		
		return serviciosOfrecidosTabla;
	}


	public void setServiciosOfrecidos(String serviciosOfrecidos) {
		this.serviciosOfrecidos = serviciosOfrecidos;
	}
	
	public void setServiciosOfrecidos(String[] serviciosOfrecidosTabla) {
		
    	String serviciosOfrecidos="";
    	
    	for (int i=0;i<serviciosOfrecidosTabla.length;i++)
    		serviciosOfrecidos+=serviciosOfrecidosTabla[i]+";";
    	if (serviciosOfrecidos.length()>0)
    		serviciosOfrecidos=serviciosOfrecidos.substring(0,serviciosOfrecidos.length()-1);

		this.serviciosOfrecidos = serviciosOfrecidos;
	}

	public int getIdUA() {
		return idUA;
	}

	public void setIdUA(int idUA) {
		this.idUA = idUA;
	}

	public String getNombreUA() {
		return nombreUA;
	}

	public void setNombreUA(String nombreUA) {
		this.nombreUA = nombreUA;
	}
	
	public void setNivelAccesibilidad(int nivelAccesibilidad) {
		this.nivelAccesibilidad = nivelAccesibilidad;
	}
	
	public int getNivelAccesibilidad() {
		return nivelAccesibilidad;
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

	public String getBuscador() {
		return buscador;
	}

	public void setBuscador(String buscador) {
		this.buscador = buscador;
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

	public List<?> getTiposServicios() {
		return tiposServicios;
	}

	public void setTiposServicios(List<?> tiposServicios) {
		this.tiposServicios = tiposServicios;
	}
	
	public String getMvsUrlMigapan() {
		return mvsUrlMigapan;
	}

	public void setMvsUrlMigapan(String mvsUrlMigapan) {
		this.mvsUrlMigapan = mvsUrlMigapan;
	}
	
	public String getMensajeInfo() {
		return mensajeInfo;
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
		
		funcionalidadTraduccion = false;
        if (!traductorHabilitat()) {
            return;
        }
        if (idiomas.contains("ca") & (idiomas.size() > 1)) {
            funcionalidadTraduccion = true;
        }
	}
	
	public static boolean traductorHabilitat() {
		return System.getProperty("es.caib.gusite.integracion.traductor").equals("S");
	}

	public String getMvsCssTiny() {
		return mvsCssTiny;
	}

	public void setMvsCssTiny(String mvsCssTiny) {
		this.mvsCssTiny = mvsCssTiny;
	}

	public ArrayList<?> getListaUAs() {
		return listaUAs;
	}

	public void setListaUAs(ArrayList<?> listaUAs) {
		this.listaUAs = listaUAs;
	}

	public Map<String, TraduccionMicrosite> getTraducciones() {
		return traducciones;
	}

	public void setTraducciones(Map traducciones) {
		this.traducciones = traducciones;
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