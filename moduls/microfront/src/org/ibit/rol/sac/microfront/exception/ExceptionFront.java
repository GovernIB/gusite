package org.ibit.rol.sac.microfront.exception;

/**
 *  Clase ExceptionFront se lanzar� cuando se genere una excepci�n en el front.
 * @author Indra
 */
public class ExceptionFront extends Exception {

	private static final long serialVersionUID = -1231866205241036271L;

	public ExceptionFront () {};
	
	public ExceptionFront (String msg) {
		super(msg);
	}

	
}