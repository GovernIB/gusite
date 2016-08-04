package es.caib.gusite.front.view;

import java.util.List;

import es.caib.gusite.front.general.bean.PathItem;

/**
 * Página genérica
 * @author agarcia
 *
 */
public class PageView extends LayoutView {

	private List<PathItem> pathData;
	private String idContenido;
	
	public void setPathData(List<PathItem> path) {
		this.pathData = path;

	}

	@Variable("MVS2_pathdata")
	public List<PathItem> getPathData() {
		return this.pathData;

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
