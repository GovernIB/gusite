package es.caib.gusite.front.general;

/**
 * Clase ExceptionFrontPagina se lanzará cuando se genere una excepción en el
 * front al cargar una página.
 *
 * @author Indra
 */
public class ExceptionFrontPaginaDoc extends ExceptionFront {

	private static final long serialVersionUID = -8884234445311075933L;
	public static final int HTTP_NOT_FOUND = 404;
	private int httpCode = 0;

	public ExceptionFrontPaginaDoc() {
	};

	public ExceptionFrontPaginaDoc(String msg) {
		super(msg);
	}

	public ExceptionFrontPaginaDoc(Exception e) {
		super(e);
	}

	public ExceptionFrontPaginaDoc(String message, Exception ne) {
		super(message, ne);
	}

	public ExceptionFrontPaginaDoc(String msg, int httpCode) {
		super(msg);
		this.setHttpCode(httpCode);
	}

	public ExceptionFrontPaginaDoc(String message, Exception ne, int httpCode) {
		super(message, ne);
		this.setHttpCode(httpCode);
	}

	public void setHttpCode(int httpCode) {
		this.httpCode = httpCode;
	}

	public int getHttpCode() {
		return this.httpCode;
	}

}