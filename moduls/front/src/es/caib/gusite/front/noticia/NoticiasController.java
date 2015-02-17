package es.caib.gusite.front.noticia;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.caib.gusite.front.general.BaseController;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.general.bean.PathItem;
import es.caib.gusite.front.service.NoticiasDataService;
import es.caib.gusite.front.util.Fechas;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.Tipo;
import es.caib.gusite.micromodel.TraduccionNoticia;
import es.caib.gusite.micromodel.TraduccionTipo;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.TipoDelegate;

/**
 * 
 * @author brujula-at4
 * 
 */
@Controller
public class NoticiasController extends BaseController {

	private static Log log = LogFactory.getLog(NoticiasController.class);

	@Autowired
	protected NoticiasDataService noticiasDataService;

	/**
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * 
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang}/l/{uriListaNoticia}")
	public String listarnoticias(
			@PathVariable("uri") SiteId URI,
			@PathVariable("lang") Idioma lang,
			@PathVariable("uriListaNoticia") String uriListaNoticia,
			Model model,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = "filtro", required = false, defaultValue = "") String filtro,
			@RequestParam(value = "pagina", required = false, defaultValue = "1") int pagina,
			@RequestParam(value = "ordenacion", required = false, defaultValue = "") String ordenacion,
			HttpServletRequest req) {

		NoticiaCriteria criteria = new NoticiaCriteria(filtro, pagina,
				ordenacion);
		Microsite microsite = null;
		try {

			microsite = super.loadMicrosite(URI.uri, lang, model, pcampa);
			Tipo tipo = this.getTipo(uriListaNoticia, lang.getLang());
			model.addAttribute("MVS_claseelemento_id", tipo.getId());
			criteria.setTipo(tipo);
			String plantillaForward = "";

			if (tipo.getTipoelemento().equals(Tipo.TIPO_CONEXIO_EXTERNA)) {

				model.addAttribute("MVS_mchtml",
						this.noticiasDataService.cargaExterno(tipo, req));
				plantillaForward = this.templateNameFactory
						.listarNoticiasExternas(microsite);

			} else {
				ResultadoNoticias<Noticia> noticias = null;
				List<String> listaAnyos = null;
				if (tipo.getTipopagina().equals(Microfront.ELEM_PAG_NORMAL)) {

					// Paginación normal
					criteria.setAnyo(0);

				} else {
					// Paginación anual
					listaAnyos = this.noticiasDataService.obtenerListaAnyos(
							microsite, lang, tipo);

					if (criteria.getAnyo() == 0) {
						if (listaAnyos != null && listaAnyos.size() > 0) {

							if (listaAnyos.get(0) == "Tots") {
								// criteria.setAnyo(-1);
								listaAnyos.remove(0);
								listaAnyos.add(0, "-1");
								criteria.setAnyo(Integer.parseInt(listaAnyos
										.get(0))); // coger el primero
							} else {
								criteria.setAnyo(Integer.parseInt(listaAnyos
										.get(0))); // coger el primero
							}
							// criteria.setAnyo(Integer.parseInt(listaAnyos.get(0)));
							// //coger el primero
						} else {
							criteria.setAnyo(Fechas.obtenerAnyo(new Date())); // obtener
																				// el
																				// del
																				// año
																				// en
																				// curso
						}
					}
				}

				noticias = this.noticiasDataService.listarNoticias(microsite,
						lang, criteria);

				if (noticias.isError()) {

					return this.getForwardError(microsite, lang, model,
							ErrorMicrosite.ERROR_AMBIT_PAGINA);
				} else {

					model.addAttribute("MVS_seulet_sin", this.urlFactory
							.listarNoticiasSinPagina(microsite, lang, tipo,
									criteria, mcont, pcampa));
					model.addAttribute("MVS_parametros_pagina",
							noticias.getParametros());
					model.addAttribute("MVS_listado", noticias.getResultados());
					String desctiponoticia = ((TraduccionTipo) tipo
							.getTraduccion(lang.getLang())).getNombre();
					model.addAttribute("MVS_tipolistado", desctiponoticia);
					model.addAttribute("MVS_claseelemento", tipo);
					model.addAttribute("MVS_busqueda",
							"" + noticias.isBusqueda());

					if (tipo.getTipopagina().equals(Microfront.ELEM_PAG_ANUAL)) {
						model.addAttribute("MVS_listadoanyos", listaAnyos);
						model.addAttribute("MVS_anyo", criteria.getAnyo());
					}

					if (tipo.getTipoelemento().equals(Microfront.ELEM_NOTICIA)) {

						if (tipo.getTipopagina().equals(
								Microfront.ELEM_PAG_ANUAL)) {
							plantillaForward = this.templateNameFactory
									.listarNoticiasAnyos(microsite);
						} else {
							plantillaForward = this.templateNameFactory
									.listarNoticias(microsite);
						}
					}

					if (tipo.getTipoelemento().equals(Microfront.ELEM_LINK)) {
						if (tipo.getTipopagina().equals(
								Microfront.ELEM_PAG_ANUAL)) {
							plantillaForward = this.templateNameFactory
									.listarLinksAnyos(microsite);
						} else {
							plantillaForward = this.templateNameFactory
									.listarLinks(microsite);
						}
					}

					if (tipo.getTipoelemento()
							.equals(Microfront.ELEM_DOCUMENTO)) {
						if (tipo.getTipopagina().equals(
								Microfront.ELEM_PAG_ANUAL)) {
							plantillaForward = this.templateNameFactory
									.listarDocumentosAnyos(microsite);
						} else {
							plantillaForward = this.templateNameFactory
									.listarDocumentos(microsite);
						}
					}

					if (tipo.getTipoelemento().equals(Microfront.ELEM_FOTO)) {
						String[] tamanyo = this.calcularTamanyoFoto(tipo
								.getFotosporfila());
						model.addAttribute("MVS_anchoFoto", tamanyo[0]);
						model.addAttribute("MVS_altoFoto", tamanyo[1]);

						if (tipo.getTipopagina().equals(
								Microfront.ELEM_PAG_ANUAL)) {
							plantillaForward = this.templateNameFactory
									.mostrarGaleriaFotosAnyos(microsite);
						} else {
							plantillaForward = this.templateNameFactory
									.mostrarGaleriaFotos(microsite);
						}
					}

					// TODO: averiguar para qué sirve mcont. Creo que para la
					// opción de menú que hay que resaltar (confirmar)
					if (!StringUtils.isEmpty(mcont)) {
						model.addAttribute("MVS_menu_cont_notic", mcont);
					}
				}
			}
			this.cargarMollapan(microsite, tipo, model, lang);
			return plantillaForward;

		} catch (DelegateException e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_PAGINA);
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
	 * Método privado para guardar el recorrido que ha realizado el usuario por
	 * el microsite.
	 * 
	 * @param microsite
	 * @param model
	 * @param lang
	 * @return
	 * @return string recorrido en el microsite
	 */
	private List<PathItem> cargarMollapan(Microsite microsite, Tipo tipo,
			Model model, Idioma lang) {

		List<PathItem> path = super.getBasePath(microsite, model, lang);
		String desctiponoticia = ((TraduccionTipo) tipo.getTraduccion(lang
				.getLang())).getNombre();
		if (!StringUtils.isEmpty(desctiponoticia)) {
			path.add(new PathItem(desctiponoticia, this.urlFactory
					.listarNoticias(microsite, lang, tipo)));
		}
		// TODO: eliminar cuando se actualicen las plantillas
		model.addAttribute("MVS2_mollapan", this.mollapan(path));
		// Datos para la plantilla
		model.addAttribute("MVS2_pathdata", path);

		return path;
	}

	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por
	 * el microsite.
	 * 
	 * @param microsite
	 * @param model
	 * @param lang
	 * @return
	 * @return string recorrido en el microsite
	 */
	private List<PathItem> cargarMollapan(Microsite microsite, Noticia noticia,
			Model model, Idioma lang) {

		List<PathItem> path = this.cargarMollapan(microsite, noticia.getTipo(),
				model, lang);
		// TODO: añadir el título de la noticia?
		path.add(new PathItem(this.getMessage("noticia.detalle", lang),
				this.urlFactory.noticia(microsite, lang, noticia)));
		// TODO: eliminar cuando se actualicen las plantillas
		model.addAttribute("MVS2_mollapan", this.mollapan(path));
		// Datos para la plantilla
		model.addAttribute("MVS2_pathdata", path);

		return path;
	}

