package es.caib.gusite.microfront.base;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import es.caib.gusite.utilities.auth.ClientPrincipal;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;

import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.microfront.base.bean.Pardato;
import es.caib.gusite.microfront.base.bean.Tridato;
import es.caib.gusite.microfront.Microfront;
import es.caib.gusite.microfront.estadistica.util.StatManager;
import es.caib.gusite.microfront.exception.ExceptionFrontMicro;
import es.caib.gusite.microfront.util.Cadenas;
import es.caib.gusite.microfront.util.microtag.MParserHTML;
import es.caib.gusite.micromodel.Estadistica;
import es.caib.gusite.micromodel.IdiomaMicrosite;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Tiposervicio;
import es.caib.gusite.micromodel.TraduccionMicrosite;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import es.caib.gusite.plugins.PluginException;
import es.caib.gusite.plugins.PluginFactory;
import es.caib.gusite.plugins.organigrama.OrganigramaProvider;
import es.caib.gusite.plugins.organigrama.UnidadData;


/**
 * Clase básica para manejar información.
 * La idea es que las demás clases que vayan destinadas a negocio de presentación hereden
 * de esta.
 * Esta clase se encarga ella solita (simplemente inicializando al constructor)
 * de averiguar el idioma en el que está el cliente, el id del microsite en el que está el cliente,
 * y monta el listado de la cabecera y del pie.
 * <P>
 * <BR>
 * Elementos que van a estar en sesion: <BR>
 * MVS_idsite: (Long) id del site <BR>
 * MVS_micrositetitulo: (String) titulo del site <BR>
 * MVS_idioma: (String) id del idioma en mayusculas  <BR>
 * MVS_listaidiomas: (ArrayList) lista de idiomas del site (el bean es Pardato) <BR>
 * MVS_listapie: (ArrayList) lista de enlaces del pie de pagina (el bean es Pardato) <BR>
 * MVS_listacabecera: (ArrayList) lista de enlaces de la cabecera de la pagina (el bean es Pardato) <BR>
 * MVS_microsite: (Microsite) El microsite actual  <BR>
 * MVS_ua: (String) Unidad Administrativa tal y como se visualiza en la cabecera del portal. <BR> 
 * MVS_menu: (ArrayList) Lista de menu (el bean es Micromenu) OJO: ahora va en request <BR>
 * MVS_seulet: (String) Contiene la url actual sin el parametro 'lang' <BR>
 * MVS_servicio: (String) Contiene la codificacion de servicio actual <BR>
 * MVS_css: (String) Contiene la hoja de estilos del microsite (tag html) <BR>
 * MVS_tiposervicios_ofrecidos: (ArrayList) Lista con los servicios ofrecidos por el microsite <BR>
 * MVS_hashsites: Hashtable con los microsites visitados en sesion  <BR>
 *  
 * @author Indra
 *
 */

public abstract class Bdbase {

	protected static Log log = LogFactory.getLog(Bdbase.class);
	
	protected String idioma="";
	protected Long idsite = new Long(0);
	protected Microsite microsite = new Microsite();
	protected ErrorMicrosite beanerror = new ErrorMicrosite();
	private boolean errorbase=false;
	protected String url = ""; //url que hay en esos momentos
	protected String tagHtmlTmpCampaya=""; //pegote de html que contiene la campaña
	protected int publico=Integer.parseInt(Microfront._DMZ_PUBLICO);
	private DelegateBase _delegateBase;
	private HttpServletRequest _request;
	
	
	public Bdbase() {}
	
	public void finalize() {
		this.dispose();
	}
	
	/**
	 * Metodo público que borra las variables.
	 */
	public void dispose() {
		idsite = null;
		microsite = null;
		beanerror =null;
		url = null;
		tagHtmlTmpCampaya= null;
		_delegateBase = null;
		_request = null;;
	}
	
