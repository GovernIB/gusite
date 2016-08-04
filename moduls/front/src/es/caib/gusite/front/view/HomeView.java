package es.caib.gusite.front.view;

import java.util.List;
import java.util.Map;
import java.util.Set;

import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Noticia;

/**
 * Contenido de la HOME proporcionada por GUSITE
 * @author at4.net
 *
 */
@TemplateView(TemplateView.INDEX)
public class HomeView extends PageView {

	private String homeCampanya;
	private List<Noticia> datosNoticiasListado;
	private Map<String, Set<Integer>> datosAgendaCalendario;
	private List<Agenda> datosAgendaListado;

	public void setHomeCampanya(String homeCampanya) {

		this.homeCampanya = homeCampanya;

	}

	/**
	 * String preparado para insertar en un html, contiene el trozo de html que muestra la imagen de la campaña
	 */
	@Variable("MVS_home_campanya")
	public String getHomeCampanya() {

		return this.homeCampanya;

	}

	public void setDatosNoticiasListado(List<Noticia> listanoticias) {
		this.datosNoticiasListado = listanoticias;

	}

	/**
	 * Listado de noticias para la home
	 */
	@Variable("MVS_home_datos_noticias_listado")
	public List<Noticia> getDatosNoticiasListado() {
		return this.datosNoticiasListado;

	}

	public void setDatosAgendaCalendario(Map<String, Set<Integer>> datosCalendario) {

		this.datosAgendaCalendario = datosCalendario;

	}

	public void setDatosAgendaListado(List<Agenda> listadoAgenda) {

		this.datosAgendaListado = listadoAgenda;

	}

	/**
	 * Datos del calendario de agenda. Cada entrada para cada clave el mes (en formato anyo + "-" + mes) el conjunto de días con eventos  
	 */
	@Variable("MVS_datos_agenda_calendario")
	public Map<String, Set<Integer>> getDatosAgendaCalendario() {
		return datosAgendaCalendario;
	}

	/**
	 * Listado de eventos para la home 
	 */
	@Variable("MVS_home_datos_agenda_listado")
	public List<Agenda> getDatosAgendaListado() {
		return datosAgendaListado;
	}

}
