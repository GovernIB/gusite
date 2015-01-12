 package es.caib.gusite.microfront.qssi.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.microfront.Microfront;
import es.caib.gusite.microfront.base.Bdbase;
import es.caib.gusite.micromodel.Frqssi;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.FrqssiDelegate;


/** 
 * Clase BdQssi. Manejador de la petición de una encuesta.
 * Recupera la encuesta y la muestra en el front.
 * 
 * @author Indra
 *
 */

public class BdQssi extends Bdbase  {
	
	protected static Log log = LogFactory.getLog(BdQssi.class);
	
	
	private HttpServletRequest req;
	private boolean error = false;
	private Frqssi qssi = new Frqssi();
	 
	/**
	 * Constructor de la clase. Carga el componente QSSI
	 * @param request
	 * @throws Exception
	 */
	public BdQssi(HttpServletRequest request) throws Exception {
		super(request);
		req = request;
		if ((microsite!=null) && (existeServicio(Microfront.RQSSI))) {
 	 
			recogerqssi();
			
		} else {
			beanerror = new ErrorMicrosite("Error", "En estos momentos no es posible mostrar la página solicitada.");
			error=true;
		}
	}
	
	/**
	 * Recoger QSSI
	 */
	private void recogerqssi(){
		FrqssiDelegate qssidel = DelegateUtil.getFrqssiDelegate();
		String idqssi = "" + req.getParameter(Microfront.PCONT);
		try {
			Long idcont = new Long(Long.parseLong(idqssi));
			 Frqssi qss = qssidel.obtenerFrqssi(idcont);
			 qssi = qss;

			//comprobacion de microsite
			if (!(qssi.getIdmicrosite().longValue()==idsite.longValue())) {
					beanerror.setAviso("Aviso");
					beanerror.setMensaje("El elemento solicitado no pertenece al site");
					error=true;
			}
			traduceqssi();
		
		 	/* si llegamos aqui.... todo ok, así que grabamos la estadistica
		 	 * if (!error) {
				if ("no".equals(""+req.getSession().getAttribute("MVS_stat"))) log.info("Skip Estadistica, preview conten"); 
				 		else StatManager.grabarestadistica(encuesta, super.publico);
			}
			*/
		} catch (Exception e) {
			log.error(e.getMessage());
            beanerror = new ErrorMicrosite("Error", "Se ha producido un error desconocido al recuperar la pagina solicitada.");
			error=true;
		}
		
	}	
	
	private void traduceqssi() {
		qssi.setIdi(idioma);
		 
	}	
	
	/**
	 * Implementacion del método abstracto.
	 * Se le indica que estamos en el servicio de encuestas.
	 */
	public String setServicio() {
		return Microfront.RQSSI;
	}

	public boolean isError() {
		return error;
	} 
 
	public Frqssi getqssi() {
		return qssi;
	}
 
	public void setQssi(Frqssi qssi) {
		this.qssi = qssi;
	}
 	
}
