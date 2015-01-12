package org.ibit.rol.sac.microfront.util.correo;

import java.io.InputStream;

/**
 * Interface para el envio de correos
 * @author Indra
 *
 */
public interface CorreoEngine {
	/**
	 * Método para inicializar un correo.
	 * @param subject
	 * @param isHTMLFormat
	 * @param body
	 */
	public void initCorreo(String subject, boolean isHTMLFormat, StringBuffer body);
	
	/**
	 * Método para inicializar un correo.
	 * @param destinatario
	 * @param subject
	 * @param isHTMLFormat
	 * @param body
	 */
	public void initCorreo(String destinatario, String subject, boolean isHTMLFormat, StringBuffer body);
	
	/**
	 * Método para añadir un destinatario.
	 * @param destinatario
	 */
	public void newDestinatario(String destinatario);
	
	/**
	 * Método para enviar un correo
	 * @return boolena Indica si el correo ha sido enviado correctamente.
	 */
	public boolean enviarCorreo();
	
}
