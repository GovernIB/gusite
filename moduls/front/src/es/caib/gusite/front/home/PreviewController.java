package es.caib.gusite.front.home;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.caib.gusite.front.general.BaseController;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;

@Controller
public class PreviewController extends BaseController {

	private static Log log = LogFactory.getLog(PreviewController.class);

	/**
	 * 
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/{lang}/menupreview/")
	public String menuPreview(
			@PathVariable("uri") SiteId URI,
			@PathVariable("lang") Idioma lang,
			Model model,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			HttpServletRequest req) {
		Microsite microsite = null;

		try {

			microsite = super.loadMicrosite(URI.uri, lang, model, pcampa);
			return this.templateNameFactory.menuPreview(microsite);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_MICRO);
		}
	}

	@Override
	public String setServicio() {
		return null;
	}
}
