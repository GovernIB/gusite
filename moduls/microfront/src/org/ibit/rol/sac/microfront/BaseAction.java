package org.ibit.rol.sac.microfront;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.microfront.base.bean.ErrorMicrosite;
import org.ibit.rol.sac.microfront.job.MenuCabecera;

/**
 *  Clase BaseAction
 * @author Indra
 *
 */
public class BaseAction extends Action {

	protected static Log log = LogFactory.getLog(BaseAction.class);
	
	protected void menucaib (HttpServletRequest request, String lang) throws Exception 	{

    	lang = lang.toLowerCase();
		
    	request.setAttribute("MVS2_uos", MenuCabecera.getUos(lang));
    	request.setAttribute("MVS2_agrupacionesmateria", MenuCabecera.getAgrupacionmaterias(lang));
    	request.setAttribute("MVS2_urldarrerboib",MenuCabecera.getUrldarrerboib(lang));
    	request.setAttribute("MVS2_portavoz", MenuCabecera.getPortavoz(lang));
	}
	
    /**
     * Metodo protegido que devuelve String de error de contenido de un site
     * @param HttpServletRequest request, Microsite microsite, ErrorMicrosite errorMicrosite
     * @exception Exception
     */
	protected String getForwardError (HttpServletRequest request, String ambitError) throws Exception {
     
	   Microsite microsite = getMicrositeFromSession(request);
	
	   return (getForwardError (request, microsite, ambitError));
	}		
	
    /**
     * Metodo protegido que devuelve String de error de contenido de un site
     * @param HttpServletRequest request, Microsite microsite, ErrorMicrosite errorMicrosite
     * @exception Exception
     */
	protected String getForwardError (HttpServletRequest request, Microsite microsite, String ambitError) throws Exception {
     
	   ErrorMicrosite errorMicrosite;
		
	   if (ambitError == ErrorMicrosite.ERROR_AMBIT_MICRO) {
	  	   
		  String pmkey = "" + request.getParameter(Microfront.PMKEY);
		  String pidsite = "" + request.getParameter(Microfront.PIDSITE);
		   
		   if (!pmkey.equals("null")) errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_MICRO_TIT, ErrorMicrosite.ERROR_MICRO_MSG + pmkey);
	  	   else if (!pidsite.equals("null")) errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_MICRO_TIT, ErrorMicrosite.ERROR_MICRO_MSG + pidsite);
	  	   else errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_MICRO_TIT, ErrorMicrosite.ERROR_MICRO_MSG_NULL);

		   request.getSession().setAttribute("MVS_errparam", errorMicrosite); 
	   }	   
	   else if (ambitError == ErrorMicrosite.ERROR_AMBIT_PAGINA) {
		   errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_PAGINA_TIT, ErrorMicrosite.ERROR_PAGINA_MSG);
		   request.getSession().setAttribute("MVS_errparam", errorMicrosite); 
	   }
	   else if (ambitError == ErrorMicrosite.ERROR_AMBIT_DOCUMENT) {
		   errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_DOCU_TIT, ErrorMicrosite.ERROR_DOCU_MSG);
		   request.getSession().setAttribute("MVS_errparam", errorMicrosite); 
	   }
	   else if (ambitError == ErrorMicrosite.ERROR_AMBIT_ACCES) {
		   errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_ACCES_TIT, ErrorMicrosite.ERROR_ACCES_MSG);
		   request.getSession().setAttribute("MVS_errparam", errorMicrosite); 
	   }
	   else if (ambitError == ErrorMicrosite.ERROR_AMBIT_SESSIO) {
		   errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_SESSIO_TIT, ErrorMicrosite.ERROR_SESSIO_MSG);
		   request.getSession().setAttribute("MVS_errparam", errorMicrosite); 
	   }	   
	   else {
		   errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_PAGINA_TIT, ErrorMicrosite.ERROR_PAGINA_MSG);
		   request.getSession().setAttribute("MVS_errparam", errorMicrosite); 
		   }
	     	   
	   microsite.setRestringido("N");
  	   microsite.setTipocabecera("1");	
  	   request.getSession().setAttribute("MVS_microsite", microsite); 
  	   
  	   request.getSession().setAttribute("MVS_idioma", "ca");
  	   request.getSession().setAttribute( Globals.LOCALE_KEY, new java.util.Locale("ca", "ca"));
  	   
  	   String forwardlocal = "errorgenericov4";
  	   
  	   return forwardlocal;
		
	}		
	
	protected Microsite getMicrositeFromSession(HttpServletRequest request) {
		   return (Microsite)request.getSession().getAttribute("MVS_microsite");
		}
	
}
