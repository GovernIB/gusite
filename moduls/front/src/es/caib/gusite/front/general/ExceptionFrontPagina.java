package es.caib.gusite.front.general;


/**
 *  Clase ExceptionFrontPagina se lanzará cuando se genere una excepción en el front al cargar una página.
 * @author Indra
 */
public class ExceptionFrontPagina extends ExceptionFront {

	private static final long serialVersionUID = -8884234445311075933L;

	public ExceptionFrontPagina () {};
	
	public ExceptionFrontPagina (String msg) {
		super(msg);
	}

	public ExceptionFrontPagina(Exception e) {
		super(e);
	}

	public ExceptionFrontPagina(String message, Exception ne) {
		super(message, ne);
	}
	
}	