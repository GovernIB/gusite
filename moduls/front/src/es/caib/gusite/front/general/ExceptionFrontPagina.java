package es.caib.gusite.front.general;


/**
 *  Clase ExceptionFrontPagina se lanzará cuando se genere una excepción en el front al cargar una página.
 * @author Indra
 */
public class ExceptionFrontPagina extends ExceptionFront {

	private static final long serialVersionUID = -8884234445311075933L;
	public static final int HTTP_NOT_FOUND = 404;
	private int httpCode = 0;

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

	public ExceptionFrontPagina (String msg, int httpCode) {
		super(msg);
		this.setHttpCode(httpCode);
	}

	public ExceptionFrontPagina(String message, Exception ne, int httpCode) {
		super(message, ne);
		this.setHttpCode(httpCode);
	}
	
	public void setHttpCode(int httpCode) {
		this.httpCode = httpCode;
	}

	public int getHttpCode() {
		return httpCode;
	}
	
}	