package es.caib.gusite.front.qssi;


import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.caib.gusite.front.general.BaseController;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.service.ContactosDataService;
import es.caib.gusite.micromodel.Frqssi;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionFrqssi;

/**
 * 
 * @author brujula-at4
 *
 */
@Controller
public class QssiController extends BaseController {
	
	private static Log log = LogFactory.getLog(QssiController.class);

	@Autowired
    protected ContactosDataService contactosDataService;
	

	/**
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="{uri}/{lang}/qssi/{qssi}/") 
	public String qssi (
					@PathVariable("uri") SiteId URI, 
					@PathVariable("lang") Idioma lang,
					@PathVariable("qssi") long idQssi,
					Model model,
					HttpServletRequest req) {
		
		Microsite microsite = null;
	  	try {
	  		
		  	microsite =  super.loadMicrosite(URI.uri, lang, model);
			Frqssi qssi = this.dataService.getFormularioQssi(microsite, lang, idQssi);

			//comprobacion de microsite
			if (qssi.getIdmicrosite().longValue()!=microsite.getId().longValue()) {
					log.error("El elemento solicitado no pertenece al site");
					return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
			}
    		
	    	 String Urlqssi= System.getProperty("es.caib.gusite.frqssi.url");
			 String laurl;
			 if (qssi.getCentro()!= null && qssi.getTipoescrito()!= null){
				  laurl = Urlqssi+"&centre="+qssi.getCentro()+"&tipus_escrit="+qssi.getTipoescrito()+"&asunto="+((TraduccionFrqssi)qssi.getTraduce()).getNombre()+"?idioma="+lang.getLang();
			 } else{ 
				 if (qssi.getCentro()!= null) {
				 laurl = Urlqssi+"&centre="+qssi.getCentro()+"&asunto="+((TraduccionFrqssi)qssi.getTraduce()).getNombre()+"?idioma="+lang.getLang();
				 } else  laurl = Urlqssi+"&asunto="+((TraduccionFrqssi)qssi.getTraduce()).getNombre()+"?idioma="+lang.getLang();
			 }

			 return "redirect:" + laurl;

	
        } catch (ExceptionFrontMicro e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
		} catch (ExceptionFrontPagina e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
		}      

	}

	@Override
	public String setServicio() {
		
		return Microfront.RQSSI;
	}

	
	
	
}
