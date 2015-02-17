package es.caib.gusite.micropersistence.util.log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

/**
 * Clase que guarda y visualiza el log de la aplicación
 * 
 * @author Indra
 * 
 */
public class MicroLog {

	private static ArrayList<String> _logs = new ArrayList<String>();
	private static int _maxlogs = 1000;// numero maximo de logs a guardar
	private static int _maxstacktrace = 6;
	private static String MVS_importprocessor = "MVS_importprocessor";
	private static String _formatofecha = "dd/MM/yyyy HH:mm:ss";

	/**
	 * Método que guarda en el Microlog
	 * 
	 * @param mensaje
	 */
	public static void addLog(String mensaje) {
		String stlog = "[" + fechaFormateada() + "] " + mensaje;
		if (_logs.size() > _maxlogs) {
			_logs.remove(0);
		}
		_logs.add(stlog);
	}

	/**
	 * Método que guarda en el Microlog un mensaje y un stacktrace de alguna
	 * excepcion
	 * 
	 * @param mensaje
	 * @param mensajes
	 */
	public static void addLogStackTrace(String mensaje,
			StackTraceElement[] mensajes) {
		String stlog = mensaje + "\n STACK TRACE = ";
		int mensmostrados = (mensajes.length < _maxstacktrace) ? mensajes.length
				: 6;
		for (int x = 0; x <= mensmostrados; x++) {
			stlog += mensajes[x].getClassName() + " ("
					+ mensajes[x].getMethodName() + " >> linea:"
					+ mensajes[x].getLineNumber() + ")" + "\n";
		}
		if (mensajes.length >= _maxstacktrace) {
			stlog += " (mas) ...\n ";
		}
		addLog(stlog);
	}

	/**
	 * Método que guarda en el Microlog. Adicionalmente pone el mensaje en una
	 * variable de sesion.
	 * 
	 * @param request
	 * @param mensaje
	 */
	public static void addLogVisual(HttpServletRequest request, String mensaje) {

		String st = ""
				+ (String) request.getSession().getAttribute(
						MVS_importprocessor);
		if (st.equals("null")) {
			st = "";
		}
		st += "[ " + fechaFormateada() + " ]  " + mensaje;
		String stlog = mensaje;
		addLog(stlog);
		request.getSession().setAttribute(MVS_importprocessor, st + "<br/>");
	}

	/**
	 * Método que guarda en el Microlog un mensaje y un stacktrace de alguna
	 * excepcion Adicionalmente pone el mensaje y el stacktrace en una variable
	 * de sesion.
	 * 
	 * @param request
	 * @param mensaje
	 * @param mensajes
	 */
	public static void addLogVisualStackTrace(HttpServletRequest request,
			String mensaje, StackTraceElement[] mensajes) {

		String st = ""
				+ (String) request.getSession().getAttribute(
						MVS_importprocessor);
		if (st.equals("null")) {
			st = "";
		}

		st += "[ " + fechaFormateada() + " ]  " + mensaje
				+ "<br/>.STACK TRACE = ";
		String stlog = mensaje + ".\n STACK TRACE = ";
		int mensmostrados = (mensajes.length < _maxstacktrace) ? mensajes.length
				: _maxstacktrace;
		for (int x = 0; x <= mensmostrados; x++) {
			st += mensajes[x].getClassName() + " ("
					+ mensajes[x].getMethodName() + " >> linea:"
					+ mensajes[x].getLineNumber() + ")" + "<br/>";
			stlog += mensajes[x].getClassName() + " ("
					+ mensajes[x].getMethodName() + " >> linea:"
					+ mensajes[x].getLineNumber() + ")" + "\n";
		}
		if (mensajes.length >= _maxstacktrace) {
			st += " (mas) ...<br/> ";
			stlog += " (mas) ...\n ";
		}
		addLog(stlog);
		request.getSession().setAttribute(MVS_importprocessor, st);
	}

	private static String fechaFormateada() {
		SimpleDateFormat dia = new SimpleDateFormat(_formatofecha);
		java.util.Date fecha = new java.util.Date();
		return dia.format(fecha);
	}

	public static ArrayList<String> getLogs() {
		return _logs;
	}

}
