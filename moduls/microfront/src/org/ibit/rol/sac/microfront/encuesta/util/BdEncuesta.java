package org.ibit.rol.sac.microfront.encuesta.util;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.microfront.Microfront;
import org.ibit.rol.sac.microfront.base.Bdbase;
import org.ibit.rol.sac.microfront.base.bean.ErrorMicrosite;
import org.ibit.rol.sac.microfront.estadistica.util.StatManager;
import org.ibit.rol.sac.microfront.util.Fechas;
import org.ibit.rol.sac.micromodel.Encuesta;
import org.ibit.rol.sac.micromodel.Estadistica;
import org.ibit.rol.sac.micromodel.Pregunta;
import org.ibit.rol.sac.micromodel.Respuesta;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.EncuestaDelegate;

/**
 * Clase Bdcontenido.  Manejador de la petición de una  encuesta. Recupera los datos de la encuesta para mostrarlos en el front.
 * Recupera la encuesta.
 * 
 * @author Indra
 */
public class BdEncuesta extends Bdbase  {
	
	protected static Log log = LogFactory.getLog(BdEncuesta.class);
	
	private HttpServletRequest req;
	private boolean error = false;
	private Encuesta encuesta = new Encuesta();
	private boolean ensesion = false;
	
	public void finalize() {
		this.dispose();
	}
	
	/**
	 * Metodo público que borra las variables
	 */
	public void dispose() {
		encuesta = null;
		req = null;
		super.dispose();
	}
	
	/**
	 *  Constructor de la clase, carga la encuesta a partir de la encuesta pedida (idencuesta)
	 * @param request
	 * @throws Exception
	 */
	public BdEncuesta(HttpServletRequest request) throws Exception {
		super(request);
		req = request;
		if ((microsite!=null) && (existeServicio(Microfront.RENCUESTA))) {

			procesaSesion();
			recogerencuesta();
			
		} else {
			beanerror = new ErrorMicrosite("Error", "En estos momentos no es posible mostrar la página solicitada.");
			error=true;
		}
	}
	
	/**
	 *  Método privado que procesa la sesión. Averigua si el usuario ha rellenado la encuesta en sesión.
	 */
	private void procesaSesion() {
		try {
			String idencuesta = "" + req.getParameter(Microfront.PCONT);
			String vsesion = "" + req.getSession().getAttribute("MVS_encuestarellena" + idencuesta);
			if (vsesion.equals("yes")) ensesion=true;
		} catch (Exception e) {
			log.warn("Error averiguando si el usario ha rellenado la encuesta en sesion");
			ensesion=false;
		}
	}
	
	/**
	 * Método privado que recoge la encuesta a partir de un id
	 */
	private void recogerencuesta(){
		EncuestaDelegate encuestadel = DelegateUtil.getEncuestaDelegate();
		String idencuesta = "" + req.getParameter(Microfront.PCONT);
		try {
			Long idcont = new Long(Long.parseLong(idencuesta));
			Encuesta encu = encuestadel.obtenerEncuesta(idcont);
			encuesta = encu;

			//comprobacion de microsite
			if (!(encuesta.getIdmicrosite().longValue()==idsite.longValue())) {
					beanerror.setAviso("Aviso");
					beanerror.setMensaje("El elemento solicitado no pertenece al site");
					error=true;
			}
			//comprobacion de visibilidad
			if (!encuesta.getVisible().equals("S")) {
					beanerror.setAviso("Aviso");
					beanerror.setMensaje("El elemento solicitado no pertenece al site");
					error=true;
			}			

			//o bien comprobacion de que esté vigente
			if (!Fechas.vigente(encuesta.getFpublicacion(), encuesta.getFcaducidad())) {
				//no pasamos error, simulamos que está rellena la encuesta
				ensesion=true;
			}
			
			traduceencuesta();
			
			//si llegamos aqui.... todo ok, así que grabamos la estadistica
			if (!error) {
				if ("no".equals(""+req.getSession().getAttribute("MVS_stat"))) log.info("Skip Estadistica, preview conten"); 
				 		else req.getSession().getServletContext().setAttribute("bufferStats", 
								StatManager.grabarestadistica(encuesta, super.publico, 
										(List<Estadistica>) req.getSession().getServletContext().getAttribute("bufferStats")));
			}
		} catch (Exception e) {
			log.error(e.getMessage());
            beanerror = new ErrorMicrosite("Error", "Se ha producido un error desconocido al recuperar la pagina solicitada.");
			error=true;
		}
		
	}	
	
	/**
	 * Método privado que recoge la preguntas y respuestas e introduce el idioma.
	 */
	private void traduceencuesta() {
		encuesta.setIdi(idioma);
		Iterator<?> iter = encuesta.getPreguntas().iterator();
	    while (iter.hasNext()) {
	    	
	    	Pregunta pregunta = (Pregunta)iter.next();
	    	pregunta.setIdi(idioma);
	    	
			Iterator<?> iter2 = pregunta.getRespuestas().iterator();
		    while (iter2.hasNext()) {
		    	
		    	Respuesta respuesta = (Respuesta)iter2.next();
		    	respuesta.setIdi(idioma);
		    }
	    	
	    }
	}	
	
	/**
	 * Implementacion del método abstracto.
	 * Se le indica que estamos en el servicio de encuestas.
	 */
	public String setServicio() {
		return Microfront.RENCUESTA;
	}

	public boolean isError() {
		return error;
	}

	public Encuesta getEncuesta() {
		return encuesta;
	}

	public void setEncuesta(Encuesta encuesta) {
		this.encuesta = encuesta;
	}

	public boolean isEnsesion() {
		return ensesion;
	}	
	
}