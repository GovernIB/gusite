 package org.ibit.rol.sac.microfront.qssi.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.microfront.Microfront;
import org.ibit.rol.sac.microfront.base.Bdbase;
import org.ibit.rol.sac.microfront.base.bean.ErrorMicrosite;
import org.ibit.rol.sac.micromodel.Frqssi;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.FrqssiDelegate;


/** 
 * Clase BdQssi. Manejador de la petici�n de una encuesta.
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
			beanerror = new ErrorMicrosite("Error", "En estos momentos no es posible mostrar la p�gina solicitada.");
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
		
		 	/* si llegamos aqui.... todo ok, as� que grabamos la estadistica
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
	 * Implementacion del m�todo abstracto.
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
