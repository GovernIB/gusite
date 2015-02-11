package es.caib.gusite.front.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.Model;

import es.caib.gusite.front.general.bean.Pardato;
import es.caib.gusite.front.general.bean.PathItem;
import es.caib.gusite.front.general.bean.Tridato;
import es.caib.gusite.front.job.MenuCabecera;
import es.caib.gusite.front.microtag.MParserHTML;
import es.caib.gusite.front.service.FrontUrlFactory;
import es.caib.gusite.front.util.Cadenas;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.IdiomaMicrosite;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Tiposervicio;
import es.caib.gusite.micromodel.TraduccionMicrosite;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;
import es.caib.gusite.utilities.rolsacAPI.APIUtil;
import es.caib.rolsac.api.v2.edifici.EdificiCriteria;
import es.caib.rolsac.api.v2.edifici.EdificiDTO;
import es.caib.rolsac.api.v2.edifici.EdificiQueryServiceAdapter;
import es.caib.rolsac.api.v2.exception.QueryServiceException;
import es.caib.rolsac.api.v2.rolsac.RolsacQueryService;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaCriteria;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaDTO;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaQueryServiceAdapter;

public abstract class BaseController extends FrontController { 
	

	private static Log log = LogFactory.getLog(BaseController.class);

	

	public Microsite loadMicrosite(String uri, Idioma lang, Model model) throws ExceptionFrontMicro {
		return this.loadMicrosite(uri, lang, model, null);
	}
	
	
	/**
	 * Realiza las tareas que antes realizaba el constructor de Bdbase
	 * @param uri
	 * @param lang
	 * @param model
	 * @return 
	 * @throws ExceptionFrontMicro 
	 * @see 
	 */
	public Microsite loadMicrosite(String uri, Idioma lang, Model model, String pcampa) throws ExceptionFrontMicro {

		/* El idioma ya viene fijado en la URI */
		String idi = lang.getLang().toUpperCase();
		model.addAttribute("MVS_idioma",idi);
		
		Microsite microsite = cargarSite(uri, lang, model);
		
		//TODO: comprobar que el idioma es de los válidos para el microsite. En caso contrario habria que lanzar un 404 not found
		cargarListaIdiomas(microsite, model);
		
		//cargarUA(microsite, model, lang);
		cargarCss(microsite, model);
		cargarPie(microsite, model, lang);
    	cargarCabecera(microsite, model, lang);
    	cargarMenu(microsite, model, lang);
	    menucaib(microsite, model, lang);
    	
    	cargarServicio(microsite, model);
    	montarUrl(microsite, model);
    	cargarCampanya(microsite, model, lang, pcampa);
		
    	return microsite;
		
	}

  
	private Microsite cargarSite(String uri, Idioma lang, Model model) throws ExceptionFrontMicro {

		Microsite microsite = null;
    	try {
    		DelegateBase _delegateBase = new DelegateBase();
    		microsite = _delegateBase.obtenerMicrositebyUri(uri, lang.getLang());
			
	    	if ( microsite == null) {
	    		throw new ExceptionFrontMicro(" [Configuracion microsite]: Se debe indicar algún microsite");
	    	}
	    	
	    	if (!"S".equals(microsite.getVisible())) {
	    		throw new ExceptionFrontMicro(" [Configuracion microsite]: El site no es visible");
	    	}

    	} catch (DelegateException e) {
    		throw new ExceptionFrontMicro(e);
		}
    	
    	
		String txnewidsite = String.valueOf(microsite.getId().longValue());
   		model.addAttribute("MVS_idsite", txnewidsite);
		model.addAttribute("MVS_microsite",microsite);

		/**
		 * TODO: comprobar que el lang coincide con alguno de los del microsite. En caso contrario, sería un 404 
    	if (microsite.getIdiomas().size()==1) {
    		Iterator<IdiomaMicrosite> it = microsite.getIdiomas().iterator();	
    		meteidioma(request, it.next().getId().getCodigoIdioma());
    	}
		 */
    	microsite.setIdi(lang.getLang());
    	String titulo_mic = ((TraduccionMicrosite)microsite.getTraduccion(lang.getLang()) != null) ? ((TraduccionMicrosite)microsite.getTraduccion(lang.getLang())).getTitulo() : "&nbsp;";
    	model.addAttribute("MVS_micrositetitulo", titulo_mic );
    	
    	String descripcion_mic=((TraduccionMicrosite)microsite.getTraduccion(lang.getLang()) != null) ? ((TraduccionMicrosite)microsite.getTraduccion(lang.getLang())).getDescription() : "&nbsp;";
    	model.addAttribute("MVS_micrositedescription", descripcion_mic );
    	
    	String keywords_mic=((TraduccionMicrosite)microsite.getTraduccion(lang.getLang()) != null) ? ((TraduccionMicrosite)microsite.getTraduccion(lang.getLang())).getKeywords() : "&nbsp;";
    	model.addAttribute("MVS_micrositekeywords", keywords_mic );
    	
    	String analytics_mic=((TraduccionMicrosite)microsite.getTraduccion(lang.getLang()) != null) ? microsite.getAnalytics() : "&nbsp;";
    	model.addAttribute("MVS_micrositeanalytics", analytics_mic );
    	
    	/* TODO: Esto lo hace de modo poco elegante. Investigar cómo previsualizar
    	//chequear la visibilidad del site
	    String previ= ""+request.getSession().getAttribute("previsualiza");
	    String misite =""+request.getSession().getAttribute("misite");
	    
	    if (!previ.equals("si")||!microsite.getId().toString().equals(misite)){	
	    	if (!microsite.getVisible().equals("S")) {
	    		throw new Exception(" [Configuracion microsite]: El site no es visible");
	    	} 
	   	}
	   	*/
	    
    	return microsite;
	}

	
	
