package org.ibit.rol.sac.micromodel;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;


/**
 * Clase Microsite. Bean que define un Microsite. 
 * Modela la tabla de BBDD GUS_MENU
 * @author Indra
 */
public class Microsite extends Traducible {	

	private static final long serialVersionUID = -608935483446670811L;
	private Long Id;
    private int unidadAdministrativa;
    
    private int idUA;
    private String nombreUA;
    private int nivelAccesibilidad;    
    
    private Date fecha;
    private String visible;
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
    private Archivo imagenPrincipal;
    private Archivo imagenCampanya;
    private Archivo estiloCSS;
    private String estiloCSSPatron;
    private String urlhome;
    private String urlcampanya;
    private String restringido;
    private String rol;
    private String domini;
    private int numeronoticias;
    private String serviciosSeleccionados;
    private String serviciosOfrecidos;
    private String tipocabecera;
    private String tipopie;
    private String buscador;
    
    private String claveunica;
    private String menucorporativo;
    
    private List<?> tiposServicios;
    private String mvsUrlMigapan;
    private String mensajeInfo;
    private String mensajeError;
    private boolean funcionalidadTraduccion;
    private String mvsCssTiny;


    private Set<String> idiomas = new HashSet<String>();
    
	public Microsite()
    {
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

	public void setDomini(String domini) {
		this.domini = domini;
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
    
    public int getUnidadAdministrativa()
    {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(int unidadAdministrativa)
    {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public String getVisible()
    {
        return visible;
    }

    public void setVisible(String visible)
    {
        this.visible = visible;
    }
    
	public Set<String> getIdiomas() {
		return idiomas;
	}
	
	public String[] getIdiomas(Set<?> idiomas) {
    	Iterator<?> it = idiomas.iterator();	
    	String[] idiomasS=new String[idiomas.size()];
    	int n=0;
    	while (it.hasNext()) {
    		idiomasS[n++]=""+it.next();
    	}
    	return idiomasS;
    	
	}
	

	public void setIdiomas(Set<String> idiomas) {
		this.idiomas = idiomas;
	}
	
	public void setIdiomas(String[] idiomas) {
		
    	HashSet<String> idiomasHash = new HashSet<String>();

    	for (int i=0; i < idiomas.length;i++)
    		idiomasHash.add(idiomas[i]);
		
		this.idiomas = idiomasHash;
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
		
		if(!traductorHabilitat() ) return;
		
		if (idiomas.contains("ca") & (idiomas.size() > 1) )
			funcionalidadTraduccion = true;
	
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
	
}