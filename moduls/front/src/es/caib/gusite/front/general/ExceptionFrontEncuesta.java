package es.caib.gusite.front.general;

/**
 * Clase ExceptionFrontContactos se lanzará cuando se genere una excepción en el
 * front al cargar un contenido.
 * 
 * @author Indra
 */
public class ExceptionFrontEncuesta extends ExceptionFront {

	private static final long serialVersionUID = 6141005456421075569L;
	private static final String ERROR = "S' ha trobat un problema amb encuesta ";
	
	public ExceptionFrontEncuesta() {
		super(ERROR);
	};
	
	public ExceptionFrontEncuesta(String uri, String id) {
		super(ERROR+" uri:"+uri+" id:"+id);
	};

	public ExceptionFrontEncuesta(String msg) {
		super(msg);
	}

	public ExceptionFrontEncuesta(Exception e) {
		super(e);
	}

}