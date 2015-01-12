package org.ibit.rol.sac.microfront.exception;

/**
 *  Clase ExceptionFrontPagina se lanzar� cuando se genere una excepci�n en el front al cargar una p�gina.
 * @author Indra
 */
public class ExceptionFrontPagina extends ExceptionFront {

	private static final long serialVersionUID = -8884234445311075933L;

	public ExceptionFrontPagina () {};
	
	public ExceptionFrontPagina (String msg) {
		super(msg);
	}
	
}	