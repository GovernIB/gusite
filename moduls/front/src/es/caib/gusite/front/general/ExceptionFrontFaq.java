
package es.caib.gusite.front.general;

/**
 * Clase ExceptionFrontFaq se lanzará cuando se genere una excepción en el
 * front al cargar un faq.
 * 
 * @author Indra
 */
public class ExceptionFrontFaq extends ExceptionFront {

	private static final long serialVersionUID = 6141005456421067869L;
	private static final String ERROR = "S' ha trobat un problema amb contingut ";
	
	public ExceptionFrontFaq() {
		super(ERROR);
	};
	
	public ExceptionFrontFaq(String uri, String lang) {
		super(ERROR+" uri:"+uri+" lang:"+lang);
	};

	public ExceptionFrontFaq(String msg) {
		super(msg);
	}

	public ExceptionFrontFaq(Exception e) {
		super(e);
	}

}