package es.caib.gusite.microintegracion.traductor;


public class TraductorException extends Exception {

    public TraductorException(String msg) {

        super(msg);
    }

	public TraductorException(String message, Throwable e) {
		super(message, e);
	}

}
