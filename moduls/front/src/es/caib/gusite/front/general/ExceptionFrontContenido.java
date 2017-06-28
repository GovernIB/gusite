package es.caib.gusite.front.general;

/**
 * Clase ExceptionFrontContenido se lanzará cuando se genere una excepción en el
 * front al cargar un contenido.
 * 
 * @author Indra
 */
public class ExceptionFrontContenido extends ExceptionFront {

	private static final long serialVersionUID = 6141005456421067869L;
	private static final String ERROR = "S' ha trobat un problema amb contingut ";
	
	public ExceptionFrontContenido() {
		super(ERROR);
	};
	
	public ExceptionFrontContenido(String uri, String id) {
		super(ERROR+" uri:"+uri+" id:"+id);
	};

	public ExceptionFrontContenido(String msg) {
		super(msg);
	}

	public ExceptionFrontContenido(Exception e) {
		super(e);
	}

	public ExceptionFrontContenido(String uri, Long idContenido) {
		super(ERROR+" uri:"+uri+" id:"+idContenido);
	}

}