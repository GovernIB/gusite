package es.caib.translatorib.api.v1.model;

import java.io.Serializable;

public class ParametrosTraduccionTexto extends ParametrosTraduccion implements Serializable {

	/** Serial version UID */
	private static final long serialVersionUID = 1L;

	/** Texto de entrada a traducir. **/
	private String textoEntrada;

	/** Tipo de entrada. **/
	private TipoEntrada tipoEntrada;

	/** Idioma de entrada. **/
	private Idioma idiomaEntrada;

	/** Idioma de salida. **/
	private Idioma idiomaSalida;

	/** Opciones **/
	private Opciones opciones;

	/**
	 * @return the textoEntrada
	 */
	public String getTextoEntrada() {
		return textoEntrada;
	}

	/**
	 * @param textoEntrada
	 *            the textoEntrada to set
	 */
	public void setTextoEntrada(final String textoEntrada) {
		this.textoEntrada = textoEntrada;
	}

	/**
	 * @return the tipoEntrada
	 */
	public TipoEntrada getTipoEntrada() {
		return tipoEntrada;
	}

	/**
	 * @param tipoEntrada
	 *            the tipoEntrada to set
	 */
	public void setTipoEntrada(final TipoEntrada tipoEntrada) {
		this.tipoEntrada = tipoEntrada;
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
