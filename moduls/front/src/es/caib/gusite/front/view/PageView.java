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

	public void setPathData(List<PathItem> path) {
		this.pathData = path;

	}

	@Variable("MVS2_pathdata")
	public List<PathItem> getPathData() {
		return this.pathData;

	}
}
