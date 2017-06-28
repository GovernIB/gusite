package es.caib.gusite.front.general;

/**
 * Clase ExceptionFrontContactos se lanzará cuando se genere una excepción en el
 * front al cargar un contenido.
 * 
 * @author Indra
 */
public class ExceptionFrontNoticia extends ExceptionFront {

	private static final long serialVersionUID = 6141005456670675569L;
	private static final String ERROR = "S' ha trobat un problema amb noticia ";
	
	public ExceptionFrontNoticia() {
		super(ERROR);
	};
	
	public ExceptionFrontNoticia(String uri, String id) {
		super(ERROR+" uri:"+uri+" id:"+id);
	};

	public ExceptionFrontNoticia(String msg) {
		super(msg);
	}

	public ExceptionFrontNoticia(Exception e) {
		super(e);
	}

	
}