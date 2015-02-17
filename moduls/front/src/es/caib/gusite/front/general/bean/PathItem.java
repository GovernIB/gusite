package es.caib.gusite.front.general.bean;

import java.io.Serializable;

/**
 * Bean para pasar el valor de los items de la miga de pan
 * 
 * @author brujula-at4
 * 
 */
public class PathItem implements Serializable {

	private static final long serialVersionUID = 7567193458667662802L;
	private String label;
	private String href;

	public PathItem() {
	}

	public PathItem(String label, String href) {
		this.setLabel(label);
		this.setHref(href);
	}

	public PathItem(String label) {
		this.setLabel(label);
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getHref() {
		return this.href;
	}

	public boolean hasHref() {
		return this.href != null && !this.href.equals("");
	}

}
