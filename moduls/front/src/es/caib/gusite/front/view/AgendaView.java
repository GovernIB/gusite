package es.caib.gusite.front.view;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.caib.gusite.micromodel.Agenda;

/**
 * Página de agenda del microsite
 * @author at4.net
 *
 */
@TemplateView(TemplateView.AGENDA_LISTADO)
public class AgendaView extends ListPageView {

	private Collection<Agenda> agendaLista;
	private Map<String, Set<Integer>> datosAgendaCalendario;

	public void setAgendaLista(Collection<Agenda> resultados) {
		this.agendaLista = resultados;
	}

	/**
	 * Listado de eventos
	 */
	@Variable("MVS_agenda_lista")
	public Collection<Agenda> getAgendaLista() {
		return this.agendaLista;
	}

	public void setDatosAgendaCalendario(Map<String, Set<Integer>> datosCalendario) {

		this.datosAgendaCalendario = datosCalendario;

	}

	/**
	 * Datos del calendario de agenda. Cada entrada para cada clave el mes (en formato anyo + "-" + mes) el conjunto de días con eventos  
	 */
	@Variable("MVS_datos_agenda_calendario")
	public Map<String, Set<Integer>> getDatosAgendaCalendario() {
		return datosAgendaCalendario;
	}

}
