package es.caib.gusite.plugins;

public class PluginException extends Exception {

	public PluginException(String message) {
		super(message);
	}

	public PluginException(String message, Exception e) {
		super(message, e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
