package org.ibit.rol.sac.microfront.home.util;

import javax.servlet.http.HttpServletRequest;

import org.ibit.rol.sac.microfront.base.Bdbase;

/**
 * Clase Bdmapa.  Manejador de la petici�n de un mapa.
 * @author pmelia
 *
 */
public class BdAccessibilitat extends Bdbase  {
	
	/**
	 * Constructor de la clase. La informaci�n se guarda a traves de la request.
	 * @param request
	 * @throws Exception
	 */
	public BdAccessibilitat(HttpServletRequest request) throws Exception {
		super(request);
	}
	
	/**
	 * Implementacion del m�todo abstracto.
	 * Se le indica que no estamos en ningun servicio.
	 */
	public String setServicio() {
		return " ";
	}	
}
