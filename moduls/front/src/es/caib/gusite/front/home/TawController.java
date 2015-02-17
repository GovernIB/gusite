package es.caib.gusite.front.home;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import es.caib.gusite.front.microtag.MicrositeParser;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;

@Controller
public class TawController extends BaseController {

	private static Log log = LogFactory.getLog(TawController.class);

	@Autowired
	private MicrositeParser microparser;

	/**
	 * 
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/{lang}/tawitem/contenido/{contenido}")
	public String tawContenido(
			@PathVariable("uri") SiteId URI,
			@PathVariable("lang") Idioma lang,
			@PathVariable("contenido") long idContenido,
			Model model,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			HttpServletRequest req) {

		Microsite microsite = null;
		try {
			microsite = super.loadMicrosite(URI.uri, lang, model, pcampa);
			ContenidoDelegate bdCon = DelegateUtil.getContenidoDelegate();
			Contenido conte = bdCon.obtenerContenido(idContenido);
			conte.setIdi(lang.getLang());

			// ******************** Para evitar los tags de los componentes.
			// ******************************** //
			TraduccionContenido tracon = (TraduccionContenido) conte
					.getTraduccion(lang.getLang());
			if ((tracon != null) && (tracon.getTexto() != null)
					&& (tracon.getTexto().length() > 0)) {
				String htmlatestear = "";
				htmlatestear = tracon.getTexto();
				String html2analizar = this.microparser.doParser2Comentario(
						microsite, htmlatestear, lang.getLang());
				tracon.setTexto(html2analizar);
			}
			model.addAttribute("MVS_contenido", conte);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_MICRO);
		} catch (DelegateException e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_MICRO);
		}
		return this.templateNameFactory.tawItem(microsite);
	}

	/**
	 * 
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/{lang}/tawitem/agenda/{agenda}")
	public String tawAgenda(
			@PathVariable("uri") SiteId URI,
			@PathVariable("lang") Idioma lang,
			@PathVariable("agenda") long idAgenda,
			Model model,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			HttpServletRequest req) {

		Microsite microsite = null;
		try {
			microsite = super.loadMicrosite(URI.uri, lang, model, pcampa);
			AgendaDelegate bdAge = DelegateUtil.getAgendaDelegate();
			Agenda agenda = bdAge.obtenerAgenda(idAgenda);
			agenda.setIdi(lang.getLang());
			model.addAttribute("MVS_agenda", agenda);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_MICRO);
		} catch (DelegateException e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_MICRO);
		}
		return this.templateNameFactory.tawItem(microsite);
	}

	/**
	 * 
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/{lang}/tawitem/noticia/{noticia}")
	public String tawNoticia(
			@PathVariable("uri") SiteId URI,
			@PathVariable("lang") Idioma lang,
			@PathVariable("noticia") long idNoticia,
			Model model,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			HttpServletRequest req) {

		Microsite microsite = null;
		try {
			microsite = super.loadMicrosite(URI.uri, lang, model, pcampa);
			NoticiaDelegate bdNot = DelegateUtil.getNoticiasDelegate();
			Noticia noticia = bdNot.obtenerNoticia(idNoticia);
			noticia.setIdi(lang.getLang());
			noticia.getTipo().setIdi(lang.getLang());
			model.addAttribute("MVS_noticia", noticia);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_MICRO);
		} catch (DelegateException e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_MICRO);
		}
		return this.templateNameFactory.tawItem(microsite);
	}

	@Override
	public String setServicio() {
		return null;
	}
}
