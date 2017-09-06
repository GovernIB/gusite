package es.caib.gusite.front.home;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.util.StringUtils;

import es.caib.gusite.front.general.BaseViewController;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.front.general.Front;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.general.bean.PathItem;
import es.caib.gusite.front.microtag.MParserHTML;
import es.caib.gusite.front.view.AccesibilidadView;
import es.caib.gusite.front.view.HomeView;
import es.caib.gusite.front.view.MapaView;
import es.caib.gusite.front.view.PageView;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micropersistence.delegate.DelegateException;

/**
 * 
 * @author brujula-at4
 * 
 */
@Controller
public class HomeController extends BaseViewController {

	private static Log log = LogFactory.getLog(HomeController.class);

	/**
	 * TODO: comprobar que el lang está disponible para el site. En caso
	 * contrario, bien dar un 404, bien redireccionar a un idioma existente.
	 * 
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang:[a-zA-Z][a-zA-Z]}")
	public ModelAndView home(@PathVariable("uri") SiteId URI, @PathVariable("lang") Idioma lang,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.URICONT, required = false, defaultValue = "") String uricontenido, HttpServletResponse response) {
		HomeView view = new HomeView();
		try {
			super.configureLayoutView(URI.uri, lang, view, pcampa, uricontenido);
			Microsite microsite = view.getMicrosite();
			if (microsite == null) {
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + URI);				
			}
			
			// microsite = super.loadMicrosite(URI.uri, lang, model, pcampa);

			this.cargarCampanya(view);

			if (microsite.getPlantilla().equals(Microfront.HOME_CONTENIDO) && !StringUtils.isEmpty(microsite.getUrlhome())) {
				// home tipo
				// "Escoger una página de contenido propio del microsite"
				UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(microsite.getUrlhome());
				uri.replaceQueryParam(Microfront.PLANG, lang.getLang());
				uri.replaceQueryParam(Microfront.PCAMPA, "yes");
				String fw = "forward:/" + uri.build().toUriString();
				return modelForView(fw, view);

			} else {
				// Home tipo
				// "Pàgina por defecto proporcionada por la herramienta"
				this.cargarNoticias(view.getMicrosite(), view.getLang(), view);
				this.cargarAgenda(view.getMicrosite(), view.getLang(), view);

			}

			this.cargarMollapan(view, lang);

			return modelForView(this.templateNameFactory.home(microsite), view);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO, response,URI.uri,lang,null);
		} catch (ExceptionFrontPagina e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		}

	}

	/**
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}")
	public ModelAndView home(@PathVariable("uri") SiteId URI,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, 
			@RequestParam(value = Microfront.URICONT, required = false, defaultValue = "") String pcontenido, HttpServletResponse response) {
		// TODO: implementar negociación de idioma y, tal vez, redireccionar en
		// lugar de aceptar la uri.
		return this.home(URI, DEFAULT_IDIOMA, pcampa, pcontenido, response);

	}

	/**
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang:[a-zA-Z][a-zA-Z]}/mapa")
	public ModelAndView mapa(@PathVariable("uri") SiteId URI, @PathVariable("lang") Idioma lang,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletResponse response) {

		MapaView view = new MapaView();
		try {
			super.configureLayoutView(URI.uri, lang, view, pcampa, null);
			this.cargarMollapanMapa(view);
			return modelForView(this.templateNameFactory.mapa(view.getMicrosite()), view);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO, response,URI.uri,lang,null);
		}

	}

	/**
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/mapa")
	public ModelAndView mapa(@PathVariable("uri") SiteId URI,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletResponse response) {
		// TODO: implementar negociación de idioma y, tal vez, redireccionar en
		// lugar de aceptar la uri.
		return this.mapa(URI, DEFAULT_IDIOMA, pcampa, response);

	}

	/**
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang:[a-zA-Z][a-zA-Z]}/accessibility")
	public ModelAndView accessibility(@PathVariable("uri") SiteId URI, @PathVariable("lang") Idioma lang,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletResponse response) {
		AccesibilidadView view = new AccesibilidadView();
		try {
			super.configureLayoutView(URI.uri, lang, view, pcampa, null);
			Microsite microsite = view.getMicrosite();

			this.cargarMollapanAccessibilitat(view);
			return modelForView(this.templateNameFactory.accessibilitat(microsite), view);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO, response,URI.uri,lang,null);
		}

	}

	/**
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/accessibility")
	public ModelAndView accessibility(@PathVariable("uri") SiteId URI,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletResponse response) {
		return this.accessibility(URI, new Idioma(LANG_EN), pcampa, response);

	}

	/**
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/accessibilitat")
	public ModelAndView accessibilitat(@PathVariable("uri") SiteId URI,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletResponse response) {
		return this.accessibility(URI, new Idioma(LANG_CA), pcampa, response);

	}

	/**
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/accesibilidad")
	public ModelAndView accesibilidad(@PathVariable("uri") SiteId URI,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletResponse response) {
		return this.accessibility(URI, DEFAULT_IDIOMA, pcampa, response);

	}

	@Override
	public String setServicio() {

		return Microfront.RMICROSITE;
	}

	/**
	 * Método privado para preparar la Agenda, en el caso que ese microsite
	 * tenga asignado ese componente
	 * 
	 * @param lang
	 * @param model
	 * @param microsite
	 * @throws ExceptionFrontPagina
	 */
	private void cargarAgenda(Microsite microsite, Idioma lang, HomeView view) throws ExceptionFrontPagina {
		if (!this.existeServicio(microsite, lang, Microfront.RAGENDA)) {
			// TODO: error 404
			return;
		}

		// MParserAgenda parseagenda = new MParserAgenda();

		try {
			view.setDatosAgendaCalendario(this.dataService.getDatosCalendario(microsite, lang));
			view.setDatosAgendaListado(this.dataService.getDatosListadoHome(microsite, lang));

		} catch (DelegateException e) {
			throw new ExceptionFrontPagina(e);
		}
	}

