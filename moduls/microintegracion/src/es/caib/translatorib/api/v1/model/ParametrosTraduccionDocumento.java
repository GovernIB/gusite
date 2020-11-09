package es.caib.translatorib.api.v1.model;

import java.io.Serializable;

public class ParametrosTraduccionDocumento extends ParametrosTraduccion implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 1L;

	/** Contenido documento **/
	private String contenidoDocumento;

	/** Tipo de documento **/
	private TipoDocumento tipoDocumento;

	/** Idioma de entrada. **/
	private Idioma idiomaEntrada;

	/** Idioma de salida. **/
	private Idioma idiomaSalida;

	/** Opciones **/
	private Opciones opciones;

	/**
	 * @return the contenidoDocumento
	 */
	public String getContenidoDocumento() {
		return contenidoDocumento;
	}

	/**
	 * @param contenidoDocumento
	 *            the contenidoDocumento to set
	 */
	public void setContenidoDocumento(final String contenidoDocumento) {
		this.contenidoDocumento = contenidoDocumento;
	}

	/**
	 * @return the tipoDocumento
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento
	 *            the tipoDocumento to set
	 */
	public void setTipoDocumento(final TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @return the idiomaEntrada
	 */
	public Idioma getIdiomaEntrada() {
		return idiomaEntrada;
	}

	/**
	 * @param idiomaEntrada
	 *            the idiomaEntrada to set
	 */
	public void setIdiomaEntrada(final Idioma idiomaEntrada) {
		this.idiomaEntrada = idiomaEntrada;
	}

	/**
	 * @return the idiomaSalida
	 */
	public Idioma getIdiomaSalida() {
		return idiomaSalida;
	}

	/**
	 * @param idiomaSalida
	 *            the idiomaSalida to set
	 */
	public void setIdiomaSalida(final Idioma idiomaSalida) {
		this.idiomaSalida = idiomaSalida;
	}

	/**
	 * @return the opciones
	 */
	public Opciones getOpciones() {
		return opciones;
	}

	/**
	 * @param opciones
	 *            the opciones to set
	 */
	public void setOpciones(final Opciones opciones) {
		this.opciones = opciones;
	}

}