	/**
	 * Constructor de la clase. Se inicializa con la request. Prepara el microsite para ser
	 * mostrado en el front. Prepara el idioma, estado, idsite, listado de idiomas, UA, css, 
	 * pie, cabecera, menu, servicio, url y campaña.
	 * @param request
	 */
	public Bdbase(HttpServletRequest request) throws ExceptionFrontMicro, Exception  {
		//en el momento que haya algún error grave en alguno de estos métodos.... zass--> NO SE VISUALIZA NADA
		try {
			_request=request;
			_delegateBase = new DelegateBase(request);
			prepararidioma(request);
			prepararStat(request);
			prepararidsite(request);
			prepararlistaidiomas(request);
			prepararua(request);
			prepararcss(request);
	    	prepararpie();
	    	prepararcabecera(request);
	    	prepararmenu();
	    	prepararservicio(request);
	    	montarUrl(request);
	    	prepararcampanya(request);

    	} catch (ExceptionFrontMicro efm) {
    		//Error al encontrar el Microsite
    		idsite=new Long(0);
    		request.getSession().setAttribute("MVS_idsite","0");
    		microsite = null;
    		errorbase=true;
    		throw new ExceptionFrontMicro(efm.getMessage() + " Stack = " + Cadenas.statcktrace2String(efm.getStackTrace(), 6));
    		
    	} catch (Exception e) {
    		//hay algún error.
    		idsite=new Long(0);
    		request.getSession().setAttribute("MVS_idsite","0");
    		microsite = null;
    		errorbase=true;
    		throw new Exception(e.getMessage() + " Stack = " + Cadenas.statcktrace2String(e.getStackTrace(), 6));
    		
    	}
   }
		
	private boolean isEmpty(String url) {
		return url == null || url.length() < 1; 
	}

	protected void direccionPie(Long coduo, String lang) throws Exception {
		
    	// DIRECCION DEL PIE DE PAGINA
		
    	ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new java.util.Locale(lang, lang));
    	StringBuilder direccion = new StringBuilder("");
    	
    	
    	OrganigramaProvider organigramaProvider = PluginFactory.getInstance().getOrganigramaProvider();
		UnidadData unidadData = organigramaProvider.getUnidadData(coduo, lang);
    	
		if (unidadData == null) {
			log.info("No hay datos de dirección");
			return;
		}

		if (!isEmpty(unidadData.getUrl())) {
			direccion.append("<a href=\"").append(unidadData.getUrl()).append("\">");
			direccion.append(unidadData.getNombre());
			direccion.append("</a>");
		} else {
			direccion.append(unidadData.getNombre());
		}

		if (!isEmpty(unidadData.getDireccion())) {
			if (!isEmpty(unidadData.getUrlPlano())) {
				direccion.append("<a href=\"").append(unidadData.getUrlPlano()).append("\">");
				//TODO: asumimos que si hay dirección, también hay cp y pob 
				direccion.append(": ").append(unidadData.getDireccion()).append(" - ").append(unidadData.getCodigoPostal()).append(" ")
				.append(unidadData.getPoblacion()).append("</a><br/>");
			} else {
				//TODO: asumimos que si hay dirección, también hay cp y pob 
				direccion.append(": ").append(unidadData.getDireccion()).append(" - ").append(unidadData.getCodigoPostal()).append(" ")
				.append(unidadData.getPoblacion()).append("<br/>");
			}
		}

		if (!isEmpty(unidadData.getTelefono())) {
			direccion.append(rb.getString("WEB_ILL067")).append(" ").append(unidadData.getTelefono());
		}


		if (!isEmpty(unidadData.getTelefono()) && !isEmpty(unidadData.getFax())) {
			direccion.append(" - ");
		}

		if (!isEmpty(unidadData.getFax())) {
			direccion.append(rb.getString("WEB_ILL068")).append(" ").append(unidadData.getFax());
		}

		if (microsite.getDomini() != null && microsite.getDomini().length() > 0) {
			direccion.append("<br />").append(rb.getString("WEB_ILL130")).append(" <a href=")
					.append(microsite.getDomini()).append(">").append(microsite.getDomini()).append("</a>");
		} else if (!isEmpty(unidadData.getDominio())) {
				direccion.append("<br />").append(rb.getString("WEB_ILL130")).append(" <a href=")
						.append(unidadData.getDominio()).append(">").append(unidadData.getDominio()).append("</a>");
		}

