package es.caib.gusite.front.faq;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.caib.gusite.front.general.BaseController;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.Front;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.general.bean.PathItem;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.DelegateException;

/**
 * 
 * @author brujula-at4
 *
 */
@Controller
public class FaqController extends BaseController {
	
	private static Log log = LogFactory.getLog(FaqController.class);
	
	/**
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang}/faq") 
	public String listarfaqs (
					@PathVariable("uri") SiteId URI, 
					@PathVariable("lang") Idioma lang,
					Model model,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa) {
		Microsite microsite = null;
	  	try {
		  	microsite =  super.loadMicrosite(URI.uri, lang, model, pcampa);
     		
			cargarFaq(microsite, model, lang);
			cargarMollapan(microsite, model, lang);

		    return this.templateNameFactory.listarFaqs(microsite);
	
        } catch (DelegateException e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
        } catch (ExceptionFrontMicro e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
		}

	}

	
	/**
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/faq") 
	public String home (
					@PathVariable("uri") SiteId URI, 
					Model model,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa) {
		//TODO: implementar negociación de idioma y, tal vez, redireccionar en lugar de aceptar la uri.
		return listarfaqs(URI, DEFAULT_IDIOMA, model, pcampa);

	}

	
	/**
	 * Método privado para preparar la Noticia, en el caso que ese microsite tenga asignado ese componente 
	 * @param lang 
	 * @param model 
	 * @param microsite 
	 * @throws DelegateException 
	 */
	private void cargarFaq(Microsite microsite, Model model, Idioma lang) throws DelegateException {
	
		
		if (existeServicio(microsite, lang, Front.RFAQ)) {
	
			List<Faqtema> listafaqstema = dataService.listarFaqs(microsite, lang);
		    model.addAttribute("MVS_listado", listafaqstema);
				
		} else {
			//TODO: error 404
		}
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
		path.add(new PathItem(getMessage("listarfaqs.preguntas", lang)));
		
		//TODO: eliminar cuando se actualicen las plantillas
	    model.addAttribute("MVS2_mollapan", mollapan(path));

	    //Datos para la plantilla
	    model.addAttribute("MVS2_pathdata", path);
	}	
	

	@Override
	public String setServicio() {
		return Microfront.RFAQ;
	}			
	

	
}
