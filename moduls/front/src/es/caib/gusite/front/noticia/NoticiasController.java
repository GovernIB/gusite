package es.caib.gusite.front.noticia;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.View;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.caib.gusite.front.general.BaseViewController;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.general.bean.PathItem;
import es.caib.gusite.front.service.NoticiasDataService;
import es.caib.gusite.front.util.Fechas;
import es.caib.gusite.front.view.LayoutView.ArchivoCSS;
import es.caib.gusite.front.view.ListarNoticiasView;
import es.caib.gusite.front.view.NoticiaView;
import es.caib.gusite.front.view.PageView;
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
public class NoticiasController extends BaseViewController {

	private static Log log = LogFactory.getLog(NoticiasController.class);

	@Autowired
	protected NoticiasDataService noticiasDataService;

	/**
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang:[a-zA-Z][a-zA-Z]}/l/{uriListaNoticia}")
	public ModelAndView listarnoticias(@PathVariable("uri") SiteId URI, @PathVariable("lang") Idioma lang,
			@PathVariable("uriListaNoticia") String uriListaNoticia,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = "filtro", required = false, defaultValue = "") String filtro,
			@RequestParam(value = "pagina", required = false, defaultValue = "1") int pagina,
			@RequestParam(value = "ordenacion", required = false, defaultValue = "") String ordenacion, HttpServletRequest req) {
	
		return this.listarnoticias(URI, lang, uriListaNoticia, 0, mcont, pcampa, filtro, pagina, ordenacion, req);
		
	}

	/**
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang:[a-zA-Z][a-zA-Z]}/l/{uriListaNoticia}/{anyo}")
	public ModelAndView listarnoticias(@PathVariable("uri") SiteId URI, @PathVariable("lang") Idioma lang,
			@PathVariable("uriListaNoticia") String uriListaNoticia,
			@PathVariable("anyo") int anyo,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = "filtro", required = false, defaultValue = "") String filtro,
			@RequestParam(value = "pagina", required = false, defaultValue = "1") int pagina,
			@RequestParam(value = "ordenacion", required = false, defaultValue = "") String ordenacion, HttpServletRequest req) {

		NoticiaCriteria criteria = new NoticiaCriteria(filtro, pagina, ordenacion);
		criteria.setAnyo(anyo);
		ListarNoticiasView view = new ListarNoticiasView();
		try {
			super.configureLayoutView(URI.uri, lang, view, pcampa);
			Microsite microsite = view.getMicrosite();

			Tipo tipo = this.getTipo(uriListaNoticia, lang.getLang(), microsite.getId().toString());
			view.setClaseElementoId(tipo.getId());

			criteria.setTipo(tipo);
			String plantillaForward = "";

			if (tipo.getTipoelemento().equals(Tipo.TIPO_CONEXIO_EXTERNA)) {

				view.setMchtml(this.noticiasDataService.cargaExterno(tipo, req));
				plantillaForward = this.templateNameFactory.listarNoticiasExternas(microsite);

			} else {
				ResultadoNoticias<Noticia> noticias = null;
				List<String> listaAnyos = null;
				if (tipo.getTipopagina().equals(Microfront.ELEM_PAG_NORMAL)) {

					// Paginación normal
					criteria.setAnyo(0);

				} else {
					// Paginación anual
					listaAnyos = this.noticiasDataService.obtenerListaAnyos(microsite, lang, tipo);

					if (criteria.getAnyo() == 0) {
						if (listaAnyos != null && listaAnyos.size() > 0) {

							if (listaAnyos.get(0) == "Tots") {
								// criteria.setAnyo(-1);
								listaAnyos.remove(0);
								listaAnyos.add(0, "-1");
								criteria.setAnyo(Integer.parseInt(listaAnyos.get(0))); // coger
																						// el
																						// primero
							} else {
								criteria.setAnyo(Integer.parseInt(listaAnyos.get(0))); // coger
																						// el
																						// primero
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

				noticias = this.noticiasDataService.listarNoticias(microsite, lang, criteria);

				if (noticias.isError()) {

					return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA);
				} else {

					view.setSeuletSin(this.urlFactory.listarNoticiasSinPagina(microsite, lang, tipo, criteria, mcont, pcampa));
					view.setParametrosPagina(noticias.getParametros());

					view.setListado(noticias.getResultados());
					String desctiponoticia = ((TraduccionTipo) tipo.getTraduccion(lang.getLang())).getNombre();
					view.setTipoListado(desctiponoticia);
					view.setClaseElemento(tipo);
					view.setBusqueda("" + noticias.isBusqueda());

					if (tipo.getTipopagina().equals(Microfront.ELEM_PAG_ANUAL)) {
						view.setListadoAnyos(listaAnyos);
						view.setAnyo(criteria.getAnyo());
					}

					if (tipo.getTipoelemento().equals(Microfront.ELEM_NOTICIA)) {

						plantillaForward = this.templateNameFactory.listarNoticias(microsite);
					}

					if (tipo.getTipoelemento().equals(Microfront.ELEM_LINK)) {
						plantillaForward = this.templateNameFactory.listarLinks(microsite);
					}

					if (tipo.getTipoelemento().equals(Microfront.ELEM_DOCUMENTO)) {
						plantillaForward = this.templateNameFactory.listarDocumentos(microsite);
					}

					if (tipo.getTipoelemento().equals(Microfront.ELEM_FOTO)) {
						String[] tamanyo = this.calcularTamanyoFoto(tipo.getFotosporfila());
						view.setAnchoFoto(tamanyo[0]);
						view.setAltoFoto(tamanyo[1]);
						plantillaForward = this.templateNameFactory.mostrarGaleriaFotos(microsite);
					}
										
					if (tipo.getTipoelemento().equals(Microfront.ELEM_MAPA)) {
						plantillaForward = this.templateNameFactory.listarUbicaciones(microsite);						
					}

					// TODO: averiguar para qué sirve mcont. Creo que para la
					// opción de menú que hay que resaltar (confirmar)
					if (!StringUtils.isEmpty(mcont)) {
						view.setMenuContenidoNoticia(mcont);
					}
				}
			}
			this.cargarMollapan(view, tipo);
			return this.modelForView(plantillaForward, view);

		} catch (DelegateException e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO);
		} catch (ExceptionFrontPagina e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA);
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
	private List<PathItem> cargarMollapan(PageView view, Tipo tipo) {

		List<PathItem> path = super.getBasePath(view);
		String desctiponoticia = ((TraduccionTipo) tipo.getTraduccion(view.getLang().getLang())).getNombre();
		if (!StringUtils.isEmpty(desctiponoticia)) {
			path.add(new PathItem(desctiponoticia, this.urlFactory.listarElementos(view.getMicrosite(), view.getLang(), tipo)));
		}
		// Datos para la plantilla
		view.setPathData(path);

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
	private List<PathItem> cargarMollapan(PageView view, Noticia noticia) {

		List<PathItem> path = this.cargarMollapan(view, noticia.getTipo());
		// TODO: añadir el título de la noticia?
		path.add(new PathItem(this.getMessage("noticia.detalle", view.getLang()), this.urlFactory.noticia(view.getMicrosite(), view.getLang(),
				noticia)));
		// Datos para la plantilla
		view.setPathData(path);

		return path;
	}

	private Tipo getTipo(String uriTipo, String lang, String site) throws ExceptionFrontPagina {
		TipoDelegate tipodel = DelegateUtil.getTipoDelegate();
		try {
			Tipo tipo = tipodel.obtenerTipoDesdeUri(lang, uriTipo, site);
			if (tipo == null) {
				// Si no lo encontramos por idioma, buscamos cualquiera. Esto
				// sirve para el cambio de idioma sencillo
				tipo = tipodel.obtenerTipoDesdeUri(null, uriTipo, site);
			}
			if (tipo == null) {
				throw new ExceptionFrontPagina("Tipo no encontrado: " + uriTipo, ExceptionFrontPagina.HTTP_NOT_FOUND);
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
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang:[a-zA-Z][a-zA-Z]}/n/{uriNoticia}")
	public ModelAndView noticia(@PathVariable("uri") SiteId URI, @PathVariable("lang") Idioma lang, @PathVariable("uriNoticia") String uriNoticia,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = Microfront.FMAPA, required = false, defaultValue = "") String fmapa) {

		NoticiaView view = new NoticiaView();
		try {
			super.configureLayoutView(URI.uri, lang, view, pcampa);
			Microsite microsite = view.getMicrosite();
			Noticia noticia = this.noticiasDataService.loadNoticia(uriNoticia, lang.getLang(), view.getMicrosite().getId().toString());

			// comprobacion de microsite
			if (noticia.getIdmicrosite().longValue() != microsite.getId().longValue()) {
				log.error("El elemento solicitado no pertenece al site");
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO);
			}
			// comprobacion de visibilidad
			if (!noticia.getVisible().equals("S")) {
				log.error("El elemento solicitado no está visible");
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}
			// o bien comprobacion de que esté vigente
			if (!Fechas.vigente(noticia.getFpublicacion(), noticia.getFcaducidad())) {
				log.error("El contenido solicitado está caducado");
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}
			view.setNoticia(noticia);
			if (noticia.getImagen() != null) {

				// TODO: this is weird
				if (noticia.getImagen().getPeso() > 100 * 1024) {
					view.setAnchoImg("460");
				} else {
					view.setAnchoImg("");
				}
			}

			view.setTipoNoticia(((TraduccionTipo) noticia.getTipo().getTraduccion(lang.getLang())).getNombre());

			// solo quiero que añada el atributo en el caso de cargar noticia
			// del listarnoticias.jsp
			if ((mcont != null) && (!mcont.equals(""))) {
				view.setMenuContenidoNoticia(mcont);
			}
			this.cargarMollapan(view, noticia);
			
			ModelAndView viewNoticia;
			
			if (noticia.getTipo().getTipoelemento().equals(Microfront.ELEM_MAPA)) {				
				viewNoticia =  this.modelForView(this.templateNameFactory.mostrarUbicacion(microsite),view);				
			}else if((fmapa.equals("true")
					 && !StringUtils.isEmpty(noticia.getLatitud())
					 && !StringUtils.isEmpty(noticia.getLongitud()))){			
				view.setForzarMapa(true);
				viewNoticia =  this.modelForView(this.templateNameFactory.mostrarUbicacion(microsite),view);				
			}else{
				viewNoticia =  this.modelForView(this.templateNameFactory.mostrarNoticia(microsite), view);				
			}

			return viewNoticia;

		} catch (DelegateException e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO);
		} catch (ExceptionFrontPagina e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA);
		}
	}
	
	
	
	
	
	

	/**
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang:[a-zA-Z][a-zA-Z]}/d/{uriDocumentoNoticia}")
	public ModelAndView documento(@PathVariable("uri") SiteId URI, @PathVariable("lang") Idioma lang,
			@PathVariable("uriDocumentoNoticia") String uriDocumentoNoticia, Model model,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa) {

		NoticiaView view = new NoticiaView();
		try {
			super.configureLayoutView(URI.uri, lang, view, pcampa);
			Microsite microsite = view.getMicrosite();
			Noticia noticia = this.noticiasDataService.loadNoticia(uriDocumentoNoticia, lang.getLang(), view.getMicrosite().getId().toString());

			// comprobacion de microsite
			if (noticia.getIdmicrosite().longValue() != microsite.getId().longValue()) {
				log.error("El elemento solicitado no pertenece al site");
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO);
			}
			// comprobacion de visibilidad
			if (!noticia.getVisible().equals("S")) {
				log.error("El elemento solicitado no está visible");
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}
			// o bien comprobacion de que esté vigente
			if (!Fechas.vigente(noticia.getFpublicacion(), noticia.getFcaducidad())) {
				log.error("El contenido solicitado está caducado");
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}
			
			if (((TraduccionNoticia) noticia.getTraduccion(lang.getLang())).getDocu() == null){
				log.error("No existe un documento asociado");
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}
			
			Long iddocumento = ((TraduccionNoticia) noticia.getTraduccion(lang.getLang())).getDocu().getId();
			return new ModelAndView("forward:" + this.urlFactory.archivopubById(microsite, iddocumento));

		} catch (DelegateException e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO);
		} catch (ExceptionFrontPagina e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA);
		}
	}

	@Override
	public String setServicio() {

		return Microfront.RNOTICIA;
	}
}
