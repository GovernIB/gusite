package es.caib.gusite.micropersistence.delegate;

import javax.ejb.EJBException;

import es.caib.gusite.micropersistence.exception.UsuarioInexistenteException;

/**
 * Excepción producida en la capa delegate.
 * 
 * @author Indra
 */
public class DelegateException extends Exception {

	private static final long serialVersionUID = 8657421208586592043L;
	private SecurityException se = null;
	private UsuarioInexistenteException ue = null;
	private String key = null;

	public DelegateException(Throwable cause) {
		super(cause);

		while (cause != null) {
			if (cause instanceof SecurityException) {
				this.setSecurityException((SecurityException) cause);
				break;
			}else if(cause.getCause() != null && cause.getCause() instanceof UsuarioInexistenteException){
				ue = (UsuarioInexistenteException) cause.getCause();
				this.setUsuarioException(ue);
				this.setKey(ue.getMensaje()); //Mensaje personalizable
				break;
			}

			if (cause instanceof EJBException) {
				cause = ((EJBException) cause).getCausedByException();
			}
			else {
				cause = cause.getCause();
			}
		}
	}

	public boolean isSecurityException() {
		return (this.se != null);
	}

	public SecurityException getSecurityException() {
		return this.se;
	}

	public void setSecurityException(SecurityException se) {
		this.se = se;
	}

	public boolean isUsuarioException() {
		return (this.ue != null);
	}
	public UsuarioInexistenteException getUsuarioException() {
		return ue;
	}

	public void setUsuarioException(UsuarioInexistenteException ue) {
		this.ue = ue;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
