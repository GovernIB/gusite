package es.caib.gusite.front.home;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import es.caib.gusite.front.general.ExceptionFrontContenido;
import es.caib.gusite.front.general.ExceptionFrontEncuesta;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.ExceptionFrontNoticia;
import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.front.general.FrontController;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.service.ContactosDataService;
import es.caib.gusite.front.service.ContenidoDataService;
import es.caib.gusite.front.service.EncuestasDataService;
import es.caib.gusite.front.service.NoticiasDataService;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.Tipo;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;

/**
 * Convierte peticiones antiguas a nuevas.
 *
 * @author at4.net
 */
@Controller
public class LegacyController extends FrontController {

	private static Log log = LogFactory.getLog(LegacyController.class);

	@Autowired
	protected ContenidoDataService contenidoDataService;

	/**
	 * Hay mucho contenido referenciado relativo
	 * (src="archivopub.do?ctrl=MCRST449ZI63868&amp;id=63868") Así que captamos toda
	 * la url **archivopub.do Con el tiempo, podemos estudiar los logs para
	 * sustituir estos requests.
	 *
	 */
	@RequestMapping(value = "/{mkey}/{lang:[a-zA-Z][a-zA-Z]}/{tipo}/{tipoId}/contenido.do", params = Microfront.PIDSITE)
	public String contenidoRelativo(@PathVariable("mkey") final SiteId prevSiteId,
			@PathVariable(value = Microfront.PLANG) final Idioma lang, @PathVariable("tipo") final char tipo,
			@PathVariable("tipoId") final String tipoId, @RequestParam(Microfront.PIDSITE) final Long idSite,
			@RequestParam(Microfront.PCONT) final Long idContenido, final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.PTIPO, required = false, defaultValue = "") final String tipobeta,
			@RequestParam(value = Microfront.PVISUALIZAR, required = false, defaultValue = "") final String previsual,
			final HttpServletResponse response) {

		return this.contenido(idSite, lang, idContenido, model, pcampa, null, tipobeta, previsual, response);
	}

	/**
	 * Urls de contenido antiguo.
	 * contenido.do?idsite=449&cont=15812&lang=ca&campa=yes z
	 */
	@RequestMapping(value = "contenido.do", params = { Microfront.PIDSITE })
	public String contenido(@RequestParam(Microfront.PIDSITE) final Long idSite,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			@RequestParam(Microfront.PCONT) final Long idContenido, final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			@RequestParam(value = Microfront.PTIPO, required = false, defaultValue = "") final String tipobeta,
			@RequestParam(value = Microfront.PVISUALIZAR, required = false, defaultValue = "") final String previsual,
			final HttpServletResponse response) {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);
			}

			final Contenido contenido = this.contenidoDataService.getContenido(microsite, idContenido, lang.getLang());
			// #81 Si el contenido ha sido borrado, esto se encarga de redirigirlo.
			if (contenido == null) {
				throw new ExceptionFrontContenido(microsite.getUri(), idContenido);
			}

			return "redirect:" + addGenericParams(this.urlFactory.contenido(microsite, lang, contenido), pcampa, mcont,
					tipobeta, previsual);

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final ExceptionFrontPagina e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (final ExceptionFrontContenido e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}

	}

	private String addGenericParams(final String baseUri, final String pcampa, final String mcont) {
		/*
		 * //se comenta por problemas en #106 if(!StringUtils.isEmpty(pcampa) ||
		 * !StringUtils.isEmpty(mcont) ){ baseUri =
		 * this.urlFactory.borraUltimaBarra(baseUri); }
		 */
		final UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(baseUri);
		if (!StringUtils.isEmpty(pcampa)) {
			uri.replaceQueryParam(Microfront.PCAMPA, pcampa);
		}
		if (!StringUtils.isEmpty(mcont)) {
			uri.replaceQueryParam(Microfront.MCONT, mcont);
		}
		return uri.build().toUriString();
	}

	private String addGenericParams(final String baseUri, final String pcampa, final String mcont, final String tipo,
			final String previsual) {
		/*
		 * //se comenta por problemas en #106 if(!StringUtils.isEmpty(pcampa) ||
		 * !StringUtils.isEmpty(mcont) || !StringUtils.isEmpty(tipo) ||
		 * !StringUtils.isEmpty(previsual) ){ baseUri =
		 * this.urlFactory.borraUltimaBarra(baseUri); }
		 */
		final UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(baseUri);

		if (!StringUtils.isEmpty(pcampa)) {
			uri.replaceQueryParam(Microfront.PCAMPA, pcampa);
		}
		if (!StringUtils.isEmpty(mcont)) {
			uri.replaceQueryParam(Microfront.MCONT, mcont);
		}
		if (!StringUtils.isEmpty(tipo)) {
			uri.replaceQueryParam(Microfront.PTIPO, tipo);
		}
		if (!StringUtils.isEmpty(previsual)) {
			uri.replaceQueryParam(Microfront.PVISUALIZAR, previsual);
		}
		return uri.build().toUriString();
	}

	/**
	 * Hay mucho contenido referenciado relativo
	 * (src="archivopub.do?ctrl=MCRST449ZI63868&amp;id=63868") Así que captamos toda
	 * la url **archivopub.do Con el tiempo, podemos estudiar los logs para
	 * sustituir estos requests.
	 *
	 */
	@RequestMapping(value = "/{mkey}/{lang:[a-zA-Z][a-zA-Z]}/{tipo}/{tipoId}/contenido.do", params = "mkey")
	public String contenidoRelativo(@PathVariable("mkey") final SiteId prevSiteId,
			@PathVariable(value = Microfront.PLANG) final Idioma lang, @PathVariable("tipo") final char tipo,
			@PathVariable("tipoId") final String tipoId, @RequestParam("mkey") final String mkey,
			@RequestParam(Microfront.PCONT) final Long idContenido, final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.PTIPO, required = false, defaultValue = "") final String tipobeta,
			@RequestParam(value = Microfront.PVISUALIZAR, required = false, defaultValue = "") final String previsual,
			final HttpServletResponse response, final HttpServletRequest req) {

		return this.contenido(mkey, lang, idContenido, model, pcampa, null, tipobeta, previsual, response, req);
	}

	/**
	 * Urls de contenido antiguo.
	 * contenido.do?idsite=449&cont=15812&lang=ca&campa=yes z
	 */
	@RequestMapping(value = "contenido.do", params = "mkey")
	public String contenido(@RequestParam("mkey") final String mkey,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			@RequestParam(Microfront.PCONT) final Long idContenido, final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			@RequestParam(value = Microfront.PTIPO, required = false, defaultValue = "") final String tipobeta,
			@RequestParam(value = Microfront.PVISUALIZAR, required = false, defaultValue = "") final String previsual,
			final HttpServletResponse response, final HttpServletRequest req) {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);
			}
			return this.contenido(microsite.getId(), lang, idContenido, model, pcampa, mcont, tipobeta, previsual,
					response);
		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	@Autowired
	protected ContactosDataService contactosDataService;

	/**
	 * Urls de contenido antiguo. contacto.do?mkey=M94&lang=ca z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "contacto.do", params = { Microfront.PIDSITE })
	public String contacto(@RequestParam(Microfront.PIDSITE) final Long idSite,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			@RequestParam(Microfront.PCONT) final Long idContacto, final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);
			}
			final Contacto contacto = this.contactosDataService.getFormulario(microsite, lang, idContacto);

			return "redirect:" + addGenericParams(this.urlFactory.contacto(microsite, lang, contacto), pcampa, mcont);

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. contacto.do?mkey=M94&lang=es&cont=213 z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "contacto.do", params = "mkey")
	public String contacto(@RequestParam("mkey") final String mkey,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			@RequestParam(Microfront.PCONT) final Long idContacto, final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);
			}
			return this.contacto(microsite.getId(), lang, idContacto, model, pcampa, mcont, response);
		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. contactos.do?mkey=M94&lang=ca z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "contactos.do", params = { Microfront.PIDSITE })
	public String contactos(@RequestParam(Microfront.PIDSITE) final Long idSite,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
			}
			return "redirect:" + this.addGenericParams(this.urlFactory.listarContactos(microsite, lang), pcampa, mcont);

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. contactos.do?mkey=M94&lang=es&cont=213 z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "contactos.do", params = "mkey")
	public String contactos(@RequestParam("mkey") final String mkey,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);
			}
			return "redirect:" + this.addGenericParams(this.urlFactory.listarContactos(microsite, lang), pcampa, mcont);
		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	@Autowired
	protected NoticiasDataService noticiasDataService;

	/**
	 * Urls de contenido antiguo.
	 * noticia.do?mkey=M11050411433211050557&lang=ca&cont=31549 z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "noticia.do", params = { Microfront.PIDSITE })
	public String noticia(@RequestParam(Microfront.PIDSITE) final Long idSite,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			@RequestParam(Microfront.PCONT) final Long idNoticia, final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);
			}

			final Noticia noticia = this.noticiasDataService.loadNoticia(idNoticia, lang);
			if (noticia == null) {
				if (idNoticia == null) {
					throw new ExceptionFrontNoticia();
				}
				throw new ExceptionFrontNoticia(idSite.toString(), idNoticia.toString());
			}

			return "redirect:"
					+ this.addGenericParams(this.urlFactory.noticia(microsite, lang, noticia), pcampa, mcont);

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final ExceptionFrontPagina e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (final ExceptionFrontNoticia e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}

	}

	/**
	 * Urls de contenido antiguo.
	 * noticia.do?mkey=M11050411433211050557&lang=ca&cont=31549 z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "noticia.do", params = "mkey")
	public String noticia(@RequestParam("mkey") final String mkey,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			@RequestParam(Microfront.PCONT) final Long idNoticia, final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);
			}

			return this.noticia(microsite.getId(), lang, idNoticia, model, pcampa, mcont, response);
		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo.
	 * noticias.do?mkey=M10101513200930602688&lang=ca&tipo=3306 z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "noticias.do", params = { Microfront.PIDSITE })
	public String noticias(@RequestParam(Microfront.PIDSITE) final Long idSite,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			@RequestParam(Microfront.PTIPO) final Long idTipo, final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;

		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);
			}

			final Tipo tipo = this.noticiasDataService.loadTipo(idTipo, lang);

			return "redirect:"
					+ addGenericParams(this.urlFactory.listarElementos(microsite, lang, tipo), pcampa, mcont);

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final ExceptionFrontPagina e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo.
	 * noticias.do?mkey=M10101513200930602688&lang=ca&tipo=3306 z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "noticias.do", params = "mkey")
	public String noticias(@RequestParam("mkey") final String mkey,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			@RequestParam(Microfront.PTIPO) final Long idTipo, final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);
			}
			return this.noticias(microsite.getId(), lang, idTipo, model, pcampa, mcont, response);
		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo.
	 * noticias.do?mkey=M10101513200930602688&lang=ca&tipo=3306&tanyo=2010 z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "noticias.do", params = { "tanyo", Microfront.PIDSITE })
	public String noticiasAnyo(@RequestParam(Microfront.PIDSITE) final Long idSite,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			@RequestParam(Microfront.PTIPO) final Long idTipo, @RequestParam("tanyo") final String anyo,
			final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;

		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);
			}

			final Tipo tipo = this.noticiasDataService.loadTipo(idTipo, lang);

			return "redirect:"
					+ this.addGenericParams(this.urlFactory.listarAnual(microsite, lang, tipo, anyo), pcampa, mcont);

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final ExceptionFrontPagina e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo.
	 * noticias.do?mkey=M10101513200930602688&lang=ca&cont=3306&tanyo=2010 z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "noticias.do", params = { "tanyo", "mkey" })
	public String noticiasAnyo(@RequestParam("mkey") final String mkey,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			@RequestParam(Microfront.PTIPO) final Long idTipo, @RequestParam("tanyo") final String anyo,
			final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);
			}
			return this.noticiasAnyo(microsite.getId(), lang, idTipo, anyo, model, pcampa, mcont, response);
		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. qssi.do?[sitelang]&cont=formQssi z
	 *
	 * @throws DelegateException
	 * @throws ExceptionFrontPagina
	 */
	@RequestMapping(value = "qssi.do", params = { Microfront.PIDSITE })
	public String qssi(@RequestParam(Microfront.PIDSITE) final Long idSite,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			@RequestParam(Microfront.PCONT) final long idQssi, final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException, ExceptionFrontPagina {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);
			}

			return "redirect:" + this.addGenericParams(this.urlFactory.qssi(microsite, lang, idQssi), pcampa, mcont);

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. qssi.do?[sitelang]&cont=formQssi z
	 *
	 * @throws DelegateException
	 * @throws ExceptionFrontPagina
	 */
	@RequestMapping(value = "qssi.do", params = "mkey")
	public String qssi(@RequestParam("mkey") final String mkey,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			@RequestParam(Microfront.PCONT) final long idQssi, final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException, ExceptionFrontPagina {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);
			}
			return this.qssi(microsite.getId(), lang, idQssi, model, pcampa, mcont, response);
		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. elementodocumento.do?mkey=M60&cont=16707&lang=CA z
	 *
	 * @throws DelegateException
	 * @throws ExceptionFrontPagina
	 */
	@RequestMapping(value = "elementodocumento.do", params = { Microfront.PIDSITE })
	public String elementodocumento(@RequestParam(Microfront.PIDSITE) final Long idSite,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			@RequestParam(Microfront.PCONT) final String idDocumento, final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			final HttpServletResponse response) throws DelegateException, ExceptionFrontPagina {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);
			}

			return "redirect:" + this.urlFactory.noticiaDescarga(microsite, lang, idDocumento);

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. elementodocumento.do?mkey=M60&cont=16707&lang=CA z
	 *
	 * @throws DelegateException
	 * @throws ExceptionFrontPagina
	 */
	@RequestMapping(value = "elementodocumento.do", params = "mkey")
	public String elementodocumento(@RequestParam("mkey") final String mkey,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			@RequestParam(Microfront.PCONT) final String idDocumento, final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			final HttpServletResponse response) throws DelegateException, ExceptionFrontPagina {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);
			}
			return this.elementodocumento(microsite.getId(), lang, idDocumento, model, pcampa, response);
		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. agenda.do?mkey=M7&lang=ca&cont=20131122 z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "agenda.do", params = { Microfront.PIDSITE })
	public String agenda(@RequestParam(Microfront.PIDSITE) final Long idSite,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			@RequestParam(Microfront.PCONT) final String idAgenda, final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);
			}

			return "redirect:" + this.addGenericParams(
					this.urlFactory.listarAgendaFechaFormateada(microsite, lang, idAgenda), pcampa, mcont);

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. agenda.do?mkey=M7&lang=ca&cont=20131122 z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "agenda.do", params = "mkey")
	public String agenda(@RequestParam("mkey") final String mkey,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			@RequestParam(Microfront.PCONT) final String idAgenda, final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);
			}

			return this.agenda(microsite.getId(), lang, idAgenda, model, pcampa, mcont, response);
		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. agendas.do?mkey=M09112511445915080171&lang=ca z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "agendas.do", params = { Microfront.PIDSITE })
	public String agendas(@RequestParam(Microfront.PIDSITE) final Long idSite,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);
			}
			return "redirect:" + this.addGenericParams(this.urlFactory.listarAgenda(microsite, lang), pcampa, mcont);

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. agendas.do?mkey=M09112511445915080171&lang=ca
	 *
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "agendas.do", params = "mkey")
	public String agendas(@RequestParam("mkey") final String mkey,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);
			}
			return this.agendas(microsite.getId(), lang, model, pcampa, mcont, response);
		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	@Autowired
	protected EncuestasDataService encuestasDataService;

	/**
	 * ***************************************** Urls de contenido antiguo.
	 * encuestas.do?idsite=5274
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "encuestas.do", params = { Microfront.PIDSITE })
	public String encuestas(@RequestParam(Microfront.PIDSITE) final Long idSite,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);
			}
			return "redirect:" + this.addGenericParams(this.urlFactory.home(microsite, lang), pcampa, mcont);

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	// ********************************************************

	/**
	 * Urls de contenido antiguo.
	 * encuesta.do?mkey=M11010412302126072344&lang=ca&cont=769 z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "encuesta.do", params = { Microfront.PIDSITE })
	public String encuesta(@RequestParam(Microfront.PIDSITE) final Long idSite,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			@RequestParam(Microfront.PCONT) final Long idEncuesta, final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);
			}

			final Encuesta encuesta = this.encuestasDataService.getEncuesta(microsite, lang, idEncuesta);
			if (microsite == null) {
				if (idEncuesta == null) {
					throw new ExceptionFrontEncuesta();
				}
				throw new ExceptionFrontEncuesta(idSite.toString(), idEncuesta.toString());
			}

			return "redirect:"
					+ this.addGenericParams(this.urlFactory.encuesta(microsite, lang, encuesta), pcampa, mcont);

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final ExceptionFrontPagina e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (final ExceptionFrontEncuesta e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo.
	 * encuesta.do?mkey=M11010412302126072344&lang=ca&cont=769 z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "encuesta.do", params = "mkey")
	public String encuesta(@RequestParam("mkey") final String mkey,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			@RequestParam(Microfront.PCONT) final Long idEncuesta, final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);
			}

			return this.encuesta(microsite.getId(), lang, idEncuesta, model, pcampa, mcont, response);
		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. index.do?mkey=M77&lang=es
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "index.do", params = { Microfront.PIDSITE })
	public String index(@RequestParam(Microfront.PIDSITE) final Long idSite,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);
			}

			return "redirect:" + this.addGenericParams(this.urlFactory.home(microsite, lang), pcampa, mcont);

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. index.do?mkey=M77&lang=es z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "index.do", params = "mkey")
	public String index(@RequestParam("mkey") final String mkey,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);
			}

			return this.index(microsite.getId(), lang, model, pcampa, mcont, response);
		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. home.do?mkey=M77&lang=es z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "home.do", params = { Microfront.PIDSITE })
	public String home(@RequestParam(Microfront.PIDSITE) final Long idSite,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);
			}

			return this.index(microsite.getId(), lang, model, pcampa, mcont, response);

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. home.do?mkey=M77&lang=es z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "home.do", params = "mkey")
	public String home(@RequestParam("mkey") final String mkey,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);
			}

			return this.index(microsite.getId(), lang, model, pcampa, mcont, response);
		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. mapa.do?idsite=1244&lang=CA z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "mapa.do", params = { Microfront.PIDSITE })
	public String mapa(@RequestParam(Microfront.PIDSITE) final Long idSite,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);
			}
			return "redirect:" + this.addGenericParams(this.urlFactory.mapa(microsite, lang), pcampa, mcont);

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. mapa.do?idsite=1244&lang=CA z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "mapa.do", params = "mkey")
	public String mapa(@RequestParam("mkey") final String mkey,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);
			}

			return this.mapa(microsite.getId(), lang, model, pcampa, mcont, response);
		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. menupreview.do?mkey=M12050713445419070774
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "menupreview.do", params = "mkey")
	public String menupreview(@RequestParam("mkey") final String mkey, final Model model,
			final HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, DEFAULT_IDIOMA);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);
			}

			return "redirect:" + this.urlFactory.menuPreview(microsite, DEFAULT_IDIOMA);
		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. accessibilitat.do?idsite=1244&lang=CA z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "accessibilitat.do", params = { Microfront.PIDSITE })
	public String accessibilitat(@RequestParam(Microfront.PIDSITE) final Long idSite,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);
			}
			return "redirect:" + this.addGenericParams(this.urlFactory.accessibilitat(microsite, lang), pcampa, mcont);

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. accessibilitat.do?idsite=1244&lang=CA z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "accessibilitat.do", params = "mkey")
	public String accessibilitat(@RequestParam("mkey") final String mkey,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);
			}

			return this.accessibilitat(microsite.getId(), lang, model, pcampa, mcont, response);
		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. faqs.do?idsite=1244&lang=CA z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "faqs.do", params = { Microfront.PIDSITE })
	public String faqs(@RequestParam(Microfront.PIDSITE) final Long idSite,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);
			}

			return "redirect:" + this.addGenericParams(this.urlFactory.listarFaqs(microsite, lang), pcampa, mcont);

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Urls de contenido antiguo. faqs.do?idsite=1244&lang=CA z
	 *
	 * @throws DelegateException
	 */
	@RequestMapping(value = "faqs.do", params = "mkey")
	public String faqs(@RequestParam("mkey") final String mkey,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			final Model model,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") final String pcampa,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") final String mcont,
			final HttpServletResponse response) throws DelegateException {
		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByKey(mkey, lang);
			if (microsite == null) {
				if (mkey == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + mkey);
			}

			return this.faqs(microsite.getId(), lang, model, pcampa, mcont, response);
		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

	/**
	 * Hay mucho contenido referenciado relativo
	 * (src="archivopub.do?ctrl=MCRST449ZI63868&amp;id=63868") Así que captamos toda
	 * la url **archivopub.do Con el tiempo, podemos estudiar los logs para
	 * sustituir estos requests.
	 *
	 */
	@RequestMapping(value = "/{mkey}/{lang:[a-zA-Z][a-zA-Z]}/{tipo}/{tipoId}/archivopub.do", params = {
			Microfront.PCTRL, "id" })
	public String archivopubRelativo(@PathVariable("mkey") final SiteId prevSiteId,
			@PathVariable(value = Microfront.PLANG) final Idioma lang, @PathVariable("tipo") final char tipo,
			@PathVariable("tipoId") final String tipoId, @RequestParam(value = Microfront.PCTRL) final String ctrl,
			@RequestParam(value = "id") final Long id, final Model model, final HttpServletResponse response) {

		return this.archivopub(ctrl, id, model, response);

	}

	/**
	 * Urls de contenido antiguo. archivopub.do?ctrl=MCRST449ZI63868&amp;id=63868
	 *
	 */
	@RequestMapping(value = "archivopub.do", params = { Microfront.PCTRL, "id" })
	public String archivopub(@RequestParam(Microfront.PCTRL) final String ctrl, @RequestParam("id") final Long id,
			final Model model, final HttpServletResponse response) {

		Microsite microsite = null;
		try {

			final Long idSite = this.idSiteFromCtrl(ctrl);

			microsite = this.dataService.getMicrosite(idSite, DEFAULT_IDIOMA);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);
			}

			return "redirect:" + this.urlFactory.archivopubById(microsite, id);

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}

	}

	private Long idSiteFromCtrl(final String ctrl) {

		final String what = ctrl.substring(0, 5);
		final Long whatKey = new Long(ctrl.substring(5, ctrl.indexOf(Microfront.separatordocs)));

		// TODO: extraer Delegates a DataService
		try {

			if (what.equals(Microfront.RAGENDA)) {
				final AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
				Agenda agenda;
				agenda = agendadel.obtenerAgenda(whatKey);
				return agenda.getIdmicrosite();
			}

			if (what.equals(Microfront.RCONTENIDO)) {
				final ContenidoDelegate contenidodel = DelegateUtil.getContenidoDelegate();
				final Contenido contenido = contenidodel.obtenerContenido(whatKey);
				return contenido.getMenu().getMicrosite().getId();
			}

			if (what.equals(Microfront.RNOTICIA)) {
				final NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
				final Noticia noticia = noticiadel.obtenerNoticia(whatKey);
				return noticia.getIdmicrosite();
			}

			if (what.equals(Microfront.RMICROSITE)) {
				return whatKey;
			}
		} catch (final DelegateException e) {
			log.error(e);
		}

		// No debería llegar aquí
		return new Long(0);
	}

	/**
	 * Urls de contenido antiguo. archivopub.do?ctrl=MCRST449ZI63868&amp;id=63868
	 *
	 */
	@RequestMapping(value = "archivopub.do", params = Microfront.PNAME)
	public String archivopubPorNombre(@RequestParam(Microfront.PNAME) final String nombre,
			@RequestParam(value = Microfront.PIDSITE) final Long idSite, final Model model,
			final HttpServletResponse response) {

		Microsite microsite = null;
		try {

			microsite = this.dataService.getMicrosite(idSite, DEFAULT_IDIOMA);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);
			}

			return "redirect:" + this.urlFactory.archivopubByNombre(microsite, nombre);

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}

	}

	/**
	 * Hay mucho contenido referenciado relativo
	 * (src="archivopub.do?ctrl=MCRST449ZI63868&amp;id=63868") Así que captamos toda
	 * la url **archivopub.do Con el tiempo, podemos estudiar los logs para
	 * sustituir estos requests.
	 *
	 */
	@RequestMapping(value = "/{mkey}/{lang:[a-zA-Z][a-zA-Z]}/{tipo}/{tipoId}/archivopub.do", params = Microfront.PNAME)
	public String archivopubRelativoPorNombre(@PathVariable("mkey") final SiteId siteId,
			@PathVariable(value = Microfront.PLANG) final Idioma lang, @PathVariable("tipo") final char tipo,
			@PathVariable("tipoId") final String tipoId, @RequestParam(value = Microfront.PNAME) final String name,
			final Model model, final HttpServletResponse response) {
		return this.archivopubRelativoPorNombre(siteId, name, model, response);

	}

	/**
	 * Hay archivos referenciados relativos desde archivos css que se cargan como
	 * archivopub
	 */
	@RequestMapping(value = "/{uri}/f/archivopub.do", params = Microfront.PNAME)
	public String archivopubRelativoPorNombre(@PathVariable("uri") final SiteId URI,
			@RequestParam(value = Microfront.PNAME) final String name, final Model model,
			final HttpServletResponse response) {

		Microsite microsite = null;
		try {

			microsite = this.dataService.getMicrositeByUri(URI.uri, DEFAULT_IDIOMA);
			if (microsite == null) {
				if (URI == null || URI.uri == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + URI.uri);
			}

			return "redirect:" + this.urlFactory.archivopubByNombre(microsite, name);

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, DEFAULT_IDIOMA, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}

	}

	/**
	 * Urls de contenido antiguo. /taw.do?ttr=CNTSP&idioma=ca&id=24454&idsite=1364
	 *
	 * @throws DelegateException
	 */
	@RequestMapping("taw.do")
	public String taw(@RequestParam(Microfront.PIDSITE) final Long idSite,
			@RequestParam(value = Microfront.PLANG, defaultValue = Microfront.DEFAULT_IDIOMA) final Idioma lang,
			@RequestParam("id") final Long idCont, @RequestParam("ttr") final String ttr, final Model model,
			final HttpServletResponse response) throws DelegateException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrosite(idSite, lang);
			if (microsite == null) {
				if (idSite == null) {
					throw new ExceptionFrontMicro();
				}
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + idSite);
			}

			String url = "";
			if (ttr.equals(Microfront.RNOTICIA)) {
				url = this.urlFactory.tawItemNoticia(microsite, lang,
						this.noticiasDataService.loadNoticia(idCont, lang));
			} else if (ttr.equals(Microfront.RAGENDA)) {
				url = this.urlFactory.tawItemAgenda(microsite, lang, this.dataService.loadAgenda(idCont, lang));
			} else if (ttr.equals(Microfront.RCONTENIDO)) {
				url = this.urlFactory.tawItemContenido(microsite, lang,
						this.contenidoDataService.getContenido(microsite, idCont, lang.getLang()));
			}

			return "redirect:" + url;

		} catch (final ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		} catch (final ExceptionFrontPagina e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (final Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		}
	}

}
