package es.caib.gusite.front.agenda;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.caib.gusite.front.general.BaseController;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.general.bean.PathItem;
import es.caib.gusite.front.general.bean.ResultadoBusqueda;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.DelegateException;

/**
 * 
 * @author brujula-at4
 * 
 */
@Controller
public class AgendaController extends BaseController {

	private static Log log = LogFactory.getLog(AgendaController.class);

	/**
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * 
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang}/agenda/{fecha}")
	public String agenda(
			@PathVariable("uri") SiteId URI,
			@PathVariable("lang") Idioma lang,
			@PathVariable("fecha") @DateTimeFormat(pattern = "yyyyMMdd") Date fecha,
			Model model,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = "pagina", required = false, defaultValue = "1") int pagina,
			HttpServletRequest req) {

		Microsite microsite = null;
		try {

			microsite = super.loadMicrosite(URI.uri, lang, model, pcampa);
			ResultadoBusqueda<Agenda> eventos = this.dataService
					.getListadoAgenda(microsite, lang, fecha, pagina);

			SimpleDateFormat agendaFechaEvento = new SimpleDateFormat(
					"dd/MM/yyyy");

			model.addAttribute("MVS_agenda_lista", eventos.getResultados());
			model.addAttribute("MVS_agenda_diaevento",
					agendaFechaEvento.format(fecha));
			model.addAttribute("MVS_seulet_sin", this.urlFactory
					.listarAgendaFechaSinPagina(microsite, lang, fecha, mcont,
							pcampa));
			model.addAttribute("MVS_parametros_pagina", eventos.getParametros());

			this.cargarMollapan(microsite, fecha, model, lang);

			return this.templateNameFactory.listarAgendaFecha(microsite);

		} catch (DelegateException e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_PAGINA);
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
	@RequestMapping("{uri}/{lang}/agenda/")
	public String agenda(
			@PathVariable("uri") SiteId URI,
			@PathVariable("lang") Idioma lang,
			Model model,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = "filtro", required = false, defaultValue = "") String filtro,
			@RequestParam(value = "pagina", required = false, defaultValue = "1") int pagina,
			@RequestParam(value = "ordenacion", required = false, defaultValue = "") String ordenacion) {

		Microsite microsite = null;
		try {

			microsite = super.loadMicrosite(URI.uri, lang, model, pcampa);
			ResultadoBusqueda<Agenda> eventos = this.dataService
					.getListadoAgenda(microsite, lang, new AgendaCriteria(
							filtro, pagina, ordenacion));

			model.addAttribute("MVS_seulet_sin", this.urlFactory
					.listarAgendaSinPagina(microsite, lang, mcont, pcampa));
			model.addAttribute("MVS_parametros_pagina", eventos.getParametros());
			model.addAttribute("MVS_datos_agenda_calendario",
					this.dataService.getDatosCalendario(microsite, lang));
			model.addAttribute("MVS_agenda_lista", eventos.getResultados());

			this.cargarMollapan(microsite, model, lang);

			return this.templateNameFactory.listarAgenda(microsite);

		} catch (DelegateException e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_PAGINA);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_MICRO);
		}

	}

	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por
	 * el microsite.
	 * 
	 * @param microsite
	 * @param model
	 * @param lang
	 * @return
	 * @return string recorrido en el microsite
	 */
	private List<PathItem> cargarMollapan(Microsite microsite, Model model,
			Idioma lang) {

		List<PathItem> path = super.getBasePath(microsite, model, lang);

		path.add(new PathItem(this.getMessage("agenda.agenda", lang),
				this.urlFactory.listarAgenda(microsite, lang)));

		// TODO: eliminar cuando se actualicen las plantillas
		model.addAttribute("MVS2_mollapan", this.mollapan(path));

		// Datos para la plantilla
		model.addAttribute("MVS2_pathdata", path);

		return path;

	}

	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por
	 * el microsite.
	 * 
	 * @param microsite
	 * @param model
	 * @param lang
	 * @return
	 * @return string recorrido en el microsite
	 */
	private List<PathItem> cargarMollapan(Microsite microsite, Date fecha,
			Model model, Idioma lang) {

		List<PathItem> path = this.cargarMollapan(microsite, model, lang);
		path.add(new PathItem(this.getMessage("agenda.evento", lang)));

		// TODO: eliminar cuando se actualicen las plantillas
		model.addAttribute("MVS2_mollapan", this.mollapan(path));

		// Datos para la plantilla
		model.addAttribute("MVS2_pathdata", path);

		return path;

	}

	@Override
	public String setServicio() {

		return Microfront.RAGENDA;
	}

}
