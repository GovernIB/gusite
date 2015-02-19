package es.caib.gusite.front.home;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.util.StringUtils;

import es.caib.gusite.front.general.BaseController;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.front.general.Front;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.general.bean.PathItem;
import es.caib.gusite.front.microtag.MParserHTML;
import es.caib.gusite.micromodel.Agenda;
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
public class HomeController extends BaseController {

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
	@RequestMapping("{uri}/{lang}")
	public String home(
			@PathVariable("uri") SiteId URI,
			@PathVariable("lang") Idioma lang,
			Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa) {
		Microsite microsite = null;
		try {
			microsite = super.loadMicrosite(URI.uri, lang, model, pcampa);

			this.cargarCampanya(microsite, model, lang);

			if (microsite.getPlantilla().equals(Microfront.HOME_CONTENIDO)
					&& !StringUtils.isEmpty(microsite.getUrlhome())) {
				// home tipo
				// "Escoger una página de contenido propio del microsite"
				UriComponentsBuilder uri = UriComponentsBuilder
						.fromUriString(microsite.getUrlhome());
				uri.replaceQueryParam(Microfront.PLANG, lang.getLang());
				uri.replaceQueryParam(Microfront.PCAMPA, "yes");
				String fw = "forward:/" + uri.build().toUriString();
				return fw;

			} else {
				// Home tipo
				// "Pàgina por defecto proporcionada por la herramienta"
				this.cargarNoticias(microsite, model, lang);
				this.cargarAgenda(microsite, model, lang);

			}

			this.cargarMollapan(microsite, model, lang);

			return this.templateNameFactory.home(microsite);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_MICRO);
		} catch (ExceptionFrontPagina e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_PAGINA);
		}

	}

	/**
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}")
	public String home(
			@PathVariable("uri") SiteId URI,
			Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa) {
		// TODO: implementar negociación de idioma y, tal vez, redireccionar en
		// lugar de aceptar la uri.
		return this.home(URI, DEFAULT_IDIOMA, model, pcampa);

	}

	/**
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang}/mapa")
	public String mapa(
			@PathVariable("uri") SiteId URI,
			@PathVariable("lang") Idioma lang,
			Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa) {
		Microsite microsite = null;
		try {
			microsite = super.loadMicrosite(URI.uri, lang, model, pcampa);

			this.cargarMollapanMapa(microsite, model, lang);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_MICRO);
		}

		return this.templateNameFactory.mapa(microsite);

	}

	/**
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/mapa")
	public String mapa(
			@PathVariable("uri") SiteId URI,
			Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa) {
		// TODO: implementar negociación de idioma y, tal vez, redireccionar en
		// lugar de aceptar la uri.
		return this.mapa(URI, DEFAULT_IDIOMA, model, pcampa);

	}

	/**
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang}/accessibility")
	public String accessibility(
			@PathVariable("uri") SiteId URI,
			@PathVariable("lang") Idioma lang,
			Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa) {
		Microsite microsite = null;
		try {
			microsite = super.loadMicrosite(URI.uri, lang, model, pcampa);

			this.cargarMollapanAccessibilitat(microsite, model, lang);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_MICRO);
		}
		return this.templateNameFactory.accessibilitat(microsite);

	}

	/**
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/accessibility")
	public String accessibility(
			@PathVariable("uri") SiteId URI,
			Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa) {
		return this.accessibility(URI, new Idioma(LANG_EN), model, pcampa);

	}

	/**
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/accessibilitat")
	public String accessibilitat(
			@PathVariable("uri") SiteId URI,
			Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa) {
		return this.accessibility(URI, new Idioma(LANG_CA), model, pcampa);

	}

	/**
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/accesibilidad")
	public String accesibilidad(
			@PathVariable("uri") SiteId URI,
			Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa) {
		return this.accessibility(URI, new Idioma(LANG_ES), model, pcampa);

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
	private void cargarAgenda(Microsite microsite, Model model, Idioma lang)
			throws ExceptionFrontPagina {
		if (!this.existeServicio(microsite, lang, Microfront.RAGENDA)) {
			// TODO: error 404
			return;
		}

		// MParserAgenda parseagenda = new MParserAgenda();

		try {
			model.addAttribute("MVS_datos_agenda_calendario",
					this.dataService.getDatosCalendario(microsite, lang));

			List<Agenda> listaagenda2 = this.dataService.getDatosListadoHome(
					microsite, lang);
			model.addAttribute("MVS_home_datos_agenda_listado", listaagenda2);

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
	private void cargarNoticias(Microsite microsite, Model model, Idioma lang)
			throws ExceptionFrontPagina {

		if (!this.existeServicio(microsite, lang, Front.RNOTICIA)) {
			// TODO: error 404
			return;
		}

		try {
			// NOTICIAS
			List<Noticia> listanoticias = this.dataService.getNoticiasHome(
					microsite, lang);
			model.addAttribute("MVS_home_datos_noticias_listado", listanoticias);

		} catch (DelegateException e) {
			throw new ExceptionFrontPagina(e);
		}
	}

	/**
	 * Método privado para preparar la campanya.
	 * 
	 * @param model
	 */
	private void cargarCampanya(Microsite microsite, Model model, Idioma lang) {
		MParserHTML parsehtml = new MParserHTML();
		model.addAttribute("MVS_home_campanya",
				parsehtml.getHtmlCampanya(microsite, lang.getLang()).toString());
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
	private void cargarMollapan(Microsite microsite, Model model, Idioma lang) {

		List<PathItem> path = super.getBasePath(microsite, model, lang);

		// Datos para la plantilla
		model.addAttribute("MVS2_pathdata", path);

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
	private void cargarMollapanMapa(Microsite microsite, Model model,
			Idioma lang) {

		List<PathItem> path = super.getBasePath(microsite, model, lang);
		path.add(new PathItem(this.getMessage("mapa.mapa", lang)));

		// Datos para la plantilla
		model.addAttribute("MVS2_pathdata", path);
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
	private void cargarMollapanAccessibilitat(Microsite microsite, Model model,
			Idioma lang) {
		List<PathItem> path = super.getBasePath(microsite, model, lang);
		path.add(new PathItem(this.getMessage("mapa.mapa", lang)));

		// Datos para la plantilla
		model.addAttribute("MVS2_pathdata", path);
	}

}
