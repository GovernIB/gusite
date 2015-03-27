package es.caib.gusite.front.view;

import java.util.Collection;

import es.caib.gusite.micromodel.Agenda;

/**
 * Listado de eventos de la agenda para una fecha
 * @author at4.net
 *
 */
@TemplateView(TemplateView.AGENDA_FECHA)
public class AgendaFechaView extends ListPageView {

	private Collection<Agenda> agendaLista;
	private String agendaDiaevento;

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

	public void setAgendaDiaevento(String dia) {
		this.agendaDiaevento = dia;
	}

	/**
	 * Representación en texto del día que se está listando
	 */
	@Variable("MVS_agenda_diaevento")
	public String getAgendaDiaevento() {
		return this.agendaDiaevento;
	}

}
