package es.caib.gusite.micropersistence.util;

import es.caib.gusite.micromodel.ValueObject;

/**
 * Contiene el resultado de las pendientes.
 */
public class SolrPendienteResultado implements ValueObject {
	
	/** Serial version UID. **/
	private static final long serialVersionUID = 1L;
	/** Devuelve si está correcto o no el resultado. **/
	private boolean correcto;
	/** Devuelve el mensaje de error en caso de ejecución errónea. **/
	private String 	mensaje;
	
	/** 
	 * Constructor 
	 * 
	 * @param iCorrecto
	 */
	public SolrPendienteResultado(final boolean iCorrecto) {
		this.correcto = iCorrecto;
	}
	
	/** 
	 * Constructor. 
	 * @param iCorrecto
	 * @param iMensaje
	 */
	public SolrPendienteResultado(final boolean iCorrecto, final String iMensaje) {
		this.correcto = iCorrecto;
		this.mensaje = iMensaje;
	}
	
	/**
	 * @return the correcto
	 */
	public final boolean isCorrecto() {
		return correcto;
	}
	/**
	 * @param correcto the correcto to set
	 */
	public final void setCorrecto(final boolean correcto) {
		this.correcto = correcto;
	}
	/**
	 * @return the mensaje
	 */
	public final String getMensaje() {
		return mensaje;
	}
	/**
	 * @param mensaje the mensaje to set
	 */
	public final void setMensaje(final String mensaje) {
		this.mensaje = mensaje;
	}
	
	@Override
	public String toString() {
		final StringBuffer texto = new StringBuffer();
		texto.append("SolrPendienteResultado correcto:");
		texto.append(correcto);
		texto.append(" mensaje:");
		texto.append(mensaje);
		return texto.toString();
	}
}
