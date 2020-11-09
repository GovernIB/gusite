package es.caib.translatorib.api.v1.model;

import java.io.Serializable;

/***
 * Enumerado
 *
 * @author Indra
 *
 */
public enum Idioma implements Serializable {
	CASTELLANO("es", "es"), CASTELLANO_ESPANYA("es", "es_ES"), CATALAN("ca", "ca"), CATALAN_CATALUNYA("ca", "ca_ES"),
	CATALAN_BALEAR("ca", "ca_ES");

	private String idioma;
	private String locale;

	/** Constructor. **/
	Idioma(final String iIdioma, final String ivalor) {
		this.idioma = iIdioma;
		this.locale = ivalor;
	}

	public String getIdioma() {
		return this.idioma;
	}

	public String getLocale() {
		return this.locale;
	}

	@Override
	public String toString() {
		return this.idioma + " " + this.locale;
	}
}
