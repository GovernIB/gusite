package es.caib.gusite.front.home;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.caib.gusite.front.general.BaseViewController;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.view.PageView;
import es.caib.gusite.micromodel.Idioma;

@Controller
public class PreviewController extends BaseViewController {

	private static Log log = LogFactory.getLog(PreviewController.class);

	/**
	 * 
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/{lang:[a-zA-Z][a-zA-Z]}/menupreview/")
	public ModelAndView menuPreview(@PathVariable("uri") SiteId URI, @PathVariable("lang") Idioma lang, Model model,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletRequest req, HttpServletResponse response) {

		PageView view = new PageView();
		try {
			super.configureLayoutView(URI.uri, lang, view, pcampa);
			return this.modelForView(this.templateNameFactory.menuPreview(view.getMicrosite()), view);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO, response,URI.uri,lang,req);
		}
	}

	@Override
	public String setServicio() {
		return null;
	}
}
