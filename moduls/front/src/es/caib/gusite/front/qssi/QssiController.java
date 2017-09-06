package es.caib.gusite.front.qssi;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.caib.gusite.front.general.BaseViewController;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.front.general.ExceptionFrontQssi;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.service.ContactosDataService;
import es.caib.gusite.front.view.PageView;
import es.caib.gusite.micromodel.Frqssi;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;


/**
 * 
 * @author brujula-at4
 * 
 */
@Controller
public class QssiController extends BaseViewController {

	private static Log log = LogFactory.getLog(QssiController.class);

	@Autowired
	protected ContactosDataService contactosDataService;

	/**
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/{lang:[a-zA-Z][a-zA-Z]}/qssi/{qssi}/")
	public ModelAndView qssi(@PathVariable("uri") SiteId URI, @PathVariable("lang") Idioma lang, @PathVariable("qssi") long idQssi, Model model,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			HttpServletResponse response) {

		PageView view = new PageView();
		try {
			Microsite microsite = this.dataService.getMicrositeByUri(URI.uri, DEFAULT_IDIOMA);
			if (microsite == null) {
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + URI);				
			}

			
			view.setMicrosite(microsite);
			view.setLang(lang);
			view.setIdContenido(mcont);
			Frqssi qssi = this.dataService.getFormularioQssi(microsite, lang, idQssi);
			if (qssi == null) {
				throw new ExceptionFrontQssi(URI.uri, idQssi);			
			}
			
			// comprobacion de microsite
			if (qssi.getIdmicrosite().longValue() != microsite.getId().longValue()) {
				log.error("El elemento solicitado no pertenece al site");
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
			}

			String laurl = this.urlFactory.qssiFinalUrl(microsite, lang.getLang(), qssi);

			return new ModelAndView("redirect:" + laurl);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO, response,URI.uri,lang,null);
		} catch (ExceptionFrontPagina e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (ExceptionFrontQssi e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
		
		

	}

	@Override
	public String setServicio() {

		return Microfront.RQSSI;
	}

}
