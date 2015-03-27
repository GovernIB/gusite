package es.caib.gusite.front.view;

import es.caib.gusite.micromodel.Contenido;

/**
 * Página de contenido
 * @author at4.net
 *
 */
@TemplateView(TemplateView.CONTENIDO)
public class ContenidoView extends PageView {

	private Contenido contenido;
	private String tipoBeta;

	public void setContenido(Contenido contenido) {
		this.contenido = contenido;
	}

	/**
	 * Contenido a mostrar
	 * @return
	 */
	@Variable("MVS_contenido")
	public Contenido getContenido() {
		return contenido;
	}

	public void setTipoBeta(String tipobeta) {
		this.tipoBeta = tipobeta;
	}

	/**
	 * Indica con el valor = "beta" que se ha de mostrar el contenido beta en lugar del final
	 * @return
	 */
	@Variable("MVS_tipobeta")
	public String getTipoBeta() {
		return tipoBeta;
	}

}
