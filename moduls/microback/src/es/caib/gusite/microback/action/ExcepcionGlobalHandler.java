package es.caib.gusite.microback.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

import es.caib.gusite.micromodel.Microsite;

/**
 * Handler para excepciones globales <P>
 * 
 * 	Definición Struts:<BR>
 *  key="errors.excepcion"<BR> 
 *  path="/error.jsp" <BR>
 *  
 *  @author - Indra
 */
public class ExcepcionGlobalHandler extends ExceptionHandler {

	 protected static Log log = LogFactory.getLog(ExcepcionGlobalHandler.class);

	    public ActionForward execute(Exception e, ExceptionConfig config,
	                                 ActionMapping mapping, ActionForm form,
	                                 HttpServletRequest request, HttpServletResponse response)
	            throws ServletException {
	    	
	    	
	        ActionForward forward;
	        ActionError error;
	        String property;

	        // Build the forward from the exception mapping if it exists
	        // or from the form input
	        if (config.getPath() != null) {
	            forward = new ActionForward(config.getPath());
	        } else {
	            forward = mapping.getInputForward();
	        }

	        error = new ActionError("errors.presentacion", e.getMessage());
	        e.printStackTrace();
	        
	        try {
	        	Microsite micro = (Microsite)request.getSession().getAttribute("MVS_microsite");
	        	log.error("Error en el microsite: " + micro.getId());
	        } catch (Exception ex) {
	        	//si salta la excepcion es porque se ha perdido la sesion del microsite
	        	log.error("Error de sesion en microsite");
	        	error = new ActionError("errors.excepcion.sesion");
	        }
	        
	        property = error.getKey();

	        // Store the exception
	        request.setAttribute(Globals.EXCEPTION_KEY, e);
	        super.storeException(request, property, error, forward, config.getScope());

	        return forward;
	    	
	    }
	
}
