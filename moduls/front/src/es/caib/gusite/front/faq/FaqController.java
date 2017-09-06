package es.caib.gusite.front.faq;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.caib.gusite.front.general.BaseViewController;
import es.caib.gusite.front.general.ExceptionFrontFaq;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.Front;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.general.bean.PathItem;
import es.caib.gusite.front.view.ListarFaqsView;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.DelegateException;

/**
 * 
 * @author brujula-at4
 * 
 */
@Controller
public class FaqController extends BaseViewController {

	private static Log log = LogFactory.getLog(FaqController.class);

	/**
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang:[a-zA-Z][a-zA-Z]}/faq")
	public ModelAndView listarfaqs(@PathVariable("uri") SiteId URI, @PathVariable("lang") Idioma lang,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletResponse response) {

		ListarFaqsView view = new ListarFaqsView();
		try {
			super.configureLayoutView(URI.uri, lang, view, pcampa);
			Microsite microsite = view.getMicrosite();
			if (microsite == null) {
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + URI);				
			}
			
			this.cargarFaq(view);
			this.cargarMollapan(view);
			view.setIdContenido(mcont);
			return this.modelForView(this.templateNameFactory.listarFaqs(microsite), view);

		} catch (DelegateException e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO, response,URI.uri,lang,null);
		} catch (ExceptionFrontFaq e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 

	}

	/**
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/faq")
	public ModelAndView listarfaqs(@PathVariable("uri") SiteId URI,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletResponse response) {
		// TODO: implementar negociación de idioma y, tal vez, redireccionar en
		// lugar de aceptar la uri.
		return this.listarfaqs(URI, DEFAULT_IDIOMA, pcampa, mcont, response);

	}

	/**
	 * Método privado para preparar la Noticia, en el caso que ese microsite
	 * tenga asignado ese componente
	 * 
	 * @param lang
	 * @param model
	 * @param microsite
	 * @throws DelegateException
	 * @throws ExceptionFrontFaq 
	 */
	private void cargarFaq(ListarFaqsView view) throws DelegateException, ExceptionFrontFaq {

		if (this.existeServicio(view.getMicrosite(), view.getLang(), Front.RFAQ)) {

			List<Faqtema> listafaqstema = this.dataService.listarFaqs(view.getMicrosite(), view.getLang());
			view.setListado(listafaqstema);

		} else {
			throw new ExceptionFrontFaq(view.getMicrosite().getUri(), view.getLang().getLang());		
		}
	}

	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por
	 * el microsite.
	 * 
	 * @param microsite
	 * @param model
	 * @param lang
	 * @return string recorrido en el microsite
	 */
	private void cargarMollapan(ListarFaqsView view) {

		List<PathItem> path = super.getBasePath(view);
		path.add(new PathItem(this.getMessage("listarfaqs.preguntas", view.getLang())));

		// Datos para la plantilla
		view.setPathData(path);
	}

	@Override
	public String setServicio() {
		return Microfront.RFAQ;
	}

}
