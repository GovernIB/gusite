package es.caib.gusite.microback.action.util;

import javax.servlet.http.HttpServletRequest;

import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;

/**
 * Clase que es llamada desde el action de descargar archivos de un site (ArchivoPubAction).
 * 
 * @author Indra
 *
 */
public class Bdarchivopub {
	
	HttpServletRequest req;
	
	public Bdarchivopub(HttpServletRequest request) {
		req = request;
	}
	
	
	/**
	 * Método que comprueba que en el parametro 'ctrl' del request se ha pasado el siguiente: <BR>
	 * formato: SSSSSxxxZIyyy <BR>
     * donde SSSSS es el tipo de servicio sacado de la clase es.caib.gusite.microfront.Microfront. <BR>
     * donde xxx es el id del elemento al que pertenece el documento <BR>
     * donde ZI es el separador <BR>
     * donde yyy es el id del documento <BR>
     * En el parametro id se ha pasado también el id de la imagen. 
	 */
	public boolean checkcontrol() {

		boolean retorno = true;

		try {
						
			// primero, comprobar que 'ctrl' tiene el formato correcto
			String ctrl = "" + req.getParameter("ctrl");
			String SSSSS;
			Long xxx;
			Long yyy;
			SSSSS = ctrl.substring(0, 5);
			xxx = new Long(ctrl.substring(5, ctrl.indexOf("ZI")));
			yyy = new Long(ctrl.substring(ctrl.indexOf("ZI") + 2, ctrl.length()));

			Long idelemento = new Long(req.getParameter("id"));

			// segundo, comprobar que yyy es igual que el parametro id
			if (idelemento.longValue() != yyy.longValue())
				retorno = false;

			if ((retorno) && (SSSSS.equals("CNTSP"))) {
				ContenidoDelegate contenidodel = DelegateUtil.getContenidoDelegate();
				Contenido contenido = contenidodel.obtenerContenido(xxx);
				String previ = "" + req.getSession().getAttribute("previsualiza");
				if (!previ.equals("si")) {
					if (!contenido.getVisible().equals("S")) {
						retorno = false;
					}
				}
			}

		} catch (Exception e) {
			
			retorno = false;
			
		}

		return retorno;
		
	}
	
}
