package es.caib.gusite.microback.utils.espera;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;

import es.caib.gusite.microback.utils.espera.ForwardActionBean;

/**
 * LongWaitRequestProcessor overrides the original RequestProcessor that
 * comes packaged with Struts.  It allows for the handling of long transactions,
 * and overrides the processActionPerform method to do this.
 * To use this functionality, the following element must be added to the
 * struts-config.xml:
 * <controller
 *  contentType="text/html;charset=UTF-8"
 *  debug="3"
 *  locale="true"
 *  nocache="true"
 *  processorClass="com.takesforever.webapp.LongWaitRequestProcessor"
 * />
 * Then the following parameter must be added to your specific action
 * mapping:
 * parameter="espera"
 * @author Indra
 */
public class LongWaitRequestProcessor extends RequestProcessor {

	 /**
	   * This method has been overridden to handle the case where forms are submitted via
	   * the POST request.  In a POST request, form parameters are not available directly
	   * from the request URI, so they cannot be saved to the ForwardActionBean like in
	   * a GET request.  However, the POST data is saved into the Struts ActionForms.
	   * This data can be stored away in the session.  We must guard against Struts
	   * resetting the form data between the requests, so the form bean is cloned, then
	   * stored.
	   * @param request
	   * @param response
	   * @param form
	   * @param mapping
	   * @return a boolean indicating whether validation was successful
	   * @throws java.io.IOException
	   * @throws javax.servlet.ServletException
	   */
	  protected boolean processValidate(HttpServletRequest request,
	                                    HttpServletResponse response,
	                                    ActionForm form,
	                                    ActionMapping mapping)
	      throws java.io.IOException, javax.servlet.ServletException {
	 
	 
	   if (request.getParameter("espera") == null) {
	      return super.processValidate(request, response, form, mapping);
	    } else if (request.getParameter("espera").equals("si")) {
	      javax.servlet.http.HttpSession session = request.getSession(true);
	      if (session.getAttribute("action_path_key") == null) {
	        return super.processValidate(request,response, form, mapping);
	      } else {
	        // Retrieve the saved form, it's not null, that means the previous request was
	        // sent via POST and we'll need this form because the current one has had
	        // all its values reset to null
	        ActionForm nonResetForm = (ActionForm)session.getAttribute("current_form_key");
	        if (nonResetForm != null) {
	          session.setAttribute(mapping.getName(), nonResetForm);
	          //System.out.println("*** Calling super.processValidate on nonResetForm ***");
	          return super.processValidate(request, response, nonResetForm, mapping);
	        } else {
	          return super.processValidate(request, response, form, mapping);
	        }
	      }
	    } else {
	      return super.processValidate(request, response, form, mapping);
	    }
	  }

