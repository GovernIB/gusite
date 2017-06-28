package es.caib.gusite.front.general;

/**
 * Clase ExceptionFrontContactos se lanzará cuando se genere una excepción en el
 * front al cargar una contacto.
 * 
 * @author Indra
 */
public class ExceptionFrontContactos extends ExceptionFront {

	private static final long serialVersionUID = 6141005456421067869L;
	private static final String ERROR = "S' ha trobat un problema amb contacte ";
	
	public ExceptionFrontContactos() {
		super(ERROR);
	};
	
	public ExceptionFrontContactos(String uri, long id) {
		super(ERROR+" uri:"+uri+" id:"+id);
	};
	
	
	public ExceptionFrontContactos(String msg) {
		super(msg);
	}

	public ExceptionFrontContactos(Exception e) {
		super(e);
	}

}