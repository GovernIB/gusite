package es.caib.gusite.microfront.home.util;

import javax.servlet.http.HttpServletRequest;

import es.caib.gusite.microfront.base.Bdbase;

/**
 * Clase Bdmapa.  Manejador de la petición de un mapa.
 * @author pmelia
 *
 */
public class BdAccessibilitat extends Bdbase  {
	
	/**
	 * Constructor de la clase. La información se guarda a traves de la request.
	 * @param request
	 * @throws Exception
	 */
	public BdAccessibilitat(HttpServletRequest request) throws Exception {
		super(request);
	}
	
	/**
	 * Implementacion del método abstracto.
	 * Se le indica que no estamos en ningun servicio.
	 */
	public String setServicio() {
		return " ";
	}	
}
