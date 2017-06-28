package es.caib.gusite.front.general;

/**
 * Clase ExceptionFrontQssi se lanzará cuando se genere una excepción en el
 * front al cargar un qssi.
 * 
 * @author Indra
 */
public class ExceptionFrontQssi extends ExceptionFront {

	private static final long serialVersionUID = 6141005456421075569L;
	private static final String ERROR = "S' ha trobat un problema amb qssi ";
	
	public ExceptionFrontQssi() {
		super(ERROR);
	};
	
	public ExceptionFrontQssi(String uri, long id) {
		super(ERROR+" uri:"+uri+" id:"+id);
	};

	public ExceptionFrontQssi(String msg) {
		super(msg);
	}

	public ExceptionFrontQssi(Exception e) {
		super(e);
	}

}