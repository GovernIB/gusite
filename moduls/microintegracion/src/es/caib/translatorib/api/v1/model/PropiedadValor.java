package es.caib.translatorib.api.v1.model;

import java.io.Serializable;

public class PropiedadValor implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 1L;

	/** Campos **/
	private String propiedad;
	private String valor;

	/** Constructor **/
	public PropiedadValor() {
		// Vacio
	}

	/** Constructor **/
	public PropiedadValor(final String iPropiedad, final String iValor) {
		this.propiedad = iPropiedad;
		this.valor = iValor;
	}

	public String getPropiedad() {
		return propiedad;
	}

	public void setPropiedad(final String propiedad) {
		this.propiedad = propiedad;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(final String valor) {
		this.valor = valor;
	}

}
