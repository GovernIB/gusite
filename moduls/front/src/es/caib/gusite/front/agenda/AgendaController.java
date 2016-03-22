package es.caib.gusite.front.agenda;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.caib.gusite.front.general.BaseViewController;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.general.bean.PathItem;
import es.caib.gusite.front.general.bean.ResultadoBusqueda;
import es.caib.gusite.front.view.AgendaFechaView;
import es.caib.gusite.front.view.AgendaView;
import es.caib.gusite.front.view.PageView;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.DelegateException;

/**
 * Gestiona las peticiones de páginas de agenda
 * 
 * @author brujula-at4
 * 
 */
@Controller
public class AgendaController extends BaseViewController {

	private static Log log = LogFactory.getLog(AgendaController.class);

	/**
	 * Eventos de la agenda para una fecha determinada
	 * 
	 * @param uri
	 *            Uri de microsite
	 * @param lang
	 *            Idioma de la petición
	 * @param fecha
	 *            en formato yyyyMMdd
	 * @param model
	 * @return
	 */
	@RequestMapping("{uri}/{lang:[a-zA-Z][a-zA-Z]}/agenda/{fecha}")
	public ModelAndView agenda(@PathVariable("uri") SiteId URI, @PathVariable("lang") Idioma lang,
			@PathVariable("fecha") @DateTimeFormat(pattern = "yyyyMMdd") Date fecha,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = "pagina", required = false, defaultValue = "1") int pagina, HttpServletRequest req) {

		AgendaFechaView view = new AgendaFechaView();
		try {

			super.configureLayoutView(URI.uri, lang, view, pcampa);
			Microsite microsite = view.getMicrosite();

			ResultadoBusqueda<Agenda> eventos = this.dataService.getListadoAgenda(microsite, lang, fecha, pagina);

			SimpleDateFormat agendaFechaEvento = new SimpleDateFormat("dd/MM/yyyy");

			view.setAgendaLista(eventos.getResultados());
			view.setAgendaDiaevento(agendaFechaEvento.format(fecha));
			view.setSeuletSin(this.urlFactory.listarAgendaFechaSinPagina(microsite, lang, fecha, mcont, pcampa));
			view.setParametrosPagina(eventos.getParametros());
			view.setIdContenido(mcont);
			this.cargarMollapan(view, fecha);

			return this.modelForView(this.templateNameFactory.listarAgendaFecha(microsite), view);

		} catch (DelegateException e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO);
		}

	}

	/**
	 * Página de agenda de microsite
	 * 
	 * @param uri
	 *            Uri de microsite
	 * @param lang
	 *            Idioma de la petición
	 * @return
	 */
	@RequestMapping("{uri}/{lang:[a-zA-Z][a-zA-Z]}/agenda/")
	public ModelAndView agenda(@PathVariable("uri") SiteId URI, @PathVariable("lang") Idioma lang,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = "filtro", required = false, defaultValue = "") String filtro,
			@RequestParam(value = "pagina", required = false, defaultValue = "1") int pagina,
			@RequestParam(value = "ordenacion", required = false, defaultValue = "") String ordenacion) {

		AgendaView view = new AgendaView();
		try {

			super.configureLayoutView(URI.uri, lang, view, pcampa);
			Microsite microsite = view.getMicrosite();

			ResultadoBusqueda<Agenda> eventos = this.dataService.getListadoAgenda(microsite, lang, new AgendaCriteria(filtro, pagina, ordenacion));

			view.setSeuletSin(this.urlFactory.listarAgendaSinPagina(microsite, lang, mcont, pcampa));
			view.setParametrosPagina(eventos.getParametros());
			view.setAgendaLista(eventos.getResultados());
			view.setDatosAgendaCalendario(this.dataService.getDatosCalendario(microsite, lang));

			this.cargarMollapan(view);

			return this.modelForView(this.templateNameFactory.listarAgenda(microsite), view);

		} catch (DelegateException e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO);
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
	private List<PathItem> cargarMollapan(PageView view) {

		List<PathItem> path = super.getBasePath(view);

		path.add(new PathItem(this.getMessage("agenda.agenda", view.getLang()), this.urlFactory.listarAgenda(view.getMicrosite(), view.getLang())));

		// Datos para la plantilla
		view.setPathData(path);

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
	private List<PathItem> cargarMollapan(PageView view, Date fecha) {

		List<PathItem> path = this.cargarMollapan(view);
		path.add(new PathItem(this.getMessage("agenda.evento", view.getLang())));

		// Datos para la plantilla
		view.setPathData(path);

		return path;

	}

	@Override
	public String setServicio() {

		return Microfront.RAGENDA;
	}

}
