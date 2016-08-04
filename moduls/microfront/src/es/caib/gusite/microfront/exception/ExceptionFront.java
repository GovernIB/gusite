package es.caib.gusite.microfront.exception;

/**
 *  Clase ExceptionFront se lanzará cuando se genere una excepción en el front.
 * @author Indra
 */
public class ExceptionFront extends Exception {

	private static final long serialVersionUID = -1231866205241036271L;

	public ExceptionFront () {};
	
	public ExceptionFront (String msg) {
		super(msg);
	}

	
}