	private Tipo getTipo(String uriTipo, String lang)
			throws ExceptionFrontPagina {
		TipoDelegate tipodel = DelegateUtil.getTipoDelegate();
		try {
			Tipo tipo = tipodel.obtenerTipoDesdeUri(lang, uriTipo);
			if (tipo == null) {
				// Si no lo encontramos por idioma, buscamos cualquiera. Esto
				// sirve para el cambio de idioma sencillo
				tipo = tipodel.obtenerTipoDesdeUri(null, uriTipo);
			}
			if (tipo == null) {
				throw new ExceptionFrontPagina(
						"Tipo no encontrado: " + uriTipo,
						ExceptionFrontPagina.HTTP_NOT_FOUND);
			}
			return tipo;
		} catch (DelegateException e) {
			throw new ExceptionFrontPagina(e);
		}
	}

	private String[] calcularTamanyoFoto(int numFotosFila) {
		int ancho = 0, alto = 0;

		if (0 != numFotosFila) {
			ancho = 100 / numFotosFila; // max = 100%
			alto = 300 / numFotosFila; // max = 300px
		}
		return new String[] { Integer.toString(ancho), Integer.toString(alto) };
	}

	/**
	 * TODO: noticia debería ser el nemotecnico de la noticia
	 * 
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang}/n/{uriNoticia}")
	public String noticia(
			@PathVariable("uri") SiteId URI,
			@PathVariable("lang") Idioma lang,
			@PathVariable("uriNoticia") String uriNoticia,
			Model model,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa) {

		Microsite microsite = null;
		try {

			microsite = super.loadMicrosite(URI.uri, lang, model, pcampa);
			Noticia noticia = this.noticiasDataService.loadNoticia(uriNoticia,
					lang.getLang());

			// comprobacion de microsite
			if (noticia.getIdmicrosite().longValue() != microsite.getId()
					.longValue()) {
				log.error("El elemento solicitado no pertenece al site");
				return this.getForwardError(microsite, lang, model,
						ErrorMicrosite.ERROR_AMBIT_MICRO);
			}
			// comprobacion de visibilidad
			if (!noticia.getVisible().equals("S")) {
				log.error("El elemento solicitado no está visible");
				return this.getForwardError(microsite, lang, model,
						ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}
			// o bien comprobacion de que esté vigente
			if (!Fechas.vigente(noticia.getFpublicacion(),
					noticia.getFcaducidad())) {
				log.error("El contenido solicitado está caducado");
				return this.getForwardError(microsite, lang, model,
						ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}
			model.addAttribute("MVS_noticia", noticia);
			if (noticia.getImagen() != null) {

				// TODO: this is weird
				if (noticia.getImagen().getPeso() > 100 * 1024) {
					model.addAttribute("MVS_anchoImg", "460");
				} else {
					model.addAttribute("MVS_anchoImg", "");
				}
			}
			model.addAttribute("MVS_tiponoticia", ((TraduccionTipo) noticia
					.getTipo().getTraduccion(lang.getLang())).getNombre());

			// solo quiero que añada el atributo en el caso de cargar noticia
			// del listarnoticias.jsp
			if ((mcont != null) && (!mcont.equals(""))) {
				model.addAttribute("MVS_menu_cont_notic", mcont);
			}
			this.cargarMollapan(microsite, noticia, model, lang);

			return this.templateNameFactory.mostrarNoticia(microsite);

		} catch (DelegateException e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_PAGINA);
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
	 * TODO: documento debería ser el nemotecnico de la noticia
	 * 
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang}/d/{uriDocumentoNoticia}")
	public String documento(
			@PathVariable("uri") SiteId URI,
			@PathVariable("lang") Idioma lang,
			@PathVariable("uriDocumentoNoticia") String uriDocumentoNoticia,
			Model model,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa) {

		Microsite microsite = null;
		try {
			microsite = super.loadMicrosite(URI.uri, lang, model, pcampa);
			Noticia noticia = this.noticiasDataService.loadNoticia(
					uriDocumentoNoticia, lang.getLang());

			// comprobacion de microsite
			if (noticia.getIdmicrosite().longValue() != microsite.getId()
					.longValue()) {
				log.error("El elemento solicitado no pertenece al site");
				return this.getForwardError(microsite, lang, model,
						ErrorMicrosite.ERROR_AMBIT_MICRO);
			}
			// comprobacion de visibilidad
			if (!noticia.getVisible().equals("S")) {
				log.error("El elemento solicitado no está visible");
				return this.getForwardError(microsite, lang, model,
						ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}
			// o bien comprobacion de que esté vigente
			if (!Fechas.vigente(noticia.getFpublicacion(),
					noticia.getFcaducidad())) {
				log.error("El contenido solicitado está caducado");
				return this.getForwardError(microsite, lang, model,
						ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}

			Long iddocumento = ((TraduccionNoticia) noticia.getTraduccion(lang
					.getLang())).getDocu().getId();
			return "forward:"
					+ this.urlFactory.archivopubById(microsite, iddocumento);

		} catch (DelegateException e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_PAGINA);
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

	@Override
	public String setServicio() {

		return Microfront.RNOTICIA;
	}
}
