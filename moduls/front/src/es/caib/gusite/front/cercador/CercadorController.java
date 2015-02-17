package es.caib.gusite.front.cercador;

import java.util.List;

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
import es.caib.gusite.front.general.bean.PathItem;
import es.caib.gusite.lucene.model.IndexResultados;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IndexerDelegate;

@Controller
public class CercadorController extends BaseController {

	private static Log log = LogFactory.getLog(CercadorController.class);

	/**
	 * 
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{uri}/{lang}/search/")
	public String cercar(
			@PathVariable("uri") SiteId URI,
			@PathVariable("lang") Idioma lang,
			Model model,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			HttpServletRequest req) {

		Microsite microsite = null;
		try {
			microsite = super.loadMicrosite(URI.uri, lang, model, pcampa);

			// metodo buscar(); de Bdcercador.java
			IndexResultados resultado;

			try {

				IndexerDelegate indexo = DelegateUtil.getIndexerDelegate();
				String words = "" + req.getParameter("cerca");
				String idi = "" + req.getSession().getAttribute("MVS_idioma");

				model.addAttribute("MVS_busquedaBuscador", words);

				resultado = indexo.buscar("" + microsite.getId().longValue(),
						idi, null, words, true);

			} catch (Exception e) {
				log.error("Error en la busqueda: " + e);
				return this.getForwardError(microsite, lang, model,
						ErrorMicrosite.ERROR_AMBIT_ACCES);

			}

			// hasta aqui metodo buscar();

			model.addAttribute("MVS_listado_buscador", resultado);
			this.cargarMollapan(microsite, model, lang);
			return this.templateNameFactory.cercar(microsite);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_MICRO);
		}

	}

	/**
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * 
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{uri}/buscar/")
	public String cercarEs(
			@PathVariable("uri") SiteId URI,
			Model model,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			HttpServletRequest req) {

		return this.cercar(URI, new Idioma(LANG_ES), model, mcont, pcampa, req);
	}

	/**
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * 
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{uri}/cercar/")
	public String cercarCa(
			@PathVariable("uri") SiteId URI,
			Model model,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			HttpServletRequest req) {

		return this.cercar(URI, new Idioma(LANG_CA), model, mcont, pcampa, req);
	}

	/**
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * 
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{uri}/search/")
	public String cercarEn(
			@PathVariable("uri") SiteId URI,
			Model model,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			HttpServletRequest req) {

		return this.cercar(URI, new Idioma(LANG_EN), model, mcont, pcampa, req);
	}

	/**
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * 
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/{lang}/search/")
	public String cercarGet(
			@PathVariable("uri") SiteId URI,
			@PathVariable("lang") Idioma lang,
			Model model,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			HttpServletRequest req) {

		return this.cercar(URI, lang, model, mcont, pcampa, req);

	}

	/**
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * 
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/buscar/")
	public String cercarEsGet(
			@PathVariable("uri") SiteId URI,
			Model model,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			HttpServletRequest req) {

		return this.cercarGet(URI, new Idioma(LANG_ES), model, mcont, pcampa,
				req);
	}

	/**
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * 
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/cercar/")
	public String cercarCaGet(
			@PathVariable("uri") SiteId URI,
			Model model,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			HttpServletRequest req) {

		return this.cercarGet(URI, new Idioma(LANG_CA), model, mcont, pcampa,
				req);
	}

	/**
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * 
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/search/")
	public String cercarEnGet(
			@PathVariable("uri") SiteId URI,
			Model model,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			HttpServletRequest req) {

		return this.cercarGet(URI, new Idioma(LANG_EN), model, mcont, pcampa,
				req);
	}

	@Override
	public String setServicio() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por
	 * el microsite.
	 * 
	 * @param microsite
	 * @param model
	 * @param lang
	 */

	private void cargarMollapan(Microsite microsite, Model model, Idioma lang) {

		List<PathItem> path = super.getBasePath(microsite, model, lang);

		/*
		 * original: path.add(new PathItem(getMessage("contacto.formulario",
		 * lang), this.urlFactory.contacto(microsite, lang, contacto)));
		 * 
		 * Mejor usar el título del formulario:
		 */

		String titulo = this.getMessage("cercar.resultados", lang);

		path.add(new PathItem(titulo, this.urlFactory.cercar(microsite, lang)));

		// Datos para la plantilla
		model.addAttribute("MVS2_pathdata", path);

	}

}
