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
	public ModelAndView contenidoSmart(@PathVariable("uri") SiteId URI, 
			
			@PathVariable("lang") Idioma lang,
			@PathVariable("uriContenido") UriContenido uriContenido, RedirectAttributes redir,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = "previsual", required = false, defaultValue = "") String previsual,
			@RequestParam(value = "tipo", required = false, defaultValue = "") String tipobeta,
			@RequestParam(value = "redi", required = false, defaultValue = "") String redi, HttpServletRequest request, HttpServletResponse response) {

		return this.contenido(URI, lang, uriContenido.nemotecnic, redir, mcont, pcampa, previsual, tipobeta, redi, request, response);

	}

	/**
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{uriContenido:.{3,}}")
	public ModelAndView contenidoSmart(@PathVariable("uri") SiteId URI, 
			
			@PathVariable("uriContenido") UriContenido uriContenido, RedirectAttributes redir,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = "previsual", required = false, defaultValue = "") String previsual,
			@RequestParam(value = "tipo", required = false, defaultValue = "") String tipobeta,
			@RequestParam(value = "redi", required = false, defaultValue = "") String redi, HttpServletRequest request, HttpServletResponse response) {
            
		return this.contenido(URI, DEFAULT_IDIOMA, uriContenido.nemotecnic, redir, mcont, pcampa, previsual, tipobeta, redi, request, response);

	}
	
	/**
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang:[a-zA-Z][a-zA-Z]}/c/{uriContenido}")
	public ModelAndView contenido(@PathVariable("uri") SiteId URI, @PathVariable("lang") Idioma lang,
			@PathVariable("uriContenido") String uriContenido, RedirectAttributes redir,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = "previsual", required = false, defaultValue = "") String previsual,
			@RequestParam(value = "tipo", required = false, defaultValue = "") String tipobeta,
			@RequestParam(value = "redi", required = false, defaultValue = "") String redi, HttpServletRequest request, HttpServletResponse response) {

		ContenidoView view = new ContenidoView();
		try {
			super.configureLayoutView(URI.uri, lang, view, pcampa, uriContenido);
			Microsite microsite = view.getMicrosite();

			Contenido contenido = this.contenidoDataService.getContenido(microsite, uriContenido, lang.getLang());

			if (contenido == null) {
				// TODO: 404
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}

			String urlredireccionada = ((TraduccionContenido) contenido.getTraduccion(lang.getLang())).getUrl();
			if (!StringUtils.isEmpty(urlredireccionada)) {
				String sMenuCont = contenido.getId().toString();
				String url = this.urlFactory.legacyToFrontUri(urlredireccionada, lang);
				
				if (!StringUtils.isEmpty(sMenuCont) && this.urlFactory.isLocalLegacyUri(urlredireccionada)) {
					return new ModelAndView("redirect:" + url + "&mcont=" + sMenuCont + "&uricont="+uriContenido);
				} else {
					if(!this.urlFactory.isLocalLegacyUri(urlredireccionada)){ //es externa #53						
						return new ModelAndView("redirect:" + url);
					}else{
						return new ModelAndView("redirect:" + url + "&uricont="+uriContenido);
					}
				}

			}

			// obtenemos el menu padre
			Menu menu = this.contenidoDataService.obtenerMenuBranch(contenido, lang.getLang());
			boolean previsualizar = !StringUtils.isEmpty(previsual);

			// comprobacion de menu en el microsite
			if (!menu.getMicrosite().getId().equals(microsite.getId())) {
				log.error("[error logico] idsite.longValue=" + microsite.getId() + ", menu.getIdmicrosite.longValue="
						+ menu.getMicrosite().getId().longValue());
				// beanerror.setAviso("Aviso");
				// beanerror.setMensaje("El contenido solicitado no pertenece al site.");
				// error=true;
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO);
			}

			// o bien comprobacion de que no esté vacio
			if (contenido.getTraduccion(lang.getLang()) == null) {
				// beanerror.setAviso("Aviso");
				// beanerror.setMensaje("El contenido solicitado no contiene información.");
				// error=true;
				// TODO: 404?
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}

			// o bien comprobacion de que esté vigente
			if (!previsualizar && !Fechas.vigente(contenido.getFpublicacion(), contenido.getFcaducidad())) {
				// beanerror.setAviso("Aviso");
				// beanerror.setMensaje("El contenido solicitado está caducado.");
				// error=true;
				// TODO: 404?
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}

			// o bien comprobacion de que no esté vacio
			if (!previsualizar && !contenido.getVisible().equals("S")) {
				// beanerror.setAviso("Aviso");
				// beanerror.setMensaje("El contenido solicitado no está disponible al público");
				// error=true;
				// TODO: 404?
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}

			this.cargarMollapan(view, contenido, menu, redi);

			// Parsear el contenido del microsite
			contenido = this.reemplazarTags(contenido, lang.getLang(), microsite, request, response);

			view.setContenido(contenido);
			view.setTipoBeta(tipobeta);

			
			return this.modelForView(this.templateNameFactory.contenido(microsite), view);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			response.setStatus(Integer.valueOf(ErrorMicrosite.ESTADO_NOT_FOUNT));
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO);
		} catch (ExceptionFrontPagina e) {
			log.error(e.getMessage());
			response.setStatus(Integer.valueOf(ErrorMicrosite.ESTADO_NOT_FOUNT));
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA);
		}

	}

	/**
     * Cuando un contenido de página, en su contenido tiene un a href a un enlace de página.
     * @param lang
     * @param uri
     * @param model
     * @return
     */
     @RequestMapping("{uri}/{uriContenido:.{3,}}/{url}")
     public ModelAndView contenidoSmart(@PathVariable("uri") SiteId URI, 
                  @PathVariable("url") String url, 
                  //@PathVariable("uriContenido") UriContenido uriContenido,
                  @PathVariable("uriContenido") String uriContenido,
                  RedirectAttributes redir,
                  @RequestParam(value = Microfront.PIDSITE, required = false, defaultValue = "") String idsite,
                  @RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
                  @RequestParam(value = Microfront.PCONT, required = false, defaultValue = "") String cont,
                  @RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
                  @RequestParam(value = Microfront.PTIPO, required = false, defaultValue = "") String tipo,
                  @RequestParam(value = "previsual", required = false, defaultValue = "") String previsual,
                  @RequestParam(value = "redi", required = false, defaultValue = "") String redi, HttpServletRequest request, HttpServletResponse response) {
       
           String urlFact = this.urlFactory.legacyToFrontUri(url, new Idioma(Idioma.getIdiomaPorDefecto()));
           StringBuffer urlAppend = new StringBuffer();
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
     * Cuando un contenido de página (con idioma), en su contenido tiene un a href a un enlace de página.
     * @param lang
     * @param uri
     * @param model
     * @return
     */
     @RequestMapping("{uri}/{lang:[a-zA-Z][a-zA-Z]}/{uriContenido:.{3,}}/{url}")
     public ModelAndView contenidoSmart(@PathVariable("uri") SiteId URI, 
                  @PathVariable("lang") Idioma lang,
                  @PathVariable("url") String url, 
                  //@PathVariable("uriContenido") UriContenido uriContenido,
                  @PathVariable("uriContenido") String uriContenido,
                  RedirectAttributes redir,
                  @RequestParam(value = Microfront.PIDSITE, required = false, defaultValue = "") String idsite,
                  @RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
                  @RequestParam(value = Microfront.PCONT, required = false, defaultValue = "") String cont,
                  @RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
                  @RequestParam(value = Microfront.PTIPO, required = false, defaultValue = "") String tipo,
                  @RequestParam(value = "previsual", required = false, defaultValue = "") String previsual,
                  @RequestParam(value = "redi", required = false, defaultValue = "") String redi, HttpServletRequest request, HttpServletResponse response) {
       
           String urlFact = this.urlFactory.legacyToFrontUri(url, lang);
           StringBuffer urlAppend = new StringBuffer();
           urlAppend.append("?lang="+lang.getLang());
           
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
	 * Método privado para remplazar tags.
	 * 
	 * @param contenido
	 * @param idioma
	 * @param microsite
	 * @return contenido contenido con los tags remplazados
	 * @throws Exception
	 */
	private Contenido reemplazarTags(Contenido contenido, String idioma, Microsite microsite, HttpServletRequest request, HttpServletResponse response)
			throws ExceptionFrontPagina {
		try {
			if (contenido.getTraduccion(idioma) != null) {
				TraduccionContenido trad =  ((TraduccionContenido) contenido.getTraduccion(idioma));
				if (trad.getTexto() != null) {
					
					if (trad.getTxbeta() != null) {
						String txtBeta = this.microparser.doParser(microsite, trad.getTxbeta(), idioma, request, response);
						trad.setTxbeta(txtBeta);
					}
					String txt = this.microparser.doParser(microsite, trad.getTexto(), idioma, request, response);
					((TraduccionContenido) contenido.getTraduccion(idioma)).setTexto(txt);

				}
			}
			return contenido;
		} catch (Exception e) { // TODO: catch Exception!!!

			throw new ExceptionFrontPagina(" [reemplazarTags, idsite=" + microsite.getId() + ", cont=" + contenido.getId() + ", idioma=" + idioma
					+ " ] Error=" + e.getMessage() + "\n Stack=" + Cadenas.statcktrace2String(e.getStackTrace(), 3));
		}
	}

	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por
	 * el microsite.
	 * 
	 * @param microsite
	 * @param menu
	 * @param model
	 * @param lang
	 * @param redi
	 * @return string recorrido en el microsite
	 */
	private void cargarMollapan(ContenidoView view, Contenido contenido, Menu menu, String redi) {

		List<PathItem> path = super.getBasePath(view);

		String titulomollapa = (menu.getVisible().equals("S")) ? ((TraduccionMenu) menu.getTraduccion(view.getLang().getLang())).getNombre() : "";
		String titol = ((TraduccionContenido) contenido.getTraduccion(view.getLang().getLang())).getTitulo();
		String submolla = ((titulomollapa != null && titulomollapa.length() > 0) && (!redi.equals("yes"))) ? titulomollapa : "";
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
