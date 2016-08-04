package es.caib.gusite.micropersistence.exception;

import javax.ejb.EJBException;

/**
 * Excepción producida cuando un usuario no tiene persimos o no existe.
 * 
 * @author Indra
 */
public class UsuarioInexistenteException extends EJBException {

	private String mensaje;

	public UsuarioInexistenteException(String paramString) {
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
