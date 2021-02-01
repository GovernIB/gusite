package es.caib.gusite.front.cercador;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.caib.gusite.front.general.BaseViewController;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.general.bean.PathItem;
import es.caib.gusite.front.view.CercarView;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.SolrDelegate;
import es.caib.gusite.solrutiles.solr.model.IndexEncontrado;
import es.caib.gusite.solrutiles.solr.model.IndexResultados;
import es.caib.solr.api.exception.ExcepcionSolrApi;

/**
 * Gestiona las búsquedas textuales en el microsite
 *
 * @author at4.net
 *
 */
@Controller
public class CercadorController extends BaseViewController {

	private static Log log = LogFactory.getLog(CercadorController.class);

	/**
	 * Post de búsqueda textual en el microsite
	 *
	 * @param uri
	 *            Uri de microsite
	 * @param lang
	 *            Idioma de la petición
	 * @param cerca
	 *            Texto a buscar
	 * @return
	 * @throws ExcepcionSolrApi
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{uri}/{lang:[a-zA-Z][a-zA-Z]}/search/")
	public ModelAndView cercar(@PathVariable("uri") final SiteId URI, @PathVariable("lang") final Idioma lang,
			@RequestParam(value = "cerca", required = true, defaultValue = "") final String cerca,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			final HttpServletRequest req, final HttpServletResponse response) {

		final String cercaHtml = StringEscapeUtils.escapeHtml(cerca);

		final CercarView view = new CercarView();
		Microsite microsite = new Microsite();
		try {
			super.configureLayoutView(URI.uri, lang, view, pcampa);
			microsite = view.getMicrosite();
			if (microsite == null) {
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + URI);
			}

			// metodo buscar(); de Bdcercador.java
			final SolrDelegate indexo = DelegateUtil.getSolrDelegate();
			IndexResultados resultado;

			resultado = indexo.buscar(req.getSession().getId(), "" + microsite.getId().longValue(), lang.getLang(),
					null, cerca, true);

			// hasta aqui metodo buscar();

			if (resultado.getLista() != null) {
				for (final IndexEncontrado res : resultado.getLista()) {
					// Las urls están "hard-coded" en formato legacy
					String url = res.getUrl();
					if (url != null) {
						if (url.startsWith("/sacmicrofront/")) {
							// Quitamos el contextpath hardcoded
							url = url.substring(15);
						} else if (url.startsWith("/sites")) {
							// Quitamos el contextpath hardcoded
							url = url.substring(6);
						}

						if (!res.isDisponible()) {
							res.setUrl("");
						} else {
							res.setUrl(this.urlFactory.legacyToFrontUri(url, lang));
						}
					} else {
						// Caso especial, la url obtenida es null y no debería.
						String error = "";
						error += "ERROR AL BUSCAR, la url del resultado está vacia: CercadorControler.java-->cercar;";
						error += "BUSQUEDA:idsesion:" + req.getSession().getId() + ", Microsite:"
								+ microsite.getId().longValue() + ", idioma:" + lang.getLang() + ", words:'" + cerca
								+ "', booleano:true.";
						error += "VALOR INDEXENCONTRADO:" + res.getStringValores();
						log.error(error);
						res.setUrl("");
						res.setDisponible(false);
					}
				}
			}

			view.setBusqueda(cercaHtml);
			view.setListado(resultado);
			this.cargarMollapan(view);
			return this.modelForView(this.templateNameFactory.cercar(microsite), view);

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO, response, URI.uri, lang, req);
		} catch (final DelegateException e) {
			log.error("Error en la busqueda: " + e);
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_ACCES, response);
		} catch (final ExcepcionSolrApi e) {
			log.error("Error en la busqueda: " + e);
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_SOLR, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Post de búsqueda textual en el microsite en castellano
	 *
	 * @param uri
	 *            Uri de microsite
	 * @param cerca
	 *            Texto a buscar
	 * @return
	 * @throws ExcepcionSolrApi
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{uri}/buscar/")
	public ModelAndView cercarEs(@PathVariable("uri") final SiteId URI,
			@RequestParam(value = "cerca", required = true, defaultValue = "") final String cerca,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			final HttpServletRequest req, final HttpServletResponse response) {

		return this.cercar(URI, new Idioma(LANG_ES), cerca, mcont, pcampa, req, response);
	}

	/**
	 * Post de búsqueda textual en el microsite en catalán
	 *
	 * @param uri
	 *            Uri de microsite
	 * @param cerca
	 *            Texto a buscar
	 * @return
	 * @throws ExcepcionSolrApi
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{uri}/cercar/")
	public ModelAndView cercarCa(@PathVariable("uri") final SiteId URI,
			@RequestParam(value = "cerca", required = true, defaultValue = "") final String cerca,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			final HttpServletRequest req, final HttpServletResponse response) {

		return this.cercar(URI, new Idioma(LANG_CA), cerca, mcont, pcampa, req, response);
	}

	/**
	 * Post de búsqueda textual en el microsite en inglés
	 *
	 * @param uri
	 *            Uri de microsite
	 * @param cerca
	 *            Texto a buscar
	 * @return
	 * @throws ExcepcionSolrApi
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{uri}/search/")
	public ModelAndView cercarEn(@PathVariable("uri") final SiteId URI,
			@RequestParam(value = "cerca", required = true, defaultValue = "") final String cerca,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			final HttpServletRequest req, final HttpServletResponse response) {

		return this.cercar(URI, new Idioma(LANG_EN), cerca, mcont, pcampa, req, response);
	}

	/**
	 * Get de búsqueda textual en el microsite
	 *
	 * @param uri
	 *            Uri de microsite
	 * @param lang
	 *            Idioma de la petición
	 * @param cerca
	 *            Texto a buscar
	 * @return
	 * @throws ExcepcionSolrApi
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/{lang:[a-zA-Z][a-zA-Z]}/search/")
	public ModelAndView cercarGet(@PathVariable("uri") final SiteId URI, @PathVariable("lang") final Idioma lang,
			@RequestParam(value = "cerca", required = true, defaultValue = "") final String cerca,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			final HttpServletRequest req, final HttpServletResponse response) {

		return this.cercar(URI, lang, cerca, mcont, pcampa, req, response);

	}

	/**
	 * Get de búsqueda textual en el microsite en castellano
	 *
	 * @param uri
	 *            Uri de microsite
	 * @param cerca
	 *            Texto a buscar
	 * @return
	 * @throws ExcepcionSolrApi
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/buscar/")
	public ModelAndView cercarEsGet(@PathVariable("uri") final SiteId URI,
			@RequestParam(value = "cerca", required = true, defaultValue = "") final String cerca,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			final HttpServletRequest req, final HttpServletResponse response) {

		return this.cercarGet(URI, new Idioma(LANG_ES), cerca, mcont, pcampa, req, response);
	}

	/**
	 * Get de búsqueda textual en el microsite en catalán
	 *
	 * @param uri
	 *            Uri de microsite
	 * @param cerca
	 *            Texto a buscar
	 * @return
	 * @throws ExcepcionSolrApi
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/cercar/")
	public ModelAndView cercarCaGet(@PathVariable("uri") final SiteId URI,
			@RequestParam(value = "cerca", required = true, defaultValue = "") final String cerca,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			final HttpServletRequest req, final HttpServletResponse response) {

		return this.cercarGet(URI, new Idioma(LANG_CA), cerca, mcont, pcampa, req, response);
	}

	/**
	 * Get de búsqueda textual en el microsite en inglés
	 *
	 * @param uri
	 *            Uri de microsite
	 * @param cerca
	 *            Texto a buscar
	 * @return
	 * @throws ExcepcionSolrApi
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/search/")
	public ModelAndView cercarEnGet(@PathVariable("uri") final SiteId URI,
			@RequestParam(value = "cerca", required = true, defaultValue = "") final String cerca,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			final HttpServletRequest req, final HttpServletResponse response) {

		return this.cercarGet(URI, new Idioma(LANG_EN), cerca, mcont, pcampa, req, response);
	}

	@Override
	public String setServicio() {
		return null;
	}

	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por el
	 * microsite.
	 *
	 * @param microsite
	 * @param model
	 * @param lang
	 */

	private void cargarMollapan(final CercarView view) {

		final List<PathItem> path = super.getBasePath(view);

		final String titulo = this.getMessage("cercar.resultados", view.getLang());

		path.add(new PathItem(titulo, this.urlFactory.cercar(view.getMicrosite(), view.getLang())));

		// Datos para la plantilla
		view.setPathData(path);

	}

}
