package org.ibit.rol.sac.microfront.exception;

/**
 *  Clase ExceptionFrontMicro se lanzar� cuando se genere una excepci�n en el front al cargar un Microsite.
 * @author Indra
 */
public class ExceptionFrontMicro extends ExceptionFront {

	private static final long serialVersionUID = 6141005456421067868L;

	public ExceptionFrontMicro () {};
	
	public ExceptionFrontMicro (String msg) {
		super(msg);
	}
	
}	