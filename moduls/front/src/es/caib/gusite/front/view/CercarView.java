package es.caib.gusite.front.view;

import es.caib.gusite.lucene.model.IndexResultados;

/**
 * Resultado de una busqueda realizada
 * @author at4.net
 *
 */
@TemplateView(TemplateView.CERCAR)
public class CercarView extends PageView {

	private IndexResultados listado;
	private String busqueda;

	public void setBusqueda(String cerca) {
		this.busqueda = cerca;
	}

	/**
	 * Búsqueda realizada
	 */
	@Variable("MVS_busquedaBuscador")
	public String getBusqueda() {
		return this.busqueda;
	}

	public void setListado(IndexResultados resultado) {
		this.listado = resultado;
	}

	/**
	 * Resultado de una busqueda realizada
	 */
	@Variable("MVS_listado_buscador")
	public IndexResultados getListado() {
		return this.listado;
	}

}
