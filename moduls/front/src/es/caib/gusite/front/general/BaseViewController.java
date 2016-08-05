package es.caib.gusite.front.general;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.servlet.ModelAndView;

import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.general.bean.Pardato;
import es.caib.gusite.front.general.bean.PathItem;
import es.caib.gusite.front.general.bean.Tridato;
import es.caib.gusite.front.microtag.MParserHTML;
import es.caib.gusite.front.service.FrontUrlFactory;
import es.caib.gusite.front.util.Cadenas;
import es.caib.gusite.front.view.ErrorGenericoView;
import es.caib.gusite.front.view.LayoutView;
import es.caib.gusite.front.view.LayoutView.ArchivoCSS;
import es.caib.gusite.front.view.PageView;
import es.caib.gusite.front.view.Variable;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.IdiomaMicrosite;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Tiposervicio;
import es.caib.gusite.micromodel.TraduccionMicrosite;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;
import es.caib.gusite.plugins.PluginException;
import es.caib.gusite.plugins.organigrama.OrganigramaProvider;
import es.caib.gusite.plugins.organigrama.UnidadData;
import es.caib.gusite.plugins.organigrama.UnidadListData;

/**
 * @author at4.net
 * 
 */
public abstract class BaseViewController extends FrontController {

	private static Log log = LogFactory.getLog(BaseViewController.class);

	public void configureLayoutView(String uri, Idioma lang, LayoutView view) throws ExceptionFrontMicro {
		this.configureLayoutView(uri, lang, view, null, null);
	}

	@Autowired
	private OrganigramaProvider organigramaProvider; 
	
	@Autowired
	private Version version;
	
	/**
	 * Realiza las tareas que antes realizaba el constructor de Bdbase
	 * 
	 * @param uri
	 * @param lang
	 * @param model
	 * @param uriContenido
	 * @return
	 * @throws ExceptionFrontMicro
	 * @see
	 */
	public void configureLayoutView(final String uri, final Idioma lang, final LayoutView view, final String pcampa) throws ExceptionFrontMicro {
		this.configureLayoutView(uri, lang, view, pcampa, null);
	}
	
	/**
	 * Realiza las tareas que antes realizaba el constructor de Bdbase
	 * 
	 * @param uri
	 * @param lang
	 * @param model
	 * @param uriContenido
	 * @return
	 * @throws ExceptionFrontMicro
	 * @see
	 */
	public void configureLayoutView(final String uri, final Idioma lang, final LayoutView view, final String pcampa, final String uriContenido) throws ExceptionFrontMicro {

		/* El idioma ya viene fijado en la URI */
		String idi = lang.getLang().toUpperCase();
		view.setIdioma(idi);
		view.setLang(lang);
		view.setVersion(version);

		this.cargarSite(uri, lang, view);

		// TODO: comprobar que el idioma es de los válidos para el microsite. En
		// caso contrario habria que lanzar un 404 not found
		this.cargarListaIdiomas(view);

		// cargarUA(microsite, model, lang);
		this.cargarCss(view);
		this.cargarPie(view);
		this.cargarCabecera(view);
		this.cargarMenu(view, uriContenido);
		this.menucaib(view);

		this.cargarServicio(view);
		this.cargarCampanya(view, pcampa);

	}

