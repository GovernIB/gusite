package es.caib.gusite.front.view;

import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Noticia;

@TemplateView(TemplateView.TAW_ITEM)
public class TawItemView extends PageView {

	private Contenido contenido;
	private Agenda agenda;
	private Noticia noticia;

	public void setContenido(Contenido contenido) {
		this.contenido = contenido;
	}

	@Variable("MVS_contenido")
	public Contenido getContenido() {
		return contenido;
	}

	public void setAgenda(Agenda agenda) {
		this.agenda = agenda;
	}

	@Variable("MVS_agenda")
	public Agenda getAgenda() {
		return this.agenda;
	}

	public void setNoticia(Noticia noticia) {
		this.noticia = noticia;
	}

	@Variable("MVS_noticia")
	public Noticia getNoticia() {
		return noticia;
	}

}