	  /**
	   * This method has been overridden to appropriately forward the 'pleaseWait.jsp'
	   * if the user specifies
	   * @param request
	   * @param response
	   * @param action
	   * @param form
	   * @param mapping
	   * @return the ActionForward object indicating what jsp page to forward to.
	   * @throws java.io.IOException
	   * @throws javax.servlet.ServletException
	   */
	  protected ActionForward processActionPerform(HttpServletRequest request,
	                                               HttpServletResponse response,
	                                               Action action,
	                                               ActionForm form,
	                                               ActionMapping mapping) throws
	      java.io.IOException, javax.servlet.ServletException {
		  
	    // Get the parameter from the action mapping to find out whether or not this
	    // action requires long transaction support.  If it doesn't continue processing
	    // as usual.  If it does, then process with a wait page.
		  //String espera= (String)request.getParameter("espera");
	    if (request.getParameter("espera") == null) {
	      //System.out.println("*** There is no wait parameter given, process as usual ***");
	      return super.processActionPerform(request, response, action, form, mapping);
	    } else if (request.getParameter("espera").equals("si")) {
	      //System.out.println("*** Parameter present, process with wait page ***");
	      //javax.servlet.http.HttpSession session = request.getSession(true);
	      // If the action_path_key attribute is null, then it is the first time this
	      // long transaction Action is being accessed.  This means we send down a
	      // wait page with a meta-refresh tag that calls on the action again.
	      if (request.getSession(true).getAttribute("action_path_key") == null) {
	        //System.out.println("*** First time the action has been requested ***");
	        //System.out.println("*** forward user to wait page, have wait page ***");
				
	        if (request.getMethod().equalsIgnoreCase("POST")) {

		     request.getSession(true).setAttribute("action_path_key", getActionPath(request, "POST"));    	
	        //System.out.println("*** Request sent via the POST method ***");
	          ActionForm nonResetForm = null;
	         
	          try {
	        	  nonResetForm = (ActionForm) form;
	        	  request.getSession(true).setAttribute("current_form_key", nonResetForm);
	        	  
					//Si existe el objeto traductor en contexto y no está como atributo de sesión lo cargamos
					if (getServletContext().getAttribute("traductor") != null 
							& request.getSession(true).getAttribute("traductor") == null)
						request.getSession(true).setAttribute("traductor", getServletContext().getAttribute("traductor"));
	        	  
	          } catch (Exception e) {
	            System.out.println("*** Error cloning the form for processWithWait ***");
	            // Your error handling goes here.
	          }
	        } else request.getSession(true).setAttribute("action_path_key", getActionPath(request, "GET")); 
	        
	        return mapping.findForward("pleaseWait");
	      
	      // The action has been called on a second time, from the wait
	      // page itself.  This time we actually execute the action.  We reset
	      // the action path key so that the next time this action is requested, the
	      // wait page is NOT served.
	      } else {
	    	  if (request.getParameter("espera").equals("si")) {
	    		  return mapping.findForward("pleaseWait");
	    	  }else{
		    	  //System.out.println("*** Second time the action has been requested ***");
		        //System.out.println("*** forward user to actual action ***");
		    	  request.getSession(true).setAttribute("action_path_key", null);
		        // Retrieve the form, it's not null, that means the previous request was
		        // sent via POST and we'll need this form because the current one has had
		        // all its values reset to null
		    	  ActionForm nonResetForm = (ActionForm)request.getSession(true).getAttribute("current_form_key");
		    	  if (nonResetForm != null) {
		    		  request.getSession(true).setAttribute("current_form_key", null);
		    		  return super.processActionPerform(request, response, action, nonResetForm, mapping);
		    	  } else {
		    		  return super.processActionPerform(request, response, action, form, mapping);
		    	  }
	    	  }
	      }
	    // A parameter has been set in the mapping but it is not the value we are
	    // looking for
	    } else {
	      return super.processActionPerform(request, response, action, form, mapping);
	    }

	  }
	  
	  private ForwardActionBean getActionPath (HttpServletRequest request, String metodoHTTP) {
		  
			ForwardActionBean forwardActionBean = new ForwardActionBean();
			String cadenaParametros = "";
			Enumeration<?> enumeraParametros = request.getParameterNames();
			
				if (enumeraParametros.hasMoreElements()) cadenaParametros="?";
				while (enumeraParametros.hasMoreElements()){
					
					String nombreParametro = (String)enumeraParametros.nextElement();
					String valorParametro = request.getParameter(nombreParametro);
							
					if (nombreParametro.equals("accion") || nombreParametro.equals("id") || nombreParametro.equals("espera")
							|| nombreParametro.equals("idsite") || nombreParametro.equals("site") ) 
					{
						if (nombreParametro.equals("espera")) cadenaParametros+=nombreParametro+"="+"no"; 
						else cadenaParametros+=nombreParametro+"="+valorParametro;
								
						cadenaParametros+="&";
					}
				}
				
				 cadenaParametros = cadenaParametros.substring(0, cadenaParametros.length()-1);
			
			forwardActionBean.setActionPath(request.getRequestURI() + cadenaParametros);
		  
			return forwardActionBean;
	  }
}
