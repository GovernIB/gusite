package org.ibit.rol.sac.micromodel;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase MicrositeCompleto. Bean que define un Microsite completo. 
 * Modela la tabla de BBDD GUS_MICROS y todas las tablas 
 * de componentes que tiene relacionado un Microsite (agendas, documentos, etc.)
 * @author Indra
 */
public class MicrositeCompleto extends Traducible {	

	private static final long serialVersionUID = 5465456305944839473L;
	private Long Id;
    private int idUA;
    private Date fecha;
    private String visible;
    private Archivo imagenPrincipal;
    private String tipomenu;
    private String plantilla;
    private String optFaq;
    private String optMapa;
    private String optContacto;
    private String opt1;
    private String opt2;
    private String opt3;
    private String opt4;
    private String opt5;
    private String opt6;
    private String opt7;
    private String urlhome;
    private Archivo imagenCampanya;
    private Archivo estiloCSS;  
    private String estiloCSSPatron;    
    private String restringido;
    private String rol;
    private int numeronoticias;
    private String serviciosSeleccionados;    
    private String urlcampanya;
    private String serviciosOfrecidos;
    private String tipocabecera;
    private String tipopie;    
    private String buscador;
    private String version;
    
    private String claveunica;
    private String menucorporativo;
    
    private Set idis = new HashSet();
    private Set docus = new HashSet();
    private Set proceds = new HashSet();
    private Set menus = new HashSet();
    private Set temas = new HashSet();
    private Set faqs = new HashSet();
    private Set actividades = new HashSet();
    private Set agendas = new HashSet();
    private Set banners = new HashSet();
    private Set tiponotis = new HashSet();
    private Set noticias = new HashSet();
    private Set frqssis = new HashSet();
    private Set formularioscontacto = new HashSet();
    private Set componentes = new HashSet();
    private Set encuestas = new HashSet();
    
    
	public Set getFormularioscontacto() {
		return formularioscontacto;
	}

	public void setFormularioscontacto(Set formularioscontacto) {
		this.formularioscontacto = formularioscontacto;
	}

	public Set getNoticias() {
		return noticias;
	}

	public void setNoticias(Set noticias) {
		this.noticias = noticias;
	}

	public Set getFrqssis() {
		return frqssis;
	}

	public void setFrqssis(Set frqssis) {
		this.frqssis = frqssis;
	}	
	
	public Set getTiponotis() {
		return tiponotis;
	}

	public void setTiponotis(Set tiponotis) {
		this.tiponotis = tiponotis;
	}

	public Set getFaqs() {
		return faqs;
	}

	public void setFaqs(Set faqs) {
		this.faqs = faqs;
	}

	public Set getTemas() {
		return temas;
	}

	public void setTemas(Set temas) {
		this.temas = temas;
	}

	public Archivo getImagenCampanya() {
		return imagenCampanya;
	}

	public Set getMenus() {
		return menus;
	}

	public void setMenus(Set menus) {
		this.menus = menus;
	}

	public Set getProceds() {
		return proceds;
	}

	public void setProceds(Set proceds) {
		this.proceds = proceds;
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
	
	public Set getIdis() {
		return idis;
	}

	public void setIdis(Set idis) {
		this.idis = idis;
	}

	public String getUrlhome() {
		return urlhome;
	}

	public void setUrlhome(String urlhome) {
		this.urlhome = urlhome;
	}
	
	public Set getDocus() {
		return docus;
	}
	public void setDocus(Set docus) {
		this.docus = docus;
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

	public Set getActividades() {
		return actividades;
	}

	public void setActividades(Set actividades) {
		this.actividades = actividades;
	}

	public Set getAgendas() {
		return agendas;
	}

	public void setAgendas(Set agendas) {
		this.agendas = agendas;
	}

	public Set getBanners() {
		return banners;
	}

	public void setBanners(Set banners) {
		this.banners = banners;
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


	public Set getComponentes() {
		return componentes;
	}

	public void setComponentes(Set componentes) {
		this.componentes = componentes;
	}

	public Set getEncuestas() {
		return encuestas;
	}

	public void setEncuestas(Set encuestas) {
		this.encuestas = encuestas;
	}

	
	// Metodos para poder leer las colecciones del XML
	
	public void addIdis(String idi) {
        idis.add(idi);
    }
    
	public void addDocus(Archivo fich) {
		docus.add(fich);
	}
	
	public void addProceds(MProcedimiento proc) {
		proceds.add(proc);
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
	
	public void addBanners(Banner ban) {
		banners.add(ban);
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
}