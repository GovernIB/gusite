package es.caib.gusite.front.view;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

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
	private String idContenido;

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
	
	@Variable("MVS_agenda_diaevento_date")
	public Date getAgendaDiaeventoDate() {
		final Calendar calendar = Calendar.getInstance();
		final String[] campos = this.agendaDiaevento.split("/");
		calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(campos[0]));
		calendar.set(Calendar.MONTH, Integer.valueOf(campos[1]) - 1 );
		calendar.set(Calendar.YEAR, Integer.valueOf(campos[2]));
		
		return calendar.getTime();
	}
	
	public void setIdContenido(String idContenido) {
		this.idContenido = idContenido;
	}
	/**
	 * Identificación del id del contenido.
	 * @return
	 */
	@Variable("MVS_idContenido")
	public String getIdContenido() {
		return idContenido;
	}

}
