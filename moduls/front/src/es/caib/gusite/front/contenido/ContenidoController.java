package es.caib.gusite.front.contenido;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.caib.gusite.front.general.BaseViewController;
import es.caib.gusite.front.general.ExceptionFrontContenido;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.general.bean.PathItem;
import es.caib.gusite.front.microtag.MicrositeParser;
import es.caib.gusite.front.service.ContenidoDataService;
import es.caib.gusite.front.util.Cadenas;
import es.caib.gusite.front.util.Fechas;
import es.caib.gusite.front.view.ContenidoView;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Menu;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micromodel.TraduccionMenu;

/**
 *
 * @author brujula-at4
 *
 */
@Controller
public class ContenidoController extends BaseViewController {

	private static Log log = LogFactory.getLog(ContenidoController.class);

	@Autowired
	private ContenidoDataService contenidoDataService;

	@Autowired
	private MicrositeParser microparser;

	/**
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang:[a-zA-Z][a-zA-Z]}/{uriContenido:.{3,}}")
	public ModelAndView contenidoSmart(@PathVariable("uri") final SiteId URI,

			@PathVariable("lang") final Idioma lang, @PathVariable("uriContenido") final UriContenido uriContenido,
			final RedirectAttributes redir,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = "previsual", required = false, defaultValue = "") final String previsual,
			@RequestParam(value = "tipo", required = false, defaultValue = "") final String tipobeta,
			@RequestParam(value = "redi", required = false, defaultValue = "") final String redi,
			final HttpServletRequest request, final HttpServletResponse response) {

		return this.contenido(URI, lang, uriContenido.nemotecnic, redir, mcont, pcampa, previsual, tipobeta, redi,
				request, response);

	}

	/**
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{uriContenido:.{3,}}")
	public ModelAndView contenidoSmart(@PathVariable("uri") final SiteId URI,

			@PathVariable("uriContenido") final UriContenido uriContenido, final RedirectAttributes redir,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = "previsual", required = false, defaultValue = "") final String previsual,
			@RequestParam(value = "tipo", required = false, defaultValue = "") final String tipobeta,
			@RequestParam(value = "redi", required = false, defaultValue = "") final String redi,
			final HttpServletRequest request, final HttpServletResponse response) {

		return this.contenido(URI, DEFAULT_IDIOMA, uriContenido.nemotecnic, redir, mcont, pcampa, previsual, tipobeta,
				redi, request, response);

	}

	/**
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang:[a-zA-Z][a-zA-Z]}/c/{uriContenido}")
	public ModelAndView contenido(@PathVariable("uri") final SiteId URI, @PathVariable("lang") final Idioma lang,
			@PathVariable("uriContenido") final String uriContenido, final RedirectAttributes redir,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.PVISUALIZAR, required = false, defaultValue = "") final String previsual,
			@RequestParam(value = "tipo", required = false, defaultValue = "") final String tipobeta,
			@RequestParam(value = "redi", required = false, defaultValue = "") final String redi,
			final HttpServletRequest request, final HttpServletResponse response) {

		final ContenidoView view = new ContenidoView();
		try {
			super.configureLayoutView(URI.uri, lang, view, pcampa, uriContenido);

			final Microsite microsite = view.getMicrosite();
			if (microsite == null) {
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + URI);
			}

			Contenido contenido = this.contenidoDataService.getContenido(microsite, uriContenido, lang.getLang());
			if (contenido == null) {
				throw new ExceptionFrontContenido(URI.uri, uriContenido);
			}

			final String urlredireccionada = ((TraduccionContenido) contenido.getTraduccion(lang.getLang())).getUrl();
			if (!StringUtils.isEmpty(urlredireccionada)) {
				final String sMenuCont = contenido.getId().toString();
				final String url = this.urlFactory.legacyToFrontUri(urlredireccionada, lang);

				if (!StringUtils.isEmpty(sMenuCont) && this.urlFactory.isLocalLegacyUri(urlredireccionada)) {
					return new ModelAndView("redirect:" + url + "&mcont=" + sMenuCont + "&uricont=" + uriContenido);
				} else {
					if (!this.urlFactory.isLocalLegacyUri(urlredireccionada)) { // es externa #53
						return new ModelAndView("redirect:" + url);
					} else {
						return new ModelAndView("redirect:" + url + "&uricont=" + uriContenido);
					}
				}

			}

			// obtenemos el menu padre
			final Menu menu = this.contenidoDataService.obtenerMenuBranch(contenido, lang.getLang());
			final boolean previsualizar = !StringUtils.isEmpty(previsual);

			// comprobacion de menu en el microsite
			if (!menu.getMicrosite().getId().equals(microsite.getId())) {
				log.error("[error logico] idsite.longValue=" + microsite.getId() + ", menu.getIdmicrosite.longValue="
						+ menu.getMicrosite().getId().longValue());
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
			}

			// o bien comprobacion de que no esté vacio
			if (contenido.getTraduccion(lang.getLang()) == null) {
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
			}

			// o bien comprobacion de que esté vigente
			if (!previsualizar && !Fechas.vigente(contenido.getFpublicacion(), contenido.getFcaducidad())) {
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
			}

			// o bien comprobacion de que no esté vacio
			if (!previsualizar && !contenido.getVisible().equals("S")) {
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
			}

			this.cargarMollapan(view, contenido, menu, redi);

			// Parsear el contenido del microsite
			contenido = this.reemplazarTags(contenido, lang.getLang(), microsite, request, response);

			view.setContenido(contenido);
			view.setTipoBeta(tipobeta);

			return this.modelForView(this.templateNameFactory.contenido(microsite), view);
		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO, response, URI.uri, lang, request);
		} catch (final ExceptionFrontPagina e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (final ExceptionFrontContenido e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}

	}

	/**
	 * Cuando un contenido de página, en su contenido tiene un a href a un enlace de
	 * página.
	 *
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{uriContenido:.{3,}}/{url}")
	public ModelAndView contenidoSmart(@PathVariable("uri") final SiteId URI, @PathVariable("url") final String url,
			// @PathVariable("uriContenido") UriContenido uriContenido,
			@PathVariable("uriContenido") final String uriContenido, final RedirectAttributes redir,
			@RequestParam(value = Microfront.PIDSITE, required = false, defaultValue = "") final String idsite,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			@RequestParam(value = Microfront.PCONT, required = false, defaultValue = "") final String cont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.PTIPO, required = false, defaultValue = "") final String tipo,
			@RequestParam(value = "previsual", required = false, defaultValue = "") final String previsual,
			@RequestParam(value = "redi", required = false, defaultValue = "") final String redi,
			final HttpServletRequest request, final HttpServletResponse response) {

		final String urlFact = this.urlFactory.legacyToFrontUri(url, new Idioma(Idioma.getIdiomaPorDefecto()));
		final StringBuffer urlAppend = new StringBuffer();
		urlAppend.append("?lang=ca");

		if (idsite != null && !idsite.isEmpty()) {
			urlAppend.append("&");
			urlAppend.append(Microfront.PIDSITE);
			urlAppend.append("=");
			urlAppend.append(idsite);
		}

		if (mcont != null && !mcont.isEmpty()) {
			urlAppend.append("&");
			urlAppend.append(Microfront.MCONT);
			urlAppend.append("=");
			urlAppend.append(mcont);
		}

		if (cont != null && !cont.isEmpty()) {
			urlAppend.append("&");
			urlAppend.append(Microfront.PCONT);
			urlAppend.append("=");
			urlAppend.append(cont);
		}

		if (pcampa != null && !pcampa.isEmpty()) {
			urlAppend.append("&");
			urlAppend.append(Microfront.PCAMPA);
			urlAppend.append("=");
			urlAppend.append(pcampa);
		}

		if (uriContenido != null && !uriContenido.isEmpty()) {
			urlAppend.append("&uricont=");
			urlAppend.append(uriContenido);
		}

		if (tipo != null && !tipo.isEmpty()) {
			urlAppend.append("&");
			urlAppend.append(Microfront.PTIPO);
			urlAppend.append("=");
			urlAppend.append(tipo);
		}

		return new ModelAndView("redirect:/" + urlFact + urlAppend.toString());
	}

	/**
	 * Cuando un contenido de página (con idioma), en su contenido tiene un a href a
	 * un enlace de página.
	 *
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang:[a-zA-Z][a-zA-Z]}/{uriContenido:.{3,}}/{url}")
	public ModelAndView contenidoSmart(@PathVariable("uri") final SiteId URI, @PathVariable("lang") final Idioma lang,
			@PathVariable("url") final String url,
			// @PathVariable("uriContenido") UriContenido uriContenido,
			@PathVariable("uriContenido") final String uriContenido, final RedirectAttributes redir,
			@RequestParam(value = Microfront.PIDSITE, required = false, defaultValue = "") final String idsite,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			@RequestParam(value = Microfront.PCONT, required = false, defaultValue = "") final String cont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.PTIPO, required = false, defaultValue = "") final String tipo,
			@RequestParam(value = "previsual", required = false, defaultValue = "") final String previsual,
			@RequestParam(value = "redi", required = false, defaultValue = "") final String redi,
			final HttpServletRequest request, final HttpServletResponse response) {

		final String urlFact = this.urlFactory.legacyToFrontUri(url, lang);
		final StringBuffer urlAppend = new StringBuffer();
		urlAppend.append("?lang=" + lang.getLang());

		if (idsite != null && !idsite.isEmpty()) {
			urlAppend.append("&");
			urlAppend.append(Microfront.PIDSITE);
			urlAppend.append("=");
			urlAppend.append(idsite);
		}

		if (mcont != null && !mcont.isEmpty()) {
			urlAppend.append("&");
			urlAppend.append(Microfront.MCONT);
			urlAppend.append("=");
			urlAppend.append(mcont);
		}

		if (cont != null && !cont.isEmpty()) {
			urlAppend.append("&");
			urlAppend.append(Microfront.PCONT);
			urlAppend.append("=");
			urlAppend.append(cont);
		}

		if (pcampa != null && !pcampa.isEmpty()) {
			urlAppend.append("&");
			urlAppend.append(Microfront.PCAMPA);
			urlAppend.append("=");
			urlAppend.append(pcampa);
		}

		if (uriContenido != null && !uriContenido.isEmpty()) {
			urlAppend.append("&uricont=");
			urlAppend.append(uriContenido);
		}
		if (tipo != null && !tipo.isEmpty()) {
			urlAppend.append("&");
			urlAppend.append(Microfront.PTIPO);
			urlAppend.append("=");
			urlAppend.append(tipo);
		}

		return new ModelAndView("redirect:/" + urlFact + urlAppend.toString());
	}

	/**
	 * Cuando un contenido de página (con idioma), en su contenido tiene un a href a
	 * un enlace de página. este caso es un caso especial para la agenda, ya que su
	 * redirección no funcionaba bien con el metodo generico.
	 *
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang:[a-zA-Z][a-zA-Z]}/agenda/{url}.do")
	public ModelAndView contenidoSmartAgenda(@PathVariable("uri") final SiteId URI,
			@PathVariable("lang") final Idioma lang, @PathVariable("url") final String url,
			final RedirectAttributes redir,
			@RequestParam(value = Microfront.PIDSITE, required = false, defaultValue = "") final String idsite,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			@RequestParam(value = Microfront.PCONT, required = false, defaultValue = "") final String cont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.PTIPO, required = false, defaultValue = "") final String tipo,
			@RequestParam(value = "previsual", required = false, defaultValue = "") final String previsual,
			@RequestParam(value = "redi", required = false, defaultValue = "") final String redi,
			final HttpServletRequest request, final HttpServletResponse response) {

		final String urlFact = this.urlFactory.legacyToFrontUri(url + ".do", lang);
		final StringBuffer urlAppend = new StringBuffer();
		urlAppend.append("?lang=" + lang.getLang());

		if (idsite != null && !idsite.isEmpty()) {
			urlAppend.append("&");
			urlAppend.append(Microfront.PIDSITE);
			urlAppend.append("=");
			urlAppend.append(idsite);
		}

		if (mcont != null && !mcont.isEmpty()) {
			urlAppend.append("&");
			urlAppend.append(Microfront.MCONT);
			urlAppend.append("=");
			urlAppend.append(mcont);
		}

		if (cont != null && !cont.isEmpty()) {
			urlAppend.append("&");
			urlAppend.append(Microfront.PCONT);
			urlAppend.append("=");
			urlAppend.append(cont);
		}

		if (pcampa != null && !pcampa.isEmpty()) {
			urlAppend.append("&");
			urlAppend.append(Microfront.PCAMPA);
			urlAppend.append("=");
			urlAppend.append(pcampa);
		}

		urlAppend.append("&uricont=");
		urlAppend.append("agenda");

		if (tipo != null && !tipo.isEmpty()) {
			urlAppend.append("&");
			urlAppend.append(Microfront.PTIPO);
			urlAppend.append("=");
			urlAppend.append(tipo);
		}

		return new ModelAndView("redirect:/" + urlFact + urlAppend.toString());
	}

	/**
	 * Método privado para remplazar tags.
	 *
	 * @param contenido
	 * @param idioma
	 * @param microsite
	 * @return contenido contenido con los tags remplazados
	 * @throws Exception
	 */
	private Contenido reemplazarTags(final Contenido contenido, final String idioma, final Microsite microsite,
			final HttpServletRequest request, final HttpServletResponse response) throws ExceptionFrontPagina {
		try {
			if (contenido.getTraduccion(idioma) != null) {
				final TraduccionContenido trad = ((TraduccionContenido) contenido.getTraduccion(idioma));
				if (trad.getTexto() != null) {

					if (trad.getTxbeta() != null) {
						final String txtBeta = this.microparser.doParser(microsite, trad.getTxbeta(), idioma, request,
								response);
						trad.setTxbeta(txtBeta);
					}
					final String txt = this.microparser.doParser(microsite, trad.getTexto(), idioma, request, response);
					((TraduccionContenido) contenido.getTraduccion(idioma)).setTexto(txt);

				}
			}
			return contenido;
		} catch (final Exception e) {

			throw new ExceptionFrontPagina(" [reemplazarTags, idsite=" + microsite.getId() + ", cont="
					+ contenido.getId() + ", idioma=" + idioma + " ] Error=" + e.getMessage() + "\n Stack="
					+ Cadenas.statcktrace2String(e.getStackTrace(), 3));
		}
	}

	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por el
	 * microsite.
	 *
	 * @param microsite
	 * @param menu
	 * @param model
	 * @param lang
	 * @param redi
	 * @return string recorrido en el microsite
	 */
	private void cargarMollapan(final ContenidoView view, final Contenido contenido, final Menu menu,
			final String redi) {

		final List<PathItem> path = super.getBasePath(view);

		final String titulomollapa = (menu.getVisible().equals("S"))
				? ((TraduccionMenu) menu.getTraduccion(view.getLang().getLang())).getNombre()
				: "";
		final String titol = ((TraduccionContenido) contenido.getTraduccion(view.getLang().getLang())).getTitulo();
		final String submolla = ((titulomollapa != null && titulomollapa.length() > 0) && (!redi.equals("yes")))
				? titulomollapa
				: "";
		if (!StringUtils.isEmpty(submolla)) {
			path.add(new PathItem(submolla));
		}
		path.add(new PathItem(titol));

		// Datos para la plantilla
		view.setPathData(path);

	}

	@Override
	public String setServicio() {

		return Microfront.RCONTENIDO;
	}

}
