package es.caib.gusite.front.view;

import java.util.List;

import es.caib.gusite.front.general.bean.Pardato;

/**
 * Muestra la lista de formularios de contacto disponibles.
 * @author at4.net
 *
 */
@TemplateView(TemplateView.CONTACTO_LISTAR_CONTACTOS)
public class ListarContactosView extends ListPageView {

	private List<Pardato> listado;

	public void setListado(List<Pardato> lista) {
		this.listado = lista;
	}

	/**
	 * Listado de formularios, donde cada elemento es un Pardato(titulo, url)
	 */
	@Variable("MVS_listado")
	public List<Pardato> getListado() {
		return this.listado;
	}

}
