package es.caib.gusite.micromodel;


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
	/** Devuelve si es indexable o no. **/
	private boolean indexable = true;
	
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
	
	public boolean isIndexable() {
		return indexable;
	}

	public void setIndexable(boolean indexable) {
		this.indexable = indexable;
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

	public String getMensajeCorto() {
		String respuesta;
		if (this.mensaje == null) {
			respuesta = "<br />";
		} else {
			if (this.mensaje.indexOf("\n") == -1) {
				respuesta = this.mensaje.replace("\\","/")+"<br />";
			} else {
				respuesta = this.mensaje.substring(0, this.mensaje.indexOf("\n")).replace("\\","/")+"<br />"; 
			}
		}
		return respuesta;
	}
}