    	_request.setAttribute("direccion", direccion.toString());
		
	}
		
	/**
	 * Mete en sesión el valor del idioma. MVS_idioma
	 * @param request
	 */
	private void prepararidioma(HttpServletRequest request) throws Exception  {
		
    	idioma = (String)getParameter(Microfront.PLANG);
    	if (idioma != null && !idioma.equals("null")) {
    		meteidioma(request, idioma);
    	} else {
    		idioma = (String)request.getSession().getAttribute("MVS_idioma");
	    	if (idioma == null || idioma.equals("null")) {
	    		meteidioma(request, "CA");
	    	}
    	}
    	idioma = idioma.toLowerCase();
	}
	
	/**
	 * Mete en sesión el valor del un idioma en concreto.
	 * @param request
	 * @param idi  un idioma
	 * @throws Exception
	 */
	private void meteidioma(HttpServletRequest request, String idi) throws Exception  {
    	if (!idi.equals("null")) {
    		idi = idi.toUpperCase();
    	    request.getSession().setAttribute("MVS_idioma",idi);
    	    request.getSession().setAttribute( Globals.LOCALE_KEY, new java.util.Locale(idi, idi));
    	    idioma = idi;
	    	idioma = idioma.toLowerCase();
    	}
	}
	
	/**
	 * Mete en sesión el idsite y si fuera necesario el site. MVS_idsite
	 * @param request
	 */
	private void prepararidsite(HttpServletRequest request) throws ExceptionFrontMicro, Exception {
		
		boolean guardoStatSesion = false;
		
		String txoldidsite = (String)request.getSession().getAttribute("MVS_idsite");
    	String txnewidsite = (String)getParameter("idsite");

    	/* ***** Inicio Pegote para recoger MKEY */
    	try {
		
			String newmkey = "" + getParameter(Microfront.PMKEY);
			if ( !"null".equals(newmkey) ) {
				Microsite micro2 = _delegateBase.obtenerMicrositebyKey(newmkey, idioma);
				txnewidsite = String.valueOf(micro2.getId().longValue());
			}

    	} catch (Exception e) {
    		
    		throw new ExceptionFrontMicro("No s' ha trobat el microsite amb id " + getIdsiteError((String)getParameter(Microfront.PMKEY)));
    	
    	}
    	/* ***** Fin Pegote para recoger MKEY */
    	
    	if ( (txnewidsite.equals("null")) && (txoldidsite.equals("null")) ) {
    		throw new ExceptionFrontMicro(" [Configuracion microsite]: Se debe indicar algún microsite");
    	}
    	
    	Hashtable<String, String> hssites = (Hashtable)request.getSession().getAttribute("MVS_hashsites");
    	
		if ((hssites == null) || (!hssites.contains(txnewidsite))) {
			
			guardoStatSesion = true;
			
			if (hssites == null)
				hssites = new Hashtable<String, String>();
			
			idsite = new Long(txnewidsite);
			request.getSession().setAttribute("MVS_idsite", txnewidsite);
			cambiarmicrositesesion(request);
			
		} else {
			
			microsite = (Microsite) request.getSession().getAttribute("MVS_microsite");
			idsite = new Long(txoldidsite);

			if (!txnewidsite.equals(txoldidsite)) {
				// si son distintos, borramos las variables temporales de sesion.
				idsite = new Long(txnewidsite);
				request.getSession().setAttribute("MVS_idsite", txnewidsite);
				cambiarmicrositesesion(request);
			} else {
				MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
				microsite = micrositedel.obtenerMicrosite(idsite);
				request.getSession().setAttribute("MVS_microsite", microsite);
			}
			
		}
    	
    	if (microsite.getIdiomas().size()==1) {
    		Iterator<IdiomaMicrosite> it = microsite.getIdiomas().iterator();	
    		meteidioma(request, it.next().getId().getCodigoIdioma());
    	}
    	
    	microsite.setIdi(idioma);
    	String titulo_mic = ((TraduccionMicrosite)microsite.getTraduccion(idioma) != null) ? ((TraduccionMicrosite)microsite.getTraduccion(idioma)).getTitulo() : "&nbsp;";
    	request.getSession().setAttribute("MVS_micrositetitulo", titulo_mic );
    	
    	//chequear la visibilidad del site
	    String previ= ""+request.getSession().getAttribute("previsualiza");
	    String misite =""+request.getSession().getAttribute("misite");
	    
	    if (!previ.equals("si")||!microsite.getId().toString().equals(misite)){	
	    	if (!microsite.getVisible().equals("S")) {
	    		throw new Exception(" [Configuracion microsite]: El site no es visible");
	    	} 
	   	}
	    
	    hssites.put(txnewidsite, txnewidsite);
	    request.getSession().setAttribute("MVS_hashsites",hssites);
	    	
	    //guardar la estadistica de visita
	    if (guardoStatSesion) request.getSession().getServletContext().setAttribute("bufferStats", 
					StatManager.grabarestadistica(microsite,publico, 
							(List<Estadistica>) request.getSession().getServletContext().getAttribute("bufferStats")));
	    
	}		
	
	/**
	 * Devuelve el IdSiteError 
	 * @param attributeMVSidSite
	 * @return String
	 */
	private String getIdsiteError(String attributeMVSidSite){
		String errorIdSite = attributeMVSidSite;
		return errorIdSite;
	}
  
	/**
	 *  Mete en sesion la lista de idiomas. MVS_listaidiomas
	 * @param request
	 * @throws Exception
	 */
	private void prepararlistaidiomas(HttpServletRequest request) throws Exception {
		try {
			ArrayList<?> lista = null;
			lista = (ArrayList<?>)request.getSession().getAttribute("MVS_listaidiomas");
			
			if (lista==null) {
				ArrayList<Pardato> listaidiomas = new ArrayList<Pardato>();
				Hashtable<String,String> hashidioma= new Hashtable<String,String>();
				Iterator<IdiomaMicrosite> iter = microsite.getIdiomas().iterator();
				while (iter.hasNext()) {
					String tmpidioma = iter.next().getId().getCodigoIdioma().toLowerCase();
					String tmptxtidioma="";
					if (tmpidioma.equals("ca")) tmptxtidioma="Catal&agrave;";
					if (tmpidioma.equals("es")) tmptxtidioma="Castellano";
					if (tmpidioma.equals("en")) tmptxtidioma="English";
					if (tmpidioma.equals("de")) tmptxtidioma="Deutsch";
					if (tmpidioma.equals("fr")) tmptxtidioma="Français";
					hashidioma.put(tmpidioma, tmptxtidioma);
				}
				
				IdiomaDelegate ididel = DelegateUtil.getIdiomaDelegate();
				Iterator<?> iteridi =  ididel.listarLenguajes().iterator();
				while (iteridi.hasNext()) {
					String tmpidioma =  ((String)iteridi.next()).toLowerCase();
					if (hashidioma.containsKey(tmpidioma)) listaidiomas.add(new Pardato(tmpidioma,(String)hashidioma.get(tmpidioma))); 
				}
				
				request.getSession().setAttribute("MVS_listaidiomas", listaidiomas);
			}
			
		} catch (Exception e) {
			errorbase=true;
			throw new Exception(" [Configuracion microsite] Preparando idiomas...: " + e.getMessage());				
		}
	}
	

	/**
	 * Mete en sesión la unidad administrativa. MVS_ua
	 * @param request
	 */
	private void prepararua(HttpServletRequest request) throws Exception {
		try {
			microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
			
		    Iterator<IdiomaMicrosite> iter = microsite.getIdiomas().iterator();
		    String tmpidioma="";
		    while (iter.hasNext()) {
		    	String uacompleta = "";
		    	tmpidioma = iter.next().getId().getCodigoIdioma();
		    	
		    	//si ya está en sesión la lista idiomatica, no la metemos
		    	uacompleta = "" + request.getSession().getAttribute("MVS_ua"+tmpidioma);
		    	
		    	if (uacompleta.equals("null")) {
		    			uacompleta=montarLinkUA(tmpidioma).toString();
				    	request.getSession().setAttribute("MVS_ua"+tmpidioma,uacompleta);
		    	}
		    	if (tmpidioma.equals(idioma)) {
		    		request.getSession().setAttribute("MVS_ua",uacompleta);
		    		break;
		    	}
	        }

		} catch (Exception e) {
			errorbase=true;
			throw new Exception(this + " [prepararua]: " + e.getMessage());				
		}
	}		
	
	/**
	 * Monta un link. Este link apunta a la página de una UnidadAdministritiva, y con el idioma que se le pasa al método
	 * @param idioma un idioma
	 * @return StringBuffer
	 */
	private StringBuffer montarLinkUA(String idioma) {
	
		StringBuffer tmp = new StringBuffer(" ");
		
		try {

	    	OrganigramaProvider organigramaProvider = PluginFactory.getInstance().getOrganigramaProvider();
			UnidadData ua = organigramaProvider.getUnidadData(microsite.getUnidadAdministrativa(), idioma);
		    
			while ( (ua != null) && Long.valueOf(ua.getIdUnidad().toString()) != 1 ) {
				
				StringBuffer uaSbuf = new StringBuffer(ua.getNombre());
				
				Cadenas.initAllTab(uaSbuf); //primera letra en mayusculas
				String uaTexto = Cadenas.initTab(uaSbuf.toString()); // articulos a minusculas
				String uaUrl = ua.getUrl();
				
				tmp.insert(0, "<a href=\"" + uaUrl + "\">" + uaTexto + "</a>" + " > " );
				
				if (ua.getIdUnidadPadre() != null) {
					
					try {
						ua = organigramaProvider.getUnidadData(ua.getIdUnidadPadre(), idioma);
					} catch (PluginException e) {
						log.error(e);
					}
					
				} else {
					
					// Salimos del while.
					break;
					
				}
				
			}
			
			if (tmp.length() > 3) 
				tmp.delete(tmp.length() - 3, tmp.length());
			else 
				tmp.append("&nbsp;");
				
		} catch (Exception e) {
			
			tmp = new StringBuffer();
			
		} 
		
		return tmp;
		
	}

	/**
	 * Comprueba si si ha accedido desde la intranet o publicamente.
	 * @param request
	 * @throws Exception
	 */
	private void prepararStat(HttpServletRequest request) throws Exception  {
		
		/* TODO amartin: ¿borrar?
		String elHost = request.getServerName();
    	String value = System.getProperty("es.indra.caib.rolsac.oficina");
    	
        if ((value == null) || value.equals("N"))
        	elHost = Microfront._HOSTCAIB;
		*/
		
		// Si se ha logado se considera intranet, fichero de apache conf ProxyPass invalida forma anterior 
		if (request.getHeader("Authorization") == null)
			publico = Integer.parseInt(Microfront._DMZ_PUBLICO);
		else
			publico = Integer.parseInt(Microfront._DMZ_INTRANET);
			
	}
	

	/**
	 * Mete en sesión el css. MVS_css
	 * @param request
	 * @throws Exception
	 */
	private void prepararcss(HttpServletRequest request) throws Exception  {
		//MCR v1.1
		MParserHTML parserhtml = new MParserHTML(microsite.getRestringido());
		if (microsite.getRestringido().equals("S")) {
			request.getSession().setAttribute("MVS_css",parserhtml.tagCSS(null, null, null));
			
		} else {
			String css = (String)request.getSession().getAttribute("MVS_css");
			if (css==null) {
				
				if (microsite.getEstiloCSS()!=null)						
					request.getSession().setAttribute("MVS_css",parserhtml.tagCSS(microsite.getId(), microsite.getEstiloCSS().getId(), microsite.getEstiloCSSPatron()) );
				else
					request.getSession().setAttribute("MVS_css",parserhtml.tagCSS(null, null,microsite.getEstiloCSSPatron()));
			}
			
		}
		
	}
	
	/**
	 * Mete en sesión una lista de beans para el pie. MVS_listapie
	 */
	private void prepararpie() throws Exception  {
		
		try {
			
			microsite = (Microsite)_request.getSession().getAttribute("MVS_microsite");
			/*sergio_ini*/
			direccionPie(new Long(microsite.getUnidadAdministrativa()), idioma);
			/*sergio_fin*/
		   	
		    Iterator<IdiomaMicrosite> iter = microsite.getIdiomas().iterator();
		    TraduccionMicrosite micrositetra;
		    String tmpidioma="";
		    Pardato par;
		    
		    while (iter.hasNext()) {
		    	ArrayList<Pardato> lista = new ArrayList<Pardato>();
		    	tmpidioma = iter.next().getId().getCodigoIdioma();
		    	
		    	//si ya está en sesión la lista idiomatica, no la metemos
		    	lista = (ArrayList<Pardato>)_request.getSession().getAttribute("MVS_listapie"+tmpidioma);
		    	if (lista==null) {
		    			lista = new ArrayList<Pardato>();
				    	micrositetra = (TraduccionMicrosite)microsite.getTraduccion(tmpidioma);
				    	if (micrositetra!=null) {
					    	if ((micrositetra.getTxtop3()!=null) && (micrositetra.getUrlop3()!=null)) {
					    		par = new Pardato(micrositetra.getTxtop3(),micrositetra.getUrlop3());
					    		if (microsite.getOpt3().equals("S")) lista.add(par);
					    	}
					    	if ((micrositetra.getTxtop4()!=null) && (micrositetra.getUrlop4()!=null)) {
					    		par = new Pardato(micrositetra.getTxtop4(),micrositetra.getUrlop4());
						    	if (microsite.getOpt4().equals("S")) lista.add(par);
					    	}
					    	if ((micrositetra.getTxtop5()!=null) && (micrositetra.getUrlop5()!=null)) {
						    	par = new Pardato(micrositetra.getTxtop5(),micrositetra.getUrlop5());
						    	if (microsite.getOpt5().equals("S")) lista.add(par);
					    	}
					    	if ((micrositetra.getTxtop6()!=null) && (micrositetra.getUrlop6()!=null)) {
						    	par = new Pardato(micrositetra.getTxtop6(),micrositetra.getUrlop6());
						    	if (microsite.getOpt6().equals("S")) lista.add(par);
					    	}
					    	if ((micrositetra.getTxtop7()!=null) && (micrositetra.getUrlop7()!=null)) {
						    	par = new Pardato(micrositetra.getTxtop7(),micrositetra.getUrlop7());
						    	if (microsite.getOpt7().equals("S")) lista.add(par);
					    	}
				    	}
				    	_request.getSession().setAttribute("MVS_listapie"+tmpidioma,lista);
		    	}
		    	if (tmpidioma.equals(idioma)) {
		    		_request.getSession().setAttribute("MVS_listapie",lista);
		    		break;
		    	}
	        }

		} catch (Exception e) {
			errorbase=true;
			throw new Exception(" [Configuracion microsite] Preparando pie de pagina ...: " + e.getMessage());				
		}
		
	}
	
	/**
	 * Mete en sesión una lista de beans para la cabecera. MVS_listacabecera
	 * @param request
	 */
	private void prepararcabecera(HttpServletRequest request) throws Exception  {
		try {
			microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
			
			ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));
			
		    Iterator<IdiomaMicrosite> iter = microsite.getIdiomas().iterator();
		    TraduccionMicrosite micrositetra;
		    String tmpidioma="";
		    Tridato trio;
		    
		    while (iter.hasNext()) {
		    	ArrayList<Tridato> lista = new ArrayList<Tridato>();
		    	tmpidioma = iter.next().getId().getCodigoIdioma();
		    	
		    	//si ya está en sesión la lista idiomatica, no la metemos
		    	lista = (ArrayList<Tridato>)request.getSession().getAttribute("MVS_listacabecera"+tmpidioma);
		    	if (lista==null) {
		    			lista = new ArrayList<Tridato>();
				    	micrositetra = (TraduccionMicrosite)microsite.getTraduccion(tmpidioma);
				    	
				    	if (microsite.getOptMapa().equals("S")) {
				    		trio = new Tridato(rb.getString("micro.es.mapa"),"mapa.do?idsite="+microsite.getId(),"mapaWeb");
				    		lista.add(trio);
				    	}
				    	if (microsite.getOptContacto().equals("S")) {
				    		trio = new Tridato(rb.getString("micro.es.contacto"),"contactos.do?idsite="+microsite.getId(),"atencio");
				    		lista.add(trio);
				    	}
				    	if (microsite.getOptFaq().equals("S")) {
				    		trio = new Tridato(rb.getString("micro.es.faq"),"faqs.do?idsite="+microsite.getId());
				    		lista.add(trio);
				    	}
					    if (micrositetra!=null) {
					    	if ((micrositetra.getTxtop1()!=null) && (micrositetra.getUrlop1()!=null)) {
					    		trio = new Tridato(micrositetra.getTxtop1(),micrositetra.getUrlop1());
						    	if (microsite.getOpt1().equals("S")) lista.add(trio);
					    	}
					    	if ((micrositetra.getTxtop2()!=null) && (micrositetra.getUrlop2()!=null)) {
					    		trio = new Tridato(micrositetra.getTxtop2(),micrositetra.getUrlop2());
						    	if (microsite.getOpt2().equals("S")) lista.add(trio);
					    	}
				    	}
				    	request.getSession().setAttribute("MVS_listacabecera"+tmpidioma,lista);
		    	}
		    	if (tmpidioma.equals(idioma)) {
		    		request.getSession().setAttribute("MVS_listacabecera",lista);
		    		break;
		    	}
	        }
		    
		} catch (Exception e) {
			errorbase=true;
			throw new Exception(" [Configuracion microsite] Preparando cabecera de pagina ...: " + e.getMessage());
		}
			
	}

	/**
	 * Método que cambia el valor MVS_menu de la sesión
	 */
	private void prepararmenu() throws Exception  {
		_request.setAttribute("MVS_menu",_delegateBase.obtenerMainMenu(microsite.getId(), idioma));
	}
	
	private void prepararservicio(HttpServletRequest request) throws Exception  {
		request.getSession().setAttribute("MVS_servicio",setServicio());
	}
	
	/**
	 * Método que mete en el request el valor de la url del servlet
	 * que se está ejecutando en estos momentos.
	 * 
	 * @param request
	 */
	private void montarUrl(HttpServletRequest request) throws Exception  {
		 
		if (errorbase) 
			url=request.getContextPath() + "/home.do?";
		else 
			url=request.getContextPath() + request.getServletPath() + "?";
		
			Enumeration<?> paramNames = request.getParameterNames();
		    while(paramNames.hasMoreElements()) {
		      String paramName = (String)paramNames.nextElement();
		      String paramValue = request.getParameter(paramName);
		      if ((!paramName.equals(Microfront.PLANG)) && (!paramName.equals(Microfront.PSTAT)))
		    	  url += paramName + "=" + paramValue + "&";
		    }
		    
		    //#47 formar correctamente la url
		    if(url != null && url.length()>0){
		    	int tam = url.length();
		    	char ultimo = url.charAt(tam-1);
		    	if(ultimo =='&'){
		    		url = url.substring(0, tam-1); //quitamos el último &
		    	}
		    	
		    }
		    request.setAttribute("MVS_seulet", url);
	}

	/**
	 * Mete en el request el html de la campaña.
	 * @param request
	 */
	private void prepararcampanya(HttpServletRequest request) throws Exception  {
		
		String tmp_param_camp=""+getParameter(Microfront.PCAMPA);
		if (tmp_param_camp.equals("yes")) {
			MParserHTML parserhtml = new MParserHTML(microsite.getRestringido());
			tagHtmlTmpCampaya=parserhtml.getHtmlCampanya(microsite,idioma).toString();
			if (tagHtmlTmpCampaya.length()>1) request.setAttribute("MVS_home_tmp_campanya",tagHtmlTmpCampaya);
		}
	}	
	
	/**
	 * Método que cambia el valor MVS_microsite de la sesión.
	 * @param request
	 */
	private void cambiarmicrositesesion(HttpServletRequest request) throws Exception {
		try {
			
			borrarvariablessesion(request); //borrar de sesion variables

			MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate(); //meter en sesion el microsite
			microsite = micrositedel.obtenerMicrosite(idsite);
			request.getSession().setAttribute("MVS_microsite",microsite);
			
		} catch (Exception e) {
			request.getSession().setAttribute("MVS_microsite",null);
			errorbase=true;
			throw new Exception(" [Configuracion microsite] Cambiando sesion de microsite ...: " + e.getMessage());				
		}
	}
	
	/**
	 * Método que borra variables de sesion.
	 * @param request
	 */
	private void borrarvariablessesion(HttpServletRequest request) throws Exception {
		IdiomaDelegate ididel = DelegateUtil.getIdiomaDelegate();
		Iterator<?> iter =  ididel.listarLenguajes().iterator();
		
		String tmpidioma;
		while (iter.hasNext()) {
			tmpidioma =  ((String)iter.next()).toLowerCase();
	    	request.getSession().removeAttribute("MVS_listacabecera"+tmpidioma);
	    	request.getSession().removeAttribute("MVS_listapie"+tmpidioma);
	    	request.getSession().removeAttribute("MVS_ua"+tmpidioma);
		}
    	request.getSession().removeAttribute("MVS_listacabecera");
    	request.getSession().removeAttribute("MVS_listapie");
    	request.getSession().removeAttribute("MVS_listaidiomas");
    	request.getSession().removeAttribute("MVS_menu");
    	request.getSession().removeAttribute("MVS_ua");
    	request.getSession().removeAttribute("MVS_css");
    	request.getSession().removeAttribute("MVS_micrositetitulo");
	}
	
	
	/**
	 * Método que devuelve si el servicio que se solicita es ofrecido o no por el microsite.
	 *
	 * @param refservicio una referencia a un servicio
	 * @return boolean true si el tipo de servicio del microsite es igual a refservicio
	 */
	public boolean existeServicio(String refservicio) {

		boolean tmp=false;
		try {
		    	ArrayList<?> listserofr = _delegateBase.obtenerListadoServiciosMicrosite(microsite, idioma);
				//chekeamos el servicio
				Iterator<?> iter2=listserofr.iterator();
				while (iter2.hasNext()) {
					Tiposervicio tiposervicio = (Tiposervicio)iter2.next();
					if (tiposervicio.getReferencia().equals(refservicio)){
						tmp=true;
					}
					if (tmp) break;
				}

		} catch (Exception e) {
			log.error("[existeServicio] con el servicio '" + refservicio + "' "  + e.getMessage());
		}
		return tmp;
	}
	
	/**
	 * Devuelve el Idioma.
	 * @return String un Idioma
	 */
	public String getIdioma() {
		return idioma;
	}

	/**
	 * Establece un idioma.
	 * @param idioma String idioma
	 */
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}

	/**
	 * Devuelve el Id del site.
	 * @return Long Id del site
	 */
	public Long getIdsite() {
		return idsite;
	}

	/**
	 * Establece el Id del site.
	 * @param idsite Id del site
	 */
	public void setIdsite(Long idsite) {
		this.idsite = idsite;
	}

	/**
	 * Devuelve el Microsite.
	 * @return Microsite
	 */
	public Microsite getMicrosite() {
		return microsite;
	}
	
	/**
	 * Establece el Aviso de error del bean.
	 * @param aviso un aviso
	 */
	public void setBeanErrorAviso(String aviso) {
		this.beanerror.setAviso(aviso);
	}

	/**
	 * Establece el Microsite.
	 * @param microsite un microsite
	 */
	public void setMicrosite(Microsite microsite) {
		this.microsite = microsite;
	}

	/**
	 * Devuelve el error del bean.
	 * @return ErrorMicrosite un bean error
	 */
	public ErrorMicrosite getBeanerror() {
		return beanerror;
	}

	/**
	 * Establece un bean error.
	 * @param beanerror ErrorMicrosite
	 */
	public void setBeanerror(ErrorMicrosite beanerror) {
		this.beanerror = beanerror;
	}

	/**
	 * Devuelve el Error Base.
	 * @return boolean
	 */
	public boolean getErrorBase() {
		return errorbase;
	}

	/**
	 * Establece el Error de la base.
	 * @param errorbase true o false si hay o no hay error
	 */
	public void setErrorBase(boolean errorbase) {
		this.errorbase = errorbase;
	}		
	
	/**
	 * Devuelve el TagHtmlTmpCampaya.
	 * @return String un TagHtmlTmpCampaya
	 */
	public String getTagHtmlTmpCampaya() {
		return tagHtmlTmpCampaya;
	}

	/**
	 * Establece tagHtmlcampaya.
	 * @param tagHtmlcampaya
	 */
	public void setTagHtmlTmpCampaya(String tagHtmlcampaya) {
		this.tagHtmlTmpCampaya = tagHtmlcampaya;
	}

    /**
     * Devuelve el Id del usuario.
     * @return String un Id de usuario
     */
    public String getIdUser() {
        String iduser="";
        ClientPrincipal principal;
        try {
            
            principal = ClientPrincipal.getCurrent();
            if (principal != null) iduser=principal.getIntranetUser();
        } catch (NamingException e) {
            log.warn(e.getMessage());
        }
        return iduser;
         
    }       
    
    /**
     * Devuelve un Objeto ClientPrincipal.
     * @param request
     * @return ClientPrincipal
     */
    public ClientPrincipal getPrincipal( HttpServletRequest request )
    {
        return ( ClientPrincipal ) request.getUserPrincipal();
    }


	/**
	 * Método que deben implementar las clases.
	 * Debe devolver un string con el servicio en el que
	 * nos encontremos.
	 * Por ejemplo: return Microfront.RCONTENIDO
	 * @return String un Servicio
	 */
	public abstract String setServicio();

	protected Object getParameter(String parameter) throws Exception {
		Object result = null;
					
		try {
			if (!ServletFileUpload.isMultipartContent(_request)) { // cas en que el request no sigui un multipart
				result = _request.getParameter(parameter);
				
			}else{ // cas en que el request sigui un multipart
				    if (_request.getAttribute(parameter)==null){
				    		result=null; 
				    } else{ 
				    	if (_request.getAttribute(parameter)instanceof ByteArrayInputStream){ // es el fitxer
				    		ByteArrayInputStream bais ; 
				    		bais = (ByteArrayInputStream) _request.getAttribute(parameter);
				    		result=bais;
				    	} else { // son parametres
					    	String str = ""; 
					    	for (int i = 0; i < ((Object[])_request.getAttribute(parameter)).length;i++){					    		
					    		if (!"".equals(str)) str += ", " + ((Object[])_request.getAttribute(parameter))[i].toString();
					    		else str = ((Object[])_request.getAttribute(parameter))[0].toString();
					    	}
					    	result=str;
				    	} 	
				    }
				}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return result;
	}
	
	protected Object getParamValues(String parameter) throws Exception{
		Object result = null;
		
		try {
			if (!ServletFileUpload.isMultipartContent(_request)) {
				if (null==(_request.getParameter(parameter))){
					String[] str= new String[1];
					str[0]="";
			    	result=str; 
				}else
				result = _request.getParameterValues(parameter);
			} else { // cas en que el request sigui un multipart
				 if (null==(_request.getAttribute(parameter))){
					 String[] str= new String[1];
					 str[0]="";
			    	 result=str; 
			    } else{
			    	String[] str= new String[((Object[])_request.getAttribute(parameter)).length]; 
			    	for (int i = 0; i < ((Object[])_request.getAttribute(parameter)).length; i++){					    		
			    		 str[i] = ((Object[])_request.getAttribute(parameter))[i].toString();
			    	}
			    	result = str;   
			    }						
			}			
		} catch (Exception e) {
			throw new Exception(e);
		}
		return result;
	}

}
