package org.ibit.rol.sac.microfront.cercador.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.lucene.indra.model.IndexResultados;
import org.ibit.rol.sac.microfront.Microfront;
import org.ibit.rol.sac.microfront.base.Bdbase;
import org.ibit.rol.sac.microfront.base.bean.ErrorMicrosite;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.IndexerDelegate;

/**
 * Clase Bdcercador. Recoge los datos para mostrarlos en el front.
 * @author Indra
 */
public class Bdcercador extends Bdbase {

	protected static Log log = LogFactory.getLog(Bdcercador.class);
	
	private HttpServletRequest req;
	private boolean error = false;
	private IndexResultados resultado;
    
	public void finalize() {
		this.dispose();
	}
	
	/**
	 * Metodo público que borra las variables
	 */
	public void dispose() {
		resultado = null;
		req = null;
		super.dispose();
	}

	/**
	 * Constructor de la clase. Carga los datos a partir de la request.
	 * @param request
	 * @throws Exception
	 */
	public Bdcercador(HttpServletRequest request) throws Exception {
		super(request);
		req = request;
		if ((microsite!=null) && (microsite.getBuscador().equals("S"))) {
			buscar();
		} else {
			beanerror = new ErrorMicrosite("Error", "En estos momentos no es posible mostrar la página solicitada.");
			error=true;
		}			
	}

	/**
	 * Implementacion del método abstracto.
	 * Da igual qué servicio es. 
	 */
	public String setServicio() {
		return Microfront.RCONTENIDO;
	}	
	
	/**
	 * Método privado, realizará la búsqueda.
	 */
	private void buscar() {
		try {
			IndexerDelegate indexo = DelegateUtil.getIndexerDelegate();
		    String words=""+req.getParameter("cerca");
		    String idi="" + req.getSession().getAttribute("MVS_idioma");
		    resultado = indexo.buscar("" + microsite.getId().longValue(), idi, null, words, true);
		} catch (Exception e) {
			log.error(e.getMessage());
			beanerror = new ErrorMicrosite("Error", e.getMessage());
			error=true;
		}  
	}

	/**
	 * Devuelve true o false en función de si hay error.
	 * @return Devuelve true o false en función de si hay error
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * Devuelve el resultado de la búsqueda
	 * @return IndexResultado
	 */
	public IndexResultados getResultado() {
		return resultado;
	}
	
}
