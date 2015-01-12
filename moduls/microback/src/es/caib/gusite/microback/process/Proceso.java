package es.caib.gusite.microback.process;

import javax.servlet.http.HttpServletRequest;

import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.utilities.statusbar.StatusBar;
import es.caib.gusite.utilities.statusbar.StatusBarFactory;


/**
 * Clase que implementa threads del proceso de validación W3C
 * 
 * @author Indra
 *
 */
public class Proceso {

	/**
	 * Lanza un Proceso paralelo encargado de repasar la accesibilidad de un microsite.<br/>
	 */
	public static String repasarMicrositeW3C(HttpServletRequest request) {
			StatusBar statusbar = StatusBarFactory.getStatusBar();
			final ProcesoThreadMicrositeW3C act = new Proceso().new ProcesoThreadMicrositeW3C(request, statusbar.getIdentificador());
			act.start();
			return statusbar.getIdentificador();
	}
	
	/**
	 * Implementación de un Thread
	 * 
	 *
	 */
	private class ProcesoThreadMicrositeW3C extends Thread {

		private String idbarra;
		private Long idmicrosite;
		private String servidor;
		private String puerto;
		private String protocolo;
		
		/**
		 * El constructor.
		 */
		private ProcesoThreadMicrositeW3C(HttpServletRequest request, String idstatusbar) {
			idmicrosite = ((Microsite)request.getSession().getAttribute("MVS_microsite")).getId();
			StringBuffer urlbuf = request.getRequestURL();
			if (urlbuf==null) urlbuf = new StringBuffer(request.getHeader("referer"));
			if (urlbuf==null) urlbuf = new StringBuffer("http://localhost");
			int posSeparadorProtocol=urlbuf.indexOf("//");
			protocolo = (urlbuf.substring(0, posSeparadorProtocol+2)).toString();
			servidor = request.getServerName();
			puerto = "" + request.getServerPort();
			idbarra = idstatusbar;
		}
		
		@Override
		public void run() {
			ProcesoW3C pw3c = new ProcesoW3C(idbarra, idmicrosite, servidor, puerto, protocolo);
			pw3c.testearMicrosite();
		}
		
	}
	
}