	/**
	 *  Carga en el modelo la lista de idiomas. MVS_listaidiomas
	 * @throws Exception
	 */
	private void cargarListaIdiomas(Microsite microsite, Model model) throws ExceptionFrontMicro {

		try {
			ArrayList<Pardato> listaidiomas = new ArrayList<Pardato>();
			Hashtable<String,String> hashidioma= new Hashtable<String,String>();
			Iterator<IdiomaMicrosite> iter = microsite.getIdiomas().iterator();
			while (iter.hasNext()) {
				String tmpidioma =  ((IdiomaMicrosite)iter.next()).getId().getCodigoIdioma().toLowerCase();
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
					
			model.addAttribute("MVS_listaidiomas", listaidiomas);
			
		} catch (DelegateException e) {
			throw new ExceptionFrontMicro(" [Configuracion microsite] Preparando idiomas...: " + e.getMessage());				
		}
	}
	
	
	/**
	 * Mete en sesión el css. MVS_css
	 * @param request
	 * @throws Exception
	 */
	private void cargarCss(Microsite microsite, Model model) throws ExceptionFrontMicro  {
		//MCR v1.1
		MParserHTML parserhtml = new MParserHTML();
		if (microsite.getEstiloCSS()!=null) {
			model.addAttribute("MVS_css",parserhtml.tagCSS(microsite.getId(), microsite.getEstiloCSS().getId(), microsite.getEstiloCSSPatron()) );
		} else {
			model.addAttribute("MVS_css",parserhtml.tagCSS(null, null,microsite.getEstiloCSSPatron()));
		}
	}
	
	/**
	 * Mete en sesión una lista de beans para el pie. MVS_listapie
	 * @param request
	 */
	private void cargarPie(Microsite microsite, Model model, Idioma lang) throws ExceptionFrontMicro  {
		
		try {
			
			/*sergio_ini*/
			direccionPie(new Long(microsite.getUnidadAdministrativa()), microsite, model, lang);
			/*sergio_fin*/
		   	
		    Iterator<IdiomaMicrosite> iter = microsite.getIdiomas().iterator();
		    TraduccionMicrosite micrositetra;
		    String tmpidioma="";
		    Pardato par;
		    
		    while (iter.hasNext()) {
		    	ArrayList<Pardato> lista = new ArrayList<Pardato>();
		    	tmpidioma = ((IdiomaMicrosite)iter.next()).getId().getCodigoIdioma().toLowerCase();
		    	
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
		    	model.addAttribute("MVS_listapie"+tmpidioma,lista);
		    	if (tmpidioma.equals(lang.getLang())) {
		    		model.addAttribute("MVS_listapie",lista);
		    		break;
		    	}
	        }

		} catch (QueryServiceException e) {
			throw new ExceptionFrontMicro(e);				
		}
		
	}


	protected void direccionPie(Long coduo, Microsite microsite, Model model, Idioma lang) throws QueryServiceException  {
		
    	// DIRECCION DEL PIE DE PAGINA
		
    	StringBuilder direccion = new StringBuilder("");
    	
    	RolsacQueryService rqs = APIUtil.getRolsacQueryService();
    	
    	// Obtener UA.
    	UnitatAdministrativaCriteria uaCriteria = new UnitatAdministrativaCriteria();
    	uaCriteria.setId(coduo.toString());
    	uaCriteria.setIdioma(lang.getLang());
    	UnitatAdministrativaQueryServiceAdapter ua = rqs.obtenirUnitatAdministrativa(uaCriteria);
    	
    	// Obtener edificios asociados y construir dirección.
    	EdificiCriteria edificiCriteria = new EdificiCriteria();
    	List<EdificiQueryServiceAdapter> edificios = ua.llistarEdificis(edificiCriteria);
    	
    	String idUOPresidencia = System.getProperty("es.caib.gusite.codigoUO.presidencia");
		if (idUOPresidencia == null)
			throw new RuntimeException("No se estableció la propiedad de sistema es.caib.gusite.codigoUO.presidencia");
		
		Long UO_PRESIDENCIA = new Long(idUOPresidencia);
    	
		if (edificios.size() > 0) {
			
			EdificiDTO edificio = edificios.get(0);
    		
   			String absUrl =  System.getProperty("es.caib.gusite.portal.url");
	    	String tel = null;
	    	String fax = null;
	    	
   			if (UO_PRESIDENCIA.compareTo(coduo) == 0){
   				direccion.append("<a href=\"" + absUrl + "/govern/presidencia.do?lang=").append(lang.getLang()).append("\">").append(ua.getNombre());
   				
   			}
   			else
   				direccion.append("<a href=\"" + absUrl +"/govern/organigrama/area.do?coduo=").append(coduo).append("&lang=").append(lang.getLang()).append("\">").append(ua.getNombre());
   			
   			direccion.append("</a>");
   			direccion.append("<a href=\"" + absUrl + "/govern/organigrama/planol.do?coduo=").append(coduo).append("&lang=").append(lang.getLang()).append("\">");
   			direccion.append(": ").append(edificio.getDireccion()).append(" - ").append(edificio.getCodigoPostal()).append(" ").append(edificio.getPoblacion()).append("</a><br />");
   			
   			// Preferencia del (tel, fax) de la Unidad Organica sobre el del edificio
			if (ua.getTelefono() != null && ua.getTelefono().length() > 0)
				tel = ua.getTelefono();
			else if (edificio.getTelefono() != null && edificio.getTelefono().length() > 0)
				tel = edificio.getTelefono();
			
			if (tel != null && tel.length() > 0)
				direccion.append(getMessage("WEB_ILL067", lang)).append(" ").append(tel);

			if (ua.getFax() != null && ua.getFax().length() > 0)
				fax = ua.getFax();
			else if (edificio.getFax() != null && edificio.getFax().length() > 0)
				fax = edificio.getFax();

			if (tel != null && tel.length() > 0 && fax != null && fax.length() > 0)
				direccion.append(" - ");
			
			if (fax != null && fax.length() > 0)
				direccion.append(getMessage("WEB_ILL068", lang)).append(" ").append(fax);
			
			if (microsite.getDomini() != null && microsite.getDomini().length() > 0) {
   				direccion.append("<br />").append(getMessage("WEB_ILL130", lang)).append(" <a href=").append(microsite.getDomini()).append(">").append(microsite.getDomini()).append("</a>");
    		} else {
   				if (ua.getUrl()!= null && ua.getUrl().length()>0)
   					direccion.append("<br />").append(getMessage("WEB_ILL130", lang)).append(" <a href=").append(ua.getUrl()).append(">").append(ua.getUrl()).append("</a>");
   			}

    	}
    	
    	model.addAttribute("direccion", direccion.toString());
		
	}
		
	
	/**
	 * Mete en sesión una lista de beans para la cabecera. MVS_listacabecera
	 * @param request
	 */
	private void cargarCabecera(Microsite microsite, Model model, Idioma lang) throws ExceptionFrontMicro  {
		Iterator<IdiomaMicrosite> iter = microsite.getIdiomas().iterator();
		TraduccionMicrosite micrositetra;
		String tmpidioma="";
		Tridato trio;
		FrontUrlFactory urlFactory=new FrontUrlFactory();
		while (iter.hasNext()) {
			ArrayList<Tridato> lista = new ArrayList<Tridato>();
			tmpidioma = ((IdiomaMicrosite) iter.next()).getId().getCodigoIdioma();
			
			micrositetra = (TraduccionMicrosite)microsite.getTraduccion(tmpidioma);
			
			if (microsite.getOptMapa().equals("S")) {
				trio = new Tridato(getMessage("micro.es.mapa", lang),urlFactory.mapa(microsite, lang),"mapaWeb");
				lista.add(trio);
			}
			if (microsite.getOptContacto().equals("S")) {
				trio = new Tridato(getMessage("micro.es.contacto", lang),urlFactory.listarContactos(microsite, lang),"atencio");
				lista.add(trio);
			}
			if (microsite.getOptFaq().equals("S")) {
				trio = new Tridato(getMessage("micro.es.faq", lang),urlFactory.listarFaqs(microsite, lang));
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
			model.addAttribute("MVS_listacabecera"+tmpidioma,lista);

			if (tmpidioma.equals(lang.getLang())) {
				model.addAttribute("MVS_listacabecera",lista);
				break;
			}
		}
			
	}

	/**
	 * Método que cambia el valor MVS_menu de la sesión
	 * @param request
	 */
	private void cargarMenu(Microsite microsite, Model model, Idioma lang) throws ExceptionFrontMicro  {
		DelegateBase delegateBase;
		try {
			delegateBase = new DelegateBase();
			model.addAttribute("MVS_menu", delegateBase.obtenerMainMenu(microsite.getId(), lang.getLang()));
		} catch (DelegateException e) {
			throw new ExceptionFrontMicro(e);
		}
	}
	
	private void cargarServicio(Microsite microsite, Model model) throws ExceptionFrontMicro  {
		model.addAttribute("MVS_servicio", setServicio());
	}

	/**
	 * Método que deben implementar las clases.
	 * Debe devolver un string con el servicio en el que
	 * nos encontremos.
	 * Por ejemplo: return Microfront.RCONTENIDO
	 * @return String un Servicio
	 */
	public abstract String setServicio();
	
	/**
	 * Método que mete en el request el valor de la url del servlet
	 * que se está ejecutando en estos momentos.
	 * 
	 * TODO: esto no puede hacerse a partir del request, claro
	 * 
	 * @param request
	 */
	private void montarUrl(Microsite microsite, Model model) throws ExceptionFrontMicro  {
		//Por ahora ponemos siempre la url de la home
		/*
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
		    */
		model.addAttribute("MVS_seulet", "/" + microsite.getClaveunica() + "/");
	}
	 

	/**
	 * Mete en el request el html de la campaña.
	 * @param request
	 */
	private void cargarCampanya(Microsite microsite, Model model, Idioma lang, String pcampa) throws ExceptionFrontMicro  {
		
		//String pcampa=""+getParameter(Microfront.PCAMPA);
		if (pcampa != null && pcampa.equals("yes")) {
			MParserHTML parserhtml = new MParserHTML();
			String tagHtmlTmpCampaya=parserhtml.getHtmlCampanya(microsite,lang.getLang()).toString();
			if (tagHtmlTmpCampaya.length()>1) model.addAttribute("MVS_home_tmp_campanya",tagHtmlTmpCampaya);
		}
	}		
	
	/**
	 * TODO: eliminar lo que ya no se usa
	 * @param microsite
	 * @param model
	 * @param lang
	 */
	protected void menucaib(Microsite microsite, Model model, Idioma lang) {
    	String idioma = lang.getLang().toLowerCase();
    	model.addAttribute("MVS2_uos", MenuCabecera.getUos(idioma));
	}
	
	/**
	 * Método que devuelve si el servicio que se solicita es ofrecido o no por el microsite.
	 *
	 * @param refservicio una referencia a un servicio
	 * @return boolean true si el tipo de servicio del microsite es igual a refservicio
	 */
	protected boolean existeServicio(Microsite microsite, Idioma lang, String refservicio) {

		boolean tmp=false;
		try {
			DelegateBase _delegateBase = new DelegateBase();
	    	ArrayList<?> listserofr = _delegateBase.obtenerListadoServiciosMicrosite(microsite, lang.getLang());
			//chekeamos el servicio
			Iterator<?> iter2=listserofr.iterator();
			while (iter2.hasNext()) {
				Tiposervicio tiposervicio = (Tiposervicio)iter2.next();
				if (tiposervicio.getReferencia().equals(refservicio)){
					tmp=true;
				}
				if (tmp) break;
			}

		} catch (DelegateException e) {
			log.error("[existeServicio] con el servicio '" + refservicio + "' "  + e.getMessage());
		}
		return tmp;
	}


	/**
	 * TODO: eliminar este método cuando en las plantillas ya se usen los datos
	 * @param microsite
	 * @param model
	 * @param lang
	 * @return
	 * @deprecated
	 */
	protected String mollapan(List<PathItem> pathList) {

		StringBuffer stbuf = new StringBuffer("");
		boolean primero = true;
		for (PathItem item : pathList) {
			
			stbuf.append("<li>");
			if (!primero) stbuf.append("&gt;");

			if (item.hasHref()) {
				stbuf.append("<a href=\"") .append(item.getHref()) .append("\">") .append( item.getLabel() ) .append("</a>");
			} else {
				stbuf.append(item.getLabel());
			}

			stbuf.append("</li>");
			primero = false;
			
		}
		
		return stbuf.toString();
		
	}

	
	/** 
	 * Crea la miga de pan genérica
	 * @param microsite
	 * @param model
	 * @param lang
	 * @return
	 */
	public List<PathItem> getBasePath(Microsite microsite, Model model, Idioma lang) {

		List<PathItem> path = new ArrayList<PathItem>();
		
		ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new Locale(lang.getLang().toUpperCase(), lang.getLang().toUpperCase()));

		path.add( new PathItem(rb.getString("general.inicio"), "http://www.caib.es"));

		UnitatAdministrativaDTO ua = dataService.getUa(microsite.getUnidadAdministrativa(), lang);
		List<PathItem> uaPath = new ArrayList<PathItem>();
		
		while (ua != null) {
			
			StringBuffer uaSbuf = new StringBuffer(ua.getNombre());
			Cadenas.initAllTab(uaSbuf); //primera letra en mayusculas
			String uaTexto = Cadenas.initTab(uaSbuf.toString()); // articulos a minusculas
			
			uaPath.add( new PathItem(uaTexto, this.urlFactory.ua(ua, lang)) );

			if (ua.getPadre() != null && ua.getPadre() != 1) { //TODO: id UA Govern hardcoded

				ua = dataService.getUa(ua.getPadre(), lang);
				
			} else {
				ua = null;
			}
			
		}
		//El uaPath lo hemos creado al revés
		Collections.reverse(uaPath);
		
		path.addAll(uaPath);
		
		//añado el titulo del microsite
		String titulo_mic = ((TraduccionMicrosite)microsite.getTraduccion(lang.getLang())!=null)?((TraduccionMicrosite)microsite.getTraduccion(lang.getLang())).getTitulo():"&nbsp;";

		path.add( new PathItem(titulo_mic, this.urlFactory.home(microsite, lang)) );
		
		return path;

	}


}
