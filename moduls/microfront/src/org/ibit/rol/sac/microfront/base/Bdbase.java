package org.ibit.rol.sac.microfront.base;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.ibit.rol.sac.microfront.Microfront;
import org.ibit.rol.sac.microfront.base.bean.ErrorMicrosite;
import org.ibit.rol.sac.microfront.base.bean.Pardato;
import org.ibit.rol.sac.microfront.base.bean.Tridato;
import org.ibit.rol.sac.microfront.estadistica.util.StatManager;
import org.ibit.rol.sac.microfront.exception.ExceptionFrontMicro;
import org.ibit.rol.sac.microfront.util.Cadenas;
import org.ibit.rol.sac.microfront.util.microtag.MParserHTML;
import org.ibit.rol.sac.micromodel.Estadistica;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.Tiposervicio;
import org.ibit.rol.sac.micromodel.TraduccionMicrosite;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.IdiomaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.MicrositeDelegate;
import org.ibit.rol.sac.model.TraduccionUA;
import org.ibit.rol.sac.model.UnidadAdministrativa;
import org.ibit.rol.sac.persistence.delegate.UnidadAdministrativaDelegate;

import es.caib.loginModule.client.SeyconPrincipal;
import es.caib.sac.unitatOrganica.model.LlocModel;
import es.caib.sac.unitatOrganica.model.UOModel;