	/**
	 * Genera el modelo spring para la vista, según sus variables
	 * 
	 * @param viewName
	 * @param view
	 * @return
	 */
	protected ModelAndView modelForView(String viewName, LayoutView view) {

		// TODO: cachear las variables (métodos) por vista
		ModelAndView mv = new ModelAndView(viewName);
		for (Method method : view.getClass().getMethods()) {
			if (method.isAnnotationPresent(Variable.class)) {
				String varName = method.getAnnotation(Variable.class).value();
				try {
					mv.addObject(varName, method.invoke(view));
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return mv;
	}

	private Microsite cargarSite(String uri, Idioma lang, LayoutView view) throws ExceptionFrontMicro {

		Microsite microsite = null;
		try {
			DelegateBase _delegateBase = new DelegateBase();
			microsite = _delegateBase.obtenerMicrositebyUri(uri, lang.getLang());

			if (microsite == null) {
				throw new ExceptionFrontMicro(" [Configuracion microsite]: Se debe indicar algún microsite");
			}

			/**
			 * Esta comprobación ahora se hace en el interceptor
			if (!"S".equals(microsite.getVisible())) {
				throw new ExceptionFrontMicro(" [Configuracion microsite]: El site no es visible");
			}
			 * 
			 */

		} catch (DelegateException e) {
			throw new ExceptionFrontMicro(e);
		}

		String txnewidsite = String.valueOf(microsite.getId().longValue());
		view.setIdsite(txnewidsite);
		view.setMicrosite(microsite);

		/**
		 * TODO: comprobar que el lang coincide con alguno de los del microsite.
		 * En caso contrario, sería un 404 if (microsite.getIdiomas().size()==1)
		 * { Iterator<IdiomaMicrosite> it = microsite.getIdiomas().iterator();
		 * meteidioma(request, it.next().getId().getCodigoIdioma()); }
		 */
		microsite.setIdi(lang.getLang());
		String titulo_mic = ((TraduccionMicrosite) microsite.getTraduccion(lang.getLang()) != null) ? ((TraduccionMicrosite) microsite
				.getTraduccion(lang.getLang())).getTitulo() : "&nbsp;";
		view.setMicrositeTitulo(titulo_mic);

		String descripcion_mic = ((TraduccionMicrosite) microsite.getTraduccion(lang.getLang()) != null) ? ((TraduccionMicrosite) microsite
				.getTraduccion(lang.getLang())).getDescription() : "&nbsp;";
		view.setMicrositeDescription(descripcion_mic);

		String keywords_mic = ((TraduccionMicrosite) microsite.getTraduccion(lang.getLang()) != null) ? ((TraduccionMicrosite) microsite
				.getTraduccion(lang.getLang())).getKeywords() : "&nbsp;";
		view.setMicrositeKeywords(keywords_mic);

		String analytics_mic = microsite.getAnalytics();
		view.setMicrositeAnalytics(analytics_mic);

		return microsite;
	}

	/**
	 * Carga en el modelo la lista de idiomas. MVS_listaidiomas
	 * 
	 * @throws Exception
	 */
	private void cargarListaIdiomas(LayoutView view) throws ExceptionFrontMicro {

		try {

			List<Pardato> listaidiomas = new ArrayList<Pardato>();
			Hashtable<String, String> hashidioma = new Hashtable<String, String>();
			Iterator<IdiomaMicrosite> iter = view.getMicrosite().getIdiomas().iterator();
			while (iter.hasNext()) {
				String tmpidioma = ((IdiomaMicrosite) iter.next()).getId().getCodigoIdioma().toLowerCase();
				String tmptxtidioma = "";
				if (tmpidioma.equals("ca")) {
					tmptxtidioma = "Catal&agrave;";
				}
				if (tmpidioma.equals("es")) {
					tmptxtidioma = "Castellano";
				}
				if (tmpidioma.equals("en")) {
					tmptxtidioma = "English";
				}
				if (tmpidioma.equals("de")) {
					tmptxtidioma = "Deutsch";
				}
				if (tmpidioma.equals("fr")) {
					tmptxtidioma = "Français";
				}
				hashidioma.put(tmpidioma, tmptxtidioma);
			}

			IdiomaDelegate ididel = DelegateUtil.getIdiomaDelegate();
			Iterator<?> iteridi = ididel.listarLenguajes().iterator();
			while (iteridi.hasNext()) {
				String tmpidioma = ((String) iteridi.next()).toLowerCase();
				if (hashidioma.containsKey(tmpidioma)) {
					listaidiomas.add(new Pardato(tmpidioma, (String) hashidioma.get(tmpidioma)));
				}
			}

			view.setListaIdiomas(listaidiomas);

		} catch (DelegateException e) {
			throw new ExceptionFrontMicro(" [Configuracion microsite] Preparando idiomas...: " + e.getMessage());
		}
	}

	/**
	 * Mete en sesión el css. MVS_css
	 * 
	 * @param request
	 * @throws Exception
	 */
	private void cargarCss(LayoutView view) throws ExceptionFrontMicro {

		List<ArchivoCSS> archivosCss = new ArrayList<ArchivoCSS>();

		if (view.getMicrosite().getEstiloCSS() != null) {
			archivosCss.add( new ArchivoCSS( this.urlFactory.archivopub(view.getMicrosite(), view.getMicrosite().getEstiloCSS())) );
			archivosCss.add( new ArchivoCSS( "/resources/css/estils_print.css", "print"));
		} else {
			archivosCss.add( new ArchivoCSS( "/resources/css/estils.css", "screen") );
			archivosCss.add( new ArchivoCSS( "/resources/css/estils_print.css", "print"));
			
			if (view.getMicrosite().getEstiloCSSPatron().equals("A")) {
				archivosCss.add( new ArchivoCSS( "/resources/css/estils_blau.css", "screen") );
			}
			if (view.getMicrosite().getEstiloCSSPatron().equals("R")) {
				archivosCss.add( new ArchivoCSS( "/resources/css/estils_roig.css", "screen") );
			}
			if (view.getMicrosite().getEstiloCSSPatron().equals("V")) {
				archivosCss.add( new ArchivoCSS( "/resources/css/estils_verd.css", "screen") );
			}
			if (view.getMicrosite().getEstiloCSSPatron().equals("G")) {
				archivosCss.add( new ArchivoCSS( "/resources/css/estils_groc.css", "screen") );
			}
			if (view.getMicrosite().getEstiloCSSPatron().equals("M")) {
				archivosCss.add( new ArchivoCSS( "/resources/css/estils_morat.css", "screen") );
			}
		}
		
		if (view.getMicrosite().getTema() != null) {

			view.setUriTema( this.urlFactory.uriTema(view.getMicrosite().getTema()));
			
			if (view.getMicrosite().getTema().getCss() != null) {
				archivosCss.add( new ArchivoCSS ( this.urlFactory.cssTema(view.getMicrosite().getTema()), "screen" ) );
			}
			
		}
		
		view.setCss(archivosCss);
		
	}

	/**
	 * Mete en sesión una lista de beans para el pie. MVS_listapie
	 * 
	 * @param request
	 */
	private void cargarPie(LayoutView view) throws ExceptionFrontMicro {

		try {

			/* sergio_ini */
			this.direccionPie(view);
			/* sergio_fin */

			Microsite microsite = view.getMicrosite();

			Iterator<IdiomaMicrosite> iter = microsite.getIdiomas().iterator();
			TraduccionMicrosite micrositetra;
			String tmpidioma = "";
			Pardato par;

			while (iter.hasNext()) {
				ArrayList<Pardato> lista = new ArrayList<Pardato>();
				tmpidioma = ((IdiomaMicrosite) iter.next()).getId().getCodigoIdioma().toLowerCase();

				micrositetra = (TraduccionMicrosite) microsite.getTraduccion(tmpidioma);
				if (micrositetra != null) {
					if ((micrositetra.getTxtop3() != null) && (micrositetra.getUrlop3() != null)) {
						par = new Pardato(micrositetra.getTxtop3(), micrositetra.getUrlop3());
						if (microsite.getOpt3().equals("S")) {
							lista.add(par);
						}
					}
					if ((micrositetra.getTxtop4() != null) && (micrositetra.getUrlop4() != null)) {
						par = new Pardato(micrositetra.getTxtop4(), micrositetra.getUrlop4());
						if (microsite.getOpt4().equals("S")) {
							lista.add(par);
						}
					}
					if ((micrositetra.getTxtop5() != null) && (micrositetra.getUrlop5() != null)) {
						par = new Pardato(micrositetra.getTxtop5(), micrositetra.getUrlop5());
						if (microsite.getOpt5().equals("S")) {
							lista.add(par);
						}
					}
					if ((micrositetra.getTxtop6() != null) && (micrositetra.getUrlop6() != null)) {
						par = new Pardato(micrositetra.getTxtop6(), micrositetra.getUrlop6());
						if (microsite.getOpt6().equals("S")) {
							lista.add(par);
						}
					}
					if ((micrositetra.getTxtop7() != null) && (micrositetra.getUrlop7() != null)) {
						par = new Pardato(micrositetra.getTxtop7(), micrositetra.getUrlop7());
						if (microsite.getOpt7().equals("S")) {
							lista.add(par);
						}
					}
				}

				if (tmpidioma.equals(view.getIdioma().toLowerCase())) {
					view.setListaPie(lista);
					break;
				}
			}

		} catch (PluginException e) {
			throw new ExceptionFrontMicro(e);
		}

	}

	protected void direccionPie(LayoutView view) throws PluginException {

		// DIRECCION DEL PIE DE PAGINA

		StringBuilder direccion = new StringBuilder("");

		Long coduo = Long.valueOf(view.getMicrosite().getUnidadAdministrativa());
		UnidadData unidadData = this.organigramaProvider.getUnidadData(coduo, view.getLang().getLang());
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
				.append(unidadData.getPoblacion()).append("</a><p></p>");
			} else {
				//TODO: asumimos que si hay dirección, también hay cp y pob 
				direccion.append(": ").append(unidadData.getDireccion()).append(" - ").append(unidadData.getCodigoPostal()).append(" ")
				.append(unidadData.getPoblacion()).append("<p></p>");
			}
		}

		if (!isEmpty(unidadData.getTelefono())) {
			direccion.append(this.getMessage("WEB_ILL067", view.getLang().getLang())).append(" ").append(unidadData.getTelefono());
		}


		if (!isEmpty(unidadData.getTelefono()) && !isEmpty(unidadData.getFax())) {
			direccion.append(" - ");
		}

		if (!isEmpty(unidadData.getFax())) {
			direccion.append(this.getMessage("WEB_ILL068", view.getLang().getLang())).append(" ").append(unidadData.getFax());
		}

		if (view.getMicrosite().getDomini() != null && view.getMicrosite().getDomini().length() > 0) {
			direccion.append("<p></p>").append(this.getMessage("WEB_ILL130", view.getLang().getLang())).append(" <a href=")
					.append(view.getMicrosite().getDomini()).append(">").append(view.getMicrosite().getDomini()).append("</a>");
		} else if (!isEmpty(unidadData.getDominio())) {
				direccion.append("<p></p>").append(this.getMessage("WEB_ILL130", view.getLang().getLang())).append(" <a href=")
						.append(unidadData.getDominio()).append(">").append(unidadData.getDominio()).append("</a>");
		}

		view.setDireccion(direccion.toString());

	}

	private boolean isEmpty(String url) {
		return url == null || url.length() < 1; 
	}

	/**
	 * Mete en sesión una lista de beans para la cabecera. MVS_listacabecera
	 * 
	 * @param request
	 */
	private void cargarCabecera(LayoutView view) throws ExceptionFrontMicro {
		Microsite microsite = view.getMicrosite();
		Idioma lang = view.getLang();
		Iterator<IdiomaMicrosite> iter = microsite.getIdiomas().iterator();
		TraduccionMicrosite micrositetra;
		String tmpidioma = "";
		Tridato trio;
		FrontUrlFactory urlFactory = new FrontUrlFactory();
		while (iter.hasNext()) {
			List<Tridato> lista = new ArrayList<Tridato>();
			tmpidioma = ((IdiomaMicrosite) iter.next()).getId().getCodigoIdioma();

			micrositetra = (TraduccionMicrosite) microsite.getTraduccion(tmpidioma);

			if (microsite.getOptMapa().equals("S")) {
				trio = new Tridato(this.getMessage("micro.es.mapa", lang), urlFactory.mapa(microsite, lang), "mapaWeb");
				lista.add(trio);
			}
			if (microsite.getOptContacto().equals("S")) {
				trio = new Tridato(this.getMessage("micro.es.contacto", lang), urlFactory.listarContactos(microsite, lang), "atencio");
				lista.add(trio);
			}
			if (microsite.getOptFaq().equals("S")) {
				trio = new Tridato(this.getMessage("micro.es.faq", lang), urlFactory.listarFaqs(microsite, lang));
				lista.add(trio);
			}
			if (micrositetra != null) {
				if ((micrositetra.getTxtop1() != null) && (micrositetra.getUrlop1() != null)) {
					trio = new Tridato(micrositetra.getTxtop1(), urlFactory.legacyToFrontUri(micrositetra.getUrlop1(), lang));
					if (microsite.getOpt1().equals("S")) {
						lista.add(trio);
					}
				}
				if ((micrositetra.getTxtop2() != null) && (micrositetra.getUrlop2() != null)) {
					trio = new Tridato(micrositetra.getTxtop2(), urlFactory.legacyToFrontUri(micrositetra.getUrlop2(), lang));
					if (microsite.getOpt2().equals("S")) {
						lista.add(trio);
					}
				}
			}

			if (tmpidioma.equals(lang.getLang())) {
				view.setListaCabecera(lista);
				break;
			}
		}

	}

	/**
	 * Método que cambia el valor MVS_menu de la sesión
	 * 
	 * @param request
	 */
	private void cargarMenu(final LayoutView view, final String uriContenido) throws ExceptionFrontMicro {
		DelegateBase delegateBase;
		try {
			delegateBase = new DelegateBase(); 
			view.setMenu(delegateBase.obtenerMainMenu(view.getMicrosite().getId(), view.getLang().getLang(), uriContenido));
		} catch (DelegateException e) {
			throw new ExceptionFrontMicro(e);
		}
	}

	private void cargarServicio(LayoutView view) throws ExceptionFrontMicro {
		view.setServicio(this.setServicio());
	}

	/**
	 * Método que deben implementar las clases. Debe devolver un string con el
	 * servicio en el que nos encontremos. Por ejemplo: return
	 * Microfront.RCONTENIDO
	 * 
	 * @return String un Servicio
	 */
	public abstract String setServicio();

	/**
	 * Mete en el request el html de la campaña.
	 * 
	 * @param request
	 */
	private void cargarCampanya(LayoutView view, String pcampa) throws ExceptionFrontMicro {

		// String pcampa=""+getParameter(Microfront.PCAMPA);
		if (pcampa != null && pcampa.equals("yes")) {
			MParserHTML parserhtml = new MParserHTML();
			String tagHtmlTmpCampaya = parserhtml.getHtmlCampanya(view.getMicrosite(), view.getLang().getLang()).toString();
			if (tagHtmlTmpCampaya.length() > 1) {
				view.setHomeTmpCampanya(tagHtmlTmpCampaya);
			}
		}
	}

	/**
	 * TODO: El menú corporativo debería ser un plugin que devolviese el árbol de menú
	 * 
	 * @param microsite
	 * @param model
	 * @param lang
	 */
	protected void menucaib(LayoutView view) {
		String idioma = view.getLang().getLang().toLowerCase();
		view.setUos(getUos(idioma));
	}
	
	/**
	 * Plantillas aplicables al microsite
	 * 
	 * @param microsite
	 * @return
	 * @throws ExceptionFront
	 */
	@Cacheable(value = "uos")
	private Collection<UnidadListData> getUos(String lang) {
		Collection<UnidadListData> lista;
		try {
			lista = this.organigramaProvider.getUnidadesPrincipales(lang);
		} catch (PluginException e) {
			log.error(e);
			return new ArrayList<UnidadListData>();
		}
		return lista;
	}
	

	/**
	 * Método que devuelve si el servicio que se solicita es ofrecido o no por
	 * el microsite.
	 * 
	 * @param refservicio
	 *            una referencia a un servicio
	 * @return boolean true si el tipo de servicio del microsite es igual a
	 *         refservicio
	 */
	protected boolean existeServicio(Microsite microsite, Idioma lang, String refservicio) {

		boolean tmp = false;
		try {
			DelegateBase _delegateBase = new DelegateBase();
			ArrayList<?> listserofr = _delegateBase.obtenerListadoServiciosMicrosite(microsite, lang.getLang());
			// chekeamos el servicio
			Iterator<?> iter2 = listserofr.iterator();
			while (iter2.hasNext()) {
				Tiposervicio tiposervicio = (Tiposervicio) iter2.next();
				if (tiposervicio.getReferencia().equals(refservicio)) {
					tmp = true;
				}
				if (tmp) {
					break;
				}
			}

		} catch (DelegateException e) {
			log.error("[existeServicio] con el servicio '" + refservicio + "' " + e.getMessage());
		}
		return tmp;
	}

	/**
	 * Crea la miga de pan genérica
	 * 
	 * @param microsite
	 * @param model
	 * @param lang
	 * @return
	 */
	public List<PathItem> getBasePath(LayoutView view) {

		Microsite microsite = view.getMicrosite();
		Idioma lang = view.getLang();
		List<PathItem> path = new ArrayList<PathItem>();

		ResourceBundle rb = ResourceBundle.getBundle("ApplicationResources_front", new Locale(lang.getLang().toUpperCase(), lang.getLang()
				.toUpperCase()));

		path.add(new PathItem(rb.getString("general.inicio"), "http://www.caib.es"));

		UnidadData ua = null;
		try {
			ua = this.organigramaProvider.getUnidadData(microsite.getUnidadAdministrativa(), lang.getLang());
		} catch (PluginException e) {
			log.error(e);
		}
		List<PathItem> uaPath = new ArrayList<PathItem>();

		while (ua != null) {

			StringBuffer uaSbuf = new StringBuffer(ua.getNombre());
			Cadenas.initAllTab(uaSbuf); // primera letra en mayusculas
			String uaTexto = Cadenas.initTab(uaSbuf.toString()); // articulos a
																	// minusculas

			uaPath.add(new PathItem(uaTexto, this.urlFactory.ua(ua, lang)));

			if (ua.getIdUnidadPadre() != null && Long.valueOf(ua.getIdUnidadPadre().toString()) != 1) { // TODO: id UA Govern hardcoded

				try {
					ua = this.organigramaProvider.getUnidadData(ua.getIdUnidadPadre(), lang.getLang());
				} catch (PluginException e) {
					log.error(e);
				}

			} else {
				ua = null;
			}

		}
		// El uaPath lo hemos creado al revés
		Collections.reverse(uaPath);

		path.addAll(uaPath);

		// añado el titulo del microsite
		String titulo_mic = ((TraduccionMicrosite) microsite.getTraduccion(lang.getLang()) != null) ? ((TraduccionMicrosite) microsite
				.getTraduccion(lang.getLang())).getTitulo() : "&nbsp;";

		path.add(new PathItem(titulo_mic, this.urlFactory.home(microsite, lang)));

		return path;

	}

	/**
	 * Metodo protegido que devuelve String de error de contenido de un site
	 * 
	 * @param HttpServletRequest
	 *            request, Microsite microsite, ErrorMicrosite errorMicrosite
	 * @exception Exception
	 */
	protected ModelAndView getForwardError(PageView causeView, String ambitError) {

		ErrorGenericoView view = new ErrorGenericoView(causeView);
		Microsite microsite = view.getMicrosite();

		ErrorMicrosite errorMicrosite = null;

		if (ambitError.equals(ErrorMicrosite.ERROR_AMBIT_MICRO)) {

			if (microsite != null) {
				errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_MICRO_TIT, ErrorMicrosite.ERROR_MICRO_MSG + microsite.getId(), "","",ErrorMicrosite.ESTADO_NOT_FOUNT);
			} else {
				errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_MICRO_TIT, ErrorMicrosite.ERROR_MICRO_MSG_NULL,"","",ErrorMicrosite.ESTADO_NOT_FOUNT);
			}
		} else if (ambitError == ErrorMicrosite.ERROR_AMBIT_PAGINA) {
			errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_PAGINA_TIT, ErrorMicrosite.ERROR_PAGINA_MSG,"","",ErrorMicrosite.ESTADO_NOT_FOUNT);
		} else if (ambitError == ErrorMicrosite.ERROR_AMBIT_DOCUMENT) {
			errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_DOCU_TIT, ErrorMicrosite.ERROR_DOCU_MSG,"","",ErrorMicrosite.ESTADO_NOT_FOUNT);
		} else if (ambitError == ErrorMicrosite.ERROR_AMBIT_ACCES) {
			errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_ACCES_TIT, ErrorMicrosite.ERROR_ACCES_MSG,"","",ErrorMicrosite.ESTADO_FORBIDDEN);
		} else if (ambitError == ErrorMicrosite.ERROR_AMBIT_SESSIO) {
			errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_SESSIO_TIT, ErrorMicrosite.ERROR_SESSIO_MSG,"","",ErrorMicrosite.ESTADO_SESSION);
		} else {
			errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_PAGINA_TIT, ErrorMicrosite.ERROR_PAGINA_MSG,"","",ErrorMicrosite.ESTADO_NOT_FOUNT);
		}

		if (errorMicrosite != null) {
			view.setErrParam(errorMicrosite);
		}
		
		if (errorMicrosite != null) {
			view.setErrEstado(errorMicrosite.getEstado());
		}

		if (microsite != null) {
			microsite.setTipocabecera("1");
		}

		return this.modelForView(this.templateNameFactory.errorGenerico(microsite), view);

	}

}
