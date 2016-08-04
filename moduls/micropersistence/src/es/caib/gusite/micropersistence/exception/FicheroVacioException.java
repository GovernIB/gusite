package es.caib.gusite.micropersistence.exception;

import javax.ejb.EJBException;

/**
 * Excepción producida cuando se intenta guardar un fichero vacio.
 * 
 * @author Indra
 */
public class FicheroVacioException extends EJBException {

	private String mensaje;

	public FicheroVacioException(String paramString) {
		this.mensaje = paramString;
	}

	public String getMensaje()
	{
	    return this.mensaje;
	}

	public void setMensaje(String paramString)
	{
	    this.mensaje = paramString;
    }
}