/**
 * Clase b�sica para manejar informaci�n.
 * La idea es que las dem�s clases que vayan destinadas a negocio de presentaci�n hereden
 * de esta.
 * Esta clase se encarga ella solita (simplemente inicializando al constructor)
 * de averiguar el idioma en el que est� el cliente, el id del microsite en el que est� el cliente,
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
	protected String tagHtmlTmpCampaya=""; //pegote de html que contiene la campa�a
	protected int publico=Integer.parseInt(Microfront._DMZ_PUBLICO);
	private DelegateBase _delegateBase;
	private MicrositeDelegate _microdel = DelegateUtil.getMicrositeDelegate();
	private HttpServletRequest _request;
	
	
	public Bdbase() {
		// TODO Auto-generated constructor stub
	}
	
	public void finalize() {
		this.dispose();
	}
	
	/**
	 * Metodo p�blico que borra las variables.
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
	 * pie, cabecera, menu, servicio, url y campa�a.
	 * @param request
	 */
	public Bdbase(HttpServletRequest request) throws ExceptionFrontMicro, Exception  {
		//en el momento que haya alg�n error grave en alguno de estos m�todos.... zass--> NO SE VISUALIZA NADA
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
    		//hay alg�n error.
    		idsite=new Long(0);
    		request.getSession().setAttribute("MVS_idsite","0");
    		microsite = null;
    		errorbase=true;
    		throw new Exception(e.getMessage() + " Stack = " + Cadenas.statcktrace2String(e.getStackTrace(), 6));
    		
    	}
   }
		
		
		protected void direccionPie(Long coduo, String lang) throws Exception {
	    	// DIRECCION DEL PIE DE PAGINA
	    	Collection<?> direcciones = _delegateBase.obtenerUacentres(coduo, idioma);
	    	UOModel uo = _delegateBase.getUaDetails(""+coduo, lang);

	    	ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new java.util.Locale(lang, lang));
	    	
	    	StringBuilder direccion = new StringBuilder("");

	    	if (direcciones.iterator().hasNext()) {
	   			LlocModel edi=(LlocModel)direcciones.iterator().next();
	   			Long UO_PRESIDENCIA= new Long(2);
	   			String absUrl =  System.getProperty("es.caib.gusite.portal.url");
		    	String tel = null;
		    	String fax = null;
		    	
	   			if (UO_PRESIDENCIA.compareTo(coduo) == 0)
	   				direccion.append("<a href=\""+ absUrl +"/govern/presidencia.do?lang=").append(lang).append("\">").append(uo.getNom());
	   			else
	   				direccion.append("<a href=\""+ absUrl +"/govern/organigrama/area.do?coduo=").append(coduo).append("&lang=").append(lang).append("\">").append(uo.getNom());
	   			
	   			direccion.append("</a>");
	   			direccion.append("<a href=\""+ absUrl +"/govern/organigrama/planol.do?coduo=").append(coduo).append("&lang=").append(lang).append("\">");
	   			direccion.append(": ").append(edi.getAdreca()).append(" - ").append(edi.getCodiPostal()).append(" ").append(edi.getPoblacio()).append("</a><br />");
	   			
	   			// Preferncia del (tel, fax) de la Unidad Organica sobre el del edificio
	   			if (uo.getTelefon()!=null && uo.getTelefon().length()>0) tel=uo.getTelefon(); 
	   			else if (edi.getTelefon()!=null && edi.getTelefon().length()>0) tel=edi.getTelefon();
	   			if (tel!=null && tel.length()>0)
	   				direccion.append(rb.getString("WEB_ILL067")).append(" ").append(tel);
	   				
	   			if (uo.getFax()!=null && uo.getFax().length()>0) fax=uo.getFax();
	   			else if (edi.getFax()!=null && edi.getFax().length()>0) fax=edi.getFax();
	   			
	   			if (tel!=null && tel.length()>0 && fax!=null && fax.length()>0)
	   				direccion.append(" - ");
	   				
	   			if (fax!=null && fax.length()>0)
	   				direccion.append(rb.getString("WEB_ILL068")).append(" ").append(fax);
	   			
	   			/*if (uo.getEmail()!=null && uo.getEmail().length()>0) 
	   				direccion.append(" - <a href=\"mailto:").append(uo.getEmail()).append(" \">").append(uo.getEmail()).append("</a>");*/
	   			
	   			if (microsite.getDomini()!= null && microsite.getDomini().length()>0){
	   				direccion.append("<br />").append(rb.getString("WEB_ILL130")).append(" <a href=").append(microsite.getDomini()).append(">").append(microsite.getDomini()).append("</a>");
	    		}else{
	   				if (uo.getUrl()!= null && uo.getUrl().length()>0)
	   					direccion.append("<br />").append(rb.getString("WEB_ILL130")).append(" <a href=").append(uo.getUrl()).append(">").append(uo.getUrl()).append("</a>");
	   			}
	   		}
	    	_request.setAttribute("direccion", direccion.toString());
			
		}
			
		/**
		 * Mete en sesi�n el valor del idioma. MVS_idioma
		 * @param request
		 */
		private void prepararidioma(HttpServletRequest request) throws Exception  {
			
	    	idioma = "" + getParameter(Microfront.PLANG);
	    	if (!idioma.equals("null")) {
	    		meteidioma(request, idioma);
	    	} else {
	    		idioma = "" + request.getSession().getAttribute("MVS_idioma");
		    	if (idioma.equals("null")) {
		    		meteidioma(request, "CA");
		    	}
	    	}
	    	idioma = idioma.toLowerCase();
		}
		
		/**
		 * Mete en sesi�n el valor del un idioma en concreto.
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
		 * Mete en sesi�n el idsite y si fuera necesario el site. MVS_idsite
		 * @param request
		 */
		private void prepararidsite(HttpServletRequest request) throws ExceptionFrontMicro, Exception {
			boolean guardoStatSesion=false;
			String txoldidsite = "" + request.getSession().getAttribute("MVS_idsite");
	    	String txnewidsite = "" + getParameter("idsite");

	    	/* ***** Inicio Pegote para recoger MKEY */
	    	try {
			    	String newmkey = "" +  getParameter(Microfront.PMKEY);
			    	if ("null".equals(newmkey)){
			    		Microsite micro2  = _microdel.obtenerMicrosite(new Long(txnewidsite));
			    	}else {
			    		Microsite micro2 = _delegateBase.obtenerMicrositebyKey(newmkey, idioma);
			    		txnewidsite = "" + micro2.getId().longValue();
			    		
			    	}

	    	} catch (Exception e) {
	    		throw new ExceptionFrontMicro("No s' ha trobat el microsite amb id " + getIdsiteError("" +  getParameter(Microfront.PMKEY)));
	    	}
	    	/* ***** Fin Pegote para recoger MKEY */
	    	
	    	if ( (txnewidsite.equals("null")) && (txoldidsite.equals("null")) ) {
	    		throw new ExceptionFrontMicro(" [Configuracion microsite]: Se debe indicar alg�n microsite");
	    	}
	    	
	    	Hashtable<String, String> hssites = (Hashtable)request.getSession().getAttribute("MVS_hashsites");
	    	
	    	if ( (hssites==null) || (!hssites.contains(txnewidsite)) ) {
	    		guardoStatSesion=true;
	    		if (hssites==null) hssites=new Hashtable<String, String>();
	    		idsite = new Long(txnewidsite);
    			request.getSession().setAttribute("MVS_idsite",txnewidsite);
    			cambiarmicrositesesion(request);
	    	} else {
	    		microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
	    		idsite = new Long(txoldidsite);

	    		if (!txnewidsite.equals(txoldidsite)) {
		    		//si son distintos, borramos las variables temporales de sesion.
		    		idsite = new Long(txnewidsite);
	    			request.getSession().setAttribute("MVS_idsite",txnewidsite);
		    		cambiarmicrositesesion(request);
		    	} else {
					MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
					microsite = micrositedel.obtenerMicrosite(idsite);
					request.getSession().setAttribute("MVS_microsite",microsite);
		    	}
	    	}
	    	
	    	if (microsite.getIdiomas().size()==1) {
	    		Iterator<?> it = microsite.getIdiomas().iterator();	
	    		meteidioma(request, (String)it.next());
	    	}
	    	
	    	microsite.setIdi(idioma);
	    	String titulo_mic = ((TraduccionMicrosite)microsite.getTraduccion(idioma)!=null)?((TraduccionMicrosite)microsite.getTraduccion(idioma)).getTitulo():"&nbsp;";
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
			String errorIdSite = attributeMVSidSite.substring(0,3);
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
					Iterator<?> iter = microsite.getIdiomas().iterator();
					while (iter.hasNext()) {
						String tmpidioma =  ((String)iter.next()).toLowerCase();
						String tmptxtidioma="";
						if (tmpidioma.equals("ca")) tmptxtidioma="Catal&agrave;";
						if (tmpidioma.equals("es")) tmptxtidioma="Castellano";
						if (tmpidioma.equals("en")) tmptxtidioma="English";
						if (tmpidioma.equals("de")) tmptxtidioma="Deutsch";
						if (tmpidioma.equals("fr")) tmptxtidioma="Fran�ais";
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
		 * Mete en sesi�n la unidad administrativa. MVS_ua
		 * @param request
		 */
		private void prepararua(HttpServletRequest request) throws Exception {
			try {
				microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
				
			    Iterator<?> iter = microsite.getIdiomas().iterator();
			    String tmpidioma="";
			    while (iter.hasNext()) {
			    	String uacompleta = "";
			    	tmpidioma = (String) iter.next();
			    	
			    	//si ya est� en sesi�n la lista idiomatica, no la metemos
			    	uacompleta = "" + request.getSession().getAttribute("MVS_ua"+tmpidioma);
			    	
			    	if (uacompleta.equals("null")) {
			    			uacompleta=motarua(tmpidioma).toString();
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
		 * Monta un link. Este link apunta a la p�gina de una UnidadAdministritiva, y con el idioma que se le pasa al m�todo
		 * @param idioma  un idioma
		 * @return StringBuffer
		 */
		private StringBuffer motarua(String idioma) {
		
			StringBuffer tmp = new StringBuffer(" ");
			try {
					UnidadAdministrativaDelegate uadel=org.ibit.rol.sac.persistence.delegate.DelegateUtil.getUADelegate();
				    UnidadAdministrativa uniadm=uadel.obtenerUnidadAdministrativa(new Long(microsite.getUnidadAdministrativa()));
					while ((uniadm!=null) && (uniadm.getId().longValue()!=1)) {
						StringBuffer ua_sbuf = new StringBuffer( ((TraduccionUA)uniadm.getTraduccion(idioma)).getNombre() );
						Cadenas.initAllTab(ua_sbuf); //primera letra en mayusculas
						String ua_texto=Cadenas.initTab(ua_sbuf.toString()); // articulos a minusculas
						String ua_url="/govern/organigrama/area.do?lang=" + idioma + "&coduo="+uniadm.getId();
						//CANVI ERRADA ACCES A ORGANIGRAMA DES DE MICROSITE AMB DOMINI ASSOCIAT
							ua_url =  System.getProperty("es.caib.gusite.portal.url") + ua_url;
						// FI CANVI
						tmp.insert(0, "<a href=\"" + ua_url + "\">" + ua_texto + "</a>" + " > " );
						uniadm = uniadm.getPadre();
					}
					if (tmp.length()>3) tmp.delete(tmp.length()-3, tmp.length());
					else tmp.append("&nbsp;");
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
			
				String elHost = request.getServerName();
		    	String value = System.getProperty("es.indra.caib.rolsac.oficina");
		        if ((value == null) || value.equals("N"))
		        	elHost = Microfront._HOSTCAIB;
			
				//String requrl = request.getRequestURL().toString().toLowerCase();

				// Si se ha logado se considera intranet, fichero de apache conf ProxyPass invalida forma anterior 
				if (request.getHeader("Authorization")== null)	publico = Integer.parseInt(Microfront._DMZ_PUBLICO);
				else 											publico = Integer.parseInt(Microfront._DMZ_INTRANET);
				
		}
		

		/**
		 * Mete en sesi�n el css. MVS_css
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
		 * Mete en sesi�n una lista de beans para el pie. MVS_listapie
		 * @param request
		 */
		private void prepararpie() throws Exception  {
			
			try {
				
				microsite = (Microsite)_request.getSession().getAttribute("MVS_microsite");
				/*sergio_ini*/
				direccionPie(new Long(microsite.getUnidadAdministrativa()), idioma);
				/*sergio_fin*/
			   	
			    Iterator<?> iter = microsite.getIdiomas().iterator();
			    TraduccionMicrosite micrositetra;
			    String tmpidioma="";
			    Pardato par;
			    
			    while (iter.hasNext()) {
			    	ArrayList<Pardato> lista = new ArrayList<Pardato>();
			    	tmpidioma = (String) iter.next();
			    	
			    	//si ya est� en sesi�n la lista idiomatica, no la metemos
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
		 * Mete en sesi�n una lista de beans para la cabecera. MVS_listacabecera
		 * @param request
		 */
		private void prepararcabecera(HttpServletRequest request) throws Exception  {
			try {
				microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
				
				ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));
				
			    Iterator<?> iter = microsite.getIdiomas().iterator();
			    TraduccionMicrosite micrositetra;
			    String tmpidioma="";
			    Tridato trio;
			    
			    while (iter.hasNext()) {
			    	ArrayList<Tridato> lista = new ArrayList<Tridato>();
			    	tmpidioma = (String) iter.next();
			    	
			    	//si ya est� en sesi�n la lista idiomatica, no la metemos
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
		 * M�todo que cambia el valor MVS_menu de la sesi�n
		 * @param request
		 */
		private void prepararmenu() throws Exception  {
			_request.setAttribute("MVS_menu",_delegateBase.obtenerMainMenu(microsite.getId(), idioma));
		}
		
		private void prepararservicio(HttpServletRequest request) throws Exception  {
			request.getSession().setAttribute("MVS_servicio",setServicio());
		}
		
		/**
		 * M�todo que mete en el request el valor de la url del servlet
		 * que se est� ejecutando en estos momentos.
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
			    request.setAttribute("MVS_seulet", url);
		}

		/**
		 * Mete en el request el html de la campa�a.
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
		 * M�todo que cambia el valor MVS_microsite de la sesi�n.
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
		 * M�todo que borra variables de sesion.
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
		 * M�todo que devuelve si el servicio que se solicita es ofrecido o no por el microsite.
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
			es.caib.loginModule.client.SeyconPrincipal principal;
			try {
				
				principal = es.caib.loginModule.client.SeyconPrincipal.getCurrent();
				if (principal != null) iduser=principal.getIntranetUser();
			} catch (NamingException e) {
				log.warn(e.getMessage());
			}
			return iduser;
			 
		} 		
		
		/**
		 * Devuelve un Objeto SeyconPrincipal.
		 * @param request
		 * @return SeyconPrincipal
		 */
		public SeyconPrincipal getPrincipal( HttpServletRequest request )
	    {
			return ( SeyconPrincipal ) request.getUserPrincipal();
	    }		
		
		/**
		 * M�todo que deben implementar las clases.
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
