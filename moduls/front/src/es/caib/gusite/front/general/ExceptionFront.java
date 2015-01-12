package es.caib.gusite.front.general;


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

	public ExceptionFront(Exception e) {
		super(e);
	}

	public ExceptionFront(String string, Exception ne) {
		super(string, ne);
	}

	
}