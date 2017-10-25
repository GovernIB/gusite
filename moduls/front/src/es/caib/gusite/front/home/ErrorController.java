package es.caib.gusite.front.home;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.caib.gusite.front.general.BaseViewController;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.view.ErrorGenericoView;
import es.caib.gusite.micromodel.Idioma;

/**
 * 
 * @author brujula-at4
 * 
 */
@Controller
public class ErrorController extends BaseViewController {

	private static Log log = LogFactory.getLog(HomeController.class);

	/**
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang:[a-zA-Z][a-zA-Z]}/errorrol")
	public ModelAndView mapa(@PathVariable("uri") SiteId URI, @PathVariable("lang") Idioma lang,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletResponse response) {

		ErrorGenericoView view = new ErrorGenericoView();
		try {
			super.configureLayoutView(URI.uri, lang, view, pcampa);
			ErrorMicrosite errorMicrosite = new ErrorMicrosite();
			errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_ACCES_TIT, ErrorMicrosite.ERROR_ACCES_MSG);
			view.setErrParam(errorMicrosite );
			return modelForView(this.templateNameFactory.errorGenerico(view.getMicrosite()), view);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
		}

	}


	@Override
	public String setServicio() {
		return null;
	}

}
