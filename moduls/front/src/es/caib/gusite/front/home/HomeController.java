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
	 * TODO: mkey debería ser el uri del site
	 * TODO: comprobar que el lang está disponible para el site. En caso contrario, bien dar un 404, bien redireccionar a un idioma existente.
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping("{mkey}/{lang}") 
	public String home (
					@PathVariable("mkey") SiteId siteId, 
					@PathVariable("lang") Idioma lang,
					Model model,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa) {
		Microsite microsite = null;
	  	try {
		  	microsite =  super.loadMicrosite(siteId.mkey, lang, model, pcampa);
     		
			cargarCampanya(microsite, model, lang);

			if ( microsite.getPlantilla().equals( Microfront.HOME_CONTENIDO ) ) { 
				//home tipo "Escoger una página de contenido propio del microsite"
				UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(microsite.getUrlhome());
				uri.replaceQueryParam(Microfront.PLANG, lang.getLang());
				uri.replaceQueryParam(Microfront.PCAMPA, "yes");
				String fw = "forward:/" + uri.build().toUriString();
				//String fw = "forward:/" + microsite.getUrlhome() + "&" + Microfront.PLANG + "=" + lang.getLang()+ "&" + Microfront.PCAMPA + "=yes";
				return fw;

			} else {
				//Home tipo "Pàgina por defecto proporcionada por la herramienta" 

				cargarNoticias(microsite, model, lang);
				cargarAgenda(microsite, model, lang);
				
			}

			cargarMollapan(microsite, model, lang);

		    return this.templateNameFactory.home(microsite);
	
        } catch (ExceptionFrontMicro e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
		} catch (ExceptionFrontPagina e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
		}      

	}

	
	/**
	 * TODO: mkey debería ser el uri del site
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping("{mkey}") 
	public String home (
					@PathVariable("mkey") SiteId siteId, 
					Model model,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa) {
		//TODO: implementar negociación de idioma y, tal vez, redireccionar en lugar de aceptar la uri.
		return home(siteId, DEFAULT_IDIOMA, model, pcampa);

	}

	/**
	 * TODO: mkey debería ser el uri del site
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping("{mkey}/{lang}/mapa") 
	public String mapa (
					@PathVariable("mkey") SiteId siteId, 
					@PathVariable("lang") Idioma lang,
					Model model,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa) {
		Microsite microsite = null;
	  	try {
	  		microsite =  super.loadMicrosite(siteId.mkey, lang, model, pcampa);

		  	this.cargarMollapanMapa(microsite, model, lang);
	  	
        } catch (ExceptionFrontMicro e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
        }      

		return this.templateNameFactory.mapa(microsite);
		
	}	

	/**
	 * TODO: mkey debería ser el uri del site
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping("{mkey}/mapa") 
	public String mapa (
					@PathVariable("mkey") SiteId siteId, 
					Model model,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa) {
		//TODO: implementar negociación de idioma y, tal vez, redireccionar en lugar de aceptar la uri.
		return mapa(siteId, DEFAULT_IDIOMA, model, pcampa);

	}

	/**
	 * TODO: mkey debería ser el uri del site
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping("{mkey}/{lang}/accessibility") 
	public String accessibility (
					@PathVariable("mkey") SiteId siteId, 
					@PathVariable("lang") Idioma lang,
					Model model,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa) {
		Microsite microsite = null;
	  	try {
	  		microsite =  super.loadMicrosite(siteId.mkey, lang, model, pcampa);

		  	cargarMollapanAccessibilitat(microsite, model, lang);
	  	
        } catch (ExceptionFrontMicro e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
        }      
		return this.templateNameFactory.accessibilitat(microsite);
		
	}	

	/**
	 * TODO: mkey debería ser el uri del site
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping("{mkey}/accessibility") 
	public String accessibility (
					@PathVariable("mkey") SiteId siteId, 
					Model model,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa) {
		return accessibility(siteId, new Idioma(LANG_EN), model, pcampa);

	}

	/**
	 * TODO: mkey debería ser el uri del site
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping("{mkey}/accessibilitat") 
	public String accessibilitat (
					@PathVariable("mkey") SiteId siteId, 
					Model model,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa) {
		return accessibility(siteId, new Idioma(LANG_CA), model, pcampa);

	}

	/**
	 * TODO: mkey debería ser el uri del site
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping("{mkey}/accesibilidad") 
	public String accesibilidad (
					@PathVariable("mkey") SiteId siteId, 
					Model model,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa) {
		return accessibility(siteId, new Idioma(LANG_ES), model, pcampa);

	}

	
	
	@Override
	public String setServicio() {
		
		return Microfront.RMICROSITE;
	}

	
	
	/**
	 * Método privado para preparar la Agenda, en el caso que ese microsite tenga asignado ese componente 
	 * @param lang 
	 * @param model 
	 * @param microsite 
	 * @throws ExceptionFrontPagina 
	 */
	private void cargarAgenda(Microsite microsite, Model model, Idioma lang) throws ExceptionFrontPagina {
		if (!existeServicio(microsite, lang, Microfront.RAGENDA)) {
			//TODO: error 404
			return;
		}
		
		//MParserAgenda parseagenda = new MParserAgenda(microsite.getRestringido());
			
		try{
        	model.addAttribute("MVS_home_datos_calendario_agenda", this.dataService.getDatosCalendarioHome(microsite, lang));
        	
        	List<Agenda> listaagenda2 = this.dataService.getDatosListadoHome(microsite, lang);
        	model.addAttribute("MVS_home_datos_agenda_listado", listaagenda2);

			//TODO: este 3 hardcoded, habría que evitarlo o mejorarlo
			//model.addAttribute("MVS_home_agenda_calendario", parseagenda.getHtmlAgendaCalendario(microsite.getId(), lang.getLang(), 3).toString());
			//model.addAttribute("MVS_home_agenda_listado", parseagenda.getHtmlAgendaListado(microsite.getId(), lang.getLang(), 3).toString());
	
		 } catch (DelegateException e) {
			throw new ExceptionFrontPagina(e);
		}
	}
	
	/**
	 * Método privado para preparar la Noticia, en el caso que ese microsite tenga asignado ese componente 
	 * @param lang 
	 * @param model 
	 * @param microsite 
	 * @throws ExceptionFrontPagina 
	 */
	private void cargarNoticias(Microsite microsite, Model model, Idioma lang) throws ExceptionFrontPagina {
	
		
		if (!existeServicio(microsite, lang, Front.RNOTICIA)) {
			//TODO: error 404
			return;
		}
		
		try{
			//NOTICIAS
			//MParserElemento parseelemento = new MParserElemento(microsite.getRestringido());
			List<Noticia> listanoticias = this.dataService.getNoticiasHome(microsite, lang);
        	
        	model.addAttribute("MVS_home_datos_noticias_listado", listanoticias);
        	model.addAttribute("MVS_ELEM_NOTICIA", Front.ELEM_NOTICIA);
        	model.addAttribute("MVS_home_idmicrosite", microsite.getId());
    
      
			//model.addAttribute("MVS_home_noticias", parseelemento.getHtmlNoticias(microsite.getId(), lang.getLang(), noticias).toString());
		 } catch (DelegateException e) {
				throw new ExceptionFrontPagina(e);
		 }
	}
		
	/**
	 * Método privado para preparar la campanya. 
	 * @param model 
	 */
	private void cargarCampanya(Microsite microsite, Model model, Idioma lang) {
		MParserHTML parsehtml = new MParserHTML(microsite.getRestringido());
		model.addAttribute("MVS_home_campanya", parsehtml.getHtmlCampanya(microsite,lang.getLang()).toString());
	}	

	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por el microsite.
	 * TODO: eliminar la generación de html aquí
	 * @param microsite
	 * @param model
	 * @param lang
	 * @return string recorrido en el microsite
	 */
	private void cargarMollapan(Microsite microsite, Model model, Idioma lang) {
		
		List<PathItem> path = super.getBasePath(microsite, model, lang);
		
		//TODO: eliminar cuando se actualicen las plantillas
	    model.addAttribute("MVS2_mollapan", mollapan(path));

	    //Datos para la plantilla
	    model.addAttribute("MVS2_pathdata", path);
		
	}


	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por el microsite.
	 * TODO: eliminar la generación de html aquí
	 * @param microsite
	 * @param model
	 * @param lang
	 * @return string recorrido en el microsite
	 */
	private void cargarMollapanMapa(Microsite microsite, Model model, Idioma lang) {

		List<PathItem> path = super.getBasePath(microsite, model, lang);
		path.add(new PathItem(getMessage("mapa.mapa", lang)));
		
		//TODO: eliminar cuando se actualicen las plantillas
	    model.addAttribute("MVS2_mollapan", mollapan(path));
	    

	    //Datos para la plantilla
	    model.addAttribute("MVS2_pathdata", path);
	}	
	
	
	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por el microsite.
	 * @param request  
	 * @param idi   idioma
	 * @return string recorrido en el microsite
	 */
	private void cargarMollapanAccessibilitat(Microsite microsite, Model model, Idioma lang) {
		List<PathItem> path = super.getBasePath(microsite, model, lang);
		path.add(new PathItem(getMessage("mapa.mapa", lang)));
		
		//TODO: eliminar cuando se actualicen las plantillas
	    model.addAttribute("MVS2_mollapan", mollapan(path));

	    //Datos para la plantilla
	    model.addAttribute("MVS2_pathdata", path);
	}
	
	
}