	/**
	 * Método privado para preparar la Noticia, en el caso que ese microsite
	 * tenga asignado ese componente
	 * 
	 * @param lang
	 * @param model
	 * @param microsite
	 * @throws ExceptionFrontPagina
	 */
	private void cargarNoticias(Microsite microsite, Idioma lang, HomeView view) throws ExceptionFrontPagina {

		if (!this.existeServicio(microsite, lang, Front.RNOTICIA)) {
			// TODO: error 404
			return;
		}

		try {
			// NOTICIAS
			List<Noticia> listanoticias = this.dataService.getNoticiasHome(microsite, lang);
			view.setDatosNoticiasListado(listanoticias);

		} catch (DelegateException e) {
			throw new ExceptionFrontPagina(e);
		}
	}

	/**
	 * Método privado para preparar la campanya.
	 * 
	 * @param model
	 */
	private void cargarCampanya(HomeView view) {
		MParserHTML parsehtml = new MParserHTML();
		view.setHomeCampanya(parsehtml.getHtmlCampanya(view.getMicrosite(), view.getLang().getLang()).toString());
	}

	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por
	 * el microsite.
	 * 
	 * @param microsite
	 * @param model
	 * @param lang
	 * @return string recorrido en el microsite
	 */
	private void cargarMollapan(HomeView view, Idioma lang) {

		List<PathItem> path = super.getBasePath(view);
		// Datos para la plantilla
		view.setPathData(path);

	}

	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por
	 * el microsite.
	 * 
	 * @param microsite
	 * @param model
	 * @param lang
	 * @return string recorrido en el microsite
	 */
	private void cargarMollapanMapa(PageView view) {

		List<PathItem> path = super.getBasePath(view);
		path.add(new PathItem(this.getMessage("mapa.mapa", view.getLang())));

		// Datos para la plantilla
		view.setPathData(path);
	}

	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por
	 * el microsite.
	 * 
	 * @param request
	 * @param idi
	 *            idioma
	 * @return string recorrido en el microsite
	 */
	private void cargarMollapanAccessibilitat(PageView view) {
		List<PathItem> path = super.getBasePath(view);
		// TODO: esto estaba así en microfront, pero debe ser un error pues pone
		// el mismo path que el mapa
		path.add(new PathItem(this.getMessage("mapa.mapa", view.getLang())));

		// Datos para la plantilla
		view.setPathData(path);
	}

}
