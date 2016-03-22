package es.caib.gusite.front.view;

import java.util.List;

import es.caib.gusite.front.faq.Faqtema;

/**
 * Listado de preguntas frecuentes.
 * @author at4.net
 *
 */
@TemplateView(TemplateView.FAQ_LISTAR_FAQS)
public class ListarFaqsView extends PageView {

	private List<Faqtema> listado;
	private String idContenido;
	
	public void setListado(List<Faqtema> lista) {
		this.listado = lista;
	}

	/**
	 * Listado de temas faq
	 * @return
	 */
	@Variable("MVS_listado")
	public List<Faqtema> getListado() {
		return this.listado;
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
